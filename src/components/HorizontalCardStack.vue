<template>
  <div ref="stackElement" class="horizontal-stack">
    <button
      class="scroll-all-btn"
      :class="{ hidden: !showScrollButton }"
      :style="buttonStyle"
      @click="scrollAll"
      @pointerdown.stop
      aria-label="Scroll through all cards"
      title="Scroll through all fleet cards"
    >
      <img src="/full-scroll-arrow.webp" alt="Scroll all" />
    </button>
    <div
      ref="scrollContainer"
      class="scroll-container px-4  my-5"
      :style="{ height: containerHeight + 'px' }"
      tabindex="0"
      @pointerdown="onPointerDown"
      @pointermove="onPointerMove"
      @pointerup="onPointerUp"
      @pointercancel="onPointerUp"
      @keydown="onKeyDown"
    >
      <div ref="track" class="scroll-track" :style="trackStyles">
        <div
          v-for="(card, i) in cards"
          :key="i"
          class="card-slot"
          :style="cardSlotStyle(i)"
        >
          <div
            class="card-inner"
            :style="{ backgroundColor: card.color || '#1e1e2a' }"
          >
            <h3 class="card-title card-title--outside" @click="navigate(card, i)" @pointerdown.stop>{{ card.title }}</h3>
            <div class="card-layout">
              <div class="card-description">
                <h3 class="card-title card-title--inside" @click="navigate(card, i)" @pointerdown.stop>{{ card.title }}</h3>
                <p @click="navigate(card, i)" @pointerdown.stop>{{ card.description }}</p>
                <div v-if="card.features && card.features.length" class="card-features">
                  <span
                    v-for="(f, fi) in card.features.slice(0, 3)"
                    :key="fi"
                    class="feature-tag"
                    @click="navigate(card, i)"
                    @pointerdown.stop
                  >{{ f }}</span>
                </div>
              </div>
              <div class="card-image-wrap">
                <div class="card-image">
                  <img :src="card.image" :alt="card.title" loading="lazy" draggable="false" />
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="scroll-sentry" :style="{ minWidth: sentryWidth + 'px' }" />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import { useRouter } from 'vue-router'
import Lenis from 'lenis'

export default {
  name: 'HorizontalCardStack',
  props: {
    cards: {
      type: Array,
      required: true,
    },
  },
  setup(props) {
    const router = useRouter()
    const scrollContainer = ref(null)
    const track = ref(null)
    const stackElement = ref(null)
    const scrollLeft = ref(0)
    const cardWidthPx = ref(800)
    const viewportWidth = ref(window.innerWidth)
    let lenis = null
    let rafId = null

    // ── Card & layout dimensions ──
    const STACK_GAP = 0.14                    // desktop: gap between stacked cards (fraction of card width)
    const FIRST_CARD_GAP = 30                 // px from left edge for first stacked card
    const MOBILE_LADDER_OFFSET = 0.12         // mobile: vertical offset between stacked cards (fraction of card height)
    const MOBILE_TRANSITION_WINDOW = 0.25     // mobile: scroll distance per card transition (fraction of card width)
    const MOBILE_SLOTS_HORIZONTAL_GAP = 16    // px gap between card slots on mobile

    const isMobile = computed(() => viewportWidth.value < 768)

    const n = computed(() => props.cards.length)
    const paddingLeft = ref(0)  // read from DOM in updateSize so CSS & JS stay in sync

    // Mobile card height — fixed; ladder grows container to fit all cards
    const cardHeightPx = computed(() => {
      if (!isMobile.value) return 0
      return Math.min(340, window.innerHeight * 0.5)
    })

    // Container tall enough for the full ladder (mobile) or 520px (desktop)
    const containerHeight = computed(() => {
      if (!isMobile.value) return 520
      return cardHeightPx.value + (n.value - 1) * cardHeightPx.value * MOBILE_LADDER_OFFSET
    })

    // How much scroll each card's transition occupies
    const gapFactor = computed(() => isMobile.value ? MOBILE_TRANSITION_WINDOW : STACK_GAP)

    // Extra horizontal gap between card slots (mobile only) — driven by JS, applied as inline style
    const cardGap = computed(() => isMobile.value ? MOBILE_SLOTS_HORIZONTAL_GAP : 0)

    // Inline column-gap on the track so flex layout matches JS calculations exactly
    const trackStyles = computed(() => ({
      ...(isMobile.value ? { columnGap: `${MOBILE_SLOTS_HORIZONTAL_GAP}px` } : {})
    }))

    // Scroll position where the last card finishes its stacking transition
    // (includes mobile gap so scroll doesn't stop too early)
    const lastTransitionEnd = computed(() => {
      if (n.value === 0) return 0
      const cw = cardWidthPx.value
      const pl = paddingLeft.value
      const gap = cardGap.value
      return (n.value - 1) * (cw + gap) + pl + cw * gapFactor.value
    })

    // Final scroll stop — past lastTransitionEnd on desktop (skim-through),
    // exactly at lastTransitionEnd on mobile (no extra drift)
    const scrollStop = computed(() => {
      if (n.value === 0) return 0
      if (isMobile.value) return lastTransitionEnd.value
      return lastTransitionEnd.value + (n.value - 1) * (cardWidthPx.value * STACK_GAP + 20)
    })

    // Sentry width sets Lenis' internal limit = scrollStop.
    // Per-card extra width (CSS padding-right) is 20 on desktop, 0 on mobile.
    // Note: column-gap applies before the sentry too (it's the (n+1)th flex item),
    // so total gaps in the track = n (not n-1) — but only relevant on mobile.
    const sentryWidth = computed(() => {
      if (n.value === 0) return 0
      const cw = cardWidthPx.value
      const pl = paddingLeft.value
      const stop = scrollStop.value
      const vw = viewportWidth.value
      const gap = cardGap.value
      const cardPadding = isMobile.value ? 0 : 20
      if (isMobile.value) {
        return Math.max(0, stop + vw + pl - n.value * cw - n.value * gap)
      }
      return Math.max(0, stop + vw - pl - n.value * (cw + cardPadding))
    })

    // ── Drag-to-scroll state ──
    const isDragging = ref(false)
    const dragStartX = ref(0)
    const dragStartScrollLeft = ref(0)
    let dragVelocity = 0          // px per ~16ms frame, for momentum on release
    let lastMoveX = 0
    let lastMoveY = 0
    let lastMoveTime = 0
    let momentumRafId = null
    const FRAME_TIME = 16
    let dragAxis = null           // 'horizontal' | 'vertical' — locked after first significant move

    // Pointer handlers for mouse + touch drag scrolling
    function onPointerDown(event) {
      userInteracted = true
      // Cancel any in-flight momentum from a previous drag
      if (momentumRafId) { cancelAnimationFrame(momentumRafId); momentumRafId = null }
      if (lenis) lenis.stop()

      isDragging.value = true
      dragStartX.value = event.clientX
      dragStartScrollLeft.value = scrollContainer.value.scrollLeft
      scrollContainer.value.setPointerCapture(event.pointerId)
      dragVelocity = 0
      dragAxis = null
      lastMoveX = event.clientX
      lastMoveY = event.clientY
      lastMoveTime = performance.now()
    }

    function onPointerMove(event) {
      if (!isDragging.value) return

      // Directional lock: on first significant move, decide horizontal vs vertical
      if (dragAxis === null) {
        const dx = Math.abs(event.clientX - lastMoveX)
        const dy = Math.abs(event.clientY - lastMoveY)
        if (dx > 5 || dy > 5) {
          dragAxis = dx >= dy ? 'horizontal' : 'vertical'
          if (dragAxis === 'vertical') {
            // Let the browser handle vertical scrolling natively — release everything
            isDragging.value = false
            scrollContainer.value.releasePointerCapture(event.pointerId)
            if (lenis) lenis.start()
            return
          }
        }
      }

      if (dragAxis !== 'horizontal') return

      const now = performance.now()
      const dt = now - lastMoveTime
      if (dt > 0) {
        dragVelocity = (event.clientX - lastMoveX) / dt * FRAME_TIME
      }
      lastMoveX = event.clientX
      lastMoveTime = now

      const deltaX = event.clientX - dragStartX.value
      scrollContainer.value.scrollLeft = dragStartScrollLeft.value - deltaX
      // Keep reactive scroll in sync so card transforms update immediately
      scrollLeft.value = Math.min(scrollContainer.value.scrollLeft, lastTransitionEnd.value)
    }

    function onPointerUp() {
      if (!isDragging.value) return
      isDragging.value = false
      const container = scrollContainer.value
      let vel = dragVelocity
      const DECAY = 0.92
      const MIN_VELOCITY = 0.5

      function momentumLoop() {
        if (Math.abs(vel) < MIN_VELOCITY) {
          // Momentum exhausted — hand back to Lenis
          if (lenis) lenis.start()
          return
        }
        // Clamp container AND reactive state so momentum never overshoots the limit
        const newScroll = container.scrollLeft - vel
        const clamp = isMobile.value ? lastTransitionEnd.value : scrollStop.value
        container.scrollLeft = Math.max(0, Math.min(clamp, newScroll))
        scrollLeft.value = Math.min(container.scrollLeft, lastTransitionEnd.value)
        vel *= DECAY
        momentumRafId = requestAnimationFrame(momentumLoop)
      }
      momentumRafId = requestAnimationFrame(momentumLoop)
    }

    // ── Keyboard scroll (left / right arrows) ──
    function onKeyDown(event) {
      userInteracted = true
      if (event.key !== 'ArrowLeft' && event.key !== 'ArrowRight') return
      event.preventDefault()
      if (!lenis) return
      const container = scrollContainer.value
      if (!container) return
      const step = cardWidthPx.value * 0.25      // quarter card per key press
      const delta = event.key === 'ArrowLeft' ? -step : step
      const target = Math.max(0, Math.min(scrollStop.value, container.scrollLeft + delta))
      lenis.scrollTo(target)
    }

    const updateSize = () => {
      viewportWidth.value = window.innerWidth
      if (scrollContainer.value) {
        const style = getComputedStyle(scrollContainer.value)
        const pl = parseFloat(style.paddingLeft)
        const pr = parseFloat(style.paddingRight)
        cardWidthPx.value = scrollContainer.value.clientWidth - pl - pr
        paddingLeft.value = pl
      }
      updateButtonBounds()
      if (lenis) lenis.resize()
    }

    // Show/hide the "scroll all" button — visible when the 2nd-last card is not fully stacked
    // and the parent section is visible on screen (mobile)
    const showScrollButton = computed(() => {
      if (n.value < 2) return false
      if (isMobile.value && !isButtonInBounds.value) return false
      return computeT(n.value - 2) < 1
    })

    // Auto-scroll through all cards to the very last scrollable pixel
    function scrollAll() {
      if (!lenis) return
      const dur = isMobile.value ? 2.5 : 8
      const ease = isMobile.value ? (t) => 1 - (1 - t) ** 3 : (t) => 1 - (1 - t) ** 2
      lenis.scrollTo(scrollStop.value, { duration: dur, easing: ease })
    }

    // Navigate to category or vehicle page on title click.
    // If card is not fully visible (stacked / off-screen), bring it into view without navigating.
    // Only navigate when clicked while fully unstacked and unobstructed.
    function navigate(card, i) {
      if (!card.id) return
      const t = computeT(i)
      const cw = cardWidthPx.value
      const gap = cardGap.value
      const pl = paddingLeft.value
      const sl = scrollLeft.value
      const vw = viewportWidth.value
      const cardStart = i * (cw + gap)
      const naturalX = cardStart - sl
      const stackX = (isMobile.value ? 0 : FIRST_CARD_GAP - pl) + (isMobile.value ? 0 : i * cw * STACK_GAP)
      const screenX = naturalX + t * (stackX - naturalX)
      const leftEdge = pl + screenX
      const rightEdge = leftEdge + cw
      const isUnstacked = t < 0.01
      const fullyVisible = leftEdge >= 0 && rightEdge <= vw

      if (!isUnstacked) {
        // Stacked → scroll to unstack it, don't navigate
        if (lenis) lenis.scrollTo(Math.max(0, cardStart + pl - 10))
        return
      }
      if (!fullyVisible) {
        // Unstacked but off-screen → scroll to center it, don't navigate
        if (lenis) lenis.scrollTo(Math.max(0, cardStart + pl - (vw - cw) / 2))
        return
      }
      // Fully visible and unstacked → navigate
      if (card.type === 'category') {
        router.push({ name: 'ListProducts', params: { id: card.id } })
      } else if (card.type === 'vehicle') {
        router.push({ name: 'ProductDetails', params: { id: card.id, name: card.name } })
      }
    }

    // ── Per-card transition progress ──
    // t = 0 → natural scroll position; t = 1 → fully stacked position
    function computeT(i) {
      if (isMobile.value && i === 0) return 1  // card 0 never moves on mobile
      const sl = scrollLeft.value
      const cw = cardWidthPx.value
      const pl = paddingLeft.value
      const gf = gapFactor.value
      const touchSl = i * (cw + cardGap.value) + pl             // card left edge reaches viewport 0 (includes mobile gap)
      const transitionStart = touchSl
      const transitionEnd = touchSl + cw * gf                   // end of transition window
      let t = 0
      if (sl <= transitionStart) t = 0
      else if (sl >= transitionEnd) t = 1
      else t = (sl - transitionStart) / (transitionEnd - transitionStart)  // linear blend
      return t
    }

    // CSS translateX to position card at its current scroll-based screen position
    function getTransform(i) {
      const t = computeT(i)
      const sl = scrollLeft.value
      const cw = cardWidthPx.value
      const pl = paddingLeft.value
      const cardStart = i * (cw + cardGap.value)   // natural flex position (includes mobile gap)
      const naturalX = cardStart - sl                            // where card would be without stacking
      const stackX = (isMobile.value ? 0 : FIRST_CARD_GAP - pl) + (isMobile.value ? 0 : i * cw * STACK_GAP)  // target stacked position in viewport coords
      const screenX = naturalX + t * (stackX - naturalX)        // interpolate between natural and stacked
      return `translateX(${screenX - naturalX}px)`               // offset from flex position
    }

    // Full inline style for a card-slot (width, transform, z-index, plus mobile height & ladder offset)
    function cardSlotStyle(i) {
      const base = {
        width: cardWidthPx.value + 'px',
        transform: getTransform(i),
        zIndex: i,                              // card 0 at bottom, last card on top
      }
      if (isMobile.value) {
        base.height = cardHeightPx.value + 'px'
        base.top = (i * cardHeightPx.value * MOBILE_LADDER_OFFSET) + 'px'  // each card sits lower
      }
      return base
    }

    // ── Lifecycle ──
    onMounted(() => {
      updateSize()
      window.addEventListener('resize', updateSize)

      const container = scrollContainer.value
      if (!container) return

      // Populate Set from current scroll state BEFORE Lenis handler runs so the
      // watch on scrollLeft can immediately skip already-stacked cards on mount
      const cw = cardWidthPx.value
      const pl = paddingLeft.value
      const gf = gapFactor.value
      const gap = cardGap.value
      const initialSl = container.scrollLeft
      for (let i = 1; i < n.value; i++) {
        if (initialSl >= i * (cw + gap) + pl + cw * gf) {
          cardsThatTriggeredScroll.add(i)
        }
      }

      // Lenis: smooth horizontal scroll via wrapper scrollLeft + CSS transform on track
      lenis = new Lenis({
        wrapper: container,
        content: track.value,
        orientation: 'horizontal',
        gestureOrientation: 'horizontal',
        smoothWheel: true,
        duration: 1.5,
        lerp: 0.06,
      })

      // Clamp rendered scroll to lastTransitionEnd — on desktop this freezes transforms
      // while container keeps scrolling (creates the skim-through drift effect).
      // On mobile the container is physically locked so no drift occurs.
      lenis.on('scroll', () => {
        const limit = lastTransitionEnd.value
        if (isMobile.value && container.scrollLeft > limit) {
          container.scrollLeft = limit
        }
        scrollLeft.value = Math.min(container.scrollLeft, limit)
      })

      function raf(time) {
        lenis.raf(time)
        rafId = requestAnimationFrame(raf)
      }
      rafId = requestAnimationFrame(raf)

      window.addEventListener('scroll', onWindowScroll, { passive: true })

      watch(() => props.cards.length, () => {
        if (lenis) lenis.resize()
        cardsThatTriggeredScroll.clear()
        const cw = cardWidthPx.value
        const pl = paddingLeft.value
        const gf = gapFactor.value
        const gap = cardGap.value
        const container = scrollContainer.value
        if (!container) return
        const sl = container.scrollLeft
        for (let i = 1; i < n.value; i++) {
          if (sl >= i * (cw + gap) + pl + cw * gf) {
            cardsThatTriggeredScroll.add(i)
          }
        }
      })
    })

    onBeforeUnmount(() => {
      if (rafId) cancelAnimationFrame(rafId)
      if (momentumRafId) cancelAnimationFrame(momentumRafId)
      if (lenis) { lenis.destroy(); lenis = null }
      window.removeEventListener('resize', updateSize)
      window.removeEventListener('scroll', onWindowScroll)
    })

    // ── Mobile: auto-scroll page vertically when a card finishes stacking ──
    // On mobile each card sits at a different vertical position (ladder).
    // When a card reaches progress=1 (fully stacked at viewport left),
    // smoothly scroll the page to center that card in the screen.
    const cardsThatTriggeredScroll = new Set()

    const activeCardIndex = ref(0)

    const buttonBottomOffset = ref(24)
    const isButtonInBounds = ref(true)

    function updateButtonBounds() {
      if (!isMobile.value || !stackElement.value) {
        isButtonInBounds.value = true
        buttonBottomOffset.value = 24
        return
      }

      const rect = stackElement.value.getBoundingClientRect()
      const vh = window.innerHeight

      if (rect.bottom <= 0 || rect.top >= vh) {
        isButtonInBounds.value = false
        return
      }

      const containerVisTop = Math.max(rect.top, 0)
      const containerVisBottom = Math.min(rect.bottom, vh)
      const offset = Math.max(16, vh - containerVisBottom + 16)
      buttonBottomOffset.value = offset

      // Button viewport rect (position: fixed; left: 16px)
      const BTN_SIZE = 63
      const BTN_LEFT = 16
      const btnL = BTN_LEFT
      const btnR = BTN_LEFT + BTN_SIZE
      const btnB = vh - offset
      const btnT = btnB - BTN_SIZE

      // Check overlap with any visible card-inner
      const inners = stackElement.value.querySelectorAll('.card-inner')
      let overlaps = false
      for (const inner of inners) {
        const ir = inner.getBoundingClientRect()
        const cardT = Math.max(ir.top, containerVisTop)
        const cardB = Math.min(ir.bottom, containerVisBottom)
        const cardL = Math.max(ir.left, rect.left)
        const cardR = Math.min(ir.right, rect.right)
        if (btnL < cardR && btnR > cardL && btnT < cardB && btnB > cardT) {
          overlaps = true
          break
        }
      }
      isButtonInBounds.value = !overlaps
    }

    const buttonStyle = computed(() => {
      if (!isMobile.value) return {}
      return { bottom: `${buttonBottomOffset.value}px` }
    })

    let scrollRafId = null
    const onWindowScroll = () => {
      if (scrollRafId) return
      scrollRafId = requestAnimationFrame(() => {
        scrollRafId = null
        updateButtonBounds()
      })
    }

    let userInteracted = false

    watch(scrollLeft, (newScroll, oldScroll) => {
      if (!isMobile.value) return
      if (!userInteracted) return

      const cw = cardWidthPx.value
      const pl = paddingLeft.value
      const gf = gapFactor.value
      const gap = cardGap.value

      for (let i = 1; i < n.value; i++) {
        const transitionEnd = i * (cw + gap) + pl + cw * gf

        // Forward: card just finished stacking → scroll to center it
        if (oldScroll < transitionEnd && newScroll >= transitionEnd) {
          if (cardsThatTriggeredScroll.has(i)) continue
          cardsThatTriggeredScroll.add(i)
          activeCardIndex.value = i

          const slots = scrollContainer.value.querySelectorAll('.card-slot')
          if (slots[i]) {
            const rect = slots[i].getBoundingClientRect()
            const cardCenterY = rect.top + rect.height / 2
            const targetY = window.scrollY + cardCenterY - window.innerHeight / 2
            window.scrollTo({ top: targetY, behavior: 'smooth' })
          }
          break
        }

        // Backward: card just left the stack → scroll to center the previous card
        if (oldScroll > transitionEnd && newScroll <= transitionEnd) {
          cardsThatTriggeredScroll.delete(i)

          const targetIndex = i - 1
          activeCardIndex.value = targetIndex
          const slots = scrollContainer.value.querySelectorAll('.card-slot')
          if (slots[targetIndex]) {
            const rect = slots[targetIndex].getBoundingClientRect()
            const cardCenterY = rect.top + rect.height / 2
            const targetY = window.scrollY + cardCenterY - window.innerHeight / 2
            window.scrollTo({ top: targetY, behavior: 'smooth' })
          }
          break
        }
      }
    })

    return {
      scrollContainer,
      track,
      cardWidthPx,
      sentryWidth,
      getTransform,
      containerHeight,
      cardSlotStyle,
      trackStyles,
      navigate,
      showScrollButton,
      scrollAll,
      buttonStyle,
      stackElement,
      // Drag-to-scroll handlers exposed to template
      onPointerDown,
      onPointerMove,
      onPointerUp,
      onKeyDown,
    }
  },
}
</script>

<style scoped>
.horizontal-stack {
  width: 100%;
  overflow: hidden;
  position: relative;
  background: transparent;
}

.scroll-all-btn {
  position: absolute;
  z-index: 1;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  opacity: 1;
  transition: opacity 0.4s ease;
}
.scroll-all-btn.hidden {
  opacity: 0;
  pointer-events: none;
}
.scroll-all-btn img {
  display: block;
  width: 36px;
  height: 36px;
}

@media (min-width: 769px) {
  .scroll-all-btn {
    top: 12px;
    right: 12px;
  }
  .scroll-all-btn img {
    width: 50px;
    height: 50px;
  }
}

@media (max-width: 768px) {
  .scroll-all-btn {
    position: fixed;
    left: 16px;
  }
  .scroll-all-btn img {
    width: 47px;
    height: 47px;
  }
}

.scroll-container {
  width: 100%;
  overflow: hidden;
  position: relative;
  cursor: grab;
  touch-action: pan-y;
  user-select: none;
  -webkit-user-select: none;

}

.scroll-container:active {
  cursor: grabbing;
}

.scroll-container:focus-visible {
  outline: 0px;
} 
.scroll-all-btn:focus-visible {
  outline: 0px;
}

.scroll-track {
  display: flex;
  height: 100%;
  position: relative;
  align-items: center;
}

.card-slot {
  flex: 0 0 auto;
  height: 85%;
  position: relative;
  will-change: transform;
  transform-origin: center center;
}

.card-inner {
  width: 100%;
  height: 100%;
  border-radius: 12px;
  padding: 28px 32px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: 0px;
  color: #fff;
  position: relative;
}

.card-title {
  font-size: 1.4rem;
  font-weight: 700;
  text-align: center;
  flex-shrink: 0;
}

.card-title--inside {
  display: none;
}

.card-title--outside {
  margin: 0 0 12px 0;
}

.card-layout {
  display: flex;
  flex: 1;
  gap: 8px;
  min-height: 0;
}

.card-description {
  flex: 0 0 25%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.card-description {
  min-width: 0;
}
.card-description p {
  font-size: 1.1rem;
  line-height: 1.5;
  opacity: 0.9;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-features {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 10px;
  min-width: 0;
  overflow: hidden;
}

.feature-tag {
  font-size: 0.85rem;
  padding: 2px 8px;
  border-radius: 4px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  color: var(--text-secondary);
  white-space: nowrap;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
}
[data-theme="light"] .card-inner{
    background-color: var(--bg-primary) !important;
    color: var(--text-sprimary);
}

.card-image-wrap {
  flex: 1;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
}

.card-image {
  width: 100%;
  height: 100%;
  position: relative;
  will-change: transform;
  border-radius: 10px;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  -webkit-user-drag: none;
  user-select: none;
  -webkit-user-select: none;
  pointer-events: none;
}

.scroll-sentry {
  flex: 0 0 auto;
  height: 1px;
}
@media (min-width: 1200px){
    .card-description{
        flex: 0 0 35%;
    }
    .feature-tag{
      font-size: 1.1rem;
    }
    .card-image img{
      object-fit: contain;
    }
    .card-image-wrap, .card-image{
      border-radius: 0px;
    }
}
@media (min-width: 768px) and (max-width: 998px){
    .card-description{
        flex: 0 0 26%;
    }.feature-tag {
      font-size: 0.75rem;
    }.card-description p {
      font-size: 1rem;
    }
    .card-slot{
        height: 65%;
    }
}

@media (min-width: 769px) {
    .card-slot{
        padding-right: 3.5vw;
    }
    .scroll-container{
        padding-left: 5rem !important;
    }
  .card-title--outside {
    display: none;
  }

  .card-title--inside {
    display: block;
    margin: 0 0 8px 0;
    text-align: left;
  }

  .card-description {
    justify-content: flex-start;
    padding-top: 8px;
  }
  .feature-tag {
    white-space: normal;
    overflow: visible;
    text-overflow: clip;
  }
}

.card-title {
  cursor: pointer;
}

@media (max-width: 768px) {
  .scroll-container {
    padding-left: 7.5vw;
    padding-right: 7.5vw;
  }.feature-tag {
    font-size: 0.70rem;
  }.card-description p {
    font-size: 0.9rem;
  }
  .scroll-track {
    align-items: flex-start;
  }
  .card-inner {
    padding: 16px;
  }
  .card-layout {
    flex-direction: column;
  }
  .card-description {
    flex: none;
  }
  .card-features {
    margin-bottom: 6px;
  }
  .card-image-wrap {
    flex: 1;
    min-height: 120px;
  }
  .card-title {
    font-size: 1.1rem;
  }
  .card-description p,
  .card-features span {
    pointer-events: none;
  }
}
/* @media (max-width: 530px){
    .card-slot{
        height: 8;
    }
} */
@media (min-width: 530px) and (max-width: 768px) {
  .scroll-container {
    padding-left: 18vw !important;
    padding-right: 18vw !important;
  }
  .card-inner {
    padding: 12px !important;
  }
  .card-layout {
    gap: 12px;
  }
  .card-image-wrap {
    min-height: 140px !important;
  }
}
</style>
