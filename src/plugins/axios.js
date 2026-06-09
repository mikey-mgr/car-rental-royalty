import axios from 'axios';

/**
 * Configure Axios for security and CSRF protection.
 * - Includes credentials (session cookies) in all requests
 * - Extracts and sends CSRF token in headers
 * - Handles CSRF token from cookies
 */

// Set default config
axios.defaults.withCredentials = true;

// CSRF Token Interceptor - Add CSRF token to request headers
axios.interceptors.request.use(config => {
  // Extract CSRF token from cookies
  const csrfToken = getCsrfTokenFromCookie();
  
  if (csrfToken && config.method !== 'get') {
    // Add CSRF token to headers for state-changing requests
    config.headers['X-XSRF-TOKEN'] = csrfToken;
  }
  
  return config;
});

// Response Interceptor - Handle errors gracefully
axios.interceptors.response.use(
  response => response,
  error => {
    // Handle CSRF token expired (403)
    if (error.response && error.response.status === 403) {
      console.warn('CSRF token validation failed. Please refresh and try again.');
    }
    return Promise.reject(error);
  }
);

/**
 * Extract CSRF token from browser cookies
 * Spring Security stores it in a cookie named "XSRF-TOKEN"
 */
function getCsrfTokenFromCookie() {
  const cookies = document.cookie.split('; ');
  for (const cookie of cookies) {
    if (cookie.startsWith('XSRF-TOKEN=')) {
      return decodeURIComponent(cookie.substring('XSRF-TOKEN='.length));
    }
  }
  return null;
}

export default axios;
