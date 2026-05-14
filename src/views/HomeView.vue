<template>
  <div id="home">
    <div class="background-container" id="background-div">
      <!-- Video element -->
      <video 
        ref="heroVideo"
        class="hero-video"
        muted 
        playsinline
        preload="auto"
        loop
        crossorigin="anonymous"
        @error="onVideoError"
        @loadeddata="onVideoLoaded"
        @canplay="onVideoCanPlay"
      >
        <source :src="videoSrc" type="video/mp4">
        Your browser does not support the video tag.
      </video>
      
      <!-- Translucent black backdrop with copywriting -->
      <div class="hero-backdrop">
        <div class="hero-backdrop-content">
          <h1 class="hero-backdrop-title">Experience Luxury on the Road</h1>
          <p class="hero-backdrop-subtitle">Premium car rental services across Africa, Australia, and the Middle East</p>
          <div class="hero-backdrop-cta">
            <button class="btn btn-primary" @click="scrollToPickup">Book Your Ride</button>
          </div>
        </div>
      </div>
      
      <div class="scroll-indicator" :class="{ visible: showScrollIndicator, hidden: !showScrollIndicator }">
        <img :src="scrollDownIcon" alt="Scroll down indicator">
      </div>
    </div>
        <section class="pickup-location-area">
          <div class="container">
            <div class="pickup-card">
              <div class="pickup-card-header">
                <h4 class="m-0">Find a Vehicle</h4>
                <p class="m-0 pickup-subtitle">Choose location and dates, then browse available fleets.</p>
              </div>
              <form class="row g-3 align-items-end" @submit="goToVehicles">
                <div class="col-12 col-md-4">
                  <label class="form-label field-label" for="pickupLocation">Pick-up Location</label>
                  <select id="pickupLocation" v-model="pickupLocation" class="form-select pickup-input">
                    <option disabled value="">Select location</option>
                    <option>34 Frank Johnson</option>
                    <option>Harare International Airport</option>
                  </select>
                </div>
                <div class="col-12 col-md-3">
                  <label class="form-label field-label" for="pickupDate">Pickup Date</label>
                  <input
                    id="pickupDate"
                    v-model="pickupDate"
                    :type="pickupDateInputType"
                    :min="minDate"
                    class="form-control pickup-input"
                    placeholder="Select pickup date"
                    @focus="onDateFocus('pickup')"
                    @blur="onDateBlur('pickup')"
                  />
                </div>
                <div class="col-12 col-md-2">
                  <label class="form-label field-label" for="dropoffDate">Dropoff Date</label>
                  <input
                    id="dropoffDate"
                    v-model="dropoffDate"
                    :type="dropoffDateInputType"
                    :min="minDate"
                    class="form-control pickup-input"
                    placeholder="Select dropoff date"
                    @focus="onDateFocus('dropoff')"
                    @blur="onDateBlur('dropoff')"
                  />
                </div>
                <div class="col-12 col-md-1">
                  <label class="form-label field-label" for="dropoffTime">Time</label>
                  <input id="dropoffTime" v-model="dropoffTime" type="time" class="form-control pickup-input" placeholder="HH:MM" />
                </div>
                <div class="col-12 col-md-2">
                  <button class="btn btn-primary pickup-btn w-100" type="submit">
                    Find your Car
                  </button>
                </div>
              </form>
              <div class="row g-2 mt-1 align-items-end">
                <div class="col-12">
                  <label class="form-label field-label" for="vehicleKeyword">Vehicle name (optional)</label>
                  <input
                    id="vehicleKeyword"
                    v-model="vehicleKeyword"
                    type="search"
                    class="form-control pickup-input"
                    placeholder="e.g. Fortuner, Mercedes, Note"
                    autocomplete="off"
                  />
                </div>
              </div>

              <div class="brand-marquee mt-4">
                <div class="brand-track" :style="{ '--brand-duration': brandTickerDuration + 's' }">
                  <div class="brand-segment">
                    <div v-for="(logo, idx) in brandLogos" :key="'seg1-' + idx" class="brand-pill">
                      <img :src="logo.src" :alt="logo.alt">
                    </div>
                  </div>
                  <div class="brand-segment" aria-hidden="true">
                    <div v-for="(logo, idx) in brandLogos" :key="'seg2-' + idx" class="brand-pill">
                      <img :src="logo.src" :alt="logo.alt">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </section>
      
        <!-- sections -->


      <div class="nav-pills nav-fill" id="sections">
        <!-- Our Fleets dropdown -->
        <section class="nav-item text-start">
          <div class="nav-link page-sections sticky-top active"
              data-toggle="collapse" aria-label="Toggle-navigation"
                aria-expanded="false" data-target="ourFleets"
                  aria-controls="ourFleets"
                  @click="scrollToSection">
                  Our Fleets
          </div>
            <div class="row" id="ourFleets">
              <div class="col-12 text-center">
                <h2 class="py-3 mt-5">What we're offering...</h2>
              </div>
            </div>
          <div class="container fade-in-scroll mb-5">
          <!--    display categories & Products-->
            <div class="row justify-content-evenly">
              <!-- Categories -->
              <div v-for="(category, index) in displayedCategories" :key="'cat-' + index"
                   class="col-md-6 col-xl-4 col-12 pt-3 my-2 justify-content-around card-stagger"
                   :style="`animation-delay: ${index * 0.1}s`">
                <CategoryBox :category="category" />
              </div>
              <!-- Products -->
              <div v-for="(product, index) in displayedProducts" :key="'prod-' + index"
                   class="col-md-6 col-xl-4 col-12 pt-3 my-2 justify-content-around card-stagger"
                :style="`animation-delay: ${(index + displayedCategories.length) * 0.1}s`">
                <ProductBox :product="product"/>
              </div>
            </div>
            
            <!-- Show More Button (mobile only) -->
            <div v-if="isMobile && (hasMoreCategories || hasMoreProducts)" class="text-center mt-4">
              <button @click="showAllItems = !showAllItems" class="btn btn-outline-primary show-more-btn">
                <span v-if="!showAllItems">Show More Vehicles</span>
                <span v-else>Show Less</span>
                <i class="ms-2" :class="showAllItems ? 'bi bi-chevron-up' : 'bi bi-chevron-down'"></i>
              </button>
            </div>
          </div>
            
          <div class="row g-0">
              <div class="col-lg-7 text-white section-text slide-in-left">
                <div class="container-fluid section-description-text pe-xl-0 h-100">
                  <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                    <h2 class="">Our Fleets</h2>
                    <p>You've got the drive to travel in luxury, and we've got the tools, know-how and knowledge to help you take charge
                        of your travel. Lets get you started on the path to your very own Apex car rental hire.
                    </p>
                  </div>
                </div>
              </div>
              <div class="col-lg-5 slide-in-right">
                <img src="../assets/AppImages/products/cars-exec.jpg" class="img-fluid max-h-100-vh parallax-img" alt="cars">
              </div>
          </div>
        </section>
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
              <div class="container-fluid section-description-text pe-xl-0 h-100">
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
              <div class="container-fluid section-description-text pe-xl-0 h-100">
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
              <div class="container-fluid section-description-text pe-xl-0 h-100">
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
            <p class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 mb-0">As a result, we've driven over $300,000 in sales and over 1 million leads for our businesses and clients. MGR Communications
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
              <div class="container-fluid section-description-text pe-xl-0 h-100">
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
            <div class="pt-4 pb-4 bottom-section">
              <div class="container">
                <div class="row align-items-center g-4">
                  <div class="col-12 col-md-8 text-center text-md-start">
                    <h3 class="text-primary mb-2">TELL US WHERE YOU WANT TO GO</h3>
                    <p class="mb-0">We assure you we can get you there safe and sound!</p>
                  </div>
                  <div class="col-12 col-md-4 text-center text-md-end">
                    <router-link class="btn btn-primary contact-us px-4 py-2" :to="{name: 'ContactUs'}">
                      CONTACT US
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      <!-- <iframe src="https://maps.google.com/maps?q=35.856737, 10.606619&z=15&output=embed" width="360" height="270" frameborder="0" style="border:0"></iframe> -->
      
  
    </div>
  </div>
</template>

<script>
import CategoryBox from "../components/Category/CategoryBox.vue";
import ProductBox from "@/components/ProductBox.vue";
import heroVideo from '../assets/AppImages/home_page/home-vid.mp4';
import heroVideoMobile from '../assets/AppImages/home_page/home-vid-mobile.mp4';
import scrollDownIcon from '../assets/AppImages/home_page/scroll-down.svg';

export default {
  name: "HomeView",
  components: {CategoryBox, ProductBox},
  props: ["categories", "products"],
  data() {
    return {
      categorySize: 0,
      productSize: 0,
      showScrollIndicator: false,
      videoSrc: this.getVideoSource(),
      scrollDownIcon,
      observedElements: [],
      videoObserver: null,
      showAllItems: false,
      mobileBreakpoint: 768,
      isMobile: false,

      pickupLocation: "",
      pickupDate: null,
      dropoffDate: null,
      dropoffTime: null,
      pickupDateInputType: "text",
      dropoffDateInputType: "text",
      brandLogos: [
        { src: require("../assets/AppImages/car-logos/benz.png"), alt: "Benz" },
        { src: require("../assets/AppImages/car-logos/mazda.png"), alt: "Mazda" },
        { src: require("../assets/AppImages/car-logos/nissan.png"), alt: "Nissan" },
        { src: require("../assets/AppImages/car-logos/toyota.png"), alt: "Toyota" },
        { src: require("../assets/AppImages/car-logos/honda.png"), alt: "Honda" },
        { src: require("../assets/AppImages/car-logos/subaru.png"), alt: "Subaru" },
        { src: require("../assets/AppImages/car-logos/lamborghini.svg"), alt: "Lamborghini" },
        { src: require("../assets/AppImages/car-logos/porsche.png"), alt: "Porsche" },
        { src: require("../assets/AppImages/car-logos/jeep.svg"), alt: "Jeep" },
        { src: require("../assets/AppImages/car-logos/ferrari.png"), alt: "Ferrari" },
        { src: require("../assets/AppImages/car-logos/lexus.png"), alt: "Lexus" },
        { src: require("../assets/AppImages/car-logos/ford.png"), alt: "Ford" },
        { src: require("../assets/AppImages/car-logos/hyundai.svg"), alt: "Hyundai" },
        { src: require("../assets/AppImages/car-logos/volkswagen.png"), alt: "Volkswagen" },
        { src: require("../assets/AppImages/car-logos/volvo.svg"), alt: "Volvo" },
        { src: require("../assets/AppImages/car-logos/tesla.jpg"), alt: "Tesla" },
      ],
    }
  },
  methods:{
    goToVehicles(e) {
      e.preventDefault();
      this.$router.push({ name: 'VehiclesView' });
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    getVideoSource() {
      // Check if screen is mobile size (less than 768px)
      const isMobile = window.innerWidth < 768;
      return isMobile ? heroVideoMobile : heroVideo;
    },
    updateVideoSource() {
      // Update video source when window is resized
      const newVideoSrc = this.getVideoSource();
      if (this.videoSrc !== newVideoSrc) {
        this.videoSrc = newVideoSrc;
        // Reload video if it's currently playing
        if (this.$refs.heroVideo) {
          this.$refs.heroVideo.load();
        }
      }
    },
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
            playPromise.catch(() => {
              // Autoplay failed, video will loop silently when user interacts
            });
          }
        }
      }
    },
    onVideoError() {
      // Video error - the loop will continue if video is not fully broken
      console.warn('Video playback error, video will attempt to loop');
    },
    onDateFocus(field) {
      if (field === "pickup") {
        this.pickupDateInputType = "date";
      } else {
        this.dropoffDateInputType = "date";
      }
    },
    onDateBlur(field) {
      if (field === "pickup" && !this.pickupDate) {
        this.pickupDateInputType = "text";
      }
      if (field === "dropoff" && !this.dropoffDate) {
        this.dropoffDateInputType = "text";
      }
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
    scrollToPickup() {
      // Scroll to the pickup location section
      const pickupSection = document.querySelector('.pickup-location-area');
      if (pickupSection) {
        const elementPosition = pickupSection.getBoundingClientRect().top + window.pageYOffset;
        const navbar = document.querySelector('.navbar');
        const navbarHeight = navbar ? navbar.offsetHeight : 70;
        const offsetPosition = elementPosition - navbarHeight;
        
        window.scrollTo({
          top: offsetPosition,
          behavior: 'smooth'
        });
      }
    },
    checkMobile() {
      this.isMobile = window.innerWidth < this.mobileBreakpoint;
      // Reset showAllItems when switching from mobile to desktop
      if (!this.isMobile) {
        this.showAllItems = false;
      }
    },
    handleScroll() {
      const shouldHideIndicator = window.scrollY > (window.innerHeight * 0.35);
      if (shouldHideIndicator && this.showScrollIndicator) {
        this.showScrollIndicator = false;
      } else if (!shouldHideIndicator && !this.showScrollIndicator) {
        this.showScrollIndicator = true;
      }

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
  computed: {
    brandTickerDuration() {
      return Math.max(28, this.brandLogos.length * 3);
    },
    minDate() {
      const today = new Date();
      const tomorrow = new Date(today);
      tomorrow.setDate(tomorrow.getDate() + 1);
      const yyyy = tomorrow.getFullYear();
      let mm = tomorrow.getMonth() + 1;
      let dd = tomorrow.getDate();
      if (mm < 10) mm = "0" + mm;
      if (dd < 10) dd = "0" + dd;
      return `${yyyy}-${mm}-${dd}`;
    },
    allCategories() {
      return this.categories.slice(0, this.categorySize);
    },
    allProducts() {
      return this.products.slice(0, this.productSize);
    },
    displayedCategories() {
      if (!this.isMobile || this.showAllItems) {
        return this.allCategories;
      }
      return this.allCategories.slice(0, 2);
    },
    displayedProducts() {
      if (!this.isMobile || this.showAllItems) {
        return this.allProducts;
      }
      const displayedCatCount = this.displayedCategories.length;
      const maxItems = 3;
      const remainingSlots = Math.max(0, maxItems - displayedCatCount);
      return this.allProducts.slice(0, remainingSlots);
    },
    hasMoreCategories() {
      return this.allCategories.length > this.displayedCategories.length;
    },
    hasMoreProducts() {
      return this.allProducts.length > this.displayedProducts.length;
    },
    totalItems() {
      return this.allCategories.length + this.allProducts.length;
    },
    displayedTotal() {
      return this.displayedCategories.length + this.displayedProducts.length;
    }
  },
  mounted() {
    this.categorySize = Math.min(6, this.categories.length );
    this.productSize = Math.min(8, this.products.length);
    this.checkMobile();
    this.preloadAssets();
    this.showScrollIndicator = true; // Show scroll indicator immediately
    
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
    
    // Add resize listener to update video source and check mobile
    window.addEventListener('resize', this.updateVideoSource);
    window.addEventListener('resize', this.checkMobile);
    
    this.handleScroll(); // Initial call
  },
  beforeUnmount() {
    // Clean up scroll listeners
    window.removeEventListener('scroll', this.handleScroll);
    window.removeEventListener('scroll', this.handleParallax);
    window.removeEventListener('resize', this.updateVideoSource);
    window.removeEventListener('resize', this.checkMobile);
    
    // Disconnect video observer
    if (this.videoObserver) {
      this.videoObserver.disconnect();
    }
  }
};
</script>


<style scoped>
/* Search / Pickup section */
.pickup-location-area {
  position: relative;
  background: linear-gradient(135deg, rgba(16, 32, 64, 0.95), rgba(10, 20, 40, 0.95));
  padding: 2.25rem 0;
  z-index: 5;
}

[data-theme="light"] .pickup-location-area {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(248, 248, 248, 0.98));
}

.pickup-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  padding: 1.25rem 1.25rem 1.5rem;
  box-shadow: 0 14px 50px rgba(0, 0, 0, 0.25);
}

.pickup-card-header h4 {
  color: var(--text-primary);
  font-weight: 700;
  letter-spacing: 0.5px;
}

.pickup-subtitle {
  color: var(--text-primary);
  opacity: 0.85;
}

.pickup-input {
  border-radius: 10px;
  border: 1px solid var(--border-color);
  background-color: var(--bg-primary);
  color: var(--text-primary);
}

.pickup-btn {
  background-color: #f0c14b;
  color: black;
  border-color: #f0c14b;
  border-radius: 12px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.pickup-btn:hover {
  background-color: white;
  color: black;
  border-color: #f0c14b;
}

.brand-marquee {
  width: 100%;
  overflow: hidden;
  border-radius: 14px;
}

.brand-track {
  display: inline-flex;
  flex-wrap: nowrap;
  width: max-content;
  animation: brandTicker var(--brand-duration, 45s) linear infinite;
  will-change: transform;
  backface-visibility: hidden;
}

.brand-segment {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  gap: 12px;
  padding-right: 12px;
}

.brand-pill {
  flex: 0 0 auto;
  width: clamp(96px, calc((100vw - 140px) / 5), 168px);
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 10px;
}

.brand-pill img {
  max-height: 28px;
  max-width: 100%;
  width: auto;
  height: auto;
  object-fit: contain;
  opacity: 0.95;
  filter: drop-shadow(0 2px 6px rgba(0, 0, 0, 0.25));
}

@keyframes brandTicker {
  0% {
    transform: translate3d(0, 0, 0);
  }
  100% {
    transform: translate3d(-50%, 0, 0);
  }
}

.scroll-indicator {
  position: fixed;
  left: 50%;
  bottom: 12px;
  transform: translateX(-50%);
  z-index: 12;
  opacity: 0;
  transition: opacity 0.5s ease, transform 0.5s ease;
  pointer-events: none;
}

.scroll-indicator.visible {
  opacity: 1;
  transform: translateX(-50%) translateY(0);
  animation: scrollBounce 1.6s ease-in-out infinite;
}

.scroll-indicator.hidden {
  opacity: 0;
  transform: translateX(-50%) translateY(10px);
}

.scroll-indicator img {
  width: 38px;
  height: 38px;
  filter: brightness(0) invert(1);
}

[data-theme="light"] .scroll-indicator img {
  filter: brightness(0) invert(0);
}

@keyframes scrollBounce {
  0%, 100% {
    transform: translateX(-50%) translateY(0); 
  }
  50% {
    transform: translateX(-50%) translateY(8px); 
  }
}

.pickup-input::placeholder {
  color: rgba(128, 128, 128, 0.85);
}

@media (max-width: 768px) {
  .pickup-location-area {
    padding: 1.5rem 0;
  }
  .brand-pill {
    width: clamp(88px, 28vw, 140px);
    height: 48px;
  }
}
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

/* Banner animation - now fixed with background */
.banner h3,
.banner h5 {
  color: white;
  -webkit-text-stroke: 7px black;
  paint-order: stroke fill;
  text-shadow: 0 0 15px rgba(212, 175, 55, 0.8),
               0 0 30px rgba(212, 175, 55, 0.5);
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
  position: fixed;
top: 0;
  left: 0;
width: 100%;
  height: 100vh;
    overflow: hidden;
z-index: 0;
  margin: 0 !important;
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
}

/* Image styling - hidden as fallback */
.hero-image {
  display: none;
}

/* Ensure content is above video/image */
.background-container ~ * {
  position: relative;
  z-index: 4;
}

/* Add spacer for fixed background */
#home {
  position: relative;
  padding-top: 100vh;
  background-color: var(--bg-primary);
  isolation: isolate;
  z-index: 1;
}

/* Section nav: compact, no pill chrome */
#home #sections.nav-pills.nav-fill {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: fit-content;
  max-width: 100%;
}

#home #sections .nav-item {
  width: fit-content;
  max-width: 100%;
}

#home > .nav-item.text-start {
  width: fit-content;
  max-width: 100%;
}

#home .page-sections{
    display: flex;
    justify-content: center;
    padding: 20px 16px;
    color: var(--accent-color) !important;
    font-weight: 700 !important;
    font-size: 1.2rem !important;
    letter-spacing: 2px !important;
    border: none !important;
    text-transform: uppercase;
    font-family: 'MV Boli', 'Brush Script MT', cursive !important;
    position: relative;
    overflow: visible;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
    transition: background-color 0.3s ease, color 0.3s ease, transform 0.3s ease, top 0.3s ease;
  }


  /* Light mode page sections - keep gold text, change background to white */
  [data-theme="light"] .page-sections {
    color: var(--accent-color) !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
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
  
  /* Light mode hover - lighter background */
  [data-theme="light"] .page-sections:hover {
    background: linear-gradient(135deg, rgb(245, 245, 245), rgb(235, 235, 235)) !important;
    color: var(--accent-color) !important;
  }
  
  .page-sections.active {
    background: linear-gradient(135deg, var(--deep-sapphire), var(--royal-midnight-blue)) !important;
    border-bottom: 4px solid transparent !important;
  }
  
  /* Light mode active page sections - white background */
  [data-theme="light"] .page-sections.active {
    background: linear-gradient(135deg, rgb(255, 255, 255), rgb(248, 248, 248)) !important;
  }
  
  .page-sections.active::after {
    animation: shimmer 2s infinite;
  }
  
/* Push sections down to account for fixed background */
#sections {
  background-color: var(--bg-primary);
  position: relative;
  z-index: 1;
}

/* Ensure sections have proper background */
.section-text {
  background-color: var(--bg-secondary);
  color: var(--text-primary) !important;
  z-index: 4;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.section-text h2,
.section-text p {
  color: var(--text-primary) !important;
}

.section-description-text {
  background-color: var(--bg-card);
  z-index: 4;
}

/* Provide a solid base behind late-page sections like fleets + footer */
.bottom-section {
  background-color: var(--bg-primary);
}

/* Bottom section background */
.bottom-section {
  background-color: var(--bg-primary);
  padding: 2rem 1rem;
  margin: 0;
  width: 100%;
  position: relative;
  z-index: 1;
}

/* Hero Backdrop with Elegant Overlay */
.hero-backdrop {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    rgba(0, 0, 0, 0.75) 0%,
    rgba(0, 0, 0, 0.6) 50%,
    rgba(0, 0, 0, 0.75) 100%
  );
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 5;
  pointer-events: none;
  backdrop-filter: blur(2px);
}

.hero-backdrop-content {
  text-align: center;
  padding: 2rem;
  max-width: 900px;
  pointer-events: auto;
  animation: fadeInUp 0.8s ease-out;
}

.hero-backdrop-title {
  color: #ffffff;
  font-size: 3.5rem;
  font-weight: 800;
  margin-bottom: 1.25rem;
  text-shadow: 0 2px 20px rgba(0, 0, 0, 0.5);
  letter-spacing: -0.02em;
  background: linear-gradient(135deg, #ffffff 0%, #f0c14b 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;
}

.hero-backdrop-subtitle {
  color: rgba(255, 255, 255, 0.95);
  font-size: 1.35rem;
  margin-bottom: 2rem;
  line-height: 1.6;
  font-weight: 400;
  text-shadow: 0 1px 8px rgba(0, 0, 0, 0.3);
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}

.hero-backdrop-cta .btn {
  background: linear-gradient(135deg, #f0c14b 0%, #c18e32 100%);
  color: #1a1a1a;
  border: none;
  padding: 0.875rem 2.5rem;
  font-size: 1.1rem;
  font-weight: 700;
  border-radius: 50px;
  transition: all 0.3s ease;
  text-transform: uppercase;
  letter-spacing: 1px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  position: relative;
  overflow: hidden;
}

.hero-backdrop-cta .btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s ease;
}

.hero-backdrop-cta .btn:hover {
  background: linear-gradient(135deg, #ffd966 0%, #d4a03a 100%);
  color: #000000;
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(212, 175, 55, 0.5);
}

.hero-backdrop-cta .btn:hover::before {
  left: 100%;
}

.hero-backdrop-cta .btn:active {
  transform: translateY(0);
}

/* Light mode adjustments */
[data-theme="light"] .hero-backdrop {
  background: linear-gradient(
    135deg,
    rgba(0, 0, 0, 0.65) 0%,
    rgba(0, 0, 0, 0.5) 50%,
    rgba(0, 0, 0, 0.65) 100%
  );
}

[data-theme="light"] .hero-backdrop-title {
  background: linear-gradient(135deg, #f0c14b 0%, #ffd700 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

[data-theme="light"] .hero-backdrop-subtitle {
  color: rgba(255, 255, 255, 0.98);
}

/* Mobile responsiveness */
@media (max-width: 768px) {
  .hero-backdrop-content {
    padding: 1.5rem;
    max-width: 95%;
  }
  
  .hero-backdrop-title {
    font-size: 2rem;
    margin-bottom: 0.75rem;
  }
  
  .hero-backdrop-subtitle {
    font-size: 1rem;
    margin-bottom: 1.5rem;
    padding: 0 0.5rem;
  }
  
  .hero-backdrop-cta .btn {
    padding: 0.7rem 1.8rem;
    font-size: 0.95rem;
  }
}

@media (max-width: 480px) {
  .hero-backdrop-title {
    font-size: 1.6rem;
  }
  
  .hero-backdrop-subtitle {
    font-size: 0.9rem;
  }
  
  .hero-backdrop-content {
    padding: 1rem;
  }
}

/* Optional: Add a subtle decorative line */
.hero-backdrop-content::before {
  content: '';
  display: block;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #f0c14b, transparent);
  margin: 0 auto 1.5rem auto;
  border-radius: 2px;
}

@media (max-width: 768px) {
  .hero-backdrop-content::before {
    width: 40px;
    margin-bottom: 1rem;
  }
}

/* Keep banner styling consistent */
.banner {
  position: fixed;
  bottom: 15%;
  left: 0;
  right: 0;
  margin: 0 auto;
  z-index: 10;
  background-color: transparent !important;
  width: 100%;
  max-width: 100%;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 1.5s ease-out 0.5s, transform 1.5s ease-out 0.5s;
}

.banner h5 {
  color: white;
  text-shadow: 0 0 20px rgba(212, 175, 55, 0.8), 0 0 40px rgba(212, 175, 55, 0.5);
  font-weight: 700;
  letter-spacing: 2px;
}

@media (max-width: 500px) {
  .banner {
    bottom: 20%;
  }
  .banner h5 {
    font-size: 1.2rem;
  }
}

/* Animation keyframes if not already present */
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

/* Ensure banner is visible on fixed background */
.banner {
  position: fixed;
  bottom: 15%;
  left: 0;
  right: 0;
  margin: 0 auto;
  z-index: 10;
  background-color: transparent !important;
  color: var(--ivory-white);
  width: 100%;
  max-width: 100%;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 1.5s ease-out 0.5s, transform 1.5s ease-out 0.5s;
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
    transition: top 0.3s ease;
    will-change: top;
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
    background-color: var(--bg-primary);
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

/* Show More Button Styling */
.show-more-btn {
  background: linear-gradient(135deg, transparent, transparent);
  border: 2px solid var(--accent-color, #f0c14b);
  color: var(--accent-color, #f0c14b);
  padding: 0.75rem 2rem;
  font-weight: 600;
  border-radius: 50px;
  transition: all 0.3s ease;
  letter-spacing: 0.5px;
}

.show-more-btn:hover {
  background: linear-gradient(135deg, var(--accent-color, #f0c14b), var(--gold-gradient-end, #c18e32));
  color: #1a1a1a;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(212, 175, 55, 0.4);
  border-color: transparent;
}

.show-more-btn:active {
  transform: translateY(0);
}

/* Mobile adjustments */
@media (max-width: 768px) {
  .show-more-btn {
    padding: 0.6rem 1.5rem;
    font-size: 0.9rem;
    width: auto;
    min-width: 200px;
  }
}

@media (max-width: 480px) {
  .show-more-btn {
    min-width: 180px;
    padding: 0.5rem 1.2rem;
    font-size: 0.85rem;
  }
}
</style>