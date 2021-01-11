<template>
  <div v-if="!$store.getters.getHasCompletedTutorial && ($router.currentRoute.path.includes('/profile') || $router.currentRoute.path.includes('/store'))" :class="!isShowingDialog ? 'overlay' : ''" >
    <div style="background-color: black; padding: 20px 10px; z-index: 1012" :class="`${$store.getters.getTutorialStrings[$store.getters.getTutorialStep].place}-placement`">
      <p style="color: white;">
        {{$store.getters.getTutorialStrings[$store.getters.getTutorialStep].content}}
      </p>
      <div>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasYesButton" style="margin: 10px;" type="primary" @click="$store.dispatch('changeTutorialStep', $store.getters.getTutorialStep+1)">Da</vk-button>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasNoButton" style="margin: 10px; color: white;" @click="$store.dispatch('changeHasCompletedTutorial', true)">Nu</vk-button>
        <vk-button v-if="$store.getters.getTutorialStrings[$store.getters.getTutorialStep].hasFinalButton" style="margin: 10px; color: white;" @click="$store.dispatch('changeHasCompletedTutorial', true)">Multumesc</vk-button>
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

<style>

</style>