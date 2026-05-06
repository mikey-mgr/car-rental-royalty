// Service Worker for caching video files and assets
const CACHE_NAME = 'apex-car-rental-v1';
const VIDEO_CACHE = 'apex-videos-v1';

// Assets to cache immediately
const STATIC_ASSETS = [
  '/',
  '/index.html',
  '/js/app.js',
  '/js/chunk-vendors.js',
  '/css/app.css'
];

// Install event - cache static assets
self.addEventListener('install', (event) => {
  console.log('Service Worker: Installing...');
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => {
        console.log('Service Worker: Caching static assets');
        return cache.addAll(STATIC_ASSETS.map(url => new Request(url, {cache: 'reload'})));
      })
      .then(() => self.skipWaiting())
      .catch((err) => console.log('Service Worker: Cache failed', err))
  );
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

  // Handle video files specially
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
              // Clone the response before caching
              const responseToCache = networkResponse.clone();
              cache.put(request, responseToCache);
              console.log('Service Worker: Video cached', request.url);
            }
            return networkResponse;
          }).catch((err) => {
            console.error('Service Worker: Video fetch failed', err);
            throw err;
          });
        });
      })
    );
    return;
  }

  // Handle other assets with cache-first strategy
  event.respondWith(
    caches.match(request).then((cachedResponse) => {
      if (cachedResponse) {
        return cachedResponse;
      }

      return fetch(request).then((networkResponse) => {
        // Cache successful responses for static assets
        if (networkResponse && networkResponse.status === 200 && 
            (request.url.includes('/js/') || 
             request.url.includes('/css/') || 
             request.url.includes('/img/') ||
             request.url.includes('.jpg') ||
             request.url.includes('.png') ||
             request.url.includes('.jpeg') ||
             request.url.includes('.svg'))) {
          const responseToCache = networkResponse.clone();
          caches.open(CACHE_NAME).then((cache) => {
            cache.put(request, responseToCache);
          });
        }
        return networkResponse;
      });
    })
  );
});
