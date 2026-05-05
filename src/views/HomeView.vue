<template>
  <div id="home">
    <div class="background-container parallax-bg" id="background-div">
      <!-- Video element -->
      <video 
        v-if="!videoPlayed"
        ref="heroVideo"
        class="hero-video"
        :class="{ 'fade-out': isVideoFadingOut }"
        muted 
        playsinline
        preload="auto"
        @ended="onVideoEnded"
        @timeupdate="checkVideoTime"
        @error="onVideoError"
        @loadeddata="onVideoLoaded"
        @canplay="onVideoCanPlay"
      >
        <source :src="videoSrc" type="video/mp4">
        Your browser does not support the video tag.
      </video>
      
      <!-- Image element (hidden initially, fades in before video ends) -->
      <div 
        class="hero-image"
        :class="{ 'fade-in': showImage }"
      ></div>
      
      <!-- Banner inside background -->
      <div class="container">
        <div class="container text-center banner py-5" :class="{ 'banner-visible': showImage }">
          <h3>Apex Car Rental Group</h3>
          <h5 class="display-6">Apex BRAND, Apex SERVICES!</h5>
        </div>
      </div>
    </div>
      <!-- sections -->
      <div class="nav-pills nav-fill mt-3" id="sections">
          <!-- our values dropdown -->
        <section class="nav-item text-start">
          <div class="nav-link page-sections active sticky-top"
              data-toggle="collapse" aria-label="Toggle navigation"
                  aria-expanded="true" data-target="ourValues"
                  aria-controls="ourValues" id="ourValuesBtn"
                  @click="scrollToSection">
                  Our Values
        </div>
          <div class="row g-0" id="ourValues" aria-labelledby="ourValuesBtn">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                  <h2 class="">Our Values</h2>
                  <p>We always place the interests of our customers first. We always conduct business with implementation of high
                    standards, trust, honesty, professionalism and ethical behaviour.
                  </p>
                </div>
              </div>
            </div>
            <div class="col-md-6 slide-in-right">
              <img src="../assets/AppImages/home_page/values.jpg" class="img-fluid max-h-100-vh parallax-img" alt="our values">
            </div>
            <!-- <div class="row p-5 mx-0"></div> -->
            <div class="row g-0 p-5 section-description-text fade-blur">
              <div class="col-lg-5 d-flex">
                <img src="../assets/AppImages/home_page/ceo1.jpg" class="img-fluid max-h-100-vh parallax-img float-animation" alt="Apex Ceo">
              </div>
              <div class="col-lg-7">
                <div class="container-fluid pe-xl-0 h-100">
                  <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 pt-1 py-xl-0">
                    <h1 class="p-0 m-0" style="font-size: 80px;">,,</h1>
                    <p class="typewriter-text">Talent is the No. 1 priority for a CEO. You think it's about vision and strategy, but you have to get the
                        right people first. Trustworthiness, honesty and reliability are the keystone values of Apex Car Rental.
                        We are entirely committed to creating fully-cusomized service at the right price, whether it is for an
                        individual or a company's needs. It's as simple as that - we are the brand you can trust. <br><br>
                        <strong>Mikey MGR</strong><br>CEO - Apex Car Rental
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

          <!-- our vision dropdown -->
        <div class="nav-item text-start">
          <div class="nav-link page-sections sticky-top active"
              data-toggle="collapse" aria-label="Toggle-navigation"
                aria-expanded="true" data-target="ourVision"
                  aria-controls="ourVision"
                  @click="scrollToSection">
                  Our Vision
        </div>
          <div class="row g-0" id="ourVision">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                  <h2 class="">Our Vision</h2>
                  <p>Our Vision is to become one of the leading brands in the rental industry
                      of Africa and to build the wide network of branches in the World
                  </p>
                </div>
              </div>
            </div>
            <div class="col-md-6 slide-in-right">
              <img src="../assets/AppImages/home_page/our-vision.jpg" class="img-fluid max-h-100-vh parallax-img" alt="our vision">
            </div>
          </div>
        </div>

        <!-- Our know-how dropdown -->
        <div class="nav-item text-start">
          <div class="nav-link page-sections sticky-top active"
              data-toggle="collapse" aria-label="Toggle-navigation"
                aria-expanded="true" data-target="ourKnowHow"
                  aria-controls="ourKnowHow"
                  @click="scrollToSection">
                  Our Know-How
        </div>
          <div class="row g-0" id="ourKnowHow">
            <div class="col-md-6 d-flex slide-in-left">
              <img src="../assets/AppImages/home_page/know-how.jpg" class="img-fluid max-h-100-vh parallax-img" alt="our know how">
            </div>
            <div class="col-md-6 text-white section-text slide-in-right">
              <div class="container-fluid pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                  <h2 class="">Our Know-How</h2>
                  <p>Apex Car Rental brand was founded by MGR Communications, a renowned world-class telecommunications and
                      marketing company from Zimbabwe. During the last decade MGR Communications has worked well with many globally
                      well-known companies around Europe, North America and Africa.
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-12 section-description-text fade-in-scroll">
            <p class="p-5 m-0">As a result, we've driven over $300 000 in sales and over 1 million leads for our businesses and clients. MGR Communications
              is a premier reputation film active in Harare, Melbourne and Dubai that specializes in printing, digital marketing, SEO activities,
              web design, and data to drive targeted visibility and engagement that builds brand reputation and delivers profit growth.
              <br><br>
              We've been in business in more than 5 countiries, and our portfolio includes clients such as Pizza Hut, Chicken Matty, Seasons
              Pharmaceuticals, Ministry of Health, ZAOGA FIF, etc.
              <br><br>
              Likewise, we know that hitting these goals moves businesses forward, and we believe that our client's success is the best
              measure of our performance.
              <br><br>
              Our Team is made up of award-winning marketers, business consultants, professors, designers, and developers, and we know what
              it takes to get real results online.
            </p>
          </div>
        </div>

        <!-- Our Locations dropdown -->
        <div class="nav-item text-start">
          <div class="nav-link page-sections sticky-top active"
              data-toggle="collapse" aria-label="Toggle-navigation"
                aria-expanded="true" data-target="ourLocation"
                  aria-controls="ourLocation"
                  @click="scrollToSection">
                  Our Locations
        </div>
          <div class="row g-0" id="ourLocation">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                  <h2 class="">Our Locations</h2>
                  <p>We are a premium car rental group in Africa, Australia and the Middle East. It doesn't matter where you are located,
                      there will always be a convenient Apex car rental branch nearby to help you continue your journery.
                  </p>
                </div>
              </div>
            </div>
            <div class="col-md-6 slide-in-right">
              <img src="../assets/AppImages/home_page/locations.jpg" class="img-fluid max-h-100-vh parallax-img" alt="locations">
            </div>
          </div>
          <div class="col-12 map-container text-center w-100 bg-light-gray py-4 px-3 py-lg-9 fade-zoom">
            <img src="../assets/AppImages/map/map-main.png" class="img-fluid max-h-100-vh" title="Apex Car Rental">
            <div class="px-3 px-lg-0 map-stats d-flex flex-column flex-sm-row justify-content-evenly align-items-center mt-4">
              <div class="d-flex flex-column justify-content-center align-items-center mb-4 fade-in-scroll">
                <div class="count-wrapper">
                  <span class="lh-1 mb-2 display-4 text-warning count-up" data-count="1">
                      0
                  </span>
                </div>

                <span>
                    year operating
                </span>
              </div>
              <div class="d-flex flex-column justify-content-center align-items-center mb-4 fade-in-scroll">
                <div class="count-wrapper">
                  <span class="lh-1 mb-2 display-4 text-warning count-up" data-count="6">
                      0
                  </span>
                </div>
                <span>
                    countries
                </span>
              </div>
              <div class="d-flex flex-column justify-content-center align-items-center mb-4 fade-in-scroll">
                <div class="count-wrapper">
                  <span class="lh-1 mb-2 display-4 text-warning count-up" data-count="7">
                      0
                  </span>
                </div>
                <span>
                    branch offices
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Our Fleets dropdown -->
        <div class="nav-item text-start">
          <div class="nav-link page-sections sticky-top active"
              data-toggle="collapse" aria-label="Toggle-navigation"
                aria-expanded="false" data-target="ourFleets"
                  aria-controls="ourFleets"
                  @click="scrollToSection">
                  Our Fleets
        </div>
          <div class="row g-0" id="ourFleets">
            <div class="col-lg-7 text-white section-text slide-in-left">
              <div class="container-fluid pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                  <h2 class="">Our Fleets</h2>
                  <p>You've got the drive to travel in luxury, and we've got the tools, know-how and knowledge to help you take charge
                      of your travel. Lets get you started on the path to your very own Apex car rental hire.
                  </p>
                </div>
              </div>
            </div>
            <div class="col-lg-5 slide-in-right">
              <img src="../assets/AppImages/cars/cars-exec.jpg" class="img-fluid max-h-100-vh parallax-img" alt="cars">
            </div>
          </div>
          
          <!--    display categories-->
          <div class="container fade-in-scroll">
            <div class="row">
              <div class="col-12 text-center">
                <h2 class="pt-3 mt-4"> Top Categories</h2>
              </div>
            </div>
            <div class="row justify-content-evenly">
              <div v-for="index in this.categorySize" :key="index"
                   class="col-md-6 col-xl-4 col-12 pt-3 my-2 justify-content-around card-stagger"
                   :style="`animation-delay: ${index * 0.1}s`">
                <CategoryBox :category="categories[index-1]" />
              </div>
            </div>
          </div>

          <!--    display Products-->
          <div class="container py-4 fade-in-scroll">
            <div class="row">
              <div class="col-12 text-center">
                <h2 class="pt-3 pb-3">Top Vehicles</h2>
              </div>
            </div>
            <div class="row justify-content-evenly">
              <div v-for="index in this.productSize" :key="index"
                class="col-md-6 col-xl-4 col-12 pt-3 my-2 justify-content-around card-stagger"
                :style="`animation-delay: ${index * 0.1}s`">
                <ProductBox :product="products[index-1]"/>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- <iframe src="https://maps.google.com/maps?q=35.856737, 10.606619&z=15&output=embed" width="360" height="270" frameborder="0" style="border:0"></iframe> -->
      <div class="container pt-4">
        <div class="row">
          <div class="col-md-8 mb-3">
            <h3 class="text-primary">TELL US WHERE YOU WANT TO GO</h3>
            We assure you we can get you there safe and sound!
          </div>
          <div class="col-md-4 col-xs-12 col-sm-12">
            <router-link class="btn btn-primary col-12 contact-us" :to="{name: 'ContactUs'}">CONTACT US</router-Link>
          </div>
        </div>
      </div>
  
  </div>
</template>

<script>
import CategoryBox from "../components/Category/CategoryBox.vue";
import ProductBox from "@/components/ProductBox.vue";
import heroVideo from '../assets/AppImages/home_page/home-vid.mp4';

export default {
  name: "HomeView",
  components: {CategoryBox, ProductBox},
  props: ["categories", "products"],
  data() {
    return {
      categorySize: 0,
      productSize: 0,
      showImage: false,
      isVideoFadingOut: false,
      videoPlayed: false,
      videoSrc: heroVideo,
      observedElements: [],
      videoObserver: null
    }
  },
  methods:{
    onVideoLoaded() {
      // Video loaded
    },
    onVideoCanPlay() {
      // Only play if video is in viewport
      const video = this.$refs.heroVideo;
      if (video) {
        const rect = video.getBoundingClientRect();
        const isInViewport = rect.top < window.innerHeight && rect.bottom > 0;
        
        if (isInViewport) {
          const playPromise = video.play();
          if (playPromise !== undefined) {
            playPromise
              .then(() => {
                // Video playing
              })
              .catch(() => {
                // Autoplay failed, show image immediately
                this.showImage = true;
                this.videoPlayed = true;
              });
          }
        } else {
          // Not in viewport, show image
          this.showImage = true;
          this.videoPlayed = true;
        }
      }
    },
    checkVideoTime() {
      const video = this.$refs.heroVideo;
      if (video && !this.videoPlayed && video.duration) {
        const timeRemaining = video.duration - video.currentTime;
        // Start fade transition 1.5 seconds before video ends
        if (timeRemaining <= 1.5 && !this.showImage) {
          this.showImage = true;
          this.isVideoFadingOut = true;
        }
      }
    },
    onVideoEnded() {
      this.videoPlayed = true;
      this.showImage = true;
    },
    onVideoError() {
      // Video error, show image immediately
      this.showImage = true;
      this.videoPlayed = true;
    },
    preloadAssets() {
      // Preload and cache the background image
      const img = new Image();
      img.src = require('../assets/AppImages/home_page/home.jpeg');
    },
    initScrollAnimations() {
      // Intersection Observer for fade-in animations
      const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
      };

      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting && !entry.target.classList.contains('animate-in')) {
            entry.target.classList.add('animate-in');
          }
        });
      }, observerOptions);

      // Observe all elements with animation classes
      const elementsToAnimate = document.querySelectorAll('.fade-in-scroll, .slide-in-left, .slide-in-right, .fade-blur, .fade-zoom');
      elementsToAnimate.forEach(el => {
        observer.observe(el);
        this.observedElements.push(el);
      });
      
      // Observe video/background to play when in viewport
      const backgroundDiv = document.getElementById('background-div');
      if (backgroundDiv) {
        const videoObserverOptions = {
          threshold: 0.3
        };
        
        this.videoObserver = new IntersectionObserver((entries) => {
          entries.forEach(entry => {
            if (entry.isIntersecting && !this.videoPlayed) {
              const video = this.$refs.heroVideo;
              if (video && video.paused) {
                video.play().catch(() => {
                  this.showImage = true;
                  this.videoPlayed = true;
                });
              }
            }
          });
        }, videoObserverOptions);
        
        this.videoObserver.observe(backgroundDiv);
      }
    },
    animateCountUp(element, target, duration = 2000) {
      const start = 0;
      const increment = target / (duration / 16);
      let current = start;

      const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
          element.textContent = target;
          clearInterval(timer);
        } else {
          element.textContent = Math.floor(current);
        }
      }, 16);
    },
    initCountUpAnimations() {
      const observerOptions = {
        threshold: 0.5,
        rootMargin: '0px'
      };

      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting && !entry.target.classList.contains('counted')) {
            entry.target.classList.add('counted');
            const target = parseInt(entry.target.getAttribute('data-count'));
            this.animateCountUp(entry.target, target);
          }
        });
      }, observerOptions);

      // Observe all count-up elements
      const countElements = document.querySelectorAll('.count-up');
      countElements.forEach(el => observer.observe(el));
    },
    handleParallax() {
      const scrolled = window.pageYOffset;
      const parallaxElements = document.querySelectorAll('.parallax-bg');
      
      parallaxElements.forEach(element => {
        const speed = 0.5;
        element.style.transform = `translateY(${scrolled * speed}px)`;
      });
    },
    scrollToSection(event) {
      // Prevent default collapse behavior
      event.preventDefault();
      
      const target = event.currentTarget;
      const targetId = target.getAttribute('data-target');
      const targetElement = document.getElementById(targetId);
      const navbar = document.querySelector('.navbar');
      const navbarHeight = navbar ? navbar.offsetHeight : 70;
      
      if (targetElement) {
        const elementPosition = targetElement.getBoundingClientRect().top + window.pageYOffset;
        const offsetPosition = elementPosition - navbarHeight;
        
        window.scrollTo({
          top: offsetPosition,
          behavior: 'smooth'
        });
      }
    },
    handleScroll() {
      // Update scroll progress for each section
      const sections = document.querySelectorAll('.page-sections');
      const navbar = document.querySelector('.navbar');
      const stickyTop = navbar ? navbar.offsetHeight : 70; // Dynamic navbar height
      
      sections.forEach((section, index) => {
        const targetId = section.getAttribute('data-target');
        const targetElement = document.getElementById(targetId);
        const nextSection = sections[index + 1];
        
        if (targetElement) {
          const sectionRect = section.getBoundingClientRect();
          
          let progress = 0;
          
          // Only start progress when section header reaches sticky position
          if (sectionRect.top <= stickyTop) {
            if (nextSection) {
              const nextSectionRect = nextSection.getBoundingClientRect();
              const sectionHeight = section.offsetHeight;
              
              // Progress fills as next section approaches current section
              if (nextSectionRect.top > stickyTop + sectionHeight) {
                // Calculate progress based on how close the next section is
                const distanceToNextSection = nextSectionRect.top - (stickyTop + sectionHeight);
                const parentElement = section.closest('.nav-item');
                const totalDistance = parentElement ? parentElement.offsetHeight - sectionHeight : 1000;
                
                progress = Math.min(100, Math.max(0, ((totalDistance - distanceToNextSection) / totalDistance) * 100));
              } else {
                // Next section has reached or overlapped, set to 100%
                progress = 100;
              }
            } else {
              // Last section - fill based on how much content has been scrolled past
              const parentElement = section.closest('.nav-item');
              if (parentElement) {
                const parentRect = parentElement.getBoundingClientRect();
                const sectionHeight = section.offsetHeight;
                const contentHeight = parentRect.height - sectionHeight;
                
                // Calculate how much of the content area has been scrolled
                const contentTop = parentRect.top + sectionHeight;
                const scrolledContent = stickyTop + sectionHeight - contentTop;
                
                // Check if we've reached the bottom of the page
                const scrolledToBottom = (window.innerHeight + window.scrollY) >= document.documentElement.scrollHeight - 10;
                
                // Progress based on content scrolled vs total scrollable content
                if (scrolledToBottom) {
                  progress = 100;
                } else if (contentHeight > 0) {
                  progress = Math.min(100, Math.max(0, (scrolledContent / contentHeight) * 100));
                } else {
                  progress = 100;
                }
              }
            }
          }
          
          // Set the CSS variable directly
          section.style.setProperty('--scroll-progress', `${progress}%`);
          section.setAttribute('data-progress', progress.toFixed(0));
        }
      });
    }
  },
  mounted() {
    this.categorySize = Math.min(6, this.categories.length );
    this.productSize = Math.min(8, this.products.length);
    this.preloadAssets();
    
    // Calculate and set navbar height
    this.$nextTick(() => {
      const navbar = document.querySelector('.navbar');
      if (navbar) {
        const navbarHeight = navbar.offsetHeight;
        document.documentElement.style.setProperty('--navbar-height', `${navbarHeight}px`);
      }
      
      // Initialize animations after DOM is ready
      setTimeout(() => {
        this.initScrollAnimations();
        this.initCountUpAnimations();
      }, 100);
    });
    
    // Add scroll listener for progress bars and parallax
    window.addEventListener('scroll', this.handleScroll);
    window.addEventListener('scroll', this.handleParallax);
    this.handleScroll(); // Initial call
  },
  beforeUnmount() {
    // Clean up scroll listeners
    window.removeEventListener('scroll', this.handleScroll);
    window.removeEventListener('scroll', this.handleParallax);
    
    // Disconnect video observer
    if (this.videoObserver) {
      this.videoObserver.disconnect();
    }
  }
};
</script>


<style scoped>
/* Animation Keyframes */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes cardStagger {
  from {
    opacity: 0;
    transform: translateY(20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInBlur {
  from {
    opacity: 0;
    filter: blur(10px);
  }
  to {
    opacity: 1;
    filter: blur(0);
  }
}

@keyframes fadeInZoom {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes glowPulse {
  0%, 100% {
    box-shadow: 0 0 10px rgba(212, 175, 55, 0.6);
  }
  50% {
    box-shadow: 0 0 30px rgba(255, 215, 0, 1), 0 0 50px rgba(212, 175, 55, 0.9);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes ping {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  75%, 100% {
    transform: scale(2);
    opacity: 0;
  }
}

/* Fade-in on scroll */
.fade-in-scroll {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}

.fade-in-scroll.animate-in {
  opacity: 1;
  transform: translateY(0);
}

/* Slide in from left */
.slide-in-left {
  opacity: 0;
  transform: translateX(-50px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}

.slide-in-left.animate-in {
  opacity: 1;
  transform: translateX(0);
}

/* Slide in from right */
.slide-in-right {
  opacity: 0;
  transform: translateX(50px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}

.slide-in-right.animate-in {
  opacity: 1;
  transform: translateX(0);
}

/* Fade in with blur */
.fade-blur {
  opacity: 0;
  filter: blur(10px);
  transition: opacity 1s ease-out, filter 1s ease-out;
}

.fade-blur.animate-in {
  opacity: 1;
  filter: blur(0);
}

/* Fade in zoom */
.fade-zoom {
  opacity: 0;
  transform: scale(0.8);
  transition: opacity 1s ease-out, transform 1s ease-out;
}

.fade-zoom.animate-in {
  opacity: 1;
  transform: scale(1);
}

/* Staggered card animations */
.card-stagger {
  opacity: 0;
  animation: cardStagger 0.6s ease-out forwards;
}

/* Count-up animation pulse */
.count-up {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease;
}

.count-up.counted {
  animation: pulse 0.5s ease-in-out;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

/* Parallax background */
.parallax-bg {
  will-change: transform;
  transition: transform 0.1s ease-out;
}

/* Enhanced hover effects for cards */
.card-stagger:hover {
  transform: translateY(-10px) scale(1.02);
  transition: all 0.3s ease;
}

/* Image zoom on hover */
.parallax-img {
  transition: transform 0.5s ease;
  display: block;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Wrap images in containers with overflow hidden */
.col-md-6:has(img),
.col-lg-5:has(img),
.col-lg-7:has(img) {
  overflow: hidden;
}

.fade-in-scroll:hover .parallax-img {
  transform: scale(1.05);
}

/* Banner animation */
.banner {
  position: absolute;
  bottom: 15%;
  left: 0;
  right: 0;
  margin: 0 auto;
  z-index: 10;
  background-color: transparent !important;
  color: var(--ivory-white);
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.8);
  width: 100%;
  max-width: 100%;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 1.5s ease-out 0.5s, transform 1.5s ease-out 0.5s;
}

.banner.banner-visible {
  opacity: 1;
  transform: translateY(0);
}

@media (max-width: 500px) {
  .banner {
    bottom: 20%;
  }
}

/* Floating animation */
.float-animation {
  animation: float 3s ease-in-out infinite;
}

/* Map stats with ping effect */
.map-stats > div {
  position: relative;
}

.count-wrapper {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 0.5rem;
}

.count-wrapper::before {
  content: '';
  position: absolute;
  transform: translate(-50%, -50%);
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(212, 175, 55, 0.3);
  animation: ping 2s cubic-bezier(0, 0, 0.2, 1) infinite;
  pointer-events: none;
  z-index: 0;
}

.count-up {
  position: relative;
  z-index: 1;
}

.contact-us{
  background-color: white;
  border-radius: 0;
  border-color: #0d6efd;
  color: black;
}
.contact-us:hover{
  background-color: #0d6efd;
}
#background-div {
  margin-top: -8.4rem;
  margin-bottom: 0;
  height: 100vh;
  position: relative;
  overflow: hidden;
}

/* Video styling */
.hero-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 2;
  opacity: 1;
  transition: opacity 1.5s ease-in-out;
}

.hero-video.fade-out {
  opacity: 0;
}

/* Image styling */
.hero-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url("../assets/AppImages/home_page/home.jpeg");
  background-size: cover;
  background-position: center;
  z-index: 1;
  opacity: 0;
  transition: opacity 1.5s ease-in-out;
}

.hero-image.fade-in {
  opacity: 1;
  z-index: 3;
}

/* Ensure content is above video/image */
.background-container ~ * {
  position: relative;
  z-index: 4;
}

/* Ensure sections have proper background */
.section-text {
  background-color: var(--bg-secondary);
  color: var(--text-primary);
  z-index: 4;
}

.section-description-text {
  background-color: var(--bg-card);
  z-index: 4;
}

#sections {
  background-color: var(--bg-primary);
}
/* .section-text{
  background-color: #102040;
}
.section-description-text{
  background-color: rgba(255, 253, 208, 0.85);
} */
.section-text p{
  font-size: large;
}
.img-fluid{
  object-fit: cover;
}
.map-stats {
  position: relative;
}
  #heading {
    font-weight: 400;
  }
  div{
    transition: all 1s ease-in-out 0s;
  }
  .sticky-top{
    position: sticky !important;
    top: var(--navbar-height, 70px);
    z-index: 100;
  }
  
  /* Fallback for different screen sizes */
  @media (max-width: 991px) {
    .sticky-top {
      top: 60px;
    }
  }
  .page-sections{
    display: flex;
    justify-content: center;
    padding: 20px 16px;
    background: linear-gradient(135deg, var(--bg-secondary), var(--royal-midnight-blue)) !important;
    color: var(--accent-color) !important;
    font-weight: 700 !important;
    font-size: 1.2rem !important;
    letter-spacing: 2px !important;
    border: none !important;
    border-bottom: 4px solid rgba(212, 175, 55, 0.2) !important;
    transition: all 0.3s ease;
    text-transform: uppercase;
    font-family: 'MV Boli', 'Brush Script MT', cursive !important;
    position: relative;
    overflow: visible;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  }
  
  /* Animated progress bar */
  .page-sections::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    height: 4px;
    width: 0%;
    background: linear-gradient(90deg, var(--gold-gradient-start), var(--gold-gradient-end));
    transition: width 0.3s ease;
    box-shadow: 0 0 10px rgba(212, 175, 55, 0.6);
    z-index: 1;
  }
  
  .page-sections[style*="--scroll-progress"]::after {
    width: var(--scroll-progress, 0%);
    animation: glowPulse 2s ease-in-out infinite;
  }
  
  .page-sections:hover{
    cursor: pointer;
    background: linear-gradient(135deg, var(--hover-bg), var(--deep-sapphire)) !important;
    color: var(--ivory-white) !important;
    letter-spacing: 2px !important;
    transform: translateY(-2px) scale(1.02);
    box-shadow: 0 4px 12px rgba(212, 175, 55, 0.3);
  }
  
  .page-sections.active {
    background: linear-gradient(135deg, var(--deep-sapphire), var(--royal-midnight-blue)) !important;
    border-bottom: 4px solid transparent !important;
  }
  
  .page-sections.active::after {
    animation: shimmer 2s infinite;
  }
  
  @keyframes shimmer {
    0%, 100% {
      box-shadow: 0 0 10px rgba(212, 175, 55, 0.6);
    }
    50% {
      box-shadow: 0 0 20px rgba(255, 215, 0, 0.8);
    }
  }
  .nav-pills {
    --bs-nav-pills-border-radius: 0px;
  }
  /* Ensure section content doesn't overlap */
  .nav-item {
    position: relative;
    z-index: 1;
  }
  
  .row.g-0 {
    position: relative;
    z-index: 0;
  }
  @media (min-width: 0px) and (max-width: 992px) {
    
  }
  @media (min-width: 0px) and (max-width: 500px) {
    /* Keep full screen dimensions on mobile */
    #background-div{
      height: 100vh;
    }
    
    /* Optimize video for mobile */
    .hero-video {
      object-fit: cover;
      object-position: center;
    }
    
    /* Optimize image for mobile */
    .hero-image {
      background-size: cover;
      background-position: center;
    }
    
    .banner{
      padding-top: 50px;
      padding-bottom: 40px;
    }
  }
</style>