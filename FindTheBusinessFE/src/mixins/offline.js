export default {
  created() {
    console.log("I am in offline created")
    document.addEventListener('swOffline', this.showOfflineDialog, { once: true })
  },
  methods: {
    showOfflineDialog() {
      console.log("Show dialog")
      UIkit.modal("#modal-offline").show();
    },
    okButton() {
      UIkit.modal("#modal-offline").hide();
    }
  }
}