<template>
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

    }
  },
  methods: {
    //method to fetch all products and categories
    async fetchData() {
      await axios.all([axios.get(this.baseURL + "/category/list"), 
                          axios.get(this.baseURL + "/product/list")])
      .then(axios.spread((res_cat, res_prod) => {
        this.categories = res_cat.data;
        this.products = res_prod.data;
        this.pieChartConfig(res_cat.data, res_prod.data);
      })).catch((err) => console.log('err', err));
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
    this.fetchData();
    this.adminInfo();
    this.token = localStorage.getItem("token");
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
</style>