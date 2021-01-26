<template>
  <div v-if="!$store.getters.getHasCompletedTutorial && ($router.currentRoute.path.includes('/profile') || ($router.currentRoute.path.includes('/store') && $route.query.owner == true))" :class="!isShowingDialog && !isShowingSureDialog ? 'overlay' : ''" >
    <div v-if="!isShowingSureDialog" style="background-color: black; padding: 20px 10px; z-index: 1012" :class="`${$store.getters.getTutorialStrings[$store.getters.getTutorialStep].place}-placement`">
      <h4 class="title" v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].title">
        {{$store.getters.getTutorialStrings[$store.getters.getTutorialStep].title}}
      </h4>
      <p>
        {{$store.getters.getTutorialStrings[$store.getters.getTutorialStep].content}}
      </p>
      <div>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasYesButton" :class="windowWidth < 768 ? 'uk-button-small' : ''" style="margin: 0 10px;" type="primary" @click="yesButton()">Da</vk-button>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasNoButton" :class="windowWidth < 768 ? 'uk-button-small' : ''" style="margin: 0 10px; color: white;" @click="noAndFinalButton()">Nu</vk-button>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasFinalButton" :class="windowWidth < 768 ? 'uk-button-small' : ''" style="margin: 0 10px;" type="primary" @click="noAndFinalButton()">Multumesc</vk-button>
      </div>
    </div>

    <vk-modal class="uk-margin-remove" :stuck="true" overflow-auto center :show.sync="isShowingSureDialog">
      <vk-modal-title slot="header" style="white-space: pre-line;">Sunteti sigur?</vk-modal-title>

      Este recomandat sa treceti prin tutorial pentru a intelege modul de utilizare al site-ului. Doriti sa parcurgeti tutorialul?

      <div slot="footer" class="uk-text-right">
        <vk-button :class="windowWidth <= 640 ? 'uk-button-small' : ''" style="margin-right: 2px;" @click="closeSureDialog(false)">DA</vk-button>
        <vk-button :class="windowWidth <= 640 ? 'uk-button-small' : ''" @click="closeSureDialog(true)" type="primary">DU-MA LA TUTORIAL</vk-button>
      </div>
    </vk-modal>
  </div>
</template>

<script>
export default {
  name: 'TutorialLayout',
  props: {
    isShowingDialog: false,
  },
  data() {
    return {
      isShowingSureDialog: false,
      isSure: false,
    }
  },
  methods: {
    yesButton() {
      this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
      this.$store.dispatch('changeIsWithinTutorial', true)
    },
    noAndFinalButton() {
      if(this.$store.getters.getTutorialStep==0 && this.isSure == false) {
        this.isShowingSureDialog = true;
        return
      }

      this.$store.dispatch('changeHasCompletedTutorial', true)
      this.$store.dispatch('changeIsWithinTutorial', false)
    },
    closeSureDialog(wantsTutorial) {
      this.isShowingSureDialog = false;
      this.isSure = true;
      if(wantsTutorial) {
        this.yesButton();
        return;
      }
      this.noAndFinalButton();
    }
  }
}
</script>

<style scoped>
p {
  color: white;
  margin-bottom: 5px;
}

.title {
  color: #6FC42B;
  font-weight: bold;
}

@media(max-width: 768px) {
  p {
    font-size: 10px;
  }
}
</style>