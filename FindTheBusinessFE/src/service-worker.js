self.__precacheManifest = [].concat(self.__precacheManifest || []);
workbox.precaching.precacheAndRoute(self.__precacheManifest, {});

workbox.routing.registerRoute(
  new RegExp("https://fonts.(?:googleapis|gstatic).com/(.*)"),
  workbox.strategies.cacheFirst({
    cacheName: "googleapis",
    plugins: [
      new workbox.expiration.Plugin({
        maxEntries: 30
      })
    ],
    method: "GET",
    cacheableResponse: { statuses: [0, 200] }
  })
);

/*workbox.routing.registerRoute(
  new RegExp("https://produsesiservicii.ro/api/(.*)"),
  workbox.strategies.networkFirst({
    cacheName: "produsesiserviciiapis",
    plugins: [
      new workbox.expiration.Plugin({
        maxEntries: 30,
        maxAgeSeconds: 7 * 24 * 60 * 60
      })
    ],
    method: "GET",
    cacheableResponse: { statuses: [0, 299] }
  })
)*/

self.addEventListener('message', (event) => {
  if (event.data && event.data.type === 'SKIP_WAITING') {
    self.skipWaiting()
  }
})