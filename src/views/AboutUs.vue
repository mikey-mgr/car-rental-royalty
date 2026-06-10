<template>
  <div id="home" class="pb-4">
    <div class="" id="background-div"></div>
    <div class="about mb-5">
     <div class="container py-5 px-5 text-center about-us col-sm-10">
        <div class="row">
          <h1 class="display-6 ff-bold">About Us</h1>
          <h4 class="mb-5 ff-light">DeRoyalty Car Rental<br><br></h4>
          <p class="text-start mt-5" style="font-size: large;">Our vision is to become the most customer centric company in car rental industry. We will be persistent in building strong relationships with our customers and business partners.<br><br>
              We will be constantly focusing on innovation and delivery of simple high-quality service to our customers. <br><br>
              We are convinced that we need to innovate and control the high technology behind our services, and to participate only in markets where we can make significant contribution. <br><br>
              We don’t settle for anything less than excellence in every branch office and group in the company. We have courage to admit when and if we are wrong and courage to change. <br><br>
              We strive to create local opportunity, competitive advantage growth, and impact in every branch office around the World. <br><br>
              Our strategy is to build best-in-class IT platforms which will enable us to be productive and to deliver carefully customized services.
          </p>
        </div>
     </div>
    </div>
      <div class="mb-5 text-center">
        <h4 class="mb-4 display-5 animated ff-bold">
            Our World</h4>
        <p class="obs mb-0 animated">
            DeRoyalty Car Rental</p>
      </div>
    <div class="px-3 px-lg-0 map-stats d-flex flex-column flex-sm-row justify-content-evenly align-items-center mt-4">
      <div class="d-flex flex-column justify-content-center align-items-center mb-4">
        <span class="lh-1 mb-2 display-4 text-warning">
            1
        </span>

        <span>
            year operating
        </span>
      </div>
      <div class="d-flex flex-column justify-content-center align-items-center mb-4">
        <span class="lh-1 mb-2 display-4 text-warning">
            6
        </span>
        <span>
            countries
        </span>
      </div>
      <div class="d-flex flex-column justify-content-center align-items-center mb-4">
        <span class="lh-1 mb-2 display-4 text-warning">
            7
        </span>
        <span>
            branch offices
        </span>
      </div>
    </div>
    <!-- sections -->
    <div class="nav-pills nav-fill mt-3" id="sections">
            <!-- our vision dropdown -->
        <div class="nav-item text-start">
          <div class="nav-link page-sections sticky-top active"
              data-toggle="collapse" aria-label="Toggle-navigation"
                aria-expanded="true" data-target="ourVision"
                  aria-controls="ourVision"
                  @click="scrollToSection">
                  Our Branches
          </div>
          <div class="row g-0" id="ourVision">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid section-description-text pe-xl-0 p h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                  <h2 class="ff-bold">Branch Offices</h2>
                  <p>The business knowledge and know-how we generated over the decades in business worldwide, gave us
                    serious competitive advantage over other companies in the industry, especially rivals.
                  </p>
                </div>
              </div>
            </div> 
            <div class="col-md-6">
              <img src="../assets/AppImages/home_page/our-vision.jpg" class="img-fluid max-h-100-vh parallax-img" alt="Our vision">
            </div>
          </div>
          <div class="container py-5 px-5">
            <div class="row text-start d-flex justify-content-space-between">
              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h3 class="border-bottom border-secondary pb-2">Africa</h3>
                  <li><strong>Zimbabwe -</strong> Harare, Bulawayo</li>
                  <li><strong>South Africa -</strong> Johannesburg</li>
                  <li><strong>Angola -</strong> Luanda</li>
                </div>
              </div>

              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h3 class="border-bottom border-secondary pb-2">Europe</h3>
                  <li><strong>United Kingdom -</strong> London</li>
                </div>
              </div>

              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h3 class="border-bottom border-secondary pb-2">Middle East</h3>
                  <li><strong>United Arab Emirates -</strong> Dubai</li>
                </div>
              </div>

              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h3 class="border-bottom border-secondary pb-2">Australia</h3>
                  <li><strong>Melbourne</strong></li>
                  </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Our Locations dropdown -->
        <div class="nav-item text-start">
          <div class="nav-link page-sections sticky-top active"
              data-toggle="collapse" aria-label="Toggle-navigation"
                aria-expanded="true" data-target="ourLocation"
                  aria-controls="ourLocation"
                  @click="scrollToSection">
                  Worldwide Locations
          </div>
          <div class="row g-0" id="ourLocation">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid section-description-text pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                  <h2 class="ff-bold">Worldwide Locations</h2>
                  <p>We are a premium car rental group in Africa, Australia and the Middle East. It doesn't matter where you are located,
                      there will always be a convenient DeRoyalty Car Rental branch nearby to help you continue your journery.
                  </p>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <img src="../assets/AppImages/home_page/locations.jpg" class="img-fluid max-h-100-vh parallax-img" alt="our locations">
            </div>
          </div>
          <div class="col-12 map-container text-center w-100 bg-light-gray py-4 px-3 py-lg-9 fade-zoom">
            <div class="map-wrapper">
              <img src="../assets/AppImages/map/map-main.png" class="img-fluid" title="DeRoyalty Car Rental" alt="map" />
              <svg class="map-overlay" viewBox="0 0 1440 935" preserveAspectRatio="xMidYMid meet" aria-hidden="true">
                <defs>
                  <radialGradient id="cityGlow" cx="50%" cy="50%" r="50%">
                    <stop offset="0%" stop-color="white" stop-opacity="1" />
                    <stop offset="100%" stop-color="white" stop-opacity="0" />
                  </radialGradient>
                </defs>
                <g class="paths">
                  <path v-for="city in cities" :key="city.name" :d="getPath(city)" class="city-path" />
                </g>
                <g class="dots">
                  <circle v-for="city in cities" :key="city.name + '-dot'" :cx="city.x" :cy="city.y" r="25" class="city-dot" />
                  <circle :cx="harare.x" :cy="harare.y" r="8" class="city-dot harare-dot" />
                </g>
                <g class="glows">
                  <circle v-for="city in cities" :key="city.name + '-glow'" :cx="city.x" :cy="city.y" r="15" fill="url(#cityGlow)" class="city-glow" />
                  <circle :cx="harare.x" :cy="harare.y" r="15" fill="url(#cityGlow)" class="city-glow" />
                </g>
              </svg>
            </div>
          </div>
        </div>


        
    </div>
    <div class="container my-4 px-3">
      <h3>Visit us at our location</h3>
      <button @click="openDirections" class="btn btn-luxury">
        Get Directions to DeRoyalty Car Rental <i class="bi bi-geo-alt-fill ms-2"></i>
      </button>
    </div>
    <div class="container px-3 px-lg-3 pb-5" style="height: 60vh">
        <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d14235.753719474422!2d31.041371909903418!3d-17.895257864334262!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x1931a3f22ee38a99%3A0x757348b8f2bdbc19!2sDeRoyalty%20Car%20Rental!5e0!3m2!1sen!2szw!4v1781128963161!5m2!1sen!2szw" width="100%" height="100%" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
    </div>
  </div>
  </template>

<script>
export default {
  name: 'AboutUs',
  data() {
    return {
      // Map city coordinates (image 1440x935)
      harare: { x: 349, y: 613 },
      cities: [
        { name: 'Luanda', x: 71, y: 478 },
        { name: 'Bulawayo', x: 312, y: 661 },
        { name: 'Johannesburg', x: 300, y: 759 },
        { name: 'Dubai', x: 914, y: 399 },
        { name: 'London', x: 561, y: 244 },
        { name: 'Melbourne', x: 1266, y: 794 },
      ],
    };
  },
  methods: {
    openDirections() {
      // DeRoyalty Car Rental coordinates
      const destination = '-17.770207,31.052499';
      const destinationName = 'DeRoyalty Car Rental';
      
      // Open Google Maps immediately (without waiting for geolocation)
      const mapsUrl = `https://www.google.com/maps/dir/?api=1&destination=${destination}&destination_place_id=${destinationName}`;
      window.open(mapsUrl, '_blank');
      
      // Optionally try to get user's location in background (for future improvements)
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          () => {
            // Location obtained - could be used for analytics or future features
          },
          () => {
            // Silently handle error - user already has directions open
          },
          { timeout: 5000, maximumAge: 300000 } // 5 second timeout, cache for 5 minutes
        );
      }
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
    },

    getPath(city) {
      const x1 = city.x;
      const y1 = city.y;
      const x2 = this.harare.x;
      const y2 = this.harare.y;

      const mx = (x1 + x2) / 2;
      const my = (y1 + y2) / 2;

      let nx = -(y2 - y1);
      let ny = x2 - x1;
      const len = Math.hypot(nx, ny) || 1;
      nx /= len; ny /= len;

      const dist = Math.hypot(x2 - x1, y2 - y1);
      const offset = Math.min(400, Math.max(60, dist / 2));

      const cx = mx + nx * offset;
      const cy = my + ny * offset;

      return `M ${x2} ${y2} Q ${cx} ${cy} ${x1} ${y1}`;
    },

    prepMapPaths() {
      this.$nextTick(() => {
        const img = this.$el.querySelector('.map-wrapper img');
        const svg = this.$el.querySelector('.map-overlay');
        if (img && svg && img.naturalWidth && img.naturalHeight) {
          svg.setAttribute('viewBox', `0 0 ${img.naturalWidth} ${img.naturalHeight}`);
        }
        this.$el.querySelectorAll('.city-path').forEach(p => {
          const len = p.getTotalLength(); p.style.strokeDasharray = len; p.style.strokeDashoffset = len;
        });
      });
    },

    setupMapAnimations() {
      const pathElements = this.$el.querySelectorAll('.city-path');
      if (!pathElements.length) return;

      Array.from(pathElements).forEach(el => {
        const len = el.style.strokeDasharray ? parseFloat(el.style.strokeDasharray) : 0;
        if (len > 0) {
          el.animate([
            { strokeDashoffset: len },
            { strokeDashoffset: 0 }
          ], {
            duration: 5000,
            easing: 'ease-out',
            fill: 'forwards'
          });
        }
      });
    },

    initScrollAnimations() {
      const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
      };
      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting && !entry.target.classList.contains('animate-in')) {
            entry.target.classList.add('animate-in');
            if (entry.target.classList.contains('map-container')) {
              this.setupMapAnimations();
            }
          }
        });
      }, observerOptions);
      const elementsToAnimate = document.querySelectorAll('.fade-in-scroll, .slide-in-left, .slide-in-right, .fade-blur, .fade-zoom');
      elementsToAnimate.forEach(el => observer.observe(el));
    }
  },
  mounted() {
    // Calculate and set navbar height
    this.$nextTick(() => {
      const navbar = document.querySelector('.navbar');
      if (navbar) {
        const navbarHeight = navbar.getBoundingClientRect().height;
        const isHidden = document.documentElement.getAttribute('data-navbar-hidden') === 'true';
        document.documentElement.style.setProperty('--navbar-height', isHidden ? '0px' : `${navbarHeight}px`);
      }
    });
    // Keep sticky sections hidden briefly while navbar measurements settle
    document.documentElement.classList.add('measuring-navbar');
    setTimeout(() => {
      document.documentElement.classList.remove('measuring-navbar');
    }, 600);
    
    // Add scroll listener for progress bars
    window.addEventListener('scroll', this.handleScroll);
    this.handleScroll(); // Initial call

    // Pre-hide map paths so they're ready to draw on scroll
    this.prepMapPaths();

    // Initialize scroll-based fade animations
    this.$nextTick(() => {
      setTimeout(() => {
        this.initScrollAnimations();
      }, 100);
    });
  },
  beforeUnmount() {
    // Clean up scroll listener
    window.removeEventListener('scroll', this.handleScroll);
  }
}
</script>

<style scoped>
#home{
  transition: background-color 0.3s ease;
}
  .about-us{
  margin-top: -180px;
  margin-bottom: 2rem;
  background-color: var(--bg-primary) !important;
}
#background-div {
  margin-top: -55px;
  margin-bottom: -20px;
  background: url("../assets/AppImages/Rental-car.jpg");
  background-size: cover !important;
  min-height: 90vh;
}
.px-5{
    padding-left: 2.2rem !important;
    padding-right: 2.2rem !important;
}
#home .page-sections{
    display: flex;
    justify-content: center;
    padding: 20px 16px;
    color: var(--accent-color) !important;
    border: none !important;
    text-transform: uppercase;
    position: relative;
    overflow: visible;
    transition: background-color 0.3s ease, color 0.3s ease, transform 0.3s ease, top 0.3s ease;
  }
  
  /* Light mode page sections - keep gold text, change background to white */
  [data-theme="light"] .page-sections {
    color: var(--accent-color) !important;
    border-bottom: 4px solid rgba(212, 175, 55, 0.3) !important;
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
  }
  
  .ff-bold{
    font-family: var(--font-akrobat-bold);
  }
  .ff-semibold{
    font-family: var(--font-akrobat-semibold);
  }
  .ff-regular{
    font-family: var(--font-akrobat-regular);
  }
  .ff-light{
    font-family: var(--font-akrobat-light);
  }

  .page-sections:hover{
    cursor: pointer;
    background: linear-gradient(135deg, var(--hover-bg), var(--deep-sapphire)) !important;
    color: var(--ivory-white) !important;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(212, 175, 55, 0.3);
  }
  
  /* Light mode hover - lighter background */
  [data-theme="light"] .page-sections:hover {
    color: var(--accent-color) !important;
  }
  
  .page-sections.active {
    background: var(--bg-primary) !important;
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
  .sticky-top{
    position: sticky !important;
    top: var(--navbar-height, 70px);
    z-index: 100;
    will-change: top;
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
  
  .section-text{
    background-color: var(--bg-secondary);
  color: var(--text-primary) !important;
  transition: color 0.3s ease;
}

.section-text h2,
.section-text p {
  color: var(--text-primary) !important;
}
.img-fluid{
  object-fit: cover;
}
  div{
    transition: all 0.3s ease;
  }
  
h3 {
  color: var(--text-primary);
}

@media (min-width: 0px) and (max-width: 992px) {
    div.about-us{
      margin-top: -1rem;
    }
    #background-div{
      display: none;
      margin-top: 0px;
    }
  }
  @media (min-width: 0px) and (max-width: 500px) {
  }

/* Scroll animation classes (matching HomeView) */
.fade-in-scroll {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}
.fade-in-scroll.animate-in {
  opacity: 1;
  transform: translateY(0);
}
.slide-in-left {
  opacity: 0;
  transform: translateX(-50px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}
.slide-in-left.animate-in {
  opacity: 1;
  transform: translateX(0);
}
.slide-in-right {
  opacity: 0;
  transform: translateX(50px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}
.slide-in-right.animate-in {
  opacity: 1;
  transform: translateX(0);
}
.fade-blur {
  opacity: 0;
  filter: blur(10px);
  transition: opacity 1s ease-out, filter 1s ease-out;
}
.fade-blur.animate-in {
  opacity: 1;
  filter: blur(0);
}
.fade-zoom {
  opacity: 0;
  transform: scale(0.8);
  transition: opacity 1s ease-out, transform 1s ease-out;
}
.fade-zoom.animate-in {
  opacity: 1;
  transform: scale(1);
}

/* Map overlay styles (copied from HomeView) */
.map-wrapper {
  position: relative;
  display: inline-block;
  width: 100%;
  max-width: 100%;
}
.map-wrapper img.img-fluid {
  display: block;
  width: 100%;
  height: auto;
  z-index: 1;
}
.map-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: visible;
  z-index: 2;
}
.city-path {
  fill: none;
  stroke: white;
  stroke-width: 4;
  stroke-linecap: round;
  stroke-linejoin: round;
  filter: drop-shadow(0 0 4px rgba(0,0,0,0.45));
  stroke-opacity: 0.6;
  vector-effect: non-scaling-stroke;
}
@media (max-width: 768px) {
  .city-path { stroke-width: 2; }
}
.city-dot {
  fill: none;
  stroke: white;
  stroke-width: 2;
  stroke-opacity: 0.65;
  transform-origin: center;
  transform-box: fill-box;
  pointer-events: none;
  animation: pulseRing 3s ease-out infinite;
}
.harare-dot {
  fill: #ffd966;
  stroke: rgba(0,0,0,0.45);
  stroke-width: 1.2;
}

.city-glow {
  pointer-events: none;
}
@keyframes pulseRing {
  0% { transform: scale(0.6); opacity: 0.9; }
  50% { transform: scale(1.8); opacity: 0.35; }
  100% { transform: scale(2.6); opacity: 0; }
}
</style>
