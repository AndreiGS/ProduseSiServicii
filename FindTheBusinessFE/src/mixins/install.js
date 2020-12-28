export default {
  data() {
    return {
      //0 -> did not respond; 1 -> has installed the app; 2 -> denied installing the app
      hasInstalledAppNow: 0,
      deferredPrompt: null,
      shouldShow: false,
      alreadyShowed: false,
      shouldShowInstallButton: false,
    }
  },
  methods: {
    close() {
      this.setShouldShow(false);
      localStorage.setItem("hasRespondedToPopUp", true);
    },
    installApp() {
      this.alreadyShowed = true;
      //this.close();
      this.deferredPrompt.prompt();
      this.deferredPrompt.userChoice.then((choiceResult) => {
        if (choiceResult.outcome === 'accepted') {
          this.hasInstalledAppNow = 1;
        } else {
          this.hasInstalledAppNow = 2;
        }
      });
    },
    setShouldShow(val) {
      this.shouldShow = val;
    },
  },
  mounted() {
    var isAppInstalled = localStorage.getItem("isAppInstalled");
    var hasRespondedToPopUp = localStorage.getItem("hasRespondedToPopUp");
    if(isAppInstalled && !hasRespondedToPopUp) {
      this.setShouldShow(true);
      return;
    }
    this.setShouldShow(false)
  },
  created() {
    window.addEventListener('DOMContentLoaded', () => {
      let displayMode = 'browser tab';
      if (navigator.standalone) {
        displayMode = 'standalone-ios';
        this.shouldShowInstallButton = false;
      }
      if (window.matchMedia('(display-mode: standalone)').matches) {
        displayMode = 'standalone';
        this.shouldShowInstallButton = false;
      }
    });

    window.addEventListener('beforeinstallprompt', (e) => {
      this.shouldShowInstallButton = true;
      e.preventDefault();
      this.deferredPrompt = e;
      this.setShouldShow(true);
    });

    window.addEventListener('appinstalled', (evt) => {
      this.shouldShowInstallButton = false;
      localStorage.setItem("isAppInstalled", true);
      if(!this.alreadyShowed) {
        this.alreadyShowed = true;
        this.hasInstalledAppNow = 1;
        this.setShouldShow(true);
        localStorage.setItem("hasRespondedToPopUp", true);
      }
    });
  }
}