<template>
  <!-- Global Loading Overlay -->
  <div v-if="isBackendLoading" class="backend-loading-overlay">
    <div class="loading-content">
      <div class="spinner-container">
        <div class="custom-spinner" :style="{ borderTopColor: currentColor, borderRightColor: currentColor }"></div>
        <div class="spinner-glow" :style="{ background: `radial-gradient(circle, ${currentGlowColor} 0%, transparent 70%)` }"></div>
      </div>
      <h3 class="loading-text">Starting up services...</h3>
      <p class="loading-subtext">This may take up to 30 seconds on first load</p>
      <div class="loading-dots">
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
      </div>
    </div>
  </div>

  <Navbar :cartCount="cartCount" 
  @resetCartCount="resetCartCount" 
  :token="token" 
  :users="users"
  @clearUsers="clearUsers"
  @usersInfo="usersInfo"
  :baseURL="baseURL"
  ></Navbar>

  <router-view v-if="categories && products" style="min-height: 60vh;"
  :baseURL="baseURL"
  :categories="categories"
  :products="products"
  @fetchData="fetchData"
  @adminInfo="adminInfo"
  @usersInfo="usersInfo"
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
  <Footer />
  
  <!-- Sticky WhatsApp Button -->
  <a href="https://wa.me/254788667111?text=Hi%2C%20I%27m%20interested%20in%20renting%20a%20vehicle.%20Can%20you%20help%20me%3F" target="_blank" rel="noopener noreferrer" class="whatsapp-float" aria-label="Chat on WhatsApp">
    <i class="bi bi-whatsapp"></i>
  </a>
</template>

<script>
import Navbar from "./components/Navbar.vue";
import axios from 'axios';
import Footer from "./components/Footer.vue";
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
  components: { Navbar, Footer },
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
      isBackendLoading: true,
      currentColor: 'rgb(212, 175, 55)', // Start with metallic gold
      currentGlowColor: 'rgba(212, 175, 55, 0.3)',
      colorIndex: 0,
      colorCycleInterval: null,
      retryInterval: null,
      // Website color palette - Gold, Bootstrap Blue, Green, Red, White
      colorPalette: [
        { color: 'rgb(212, 175, 55)', glow: 'rgba(212, 175, 55, 0.3)' },  // Metallic Gold
        { color: 'rgb(13, 110, 253)', glow: 'rgba(13, 110, 253, 0.3)' },  // Bootstrap Blue
        { color: 'rgb(25, 135, 84)', glow: 'rgba(25, 135, 84, 0.3)' },    // Bootstrap Green
        { color: 'rgb(220, 53, 69)', glow: 'rgba(220, 53, 69, 0.3)' },    // Bootstrap Red
        { color: 'rgb(245, 245, 240)', glow: 'rgba(245, 245, 240, 0.3)' }, // Ivory White
      ],
    }
  },
  methods: {
    //method to cycle through colors for the loading spinner
    cycleColors() {
      this.colorIndex = (this.colorIndex + 1) % this.colorPalette.length;
      this.currentColor = this.colorPalette[this.colorIndex].color;
      this.currentGlowColor = this.colorPalette[this.colorIndex].glow;
    },

    //method to check if backend is ready and retry
    async checkBackendHealth() {
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

    //method to fetch all products and categories
    async fetchData() {
      await axios.all([axios.get(this.baseURL + "/category/list"), 
                          axios.get(this.baseURL + "/product/list")])
      .then(axios.spread((res_cat, res_prod) => {
        this.categories = res_cat.data;
        this.products = res_prod.data;
        this.pieChartConfig(res_cat.data, res_prod.data);
        // Hide loading overlay once data is fetched
        this.isBackendLoading = false;
        // Clear intervals
        if (this.colorCycleInterval) {
          clearInterval(this.colorCycleInterval);
        }
        if (this.retryInterval) {
          clearInterval(this.retryInterval);
        }
      })).catch((err) => {
        console.log('Backend not available, showing loading screen...', err);
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
    }
  },

  mounted() {
    // Start color cycling for loading spinner
    this.colorCycleInterval = setInterval(() => {
      if (this.isBackendLoading) {
        this.cycleColors();
      }
    }, 1200); // Change color every spinner revolution (1.2s)

    this.fetchData();
    this.adminInfo();
    this.token = localStorage.getItem("token");
  },

  beforeUnmount() {
    // Clean up intervals when component is destroyed
    if (this.colorCycleInterval) {
      clearInterval(this.colorCycleInterval);
    }
    if (this.retryInterval) {
      clearInterval(this.retryInterval);
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
#app{
  margin-top: 8.4rem;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  min-height: 100vh;
}
router-view{
  min-height: 90vh;
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

.loading-content {
  text-align: center;
  animation: slideUp 0.6s ease-out;
}

.spinner-container {
  position: relative;
  width: 120px;
  height: 120px;
  margin: 0 auto 2rem;
}

.custom-spinner {
  width: 120px;
  height: 120px;
  border: 4px solid rgba(255, 255, 255, 0.1);
  border-top: 4px solid rgb(212, 175, 55);
  border-right: 4px solid rgb(212, 175, 55);
  border-radius: 50%;
  animation: spin 1.2s cubic-bezier(0.68, -0.55, 0.265, 1.55) infinite;
  position: relative;
  z-index: 2;
  transition: border-top-color 0.6s ease, border-right-color 0.6s ease;
}

.spinner-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(212, 175, 55, 0.3) 0%, transparent 70%);
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
  z-index: 1;
  transition: background 0.6s ease;
}

.loading-text {
  color: rgb(245, 245, 240);
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  letter-spacing: 0.5px;
  animation: textGlow 2s ease-in-out infinite;
}

.loading-subtext {
  color: rgba(245, 245, 240, 0.6);
  font-size: 1rem;
  font-weight: 400;
  margin: 0 0 1.5rem 0;
  animation: fadeInOut 3s ease-in-out infinite;
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

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.5;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.2);
    opacity: 0.8;
  }
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
  z-index: 1050;
}
</style>