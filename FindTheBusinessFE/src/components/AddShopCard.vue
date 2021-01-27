<template>
  <vk-card padding="small" class="card pointer" @click="addShop">
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
    }
  }
}
</script>

<style lang="scss" scoped>
.pointer {
  cursor: pointer;
}
</style>