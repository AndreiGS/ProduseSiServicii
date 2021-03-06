export default {
  data() {
    return {
      registration: null,
      updateExists: false,
      refreshing: false
    }
  },
  created() {
    document.addEventListener('swUpdated', this.updateAvailable, { once: true })
    navigator.serviceWorker.addEventListener('controllerchange', () => {
      if (this.refreshing) return
      this.refreshing = true
      window.location.reload()
    })
  },
  methods: {
    updateAvailable(event) {
      this.registration = event.detail
      this.updateExists = true
      UIkit.modal("#modal-update").show();
    },
    refreshApp() {
      this.updateExists = false
      UIkit.modal("#modal-offline").hide();
      // Make sure we only send a 'skip waiting' message if the SW is waiting
      if (!this.registration || !this.registration.waiting) return
      // Send message to SW to skip the waiting and activate the new SW
      this.registration.waiting.postMessage({ type: 'SKIP_WAITING' })
    }
  }
}