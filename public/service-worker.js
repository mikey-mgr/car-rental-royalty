// Service Worker for caching video files and assets
// ONLY RUNS IN PRODUCTION (not on localhost)
const CACHE_NAME = 'apex-car-rental-v1';
const VIDEO_CACHE = 'apex-videos-v1';

// Install event - skip caching on install
self.addEventListener('install', (event) => {
  console.log('Service Worker: Installing...');
  self.skipWaiting(); // Activate immediately
});

// Activate event - clean up old caches
self.addEventListener('activate', (event) => {
  console.log('Service Worker: Activating...');
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cache) => {
          if (cache !== CACHE_NAME && cache !== VIDEO_CACHE) {
            console.log('Service Worker: Clearing old cache', cache);
            return caches.delete(cache);
          }
        })
      );
    }).then(() => self.clients.claim())
  );
});

// Fetch event - serve from cache, fallback to network
self.addEventListener('fetch', (event) => {
  const { request } = event;
  const url = new URL(request.url);

  // IGNORE localhost requests (dev mode)
  if (url.hostname === 'localhost' || url.hostname === '127.0.0.1') {
    return; // Let browser handle it normally
  }

  // IGNORE webpack HMR and hot-update files
  if (request.url.includes('hot-update') || 
      request.url.includes('webpack') ||
      request.url.includes('sockjs-node') ||
      request.url.includes('/ws')) {
    return; // Don't cache dev files
  }

  // IGNORE API calls (let them go to network)
  if (request.url.includes('/api/') || 
      request.url.includes(':8081') ||
      request.url.includes('/category/') ||
      request.url.includes('/product/') ||
      request.url.includes('/cart/') ||
      request.url.includes('/wishlist/')) {
    return; // Don't cache API requests
  }

  // Handle video files specially (PRODUCTION ONLY)
  if (request.url.includes('.mp4') || request.url.includes('.webm')) {
    event.respondWith(
      caches.open(VIDEO_CACHE).then((cache) => {
        return cache.match(request).then((cachedResponse) => {
          if (cachedResponse) {
            console.log('Service Worker: Serving video from cache', request.url);
            return cachedResponse;
          }

          // Not in cache, fetch from network and cache it
          console.log('Service Worker: Fetching video from network', request.url);
          return fetch(request).then((networkResponse) => {
            // Only cache successful responses
            if (networkResponse && networkResponse.status === 200) {
              const responseToCache = networkResponse.clone();
              cache.put(request, responseToCache);
              console.log('Service Worker: Video cached', request.url);
            }
            return networkResponse;
          }).catch((err) => {
            console.error('Service Worker: Video fetch failed', err);
            // Return network error, don't cache
            return fetch(request);
          });
        });
      })
    );
    return;
  }

  // Handle images (PRODUCTION ONLY)
  if (request.url.includes('.jpg') ||
      request.url.includes('.png') ||
      request.url.includes('.jpeg') ||
      request.url.includes('.svg') ||
      request.url.includes('.gif') ||
      request.url.includes('.webp')) {
    event.respondWith(
      caches.match(request).then((cachedResponse) => {
        if (cachedResponse) {
          return cachedResponse;
        }

        return fetch(request).then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200) {
            const responseToCache = networkResponse.clone();
            caches.open(CACHE_NAME).then((cache) => {
              cache.put(request, responseToCache);
            });
          }
          return networkResponse;
        }).catch(() => {
          // If fetch fails, try cache one more time
          return caches.match(request);
        });
      })
    );
    return;
  }

  // For everything else, network first (don't cache JS/CSS in dev)
});
