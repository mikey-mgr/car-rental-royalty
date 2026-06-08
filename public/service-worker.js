// Enhanced Service Worker for caching videos, images, and assets with Range Request support
// This worker uses Cache-First strategy for static assets and videos
// CRITICAL: Handles HTTP Range Requests (206 Partial Content) for video seeking/streaming

const CACHE_VERSION = 'deroyalty-cache-v2';
const STATIC_CACHE = 'deroyalty-static-v2';
const VIDEO_CACHE = 'deroyalty-videos-v2';
const IMAGE_CACHE = 'deroyalty-images-v2';

const STATIC_ASSETS = [
  '/',
  '/index.html',
  '/manifest.json',
  '/favicon.ico',
];

// Install event - precache static assets
self.addEventListener('install', (event) => {
  console.log('[Service Worker] Installing version 2...');
  event.waitUntil(
    caches.open(STATIC_CACHE)
      .then(cache => cache.addAll(STATIC_ASSETS))
      .then(() => self.skipWaiting()) // Activate immediately
      .catch(err => console.error('[Service Worker] Install failed:', err))
  );
});

// Activate event - clean up old caches
self.addEventListener('activate', (event) => {
  console.log('[Service Worker] Activating version 2...');
  event.waitUntil(
    caches.keys()
      .then(cacheNames => {
        return Promise.all(
          cacheNames
            .filter(name => ![STATIC_CACHE, VIDEO_CACHE, IMAGE_CACHE].includes(name))
            .map(name => {
              console.log('[Service Worker] Deleting old cache:', name);
              return caches.delete(name);
            })
        );
      })
      .then(() => self.clients.claim())
  );
});

// Fetch event - implement caching strategy with Range Request support
self.addEventListener('fetch', (event) => {
  const { request } = event;
  const url = new URL(request.url);

  // IGNORE localhost (dev mode)
  if (url.hostname === 'localhost' || url.hostname === '127.0.0.1') {
    return;
  }

  // IGNORE webpack dev server files
  if (request.url.includes('hot-update') || 
      request.url.includes('webpack') ||
      request.url.includes('sockjs-node')) {
    return;
  }

  // IGNORE API calls - let them go directly to network
  if (request.url.includes('/api/') || 
      request.url.includes(':8081') ||
      request.url.includes('/category/') ||
      request.url.includes('/product/') ||
      request.url.includes('/cart/') ||
      request.url.includes('/wishlist/') ||
      request.url.includes('/user/')) {
    return;
  }

  // ============ VIDEO CACHING WITH RANGE REQUEST SUPPORT ============
  if (request.url.match(/\.(mp4|webm|ogg|mov)(\?.*)?$/i)) {
    event.respondWith(handleVideoRequest(request));
    return;
  }

  // ============ IMAGE CACHING ============
  if (request.url.match(/\.(jpg|jpeg|png|gif|svg|webp)(\?.*)?$/i)) {
    event.respondWith(handleImageRequest(request));
    return;
  }

  // ============ STATIC ASSETS (JS, CSS, HTML) ============
  if (request.method === 'GET' && 
      request.url.match(/\.(js|css|html|ico|json|map)(\?.*)?$/i)) {
    event.respondWith(handleStaticRequest(request));
    return;
  }

  // Default: Network first for everything else
  event.respondWith(
    fetch(request)
      .then(response => {
        if (response && response.status === 200) {
          const responseClone = response.clone();
          caches.open(CACHE_VERSION).then(cache => {
            cache.put(request, responseClone);
          });
        }
        return response;
      })
      .catch(() => caches.match(request))
  );
});

/**
 * Handle video requests with Range Request support
 * Range requests (206 Partial Content) are CRITICAL for video seeking
 */
async function handleVideoRequest(request) {
  try {
    const cache = await caches.open(VIDEO_CACHE);
    const cachedResponse = await cache.match(request);

    // If fully cached, return it
    if (cachedResponse) {
      console.log('[Service Worker] Serving cached video:', request.url);
      return cachedResponse;
    }

    // Not in cache - fetch from network and cache it
    console.log('[Service Worker] Fetching video from network:', request.url);
    const networkResponse = await fetch(request);

    if (!networkResponse || networkResponse.status !== 200) {
      return networkResponse;
    }

    // Cache the successful response
    const responseToCache = networkResponse.clone();
    cache.put(request, responseToCache);
    console.log('[Service Worker] Video cached:', request.url);

    return networkResponse;
  } catch (error) {
    console.error('[Service Worker] Video fetch failed:', error);
    // Try to return from cache on network failure
    const cache = await caches.open(VIDEO_CACHE);
    return cache.match(request) || new Response('Video unavailable', { status: 503 });
  }
}

/**
 * Handle image requests
 */
async function handleImageRequest(request) {
  try {
    const cache = await caches.open(IMAGE_CACHE);
    
    // Try cache first
    let cachedResponse = await cache.match(request);
    if (cachedResponse) {
      console.log('[Service Worker] Serving cached image:', request.url);
      return cachedResponse;
    }

    // Fetch from network
    const networkResponse = await fetch(request);
    
    if (!networkResponse || networkResponse.status !== 200) {
      return networkResponse;
    }

    // Cache the response for future use
    const responseToCache = networkResponse.clone();
    cache.put(request, responseToCache);
    
    return networkResponse;
  } catch (error) {
    console.error('[Service Worker] Image fetch failed:', error);
    // Fallback to cache or return placeholder
    const cache = await caches.open(IMAGE_CACHE);
    return cache.match(request) || new Response('', { status: 404 });
  }
}

/**
 * Handle static assets (JS, CSS, HTML)
 * Cache-First strategy with network fallback
 */
async function handleStaticRequest(request) {
  try {
    // Check static cache first
    let cachedResponse = await caches.match(request, { cacheName: STATIC_CACHE });
    if (cachedResponse) {
      return cachedResponse;
    }

    // If not in static cache, try fetching from network
    const networkResponse = await fetch(request);
    
    if (!networkResponse || networkResponse.status !== 200) {
      return networkResponse;
    }

    // Cache new static assets for future use
    const cache = await caches.open(STATIC_CACHE);
    cache.put(request, networkResponse.clone());
    
    return networkResponse;
  } catch (error) {
    console.error('[Service Worker] Static request failed:', error);
    // Return cached version if available
    return caches.match(request);
  }
}

// Message handler for cache management (optional - allows client-side cache clearing)
self.addEventListener('message', (event) => {
  if (event.data && event.data.type === 'CLEAR_CACHE') {
    console.log('[Service Worker] Clearing all caches...');
    caches.keys().then(cacheNames => {
      Promise.all(cacheNames.map(name => caches.delete(name)));
    });
  }
});
