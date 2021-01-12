<template>
  <div v-if="!$store.getters.getHasCompletedTutorial && ($router.currentRoute.path.includes('/profile') || $router.currentRoute.path.includes('/store'))" :class="!isShowingDialog ? 'overlay' : ''" >
    <div style="background-color: black; padding: 20px 10px; z-index: 1012" :class="`${$store.getters.getTutorialStrings[$store.getters.getTutorialStep].place}-placement`">
      <p>
        {{$store.getters.getTutorialStrings[$store.getters.getTutorialStep].content}}
      </p>
      <div>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasYesButton" :class="windowWidth < 768 ? 'uk-button-small' : ''" style="margin: 0 10px;" type="primary" @click="$store.dispatch('changeTutorialStep', $store.getters.getTutorialStep+1)">Da</vk-button>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasNoButton" :class="windowWidth < 768 ? 'uk-button-small' : ''" style="margin: 0 10px; color: white;" @click="$store.dispatch('changeHasCompletedTutorial', true)">Nu</vk-button>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasFinalButton" :class="windowWidth < 768 ? 'uk-button-small' : ''" style="margin: 0 10px;" type="primary" @click="$store.dispatch('changeHasCompletedTutorial', true)">Multumesc</vk-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TutorialLayout',
  props: {
    isShowingDialog: false,
  }
}
</script>

<style scoped>
p {
  color: white;
  margin-bottom: 5px;
}

@media(max-width: 768px) {
  p {
    font-size: 10px;
  }
}
</style>