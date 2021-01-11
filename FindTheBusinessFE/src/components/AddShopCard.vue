<template>
  <vk-card padding="small" class="card" @click="addShop">
    <vk-icons-plus :height="'30px'" :width="'30px'" class="plusButton"/>
  </vk-card>
</template>

<script>
import VkIconsPlus from '@vuikit/icons/lib/plus'

export default {
  name: 'AddShopCard',
  components: {
    VkIconsPlus,
  },
  methods: {
    addShop() {
      this.$emit('add_shop')
      this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
    }
  }
}
</script>

<style lang="scss" scoped>
.plusButton {
  transition: transform 0.2s ease-in-out;
}
</style>