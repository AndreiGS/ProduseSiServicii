<template>
  <vk-card padding="small" class="card pointer" @click="$emit('add_new_tab')">
    <vk-icons-plus :height="'30px'" :width="'30px'" class="plusButton"/>
  </vk-card>
</template>

<script>
import VkIconsPlus from '@vuikit/icons/lib/plus'

export default {
  name: 'AddTabCard',
  components: {
    VkIconsPlus,
  },
  mounted() {
    if(!this.$store.getters.getHasCompletedTutorial) {
      window.scrollTo({
        top: document.body.scrollHeight,
        behavior: 'smooth',
      })
    }
  }
}
</script>

<style scoped>
.pointer {
  cursor: pointer;
}
</style>