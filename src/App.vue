<template>
  <!-- Global Initial-Boot Loading / Error Overlay (only while app is mounting and backend is not ready) -->
  <div v-if="isBackendLoading" class="backend-loading-overlay">
    <div class="loading-content">
      <template v-if="!backendBootFailed">
        <div class="car-animation-container">
          <img src="/car-only.svg" alt="Loading..." class="loading-car" />
        </div>
        <div class="loading-dots" aria-hidden="true">
          <span class="dot"></span>
          <span class="dot"></span>
          <span class="dot"></span>
        </div>
      </template>

      <template v-else>
        <div class="boot-error-icon mb-3" aria-hidden="true">
          <i class="bi bi-wifi-off"></i>
        </div>
        <h3 class="loading-text mb-2">We're having trouble connecting to our servers.</h3>
        <button class="btn btn-primary boot-retry-btn" @click="retryBoot">
          Try Again
        </button>
      </template>
    </div>
  </div>

  <!-- Top Route/Data Loading Progress Bar (non-blocking, YouTube-style) -->
  <div v-if="isRouteLoading" class="route-progress-bar">
    <div
      class="route-progress-inner"
      :style="{ width: routeProgress + '%' }"
    ></div>
  </div>

  <Navbar :cartCount="cartCount" 
  @resetCartCount="resetCartCount" 
  :token="token" 
  :users="users"
  @clearUsers="clearUsers"
  @usersInfo="usersInfo"
  @openAuthModal="openAuthModal"
  :baseURL="baseURL"
  ></Navbar>

  <router-view v-if="categories && products" style="min-height: 60vh;"
  :baseURL="baseURL"
  :categories="categories"
  :products="products"
  @fetchData="fetchData"
  @adminInfo="adminInfo"
  @usersInfo="usersInfo"
  @openAuthModal="openAuthModal"
  :usrCartItems="usrCartItems"
  :usrTotalCost="usrTotalCost"
  :users="users"
  :cartItems="cartItems"
  :totalCost="totalCost"
  :wishlists="wishlists"
  :labels1="labels1"
  :data1="data1"
  :colors="colors"
  ></router-view>

  <!-- Auth Modal (Login / Signup) -->
  <div class="modal fade" id="authModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
      <div class="modal-content auth-modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ authTab === 'signup' ? 'Create Account' : 'Sign In' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form v-if="authTab === 'login'" @submit="modalSignin" class="row g-3">
            <div class="col-12">
              <label for="authEmail" class="form-label">Email</label>
              <input v-model="authLoginEmail" id="authEmail" type="email" class="form-control" required>
            </div>
            <div class="col-12">
              <label for="authPassword" class="form-label">Password</label>
              <input v-model="authLoginPassword" id="authPassword" type="password" class="form-control" required>
            </div>
            <div class="col-12 d-flex justify-content-between align-items-center">
              <label class="form-label small fst-italic">
                Don't have an account?
              </label>
              <button type="submit" class="btn btn-primary" :disabled="authSubmitting">
                <span
                  v-if="authSubmitting"
                  class="spinner-border spinner-border-sm me-2 button-spinner"
                  role="status"
                  aria-hidden="true"
                ></span>
                <span>{{ authSubmitting ? 'Logging in...' : 'Login' }}</span>
              </button>
            </div>
            <div class="d-flex justify-content-start w-100 m-0">
              <button
                class="m-0 p-0 border-0 bg-transparent text-warning link-primary"
                type="button"
                @click="authTab = 'signup'"
              >
                Sign up
              </button>
            </div>
          </form>

          
          <!-- <ul class="nav nav-pills mb-3 auth-tabs" role="tablist">
            <li class="nav-item" role="presentation">
              <button
                class="nav-link"
                :class="{ active: authTab === 'login' }"
                type="button"
                @click="authTab = 'login'"
              >
                Login
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button
                class="nav-link"
                :class="{ active: authTab === 'signup' }"
                type="button"
                @click="authTab = 'signup'"
              >
                Sign up
              </button>
            </li>
          </ul> -->




          <form v-else @submit="modalSignup" class="row g-3">
            <div class="col-12">
              <label for="authSignupEmail" class="form-label">Email</label>
              <input v-model="authSignupEmail" id="authSignupEmail" type="email" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label for="authFirstName" class="form-label">First Name</label>
              <input v-model="authFirstName" id="authFirstName" type="text" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label for="authLastName" class="form-label">Last Name</label>
              <input v-model="authLastName" id="authLastName" type="text" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label for="authSignupPassword" class="form-label">Password</label>
              <input v-model="authSignupPassword" id="authSignupPassword" type="password" class="form-control" required>
            </div>
            <div class="col-md-6">
              <label for="authSignupConfirm" class="form-label">Confirm Password</label>
              <input v-model="authSignupConfirmPassword" id="authSignupConfirm" type="password" class="form-control" required>
            </div>
            <div class="col-12 d-flex justify-content-between align-items-center">
              <label class="form-label small fst-italic">
                Already have an account?
              </label>
              <button type="submit" class="btn btn-primary" :disabled="authSubmitting">
                <span
                  v-if="authSubmitting"
                  class="spinner-border spinner-border-sm me-2 button-spinner"
                  role="status"
                  aria-hidden="true"
                ></span>
                <span>{{ authSubmitting ? 'Creating account...' : 'Create Account' }}</span>
              </button>
            </div>
            <div class="d-flex justify-content-start w-100 m-0">
              <button
                class="m-0 p-0 border-0 bg-transparent text-warning link-primary"
                type="button"
                @click="authTab = 'login'"
              >
                Sign in
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <AppFooter />
  
  <!-- Sticky WhatsApp Button -->
  <a href="https://wa.me/263717827381?text=Hi%2C%20I%27m%20interested%20in%20renting%20a%20vehicle.%20Can%20you%20help%20me%3F" target="_blank" rel="noopener noreferrer" class="whatsapp-float" aria-label="Chat on WhatsApp">
    <i class="bi bi-whatsapp"></i>
  </a>
</template>

<script>
import Navbar from "./components/Navbar.vue";
import axios from 'axios';
import AppFooter from "./components/Footer.vue";
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle'
import swal from "sweetalert";

// Set the default value for withCredentials to true to allow cookies
axios.defaults.withCredentials = true;


//function to hide navbar when click happens outside it
document.addEventListener('click', function(e) {
  // get the navbar element
  let navbar = document.getElementById('navbarSupportedContent');
  // check if the target element is inside the navbar or not
  if (!e.target.closest('.navbar')) {
    // if not, toggle the navbar collapse
    let bsCollapse = new bootstrap.Collapse(navbar, {toggle: false});
    bsCollapse.hide();
  }
});
//function to hide navbar when scroll happens
document.addEventListener('scroll', function(){
  let navbar = document.getElementById('navbarSupportedContent');
  let bsCollapse = new bootstrap.Collapse(navbar, {toggle: false});
    bsCollapse.hide();
})

export default {
  components: { Navbar, AppFooter },
  data() {
    return {
      baseURL : process.env.VUE_APP_API_URL || "http://localhost:8081",
      products: null,
      categories: null,
      cartCount: 0,
      usrCartItems: null,
      usrTotalCost: null,
      token: null,
      users: null,
      cartItems: null,
      totalCost: null,
      wishlists: null,
      colors: [],
      labels1: [],
      data1: [],
      isBackendLoading: false,
      showOverlayTimer: null,
      firstVisitTimer: null,
      backendBootFailed: false,
      backendBootTimer: null,
      retryInterval: null,
      lastScrollY: 0,
      navbarVisible: true,
      isRouteLoading: false,
      routeProgress: 0,
      routeTimer: null,

      authTab: 'login',
      authSubmitting: false,
      authLoginEmail: null,
      authLoginPassword: null,
      authSignupEmail: null,
      authFirstName: null,
      authLastName: null,
      authSignupPassword: null,
      authSignupConfirmPassword: null,
    }
  },
  methods: {
    //method to check if backend is ready and retry
    async checkBackendHealth() {
      console.log('Checking backend health...');
      try {
        // Try to fetch the public endpoints
        await axios.all([
          axios.get(this.baseURL + "/category/list", { timeout: 5000 }), 
          axios.get(this.baseURL + "/product/list", { timeout: 5000 })
        ]);
        // If successful, backend is ready - reload the page
        console.log('Backend is ready! Reloading page...');
        window.location.reload();
      } catch (err) {
        // Backend not ready yet, will retry on next interval
        console.log('Backend not ready, retrying...');
      }
    },

    //method to fetch all products and categories (runs during initial app boot)
    async fetchData() {
      await axios.all([
        axios.get(this.baseURL + "/category/list", { timeout: 8000 }), 
        axios.get(this.baseURL + "/product/list", { timeout: 8000 })
      ])
      .then(axios.spread((res_cat, res_prod) => {
        this.categories = res_cat.data;
        this.products = res_prod.data;
        this.pieChartConfig(res_cat.data, res_prod.data);

        // Hide initial-boot loading overlay once data is fetched
        if (this.showOverlayTimer) {
          clearTimeout(this.showOverlayTimer);
          this.showOverlayTimer = null;
        }
        this.isBackendLoading = false;

        if (this.backendBootTimer) {
          clearTimeout(this.backendBootTimer);
          this.backendBootTimer = null;
        }
        this.backendBootFailed = false;

        // Clear retry interval
        if (this.retryInterval) {
          clearInterval(this.retryInterval);
        }
      })).catch((err) => {
        console.log('Backend not available, showing loading screen...', err);

        // If we've already timed out into error state, don't auto-retry
        if (this.backendBootFailed) {
          return;
        }

        // Keep showing loading if backend is not ready
        // Start retry interval to check backend health
        if (!this.retryInterval) {
          this.retryInterval = setInterval(() => {
            this.checkBackendHealth();
          }, 3000); // Check every 3 seconds
        }
      });
    },

    //methods to fetch cart for logged in user
    async usersInfo(){
      //fetch cart items if token is present ie. logged in
      let token = localStorage.getItem("token");
      await axios.get(`${this.baseURL}/cart/?token=${token}`)
      .then((res) => {
        const result = res.data;
        if(this.$route.path == "/cart" && result == "<!DOCTYPE html>\n<html lang=\"en\">\n  <head>\n    <meta charset=\"utf-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n    <meta name=\"description\" content=\"\">\n    <meta name=\"author\" content=\"\">\n    <title>Please sign in</title>\n    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" integrity=\"sha384-oOE/3m0LUMPub4kaC09mrdEhIc+e3exm4xOGxAmuFXhBNF4hcg/6MiAXAf5p0P56\" crossorigin=\"anonymous\"/>\n  </head>\n  <body>\n     <div class=\"container\">\n      <form class=\"form-signin\" method=\"post\" action=\"/login\">\n        <h2 class=\"form-signin-heading\">Please sign in</h2>\n        <p>\n          <label for=\"username\" class=\"sr-only\">Username</label>\n          <input type=\"text\" id=\"username\" name=\"email\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n        </p>\n        <p>\n          <label for=\"password\" class=\"sr-only\">Password</label>\n          <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n        </p>\n        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n      </form>\n</div>\n</body></html>"){
          this.$router.push({name: 'SigninView'});
          swal({
            text: "Please login or signup",
            icon: "info"
          });
          localStorage.removeItem("token");
          localStorage.removeItem("role");
          return;
        }
        if(result.totalCost && result.cartItems){
          this.usrCartItems = result.cartItems;
          this.usrTotalCost = result.totalCost.toFixed(2);
        } else { this.usrCartItems = false; this.usrTotalCost = false; }
        if(result.cartItems){
          this.cartCount = result.cartItems.length;
        }
      }).catch((err) => {console.log("err", err)});
    },

    //method to fetch all admin related info
    async adminInfo(){
      //fetch all carts, wishlists and users
      await axios.all([axios.get(`${this.baseURL}/admin/all-cart-items/`), axios.get(`${this.baseURL}/admin/all-wishlists/`),
                        axios.get(`${this.baseURL}/admin/users/`)])
      .then(axios.spread((resCarts, resWishlists, resUsers) => {
        if(this.$route.path == "/admin" && resUsers.data == "<!DOCTYPE html>\n<html lang=\"en\">\n  <head>\n    <meta charset=\"utf-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n    <meta name=\"description\" content=\"\">\n    <meta name=\"author\" content=\"\">\n    <title>Please sign in</title>\n    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" integrity=\"sha384-oOE/3m0LUMPub4kaC09mrdEhIc+e3exm4xOGxAmuFXhBNF4hcg/6MiAXAf5p0P56\" crossorigin=\"anonymous\"/>\n  </head>\n  <body>\n     <div class=\"container\">\n      <form class=\"form-signin\" method=\"post\" action=\"/login\">\n        <h2 class=\"form-signin-heading\">Please sign in</h2>\n        <p>\n          <label for=\"username\" class=\"sr-only\">Username</label>\n          <input type=\"text\" id=\"username\" name=\"email\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n        </p>\n        <p>\n          <label for=\"password\" class=\"sr-only\">Password</label>\n          <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n        </p>\n        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n      </form>\n</div>\n</body></html>"){
          this.$router.push({name: 'SigninView'});
          swal({
            text: "Please login or signup",
            icon: "info"
          });
          return;
        }
        this.cartItems = resCarts.data.cartItems;
        this.totalCost =resCarts.data.totalCost;
        this.wishlists = resWishlists.data;
        this.users = resUsers.data;
      })).catch((err) => console.log('err', err));
    },
          
    //configs for doughnut pie chart
    pieChartConfig(categories, products){
      if(categories){
          for (let k = 0; k < categories.length; k++) {
              this.data1.push(0);
              this.labels1.push(categories[k].categoryName)
          }
      }
      if(products){
          for(let i = 0; i < products.length; i++){
              for(let j = 0; j < categories.length; j++){
                  if(categories[j].id == products[i].categoryId){
                    this.data1[j]++;
                  }
              }
          }
      }
      //generate a list of random colors
      if(this.data1){
        let count = this.data1.length;
        const goldenRatioConjugate = 0.618033988749895;
        let hue = Math.random();
        for(let i=0; i<count; i++){
          hue += goldenRatioConjugate;
          hue %=1;
          const color = this.hsvToRgb(hue, 0.5, 0.95); //50% saturation and 95% value
          this.colors.push(this.rgbToHex(color));
        }
      }
    },
    //converts hsv color to rgb
    hsvToRgb(h, s, v) {
      let r, g, b;
      const i = Math.floor(h * 6);
      const f = h * 6 - i;
      const p = v * (1 - s);
      const q = v * (1 - f * s);
      const t = v * (1 - (1 - f) * s);

      switch (i % 6) {
        case 0: r = v, g = t, b = p; break;
        case 1: r = q, g = v, b = p; break;
        case 2: r = p, g = v, b = t; break;
        case 3: r = p, g = q, b = v; break;
        case 4: r = t, g = p, b = v; break;
        case 5: r = v, g = p, b = q; break;
      }

      return [r * 255, g * 255, b * 255];
    },
    //converts rgb color to hexadecimal
    rgbToHex([r, g, b]) {
      return "#" + [r, g, b].map(x => {
        const hex = Math.round(x).toString(16);
        return hex.length === 1 ? '0' + hex : hex;
      }).join('');
    },

    resetCartCount(){
      this.cartCount = 0;
      this.$router.push({name: 'HomeView'});
    },

    clearUsers(){
      this.users = null
    },

    handleNavbarScroll() {
      const currentScrollY = window.scrollY;
      const scrollDelta = currentScrollY - this.lastScrollY;
      const navbar = document.querySelector('.navbar');
      
      if (!navbar) return;
      
      // Only hide navbar if scrolling down more than 5px
      if (scrollDelta > 5 && this.navbarVisible) {
        this.navbarVisible = false;
        navbar.classList.add('navbar-hidden');
        document.documentElement.setAttribute('data-navbar-hidden', 'true');
        // Update CSS variable to 0 when navbar is hidden
        document.documentElement.style.setProperty('--navbar-height', '0px');
      }
      // Show navbar if scrolling up more than 5px
      else if (scrollDelta < -5 && !this.navbarVisible) {
        this.navbarVisible = true;
        navbar.classList.remove('navbar-hidden');
        document.documentElement.setAttribute('data-navbar-hidden', 'false');
        // Restore navbar height when shown
        const navbarHeight = navbar.offsetHeight;
        document.documentElement.style.setProperty('--navbar-height', `${navbarHeight}px`);
      }
      
      this.lastScrollY = currentScrollY;
    },

    // ROUTE PROGRESS BAR HELPERS
    startRouteProgress() {
      // Do not start a new bar while the initial backend overlay is active
      if (this.isBackendLoading) {
        return;
      }

      this.isRouteLoading = true;
      this.routeProgress = 0;

      if (this.routeTimer) {
        clearInterval(this.routeTimer);
      }

      // Increment progress in a non-linear, NProgress-style way
      this.routeTimer = setInterval(() => {
        if (this.routeProgress < 30) {
          this.routeProgress += 10;
        } else if (this.routeProgress < 70) {
          this.routeProgress += 5;
        } else if (this.routeProgress < 90) {
          this.routeProgress += 2;
        } else {
          // Stop auto-increment near completion; finalization will handle the rest
          clearInterval(this.routeTimer);
          this.routeTimer = null;
        }

        if (this.routeProgress > 95) {
          this.routeProgress = 95;
        }
      }, 200);
    },

    finishRouteProgress() {
      if (!this.isRouteLoading) return;

      if (this.routeTimer) {
        clearInterval(this.routeTimer);
        this.routeTimer = null;
      }

      this.routeProgress = 100;

      // Give users a moment to see the completed bar before hiding it
      setTimeout(() => {
        this.isRouteLoading = false;
        this.routeProgress = 0;
      }, 250);
    },

    retryBoot() {
      this.backendBootFailed = false;
      this.isBackendLoading = true;
      this.fetchData();
    },

    openAuthModal(tab = 'login') {
      this.authTab = tab;
      const modalEl = document.getElementById('authModal');
      if (!modalEl) return;

      // bootstrap bundle is already imported (as "bootstrap")
      const bsModal = bootstrap.Modal.getOrCreateInstance(modalEl);
      bsModal.show();
    },

    async modalSignin(e) {
      e.preventDefault();
      if (this.authSubmitting) return;
      this.authSubmitting = true;

      const body = {
        email: this.authLoginEmail,
        password: this.authLoginPassword,
      };

      try {
        await axios({
          method: "post",
          url: `${this.baseURL}/login`,
          data: new URLSearchParams(body),
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          withCredentials: true,
          maxRedirects: 0,
          validateStatus: (status) => status >= 200 && status < 400,
        });

        const signinResponse = await axios.get(`${this.baseURL}/user/signin`, {
          withCredentials: true
        });

        const loginInfo = signinResponse.data;
        if (loginInfo.status === "Login Success") {
          this.usersInfo();
          swal({
            text: "Login successful, redirecting",
            icon: "success"
          });
          localStorage.setItem("token", loginInfo.token);
          localStorage.setItem("role", loginInfo.role);

          const modalEl = document.getElementById('authModal');
          const bsModal = modalEl ? bootstrap.Modal.getInstance(modalEl) : null;
          if (bsModal) bsModal.hide();

          window.location.replace("/home");
        } else {
          swal({
            text: "Invalid details",
            icon: "warning"
          });
        }
      } catch (err) {
        console.log('err', err);
        if (err.response && err.response.status === 401) {
          swal({
            text: "Invalid email or password",
            icon: "error"
          });
        } else if (err.code === "ERR_NETWORK") {
          swal({
            text: "Network error, please check your connection",
            icon: "error"
          });
        } else {
          swal({
            text: "Login failed. Please try again.",
            icon: "error"
          });
        }
      } finally {
        this.authSubmitting = false;
      }
    },

    async modalSignup(e) {
      e.preventDefault();
      if (this.authSubmitting) return;
      this.authSubmitting = true;

      try {
        if (this.authSignupPassword !== this.authSignupConfirmPassword) {
          swal({
            text: "Passwords don't match, please try again",
            icon: "error"
          });
          return;
        }

        const user = {
          email: this.authSignupEmail,
          firstName: this.authFirstName,
          lastName: this.authLastName,
          password: this.authSignupPassword,
        };

        await axios.post(`${this.baseURL}/user/signup`, user);

        swal({
          text: "Signup successful, please login now",
          icon: "success"
        });

        // Switch to login tab after signup
        this.authTab = 'login';
      } catch (err) {
        console.error('err', err);
        if (err.response && err.response.data == "User with email is already present") {
          swal({
            text: "A user with this email already exists",
            icon: "info"
          });
        } else {
          swal({
            text: "Signup failed. Please try again.",
            icon: "error"
          });
        }
      } finally {
        this.authSubmitting = false;
      }
    }
  },

  mounted() {
    this.fetchData();
    this.adminInfo();
    this.token = localStorage.getItem("token");
    
    // Add scroll listener for navbar hide/show on all pages
    window.addEventListener('scroll', this.handleNavbarScroll);

    // Hook into router navigation for top progress bar
    if (this.$router) {
      this.$router.beforeEach((to, from, next) => {
        this.startRouteProgress();
        next();
      });

      this.$router.afterEach(() => {
        this.finishRouteProgress();
      });
    }

    // Start backend overlay timers after initial render (5s delay then 10s error)
    // Begin countdown only after frontend finished initial render.
    const startOverlayCountdown = () => {
      if (this.categories === null && this.products === null) {
        this.backendBootFailed = false;
        if (this.showOverlayTimer) clearTimeout(this.showOverlayTimer);
        this.showOverlayTimer = setTimeout(() => {
          this.isBackendLoading = true;
        }, 2500);

        if (this.backendBootTimer) clearTimeout(this.backendBootTimer);
        this.backendBootTimer = setTimeout(() => {
          this.backendBootFailed = true;
          if (this.retryInterval) { clearInterval(this.retryInterval); this.retryInterval = null; }
        }, 10000);
      }
    };

    // If it's the user's first-ever visit, show a mandatory 3s intro loader (simple localStorage flag)
    try {
      const hasVisited = localStorage.getItem('hasVisited');
      if (!hasVisited) {
        // Force loader for 3s, then start the normal overlay countdown
        this.isBackendLoading = true;
        if (this.firstVisitTimer) clearTimeout(this.firstVisitTimer);
        this.firstVisitTimer = setTimeout(() => {
          this.isBackendLoading = false;
          try {
            localStorage.setItem('hasVisited', '1');
          } catch (e) {
            console.warn('Unable to persist first-visit flag to localStorage', e);
          }
          startOverlayCountdown();
        }, 3000);
      } else {
        startOverlayCountdown();
      }
    } catch (e) {
      // If localStorage isn't available for any reason fall back to normal behavior
      startOverlayCountdown();
    }
  },

  beforeUnmount() {
    // Clean up retry interval when component is destroyed
    if (this.retryInterval) {
      clearInterval(this.retryInterval);
    }
    // Remove scroll listener
    window.removeEventListener('scroll', this.handleNavbarScroll);

    // Clean up route progress timer
    if (this.routeTimer) {
      clearInterval(this.routeTimer);
    }

    if (this.backendBootTimer) {
      clearTimeout(this.backendBootTimer);
    }
    if (this.firstVisitTimer) {
      clearTimeout(this.firstVisitTimer);
    }
  }
};
</script>

<style>
html{
  overflow-y: scroll;
  transition: all 0.3s ease;
  background-color: var(--bg-primary);
}
body {
  overflow-x: clip;
}
div{
  font-family: Akrobat-Regular, sans-serif
}

.container{
  padding-top: 30px;
  padding-bottom: 30px;
}

#app{
  margin-top: 85px;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  min-height: 100vh;
}
router-view{
  min-height: 90vh;
}

/* Route / Page Transition Top Progress Bar (reuses gold gradient styling) */
.route-progress-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  z-index: 9500;
  background: transparent;
  pointer-events: none;
}

.route-progress-inner {
  height: 100%;
  width: 0%;
  background: linear-gradient(
    90deg,
    var(--gold-gradient-start, #c18e32),
    var(--gold-gradient-end, #f0c14b)
  );
  box-shadow: 0 0 10px rgba(212, 175, 55, 0.8);
  transition: width 0.2s ease-out;
}

/* Backend Loading Overlay */
.backend-loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, rgb(10, 20, 40) 0%, rgb(16, 32, 64) 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  animation: fadeIn 0.3s ease-in;
}

.boot-error-icon {
  font-size: 48px;
  color: var(--accent-color);
  text-shadow: 0 0 12px rgba(212, 175, 55, 0.5);
}

.boot-retry-btn {
  background-color: #f0c14b;
  color: black;
  border-color: #f0c14b;
  border-radius: 8px;
  padding: 0.6rem 1.25rem;
  font-weight: 600;
}

.boot-retry-btn:hover {
  background-color: white;
  color: black;
  border-color: #f0c14b;
}

.button-spinner {
  color: black;
}

.loading-content {
  text-align: center;
  animation: slideUp 0.6s ease-out;
}

.car-animation-container {
  position: relative;
  width: 100vw;
  height: 120px;
  margin: 0 auto 2rem;
  overflow: hidden;
  left: 50%;
  transform: translateX(-50%);
}

.loading-car {
  width: 180px;
  height: auto;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  animation: carDrive 5s infinite;
}

@keyframes carDrive {
  0% {
    left: 50%;
    transform: translate(-50%, -50%);
    animation-timing-function: cubic-bezier(0.6, 0, 1, 0.4);
  }
  20% {
    left: calc(100% + 90px);
    transform: translate(-50%, -50%);
    animation-timing-function: linear;
  }
  35% {
    left: calc(100% + 90px);
    transform: translate(-50%, -50%);
    animation-timing-function: linear;
  }
  35.01% {
    left: -90px;
    transform: translate(-50%, -50%);
    animation-timing-function: linear;
  }
  50% {
    left: -90px;
    transform: translate(-50%, -50%);
    animation-timing-function: linear;
  }
  75% {
    left: 45%;
    transform: translate(-50%, -50%);
    animation-timing-function: cubic-bezier(0.2, 0.8, 0.4, 1);
  }
  82% {
    left: 50%;
    transform: translate(-50%, -50%);
    animation-timing-function: linear;
  }
  100% {
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.loading-text {
  color: var(--text-primary);
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  letter-spacing: 0.5px;
  transition: color 0.3s ease;
}

/* Light mode loading overlay */
[data-theme="light"] .backend-loading-overlay {
  background: linear-gradient(135deg, rgb(250, 250, 248) 0%, rgb(240, 240, 238) 100%);
}

.loading-dots {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 1rem;
}

.loading-dots .dot {
  width: 10px;
  height: 10px;
  background-color: rgb(212, 175, 55);
  border-radius: 50%;
  animation: dotBounce 1.4s ease-in-out infinite;
}

.loading-dots .dot:nth-child(1) {
  animation-delay: 0s;
}

.loading-dots .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.loading-dots .dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes textGlow {
  0%, 100% {
    text-shadow: 0 0 10px rgba(212, 175, 55, 0.5);
  }
  50% {
    text-shadow: 0 0 20px rgba(212, 175, 55, 0.8), 0 0 30px rgba(212, 175, 55, 0.6);
  }
}

@keyframes fadeInOut {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}

@keyframes dotBounce {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1.2);
    opacity: 1;
  }
}

/* Prevent scrolling when loading */
body:has(.backend-loading-overlay) {
  overflow: hidden;
}

/* Sticky WhatsApp Button */
.whatsapp-float {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #25D366 0%, #128C7E 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  box-shadow: 0 4px 20px rgba(37, 211, 102, 0.4), 0 8px 40px rgba(37, 211, 102, 0.2);
  z-index: 1000;
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  text-decoration: none;
  animation: whatsappPulse 2s ease-in-out infinite;
}

.whatsapp-float:hover {
  transform: scale(1.15) rotate(5deg);
  box-shadow: 0 6px 30px rgba(37, 211, 102, 0.6), 0 12px 60px rgba(37, 211, 102, 0.3);
  background: linear-gradient(135deg, #2EE673 0%, #15A589 100%);
}

.whatsapp-float:active {
  transform: scale(1.05);
}

.whatsapp-float i {
  color: white;
  transition: transform 0.3s ease;
}

.whatsapp-float:hover i {
  transform: scale(1.1);
}

@keyframes whatsappPulse {
  0%, 100% {
    box-shadow: 0 4px 20px rgba(37, 211, 102, 0.4), 0 8px 40px rgba(37, 211, 102, 0.2);
  }
  50% {
    box-shadow: 0 6px 30px rgba(37, 211, 102, 0.6), 0 12px 60px rgba(37, 211, 102, 0.3);
  }
}

/* Responsive adjustments for mobile */
@media (max-width: 768px) {
  .whatsapp-float {
    bottom: 20px;
    right: 20px;
    width: 55px;
    height: 55px;
    font-size: 28px;
  }
}

/* Ensure it's above other elements but below modals */
.whatsapp-float {
  z-index: 1000;
}

/* If there's a modal, ensure modal is above */
.modal {
  z-index: 1055;
}

[data-theme="dark"] .auth-modal-content{
  background-color: var(--royal-midnight-blue) !important;
}

.modal-open{
  padding-right: 0px !important;
}

[data-theme="dark"] .btn-close{
  filter: invert(1) grayscale(100%) brightness(200%)
}

.modal-backdrop {
    z-index: 1050 !important;
}

#app > footer {
  position: relative;
  z-index: 40;
  isolation: isolate;
  background-color: var(--navbar-bg) !important;
}
</style>