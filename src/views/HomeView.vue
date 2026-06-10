<template>
  <div id="home">
    <div class="background-container" id="background-div">
      <!-- Static hero image (JS may upgrade to video when appropriate) -->
      <div id="hero-container" class="hero-image" :style="{ backgroundImage: `url(${heroImage})` }"></div>
      
      <!-- Translucent black backdrop with copywriting -->
      <div class="hero-backdrop">
        <div class="hero-backdrop-content">
          <p class="hero-backdrop-subtitle">DeRoyalty Car Rentals</p>
          <h1 class="hero-backdrop-title">ROYAL BRAND, ROYAL SERVICES!</h1>
          <!-- <div class="hero-backdrop-cta">
            <button class="btn btn-primary" @click="scrollToPickup">Book Your Ride</button>
          </div> -->
        </div>
      </div>
      
      <div class="scroll-indicator" :class="{ visible: showScrollIndicator, hidden: !showScrollIndicator }">
        <img :src="scrollDownIcon" alt="Scroll down indicator">
      </div>
    </div>
        <div class="pickup-location-area">
          <div class="container">
            <div class="pickup-card">
              <div class="pickup-card-header">
                <h4 class="m-0 ff-semibold">Find a Vehicle</h4>
                <p class="m-0 pickup-subtitle">Choose location and dates, then browse available fleets.</p>
              </div>
              <form class="row g-3 align-items-end" @submit="goToVehicles">
                
                <div class="col-12 col-md-4">
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
                    @change="onPickupDateChange"
                  />
                </div>
                <div class="col-12 col-md-4">
                  <label class="form-label field-label" for="dropoffDate">Dropoff Date</label>
                  <input
                    id="dropoffDate"
                    v-model="dropoffDate"
                    :type="dropoffDateInputType"
                    :min="dropoffMinDate"
                    class="form-control pickup-input"
                    placeholder="Select dropoff date"
                    @focus="onDateFocus('dropoff')"
                    @blur="onDateBlur('dropoff')"
                    @change="validateDropoffDate"
                  />
                </div>
                <div class="col-12 col-md-4">
                  <label class="form-label field-label" for="dropoffTime">Dropoff Time</label>
                  <input id="dropoffTime" v-model="dropoffTime" type="time" class="form-control pickup-input" placeholder="HH:MM" />
                </div>
                
                <div class="col-12 col-md-4">
                  <label class="form-label field-label" for="pickupLocation">Pick-up Location</label>
                  <div class="position-relative">
                    <input
                      id="pickupLocation"
                      v-model="pickupLocation"
                      type="text"
                      class="form-control pickup-input"
                      placeholder="Select location"
                      readonly
                      autocomplete="off"
                      @focus="showLocationDropdown = true"
                      @blur="hideLocationDropdown"
                    />
                    <ul v-if="showLocationDropdown" class="vehicle-dropdown">
                      <li
                        v-for="loc in locationOptions"
                        :key="loc"
                        @mousedown.prevent="selectLocation(loc)"
                      >
                        {{ loc }}
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="col-12 col-md-4">
                  <label class="form-label field-label" for="vehicleKeyword">Vehicle name</label>
                  <div class="position-relative">
                    <input
                      id="vehicleKeyword"
                      v-model="vehicleKeyword"
                      type="text"
                      inputmode="search"
                      class="form-control pickup-input"
                      placeholder="e.g. Fortuner, Mercedes, Note"
                      autocomplete="off"
                      @focus="showVehicleDropdown = true"
                      @blur="hideVehicleDropdown"
                    />
                    <ul v-if="showVehicleDropdown && filteredVehicles.length" class="vehicle-dropdown">
                      <li
                        v-for="p in filteredVehicles"
                        :key="p.id"
                        @mousedown.prevent="selectVehicle(p.name)"
                      >
                        {{ p.name }}
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="col-12 col-md-4">
                  <button class="btn btn-primary pickup-btn w-100" type="submit">
                    Find your Car
                  </button>
                </div>
              </form>

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

        </div>
      
        <!-- sections -->
      <div id="sections">
          <div class="nav-link page-sections sticky-top" data-bs-target="#ourFleets" @click="scrollToSection">
            01 Our Fleet
          </div>
          <div class="our-fleets collapse show" id="ourFleets">
            <div class="row g-0">
                <div class="col-lg-7 text-white section-text slide-in-left">
                  <div class="container-fluid section-description-text pe-xl-0 h-100">
                    <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                      <h1 class="ff-bold display-1">01</h1>
                      <h2 class="ff-bold">Our Fleet</h2>
                      <p>You've got the drive to travel in luxury, and we've got the tools, know-how and knowledge to help you take charge
                          of your travel. Lets get you started on the path to your very own DeRoyalty Car Rental hire.
                      </p>
                    </div>
                  </div>
                </div>
                <div class="col-lg-5 slide-in-right">
                  <img src="../assets/AppImages/products/cars-exec.jpg" class="img-fluid max-h-100-vh parallax-img" alt="cars">
                </div>
            </div>
            <HorizontalCardStack :cards="fleetCards" />
              
            
          </div>
          <div class="nav-link page-sections sticky-top" data-bs-target="#ourValues" id="ourValuesBtn" @click="scrollToSection">
            02 Our Values
          </div>
          <div class="collapse show" id="ourValues">
            <div class="row g-0 z-hide-arrow" aria-labelledby="ourValuesBtn">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid section-description-text pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                    <h1 class="ff-bold display-1">02</h1>
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
            <div class="row g-0 section-description-text fade-blur">
              <div class="col-lg-5 ps-xl-4 p-4 pb-0 d-flex">
                <img src="../assets/AppImages/home_page/ceo1.jpg" class="img-fluid max-h-100-vh parallax-img float-animation" alt="DeRoyalty Ceo">
              </div>
              <div class="col-lg-7">
                <div class="container-fluid pe-xl-0 h-100">
                  <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 pt-1 py-xl-0">
                    <h1 class="p-0 m-0" style="font-size: 80px;">,,</h1>
                    <p class="typewriter-text">Talent is the No. 1 priority for a CEO. You think it's about vision and strategy, but you have to get the
                        right people first. Trustworthiness, honesty and reliability are the keystone values of DeRoyalty Car Rental.
                        We are entirely committed to creating fully-cusomized service at the right price, whether it is for an
                        individual or a company's needs. It's as simple as that - we are the brand you can trust. <br><br>
                        <strong>Brian Langsoni</strong><br>CEO - DeRoyalty Car Rental
                    </p>
                  </div>
                </div>
              </div>
            </div>
            </div>
          </div>
          <div class="nav-link page-sections sticky-top" data-bs-target="#ourVision" @click="scrollToSection">
            03 Our Vision
          </div>
          <div class="collapse show" id="ourVision">
            <div class="row g-0">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid section-description-text pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                    <h1 class="ff-bold display-1">03</h1>
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
          <div class="nav-link page-sections sticky-top" data-bs-target="#ourKnowHow" @click="scrollToSection">
            04 Our Know-How
          </div>
          <div class="collapse show" id="ourKnowHow">
            <div class="row g-0">
            <div class="col-md-6 d-flex slide-in-left">
              <img src="../assets/AppImages/home_page/know-how.jpg" class="img-fluid max-h-100-vh parallax-img" alt="our know how">
            </div>
            <div class="col-md-6 text-white section-text slide-in-right">
              <div class="container-fluid section-description-text pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                    <h1 class="ff-bold display-1">04</h1>
                  <h2 class="">Our Know-How</h2>
                  <p>DeRoyalty Car Rental brand was founded by Braso Communications, a renowned world-class telecommunications and
                      marketing company from Zimbabwe. During the last decade Braso Communications has worked well with many globally
                      well-known companies around Europe, North America and Africa.
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-12 section-description-text fade-in-scroll">
            <p class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 mb-0">As a result, we've driven over $300,000 in sales and over 1 million leads for our businesses and clients. Braso Communications
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
          <div class="nav-link page-sections sticky-top" data-bs-target="#ourLocation" @click="scrollToSection">
            05 Our Locations
          </div>
          <div class="collapse show" id="ourLocation">
            <div class="row g-0">
            <div class="col-md-6 text-white section-text slide-in-left">
              <div class="container-fluid section-description-text pe-xl-0 h-100">
                <div class="d-flex flex-column justify-content-center align-items-start max-w-500 h-100 mx-auto ms-lg-0 me-lg-auto px-3 px-lg-3 py-5 py-xl-0">
                    <h1 class="ff-bold display-1">05</h1>
                  <h2 class="">Our Locations</h2>
                  <p>We are a premium car rental group in Africa, Australia and the Middle East. It doesn't matter where you are located,
                      there will always be a convenient DeRoyalty Car Rental branch nearby to help you continue your journery.
                  </p>
                </div>
              </div>
            </div>
            <div class="col-md-6 slide-in-right">
              <img src="../assets/AppImages/home_page/locations.jpg" class="img-fluid max-h-100-vh parallax-img" alt="locations">
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
                  <!-- Paths from cities to Harare -->
                  <g class="paths">
                    <path v-for="city in cities" :key="city.name" :d="getPath(city)" class="city-path" />
                  </g>
                  <!-- City dots -->
                  <g class="dots">
                    <circle v-for="city in cities" :key="city.name + '-dot'" :cx="city.x" :cy="city.y" r="25" class="city-dot" />
                    <!-- Harare center marker -->
                    <circle :cx="harare.x" :cy="harare.y" r="7" class="city-dot harare-dot" />
                  </g>
                  <g class="glows">
                    <circle v-for="city in cities" :key="city.name + '-glow'" :cx="city.x" :cy="city.y" r="15" fill="url(#cityGlow)" class="city-glow" />
                    <circle :cx="harare.x" :cy="harare.y" r="15" fill="url(#cityGlow)" class="city-glow" />
                  </g>
                </svg>
              </div>
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
      </div>
  </div>
</template>

<script>
import HorizontalCardStack from "@/components/HorizontalCardStack.vue";
import heroImage from '../assets/AppImages/home_page/royal-car-rental-group-hero.jpg';
import scrollDownIcon from '../assets/AppImages/home_page/scroll-down.svg';
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle'
import { resolveImageUrl } from '@/utils/resolveImageUrl';

export default {
  name: "HomeView",
  components: {HorizontalCardStack},
  props: ["categories", "products"],
  data() {
    return {
      categorySize: 0,
      productSize: 0,
      showScrollIndicator: false,
      scrollDownIcon,
      observedElements: [],
      showAllItems: false,
      mobileBreakpoint: 768,
      isMobile: false,
      categorySort: 'default',
      productSort: 'default',

      pickupLocation: "",
      pickupDate: null,
      dropoffDate: null,
      dropoffTime: null,
      pickupDateInputType: "text",
      dropoffDateInputType: "text",
      vehicleKeyword: "",
      showVehicleDropdown: false,
      showLocationDropdown: false,
      locationOptions: ["2870 Mainway Meadows Waterfalls", "Harare International Airport"],
      brandLogos: [
        { src: require("../assets/AppImages/car-logos/benz.png"), alt: "Benz" },
        { src: require("../assets/AppImages/car-logos/nissan.png"), alt: "Nissan" },
        { src: require("../assets/AppImages/car-logos/toyota.png"), alt: "Toyota" },
        { src: require("../assets/AppImages/car-logos/honda.png"), alt: "Honda" },
        { src: require("../assets/AppImages/car-logos/ford.png"), alt: "Ford" },
        { src: require("../assets/AppImages/car-logos/isuzu.png"), alt: "Isuzu" },
        { src: require("../assets/AppImages/car-logos/LandRover.svg.png"), alt: "Land Rover" },
        { src: require("../assets/AppImages/car-logos/rangeRover.png"), alt: "Range Rover" }
      ],
      heroImage: heroImage,
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
    }
  },
  methods:{
    goToVehicles(e) {
      e.preventDefault();
      this.showVehicleDropdown = false;
      const query = {};
      if (this.vehicleKeyword && this.vehicleKeyword.trim()) {
        query.q = this.vehicleKeyword.trim();
      }
      if (this.pickupLocation) query.pickupLocation = this.pickupLocation;
      if (this.pickupDate) query.pickupDate = this.pickupDate;
      if (this.dropoffDate) query.dropoffDate = this.dropoffDate;
      if (this.dropoffTime) query.dropoffTime = this.dropoffTime;
      this.$router.push({ name: 'VehiclesView', query });
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    hideVehicleDropdown() {
      setTimeout(() => { this.showVehicleDropdown = false; }, 200);
    },
    selectVehicle(name) {
      this.vehicleKeyword = name;
      this.showVehicleDropdown = false;
    },
    hideLocationDropdown() {
      setTimeout(() => { this.showLocationDropdown = false; }, 200);
    },
    selectLocation(loc) {
      this.pickupLocation = loc;
      this.showLocationDropdown = false;
    },
    validateDropoffDate() {
      if (this.pickupDate && this.dropoffDate) {
        const pickup = new Date(this.pickupDate);
        const dropoff = new Date(this.dropoffDate);
        const dayAfter = new Date(pickup);
        dayAfter.setDate(dayAfter.getDate() + 1);
        if (dropoff < dayAfter) {
          this.dropoffDate = null;
        }
      }
    },
    onPickupDateChange() {
      if (this.pickupDate && this.dropoffDate) {
        const pickup = new Date(this.pickupDate);
        const dropoff = new Date(this.dropoffDate);
        const dayAfter = new Date(pickup);
        dayAfter.setDate(dayAfter.getDate() + 1);
        if (dropoff < dayAfter) {
          this.dropoffDate = null;
        }
      }
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
            // Trigger path drawing when the map container enters view
            if (entry.target.classList.contains('map-container')) {
              this.setupMapAnimations();
            }
          }
        });
      }, observerOptions);

      // Observe all elements with animation classes
      const elementsToAnimate = document.querySelectorAll('.fade-in-scroll, .slide-in-left, .slide-in-right, .fade-blur, .fade-zoom');
      elementsToAnimate.forEach(el => {
        observer.observe(el);
        this.observedElements.push(el);
      });
      
      // Hero is a static background image; no video playback.
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
    // Build a quadratic bezier path from a city to Harare
    getPath(city) {
      const x1 = city.x;
      const y1 = city.y;
      const x2 = this.harare.x;
      const y2 = this.harare.y;

      // Midpoint
      const mx = (x1 + x2) / 2;
      const my = (y1 + y2) / 2;

      // Perpendicular direction for control point
      let nx = -(y2 - y1);
      let ny = x2 - x1;
      const len = Math.hypot(nx, ny) || 1;
      nx /= len; ny /= len;

      // Curvature amount scales with distance; increase for more pronounced parabolic arcs
      const dist = Math.hypot(x2 - x1, y2 - y1);
      // Use a larger fraction of the distance to create stronger curvature; clamp to reasonable bounds
      const offset = Math.min(400, Math.max(60, dist / 2));

      const cx = mx + nx * offset;
      const cy = my + ny * offset;

      return `M ${x2} ${y2} Q ${cx} ${cy} ${x1} ${y1}`;
    },

    // Pre-hide all paths on mount so they're ready to animate
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

    handleParallax() {
      const scrolled = window.pageYOffset;
      const parallaxElements = document.querySelectorAll('.parallax-bg');
      
      parallaxElements.forEach(element => {
        const speed = 0.5;
        element.style.transform = `translateY(${scrolled * speed}px)`;
      });
    },
    scrollToSection(event) {
      event.preventDefault();
      
      const target = event.currentTarget;
      const targetId = target.getAttribute('data-bs-target');
      const targetElement = document.getElementById(targetId ? targetId.replace('#', '') : null);

      // Update active class
      document.querySelectorAll('.page-sections').forEach(s => s.classList.remove('active'));
      target.classList.add('active');

      // On mobile: toggle accordion via Bootstrap collapse API
      if (window.innerWidth < 1200 && targetElement && targetElement.classList.contains('collapse')) {
        let bsCollapse = bootstrap.Collapse.getInstance(targetElement);
        if (!bsCollapse) {
          bsCollapse = new bootstrap.Collapse(targetElement, { toggle: false });
        }
        bsCollapse.toggle();
      }
      
      // Scroll to the section
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

      // Determine active section based on scroll position
      let activeSection = null;
      for (let i = sections.length - 1; i >= 0; i--) {
        const section = sections[i];
        const contentId = section.getAttribute('data-bs-target');
        const contentEl = contentId ? document.getElementById(contentId.replace('#', '')) : null;
        if (contentEl && contentEl.getBoundingClientRect().top <= stickyTop + 50) {
          activeSection = section;
          break;
        }
      }
      sections.forEach(s => s.classList.remove('active'));
      if (activeSection) activeSection.classList.add('active');

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
                const targetId = section.getAttribute('data-bs-target');
                const parentElement = targetId ? document.getElementById(targetId.replace('#', '')) : null;
                const totalDistance = parentElement ? parentElement.offsetHeight - sectionHeight : 1000;
                
                progress = Math.min(100, Math.max(0, ((totalDistance - distanceToNextSection) / totalDistance) * 100));
              } else {
                // Next section has reached or overlapped, set to 100%
                progress = 100;
              }
            } else {
              // Last section - fill based on how much content has been scrolled past
              const targetId = section.getAttribute('data-bs-target');
              const parentElement = targetId ? document.getElementById(targetId.replace('#', '')) : null;
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
    resolveCategoryImg(cat) {
      return resolveImageUrl(cat?.imageUrl);
    },
    resolveProductImg(product) {
      return resolveImageUrl(product?.imageURL);
    },
  },
  computed: {
    filteredVehicles() {
      if (!this.products) return [];
      const term = (this.vehicleKeyword || '').toLowerCase().trim();
      if (!term) return this.products;
      return this.products.filter(p => p.name.toLowerCase().includes(term));
    },
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
    dropoffMinDate() {
      if (this.pickupDate) {
        const dayAfter = new Date(this.pickupDate);
        dayAfter.setDate(dayAfter.getDate() + 1);
        return dayAfter.toISOString().split('T')[0];
      }
      return this.minDate;
    },
    fleetCards() {
      const cats = (this.categories || []).slice(0, this.categorySize).map(cat => ({
        image: resolveImageUrl(cat?.imageUrl),
        title: cat.categoryName,
        description: cat.description,
        features: [],
        id: cat.id,
        type: 'category',
      }));
      const prods = (this.products || []).slice(0, this.productSize).map(p => ({
        image: resolveImageUrl(p?.imageURL),
        title: p.name,
        description: `$${p.price} / day`,
        features: p.features || [],
        id: p.id,
        name: p.name,
        type: 'vehicle',
      }));
      return [...cats, ...prods];
    }
  },
  watch: {
    vehicleKeyword() {
      this.showVehicleDropdown = true;
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
        const navbarHeight = navbar.getBoundingClientRect().height;
        const isHidden = document.documentElement.getAttribute('data-navbar-hidden') === 'true';
        document.documentElement.style.setProperty('--navbar-height', isHidden ? '0px' : `${navbarHeight}px`);
      }
      
      // Initialize animations after DOM is ready
      setTimeout(() => {
        this.initScrollAnimations();
        this.initCountUpAnimations();
      }, 100);
    });

    // Pre-hide map paths so they're ready to draw on scroll
    this.prepMapPaths();

    // Add scroll listener for progress bars and parallax
    window.addEventListener('scroll', this.handleScroll);
    window.addEventListener('scroll', this.handleParallax);
    window.addEventListener('resize', this.checkMobile);
    
    this.handleScroll(); // Initial call
  },
  beforeUnmount() {
    // Clean up scroll listeners
    window.removeEventListener('scroll', this.handleScroll);
    window.removeEventListener('scroll', this.handleParallax);
    window.removeEventListener('resize', this.checkMobile);
  }
};
</script>


<style scoped>
/* Search / Pickup section */
.pickup-location-area {
  position: relative;
  padding: 2.25rem 0;
  z-index: 5;
}
.z-hide-arrow{
  z-index: 2 !important;
}

  .pickup-card {
  background: var(--bg-primary);
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
  background-color: var(--bg-card);
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
  -webkit-mask-image: linear-gradient(to right, transparent 0%, black 6%, black 94%, transparent 100%);
  mask-image: linear-gradient(to right, transparent 0%, black 6%, black 94%, transparent 100%);
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

.vehicle-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  z-index: 100;
  max-height: 200px;
  overflow-y: auto;
  list-style: none;
  margin: 0;
  padding: 0;
  background: var(--bg-card, #fff);
  border: 1px solid var(--border-color, #ccc);
  border-radius: 0 0 10px 10px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}
.vehicle-dropdown li {
  padding: 0.5rem 0.75rem;
  cursor: pointer;
  color: var(--text-primary, #333);
}
.vehicle-dropdown li:hover {
  background: rgba(193, 142, 50, 0.12);
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
.display-1{
  font-size: 110px;
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

/* Map overlay styles */
.map-container{
  background-color: var(--bg-secondary) !important;
}
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
  .display-1{
    font-size: 60px;
  }
}
.city-dot {
  fill: none;
  stroke: white;
  stroke-width: 2;
  stroke-opacity: 0.65;
  transform-origin: center;
  transform-box: fill-box;
  pointer-events: none;
  /* Continuous pulsing ring */
  animation: pulseRing 3s ease-out infinite;
}
.harare-dot {
  /* Keep Harare as a subtle filled marker */
  fill: #ffd966;
  stroke: rgba(0,0,0,0.45);
  stroke-width: 1.2;
}

.city-glow {
  pointer-events: none;
}
@keyframes pulseRing {
  0% {
    transform: scale(0.6);
    opacity: 0.9;
  }
  50% {
    transform: scale(1.8);
    opacity: 0.35;
  }
  100% {
    transform: scale(2.6);
    opacity: 0;
  }
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
  isolation: isolate;
  z-index: 1;
}

#home .page-sections{
    display: flex;
    justify-content: center;
    padding: 20px 16px;
    color: var(--accent-color) !important;
    background-color: var(--navbar-bg) !important;
    border: none !important;
    text-transform: uppercase;
    position: relative;
    overflow: visible;
    transition: background-color 0.3s ease, color 0.3s ease, transform 0.3s ease, top 0.3s ease;
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

/* Video styling removed (static image only) */

/* Image styling - visible and covers hero area */
.hero-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  z-index: 1;
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
  }

  .our-fleets{
    background-color: var(--bg-primary);
  }
  
  
  .page-sections.active::after {
    animation: shimmer 2s infinite;
  }
  
/* Sections layout */
#sections {
  background-color: var(--page-bg);
  position: relative;
  z-index: 1;
}
#sections .collapse.show {
  display: block;
}
.page-sections {
  position: sticky;
  top: var(--navbar-height, 70px);
  z-index: 100;
}
@media (min-width: 1200px) {
  #sections {
    display: flex;
    flex-wrap: wrap;
  }
  #sections > .nav-link.page-sections {
    width: 20%;
    order: 0;
    text-align: center;
    position: sticky;
    top: var(--navbar-height, 70px);
    z-index: 100;
  }
  
  #sections > .collapse {
    width: 100%;
    order: 1;
    display: block !important;
    visibility: visible !important;
  }
  #sections > .collapse .row.g-0 {
    display: flex !important;
  }
  #sections > .nav-link.page-sections.active {
    background-color: var(--accent-color) !important;
    color: var(--charcoal-black) !important;
  }
  
  [data-theme="light"] #sections > .nav-link.page-sections.active {
    background-color: var(--accent-color) !important;
    color: var(--charcoal-black) !important;
  }
}
#ourValues{
  background-color: var(--bg-primary) !important;
}

/* Ensure sections have proper background */
.section-text {
  background-color: var(--bg-primary);
  color: var(--text-primary) !important;
  z-index: 4;
  transition: background-color 0.3s ease, color 0.3s ease;
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

.section-text h2{
  font-family: var(--font-akrobat-bold);

}

.section-text h2,
.section-text p {
  color: var(--text-primary) !important;
}

.section-description-text {
  background-color: var(--bg-primary) !important;
  z-index: 4;
}

/* Provide a solid base behind late-page sections like fleets + footer */

/* Bottom section background */
.bottom-section {
  background-color: var(--bg-secondary);
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
  height: 150%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 5;
  pointer-events: none;
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
  font-family: var(--font-akrobat-bold);
  margin-bottom: 1.25rem;
  text-shadow: 0 2px 20px rgba(0, 0, 0, 0.5);
  letter-spacing: -0.02em;
  background:  #f0c14b;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;
}

.hero-backdrop-subtitle {
  color: rgba(255, 255, 255, 0.95);
  font-size: 2.35rem;
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

  
  .row.g-0 {
    position: relative;
    z-index: 0;
  }
@media (min-width: 0px) and (max-width: 992px) {
  .scroll-indicator{
    display: none;
  }
  #home{
    padding-top: 0px;
    margin-top: -85px;
  }
  #background-div{
    height: 70vh;
    position: relative;
  }
  .hero-image{
    position: relative;
  }
}

@media (min-width: 0px) and (max-width: 650px) {
  #background-div{
    height: 50vh;
  }
}

@media (min-width: 0px) and (max-width: 450px) {
  #background-div{
    height: 40vh;
  }
  .hero-image{
    background-size: contain;
    background-position: center;
  }
}

/* Sort controls styling */
.sort-controls .form-select {
  background-color: var(--bg-primary);
  color: var(--text-primary);
  border-color: var(--border-color, rgba(255,255,255,0.15));
}
.sort-controls .form-select:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 0.2rem rgba(212, 175, 55, 0.25);
}
.sort-label {
  color: var(--text-primary);
  opacity: 0.8;
}
@media (min-width: 576px) {
  .sort-controls .w-sm-auto {
    width: auto !important;
  }
}

/* --- Stacked Card section titles --- */
.stack-section-title {
  font-weight: 700;
  font-size: 0.95rem;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--accent-color, #d4af37);
  margin-bottom: 0.75rem;
  padding-left: 4px;
}

/* Slot content styles for HorizontalCardStack static card */
.stack-card-title {
  font-weight: 700;
  font-size: 1rem;
  margin: 0;
  color: var(--text-primary, #f8f9fa);
  line-height: 1.3;
}
[data-theme="light"] .stack-card-title {
  color: #102040;
}

.stack-card-desc {
  font-size: 0.85rem;
  color: var(--text-primary, #e9ecef);
  opacity: 0.75;
  margin: 0;
  line-height: 1.4;
  overflow-y: auto;
}
[data-theme="light"] .stack-card-desc {
  color: #334155;
  opacity: 1;
}

@media (max-width: 480px) {
  .stack-card-title {
    font-size: 0.85rem;
  }
  .stack-card-desc {
    font-size: 0.75rem;
  }
}
</style>
