import 'mutationobserver-shim'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle'
import 'bootstrap-icons/font/bootstrap-icons.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/scss/theme.scss' // Import luxury theme
import './plugins/axios' // Configure Axios with CSRF and security headers

// Disable browser native scroll restoration so the router's custom
// scrollBehavior is the sole controller of page scroll on navigation
if (window.history.scrollRestoration) {
  window.history.scrollRestoration = 'manual'
}

// Save scroll position on refresh/close so hard refreshes also restore
// the last position (router.beforeEach only saves on client navigation)
window.addEventListener('beforeunload', () => {
  const key = 'scrollPosition_' + window.location.pathname
  sessionStorage.setItem(key, window.scrollY.toString())
})

createApp(App).use(router).mount('#app')

// Register Service Worker for caching videos and assets (PRODUCTION ONLY)
if ('serviceWorker' in navigator && process.env.NODE_ENV === 'production') {
  window.addEventListener('load', () => {
    navigator.serviceWorker
      .register('/service-worker.js')
      .catch(() => undefined);
  });
}
