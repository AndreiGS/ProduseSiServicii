export default {
  created() {
    document.addEventListener('swOffline', this.showOfflineDialog, { once: true })
  },
  methods: {
    showOfflineDialog() {
      UIkit.modal("#modal-offline").show();
    },
    okButton() {
      UIkit.modal("#modal-offline").hide();
    }
  }
}