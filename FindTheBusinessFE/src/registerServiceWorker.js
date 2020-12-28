/* eslint-disable no-console */

import { register } from 'register-service-worker'

if (process.env.NODE_ENV === 'production') {
  register(`${process.env.BASE_URL}service-worker.js`, {
    ready () {
    },
    registered () {
    },
    cached () {
    },
    updatefound () {
    },
    updated (registration) {
      document.dispatchEvent(
        new CustomEvent('swUpdated', { detail: registration })
      )
      //if (window.confirm("O noua versiune este disponibila. Doriti sa acutalizati?")) {
        //const worker = registration.waiting;
        //worker.postMessage({ action: "SKIP_WAITING" });
        //// refresh the page or trigger a refresh programatically!  
      //}
    },
    offline () {
      document.dispatchEvent(
        new CustomEvent('swOffline')
      )
    },
    error (error) {
    }
  })
}
