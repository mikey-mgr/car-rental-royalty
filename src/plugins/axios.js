import axios from 'axios';

/**
 * Configure Axios for security and CSRF protection.
 * - Includes credentials (session cookies) in all requests
 * - Extracts and sends CSRF token in headers
 * - Refreshes CSRF tokens from the backend when needed
 */

export const API_BASE_URL = process.env.VUE_APP_API_URL || '';
let cachedCsrfToken = null;

// Set default config
axios.defaults.withCredentials = true;

// CSRF Token Interceptor - Add CSRF token to request headers
axios.interceptors.request.use(config => {
  // Skip CSRF for login endpoint (uses password auth instead)
  if (config.url && config.url.includes('/api-login')) {
    return config;
  }

  const csrfToken = cachedCsrfToken || getCsrfTokenFromCookie();

  if (csrfToken && (config.method === 'post' || config.method === 'put' || config.method === 'delete')) {
    config.headers = config.headers || {};
    config.headers['X-XSRF-TOKEN'] = csrfToken;
  }

  return config;
});

// Response Interceptor - Handle errors gracefully
axios.interceptors.response.use(
  response => response,
  error => {
    // Handle CSRF token expired (403) - attempt to refresh token and retry once
    if (error.response && error.response.status === 403) {
      const originalRequest = error.config;
      const originalUrl = originalRequest?.url || '';
      if (originalUrl.includes('/user/csrf-token')) {
        return Promise.reject(error);
      }
      if (!originalRequest || originalRequest._retry) {
        return Promise.reject(error);
      }

      originalRequest._retry = true;

      return axios.get(`${API_BASE_URL}/user/csrf-token`, { withCredentials: true })
        .then((response) => {
          const newToken = response?.data?.token || getCsrfTokenFromCookie();
          if (newToken) {
            setCachedCsrfToken(newToken);
            originalRequest.headers = originalRequest.headers || {};
            originalRequest.headers['X-XSRF-TOKEN'] = newToken;
            return axios(originalRequest);
          }
          return Promise.reject(error);
        })
        .catch(() => Promise.reject(error));
    }

    // Handle unauthorized (401) - session invalid
    if (error.response && error.response.status === 401) {
      clearSessionCookies();
    }

    // Handle redirect to login (when backend returns HTML login page)
    if (error.response && error.response.data && typeof error.response.data === 'string' &&
        error.response.data.includes('Please sign in')) {
      clearSessionCookies();
      if (!window.location.pathname.includes('/signin')) {
        window.location.href = '/';
      }
    }

    return Promise.reject(error);
  }
);

/**
 * Securely clear all session-related cookies and storage
 * Called when session is detected as invalid
 */
function clearSessionCookies() {
  try {
    localStorage.removeItem('token');
    localStorage.removeItem('role');

    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
      const cookieName = cookie.split('=')[0].trim();
      if (cookieName === 'JSESSIONID' || cookieName === 'XSRF-TOKEN' || cookieName.includes('SESSION')) {
        document.cookie = `${cookieName}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; SameSite=Strict;`;
      }
    }
  } catch (e) {
    void e;
  }
}

/**
 * Extract CSRF token from browser cookies.
 * Spring Security stores it in a cookie named "XSRF-TOKEN".
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

export function setCachedCsrfToken(token) {
  cachedCsrfToken = token || null;
}

export function getCachedCsrfToken() {
  return cachedCsrfToken;
}

export default axios;
