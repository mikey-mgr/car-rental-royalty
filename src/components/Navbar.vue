<template>
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top p-0">
      <div class="container-fluid">
        <!-- Navbar content -->
        <!--    Logo-->
        <router-link class="navbar-brand p-0 mr-0" :to="{ name: 'HomeView' }" >
          <img id="logo" src="../../public/royalty-logo-dark.jpeg"/>
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
        <div class="collapse navbar-collapse mr-4 justify-content-center" id="navbarSupportedContent">
          <ul class="navbar-nav justify-content-evenly w-100">
              <li class="nav-item dropdown" v-if="role === 'ADMIN'">
                <button type="button" class="nav-link text-light dropdown-toggle border-0 bg-transparent"  :class="{'active': $route.path==='/admin' || $route.path==='/admin/vehicle' || $route.path==='/admin/category' || $route.path==='/admin/users'}" id="navbarAdmin" data-toggle="dropdown">
                  ADMIN
                </button>
                <ul class="dropdown-menu admin-dropdown" aria-labelledby="navbarAdmin">
                  <router-link v-if="role === 'ADMIN'" class="dropdown-item" :to="{name: 'AdminView'}" @click="closeNavbar">Dashboard</router-link>
                  <router-link v-if="role === 'ADMIN'" class="dropdown-item" :to="{name: 'AdminProduct'}" @click="closeNavbar">Vehicles</router-link>
                  <router-link v-if="role === 'ADMIN'" class="dropdown-item" :to="{name: 'AdminCategory'}" @click="closeNavbar">Categories</router-link>
                  <router-link v-if="role === 'ADMIN'" class="dropdown-item" :to="{name: 'UsersView'}" @click="closeNavbar">Users</router-link>
                </ul>
                <!-- <router-link :class="{'active': $route.path==='/admin' || $route.path==='/admin/vehicle' || $route.path==='/admin/category'}" class="nav-link text-light" v-if="role == 'ADMIN'" :to="{name: 'AdminView'}">ADMIN</router-link> -->
              </li>


              <li class="nav-item"><router-link :class="{'active': $route.path==='/home'}" class="nav-link text-light" :to="{name: 'HomeView'}" @click="closeNavbar">HOME</router-link></li>
              <li class="nav-item"><router-link :class="{'active': $route.path==='/about'}" class="nav-link text-light" :to="{name: 'AboutUs'}" @click="closeNavbar">ABOUT US</router-link></li>
              <li class="nav-item"><router-link :class="{'active': $route.path==='/vehicles/'}" class="nav-link text-light" :to="{name: 'VehiclesView'}" @click="closeNavbar">VEHICLES</router-link></li>
              
            <!-- Dropdown for account -->
            <!-- <ul class="navbar-nav nav-underline mr-auto"> -->
              <li class="nav-item dropdown">
                <button type="button"
                    class="nav-link text-light dropdown-toggle" 
                    :class="{'active': $route.path==='/wishlist'}"
                      id="navbarAccount" 
                        data-toggle="dropdown"
                        >ACCOUNT
                </button>
                <ul class="dropdown-menu account-dropdown" aria-labelledby="navbarAccount">
                  <router-link v-if="role" class="dropdown-item" :to="{name: 'WishList'}" @click="closeNavbar">Wishlist</router-link>
                  <a v-if="!role" class="dropdown-item ff-regular" href="#" @click.prevent="openAuth('signup')">Signup</a>
                  <li><hr class="dropdown-divider"></li>
                  <a v-if="!role" class="dropdown-item ff-regular" href="#" @click.prevent="openAuth('login')">Login</a>
                  <a href="#" v-if="role" @click.prevent="logout" class="dropdown-item ff-regular">Logout</a>
                </ul>
              </li>
              <li class="nav-item"><router-link :class="{'active': $route.path==='/contact'}" class="nav-link text-light" :to="{name: 'ContactUs'}" @click="closeNavbar">CONTACT</router-link></li>
            
              <!-- Theme Toggle -->
              <li class="nav-item theme-toggle-wrapper">
                <button @click="toggleTheme" class="theme-toggle-button" aria-label="Toggle dark/light mode">
                  <i :class="isDarkMode ? 'bi bi-sun-fill' : 'bi bi-moon-fill'" class="theme-icon"></i>
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
    props:["cartCount", "users", "baseURL", "role"],
      data() {
      return {
        roleLocal: null,
        isDarkMode: true,
        navbarHeightReady: false,
      }
    },
    methods: {
      closeNavbar() {
        // Close the navbar collapse on mobile
        const navbarCollapse = document.querySelector('.navbar-collapse');
        if (navbarCollapse && navbarCollapse.classList.contains('show')) {
          navbarCollapse.classList.remove('show');
        }
      },
      updateNavbarHeight() {
        // Hide sticky sections during measurement
        document.documentElement.classList.add('measuring-navbar');
        
        // Update CSS variable with actual navbar height (collapsed state only)
        const navbar = document.querySelector('.navbar');
        const navbarCollapse = document.querySelector('.navbar-collapse');
        
        if (navbar) {
          // Check if navbar is currently transitioning
          const isTransitioning = navbarCollapse && navbarCollapse.classList.contains('collapsing');
          
          if (isTransitioning) {
            // Wait for transition to complete
            setTimeout(() => this.updateNavbarHeight(), 100);
            return;
          }
          
          // Force collapse by removing 'show' class and waiting for transition
          let wasExpanded = false;
          
          if (navbarCollapse && navbarCollapse.classList.contains('show')) {
            wasExpanded = true;
            navbarCollapse.classList.remove('show');
            
            // Wait for collapse transition to complete (Bootstrap default is 350ms)
            setTimeout(() => {
              // Now measure the collapsed height
              const navbarHeight = navbar.getBoundingClientRect().height;
              const isHidden = document.documentElement.getAttribute('data-navbar-hidden') === 'true';
              document.documentElement.style.setProperty('--navbar-height', isHidden ? '0px' : `${navbarHeight}px`);
              
              // Show sticky sections with fade-in
              document.documentElement.classList.remove('measuring-navbar');
              this.navbarHeightReady = true;
              
              // Restore expanded state if needed
              if (wasExpanded) {
                navbarCollapse.classList.add('show');
              }
            }, 400); // Wait for Bootstrap's collapse transition
            
            return;
          }
          
          // Navbar is already collapsed, measure directly
          const navbarHeight = navbar.getBoundingClientRect().height;
          const isHidden = document.documentElement.getAttribute('data-navbar-hidden') === 'true';
          document.documentElement.style.setProperty('--navbar-height', isHidden ? '0px' : `${navbarHeight}px`);
          
          // Show sticky sections with fade-in
          document.documentElement.classList.remove('measuring-navbar');
          this.navbarHeightReady = true;
        }
      },
      toggleTheme() {
        this.isDarkMode = !this.isDarkMode;
        const theme = this.isDarkMode ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
      },
      async logout(){
        try {
          const response = await axios.get(`${this.baseURL}/user/logout`, {
            withCredentials: true
          });
          
          if(response.data.message && response.data.message.toLowerCase().includes('success')) {
            swal({
              text: "You have logged out",
              icon: "success"
            });
            // Clear role from localStorage (parent will reactively update prop)
            localStorage.removeItem('role');
            this.$emit("clearUsers");
            this.$emit("resetCartCount");
            this.$router.replace({ name: 'HomeView' }).then(() => window.location.reload());
          } else {
            swal({
              text: "Something went wrong, please try again",
              icon: "warning"
            });
          }
        } catch (err) {
          swal({
            text: "Logout failed: " + (err.response?.data?.message || err.message),
            icon: "error"
          });
        }
      },
      openAuth(tab) {
        this.closeNavbar();
        this.$emit('openAuthModal', tab);
      }
    },
    mounted(){
      // Role is now passed as a prop from parent App.vue
      // This ensures it updates reactively when user logs in/out
      
      // Only fetch user info if user is logged in (has a role)
      if (this.role) {
        this.$emit("usersInfo");
      }
        
        // Load theme preference
        const savedTheme = localStorage.getItem('theme') || 'dark';
        this.isDarkMode = savedTheme === 'dark';
        document.documentElement.setAttribute('data-theme', savedTheme);
        
        // Start with sticky sections hidden
        document.documentElement.classList.add('measuring-navbar');
        
        // Set navbar height CSS variable after DOM is ready
        this.$nextTick(() => {
          setTimeout(() => {
            this.updateNavbarHeight();
          }, 200);
        });
        
        // Update on window resize
        window.addEventListener('resize', this.updateNavbarHeight);
        
        // Update navbar height after every route change (with delay to avoid transition)
        this.$router.afterEach(() => {
          this.navbarHeightReady = false;
          document.documentElement.classList.add('measuring-navbar'); // Hide sticky sections
          
          // Close navbar on navigation
          this.closeNavbar();
          
          setTimeout(() => {
            this.updateNavbarHeight();
          }, 500); // Wait for any navbar animations to complete
        });
    },
    beforeUnmount() {
      window.removeEventListener('resize', this.updateNavbarHeight);
    },
  }
</script>


<style scoped>
.navbar{
  background-color: var(--navbar-bg);
  color: var(--accent-color);
  transition: background-color 0.3s ease, color 0.3s ease, transform 0.3s ease;
  transform: translateY(0);
}

.navbar.navbar-hidden {
  transform: translateY(-100%);
}


/* Navbar toggler icon visibility in light mode */
[data-theme="light"] .navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba(25, 25, 25, 0.75)' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}
#logo {
  width: 75px;
  margin: 5px 0px 5px 20px;
  transition: transform 0.3s ease, filter 0.3s ease;
  content: url('../../public/royalty-logo-dark.jpeg');
  filter: none !important;
}

/* Switch to white logo in light mode ...*/
[data-theme="light"] #logo {
  content: url('../../public/royalty-logo-white.jpeg');
  /* remove drop-shadow for cleaner flat logo */
  filter: none;
}

#logo:hover {
  transform: scale(1.05);
  filter: brightness(1.1);
}

[data-theme="light"] #logo:hover {
  filter: brightness(0.9);
}
.dropdown-menu{
  right: 0;
  left: auto;
  top: 2.8rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  border-radius: 8px;
  overflow: hidden;
}

/* Dropdown items - Override all Bootstrap and Vue Router styles */
.dropdown-item,
.dropdown-item:link,
.dropdown-item:visited,
a.dropdown-item,
a.dropdown-item:link,
a.dropdown-item:visited {
  border-color: var(--border-color);
  color: var(--text-primary) !important;
  background-color: transparent !important;
  padding: 0.75rem 1.5rem;
  transition: all 0.3s ease;
  position: relative;
  text-decoration: none !important;
  display: block;
}

.dropdown-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 3px;
  background: linear-gradient(135deg, var(--gold-gradient-start), var(--gold-gradient-end));
  transform: scaleY(0);
  transition: transform 0.3s ease;
}

.dropdown-item:hover,
.dropdown-item:focus,
.dropdown-item:active,
a.dropdown-item:hover,
a.dropdown-item:focus,
a.dropdown-item:active {
  background-color: var(--hover-bg) !important;
  color: var(--accent-color) !important;
  padding-left: 2rem;
  text-decoration: none !important;
}

.dropdown-item:hover::before,
.dropdown-item:focus::before {
  transform: scaleY(1);
}

/* Elegant Luxury Nav Link Styling - Override ALL Bootstrap and Vue Router classes */
.nav-link,
.nav-link:link,
.nav-link:visited,
a.nav-link,
a.nav-link:link,
a.nav-link:visited {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  height: 100%;
  position: relative;
  transition: all 0.3s ease;
  padding: 0.5rem 1rem !important;
  text-decoration: none !important;
  background-color: transparent !important;
  border: none !important;
  color: var(--text-primary) !important;
}

/* Light mode nav links - ensure visibility */
[data-theme="light"] .nav-link,
[data-theme="light"] a.nav-link {
  color: rgb(25, 25, 25) !important;
}

/* Underline animation on hover - for non-dropdown links using ::after */
.nav-link:not(.dropdown-toggle)::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--accent-color), transparent);
  transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  transform: translateX(-50%);
  z-index: 1;
}

.nav-link:not(.dropdown-toggle):hover::after,
.nav-link:not(.dropdown-toggle):focus::after {
  width: 80%;
}

/* Underline animation on hover - for dropdown links using ::before (so ::after is free for the arrow) */
.nav-link.dropdown-toggle::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--accent-color), transparent);
  transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  transform: translateX(-50%);
  z-index: 1;
}

.nav-link.dropdown-toggle:hover::before,
.nav-link.dropdown-toggle:focus::before {
  width: 80%;
}

.nav-link:hover,
.nav-link:focus,
a.nav-link:hover,
a.nav-link:focus {
  color: var(--accent-color) !important;
  transform: translateY(-2px);
  text-shadow: 0 0 8px rgba(212, 175, 55, 0.3);
  background-color: transparent !important;
  text-decoration: none !important;
}

/* Active/Selected link styling - Simple highlighted text with glow, NO lines */
.nav-link:not(.dropdown-toggle).active,
.nav-link:not(.dropdown-toggle).router-link-active,
.nav-link:not(.dropdown-toggle).router-link-exact-active,
a.nav-link:not(.dropdown-toggle).active,
a.nav-link:not(.dropdown-toggle).router-link-active,
a.nav-link:not(.dropdown-toggle).router-link-exact-active,
.nav-item.active .nav-link:not(.dropdown-toggle) {
  color: var(--accent-color) !important;
  font-weight: 500 !important;
  text-shadow: 0 0 12px rgba(212, 175, 55, 0.6), 0 0 20px rgba(212, 175, 55, 0.3);
  background-color: transparent !important;
  border-color: transparent !important;
  text-decoration: none !important;
}

/* Remove the ::after for active non-dropdown links - no underline */
.nav-link:not(.dropdown-toggle).active::after,
.nav-link:not(.dropdown-toggle).router-link-active::after,
.nav-link:not(.dropdown-toggle).router-link-exact-active::after,
a.nav-link:not(.dropdown-toggle).active::after,
a.nav-link:not(.dropdown-toggle).router-link-active::after,
a.nav-link:not(.dropdown-toggle).router-link-exact-active::after {
  display: none;
}

/* Dropdown toggle specific styling */
.nav-link.dropdown-toggle {
  display: flex !important;
  align-items: center !important;
}

/* Active dropdown styling - also just highlighted text */
.nav-link.dropdown-toggle.active,
.show > .nav-link.dropdown-toggle {
  color: var(--accent-color) !important;
  font-weight: 500 !important;
  text-shadow: 0 0 12px rgba(212, 175, 55, 0.6), 0 0 20px rgba(212, 175, 55, 0.3);
}

/* Remove underline from dropdown when active/open */
.nav-link.dropdown-toggle.active::before,
.show > .nav-link.dropdown-toggle::before {
  display: none;
}

/* Cart Icon Styling */
.cart-container {
  margin-top: 0;
}

.cart-link {
  padding: 0.5rem 1rem !important;
  transition: transform 0.3s ease;
}

.cart-link:hover {
  transform: scale(1.1) rotate(5deg);
}

.cart-link.active .cart-icon-wrapper i,
.cart-link.router-link-active .cart-icon-wrapper i,
.cart-link.router-link-exact-active .cart-icon-wrapper i {
  color: var(--gold-gradient-end) !important;
  filter: none !important;
}

.cart-icon-wrapper {
  position: relative;
  display: inline-block;
  transition: all 0.3s ease;
}

.cart-badge {
  position: absolute;
  top: -3px;
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
  box-shadow: 0 2px 8px rgba(220, 53, 69, 0.4);
  border: 2px solid var(--navbar-bg);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 2px 8px rgba(220, 53, 69, 0.4);
  }
  50% {
    transform: scale(1.1);
    box-shadow: 0 4px 12px rgba(220, 53, 69, 0.6);
  }
}

.cart-badge:empty {
  display: none;
}

.cart {
  margin-top: -0.5rem;
}

/* Dropdown toggle arrow positioning - Bootstrap's ::after pseudo-element */
.dropdown-toggle::after {
  display: inline-block !important;
  margin-left: 0.5em !important;
  vertical-align: 0.2em !important;
  content: "" !important;
  border-top: 0.3em solid !important;
  border-right: 0.3em solid transparent !important;
  border-bottom: 0 !important;
  border-left: 0.3em solid transparent !important;
  transition: transform 0.3s ease, border-top-color 0.3s ease !important;
}

/* Specific positioning for navbar dropdowns */
#navbarAccount.dropdown-toggle::after,
#navbarAdmin.dropdown-toggle::after {
  margin-left: 0.5em !important;
  vertical-align: 0.2em !important;
}

/* Rotate arrow when dropdown is open */
.dropdown.show .dropdown-toggle::after,
.show > .dropdown-toggle::after {
  transform: rotate(180deg);
}

/* Dropdown toggle hover effect */
.dropdown-toggle:hover::after {
  border-top-color: var(--accent-color) !important;
}

/* Theme Toggle Button Styling */
.theme-toggle-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  margin: 0;
}

.theme-toggle-button {
  background: transparent;
  border: none;
  padding: 0;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  outline: none;
}

.theme-toggle-button:hover {
  transform: translateY(-2px);
}

.theme-icon {
  font-size: 26px;
  color: var(--accent-color);
  transition: all 0.3s ease;
}

.theme-toggle-button:hover .theme-icon {
  transform: rotate(20deg) scale(1.2);
  filter: none !important;
  text-shadow: 0 0 12px rgba(212, 175, 55, 0.6);
}

/* Mobile responsive adjustments */
@media (max-width: 991px) {
  /* Mobile menu: left-align items (instead of centered) */
  .navbar-collapse {
    justify-content: flex-start !important;
  }
  .navbar-nav {
    justify-content: flex-start !important;
    align-items: flex-start !important;
    width: 100% !important;
  }
  .nav-link,
  a.nav-link {
    justify-content: flex-start !important;
    width: 100% !important;
  }

  /* Component-scoped override: force Akrobat Regular for light nav links */
  .nav-link.text-light,
  .nav-link.text-light.dropdown-toggle,
  .nav-link.text-light.dropdown-toggle:link,
  .nav-link.text-light.dropdown-toggle:visited {
    font-family: var(--font-akrobat-regular);
    font-weight: 400 !important;
    text-transform: uppercase !important;
    padding-left: 0px !important;
  }

  .ff-bold{
    font-family: var(--font-akrobat-bold);
  }
  .ff-semibold{
    font-family: var(--font-akrobat-semibold);
  }
  .ff-regular{
    font-family: var(--font-akrobat-regular) !important;
  }
  .ff-light{
    font-family: var(--font-akrobat-light);
  }

  a.nav-link.text-light{
    padding-left: 0px !important;
  }


  .theme-toggle-wrapper {
    margin: 0.5rem 0;
  }

  #navbarSupportedContent{
    margin-left: 20px;
  }
}
</style>

<style>
/* Global styles for sticky sections during navbar measurement */
html.measuring-navbar .sticky-top,
html.measuring-navbar .page-sections {
  visibility: hidden !important;
  opacity: 0 !important;
}

html:not(.measuring-navbar) .sticky-top,
html:not(.measuring-navbar) .page-sections {
  visibility: visible !important;
  opacity: 1 !important;
  transition: opacity 0.3s ease;
}
</style>
