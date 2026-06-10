import 'mutationobserver-shim'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle'
import 'bootstrap-icons/font/bootstrap-icons.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/scss/theme.scss' // Import luxury theme
import './plugins/axios' // Configure Axios with CSRF and security headers

createApp(App).use(router).mount('#app')

// Register Service Worker for caching videos and assets (PRODUCTION ONLY)
if ('serviceWorker' in navigator && process.env.NODE_ENV === 'production') {
  window.addEventListener('load', () => {
    navigator.serviceWorker
      .register('/service-worker.js')
      .catch(() => undefined);
  });
}
