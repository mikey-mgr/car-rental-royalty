<template>
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top nav-underline p-0">
      <div class="container-fluid">
        <!-- Navbar content -->
        <!--    Logo-->
        <router-link class="navbar-brand p-0 mr-0" :to="{ name: 'HomeView' }" >
          <img id="logo" src="../../public/logo-dark.png"/>
        </router-link>
        <!--    Burger Button-->
        <button
          class="navbar-toggler mr-4"
          type="button"
          data-toggle="collapse"
          data-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
          aria-current="true"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse mx-4 justify-content-center" id="navbarSupportedContent">
          <ul class="navbar-nav justify-content-evenly w-100">
              <li class="nav-item dropdown" v-if="role == 'ADMIN'">
                <a href="" class="nav-link text-light dropdown-toggle"  :class="{'active': $route.path==='/admin' || $route.path==='/admin/vehicle' || $route.path==='/admin/category' || $route.path==='/admin/users'}" id="navbarAdmin" data-toggle="dropdown">
                  ADMIN
                </a>
                <ul class="dropdown-menu admin-dropdown" aria-labelledby="navbarAdmin">
                  <router-link v-if="token" class="dropdown-item" :to="{name: 'AdminView'}">Dashboard</router-link>
                  <router-link v-if="token" class="dropdown-item" :to="{name: 'AdminProduct'}">Vehicles</router-link>
                  <router-link v-if="token" class="dropdown-item" :to="{name: 'AdminCategory'}">Categories</router-link>
                  <router-link v-if="token" class="dropdown-item" :to="{name: 'UsersView'}">Users</router-link>
                </ul>
                <!-- <router-link :class="{'active': $route.path==='/admin' || $route.path==='/admin/vehicle' || $route.path==='/admin/category'}" class="nav-link text-light" v-if="role == 'ADMIN'" :to="{name: 'AdminView'}">ADMIN</router-link> -->
              </li>


              <li class="nav-item"><router-link :class="{'active': $route.path==='/home'}" class="nav-link text-light" :to="{name: 'HomeView'}">HOME</router-link></li>
              <li class="nav-item"><router-link :class="{'active': $route.path==='/about'}" class="nav-link text-light" :to="{name: 'AboutUs'}">ABOUT US</router-link></li>
              <li class="nav-item"><router-link :class="{'active': $route.path==='/vehicles/'}" class="nav-link text-light" :to="{name: 'VehiclesView'}">VEHICLES</router-link></li>
              
            <!-- Dropdown for account -->
            <!-- <ul class="navbar-nav nav-underline mr-auto"> -->
              <li class="nav-item dropdown">
                <a href=""
                    class="nav-link text-light dropdown-toggle" 
                      id="navbarAccount" 
                        data-toggle="dropdown"
                        >ACCOUNT
                </a>
                <ul class="dropdown-menu account-dropdown" aria-labelledby="navbarAccount">
                  <router-link v-if="token" class="dropdown-item" :to="{name: 'WishList'}">Wishlist</router-link>
                  <router-link v-if="!token" class="dropdown-item" :to="{name: 'SignupView'}">Signup</router-link>
                  <li><hr class="dropdown-divider"></li>
                  <router-link v-if="!token" class="dropdown-item" :to="{name: 'SigninView'}">Login</router-link>
                  <a href="#" v-if="token" @click="logout" class="dropdown-item">Logout</a>
                </ul>
              </li>
              <li class="nav-item"><router-link :class="{'active': $route.path==='/contact'}" class="nav-link text-light" :to="{name: 'ContactUs'}">CONTACT</router-link></li>
            
              <!-- Theme Toggle -->
              <li class="nav-item">
                <button @click="toggleTheme" class="btn btn-link nav-link text-light" style="border: none; background: none;">
                  <i :class="isDarkMode ? 'bi bi-sun-fill' : 'bi bi-moon-fill'" style="font-size: 20px; color: #c18e32;"></i>
                </button>
              </li>

            <!-- </ul > -->
              <li v-if="role" class="nav-item cart-container position-relative">
                  <router-link 
                      :to="{name:'CartView'}" 
                      class="cart-link nav-link text-decoration-none d-flex align-items-center"
                      :class="{'active': $route.path==='/cart'}">
                    <div class="cart-icon-wrapper position-relative">
                      <i class="bi bi-cart3" style="font-size: 28px; color: #c18e32;"></i>
                      <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
                    </div>
                  </router-link>
              </li>
          </ul>
        </div> 
      </div>
    </nav>
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';


  export default {
    name: "NavbarView",
    props:["cartCount", "users", "baseURL"],
    data() {
      return {
        role: null,
        isDarkMode: true,
      }
    },
    methods: {
      toggleTheme() {
        this.isDarkMode = !this.isDarkMode;
        const theme = this.isDarkMode ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
      },
      async logout(){
        await axios.get(`${this.baseURL}/logout`)
        .then((res) =>{
          if(res.data == "Logout Success"){
            swal({
              text: "You have logged out",
              icon: "success"
            });
            localStorage.removeItem("token");
            localStorage.removeItem("role");
            this.token = null;
            this.$emit("clearUsers");
            this.$router.push({name: 'HomeView'});
            this.$emit("resetCartCount");
            window.location.replace("/home");
          } else swal({
            text: "Something went wrong, please try again",
            icon: "warning"
          })
        }).catch((err) => console.log('err', err));
      },
    },
    mounted(){
        this.token = localStorage.getItem("token");
        this.role = localStorage.getItem("role");
        this.$emit("usersInfo");
        
        // Load theme preference
        const savedTheme = localStorage.getItem('theme') || 'dark';
        this.isDarkMode = savedTheme === 'dark';
        document.documentElement.setAttribute('data-theme', savedTheme);
    },
  }
</script>


<style scoped>
.navbar{
  background-color: var(--navbar-bg);
  color: var(--accent-color);
  transition: background-color 0.3s ease;
}
#logo {
  width: 120px;
  margin-left: 0px;
  margin-right: 20px;
}
.dropdown-menu{
  right: 0;
  left: auto;
  top: 2.8rem;
  background-color: var(--bg-card);
  border-color: var(--border-color);
}
.dropdown-item{
  border-color: var(--border-color);
  color: var(--text-primary);
}
.dropdown-item:hover{
  background-color: var(--hover-bg);
  color: var(--accent-color);
}
.nav-link{
  display: flex;
  height: 100%;
  justify-items: center;
  transition: color 0.3s ease;
}

.nav-link:hover {
  color: var(--accent-color) !important;
}

/* Cart Icon Styling */
.cart-container {
  margin-top: 0;
}

.cart-link {
  padding: 0.5rem 1rem !important;
  transition: transform 0.2s ease;
}

.cart-link:hover {
  transform: scale(1.05);
}

.cart-icon-wrapper {
  position: relative;
  display: inline-block;
}

.cart-badge {
  position: absolute;
  top: -8px;
  right: -10px;
  background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
  color: white;
  font-size: 11px;
  font-weight: 600;
  min-width: 20px;
  height: 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  border: 2px solid var(--navbar-bg);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.cart-badge:empty {
  display: none;
}

.cart {
  margin-top: -0.5rem;
}
#navbarAccount.dropdown-toggle::after{
  display: inline-block;
    margin-left: 0.4em;
    margin-top: 0.7rem;
}
#navbarAdmin.dropdown-toggle::after{
  display: inline-block;
    margin-left: 0.4em;
    margin-top: 0.7rem;
}
</style>
