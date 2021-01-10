<template>
  <vk-card padding="small" class="card" @click="$emit('add_new_tab')">
    <vk-icons-plus :height="'30px'" :width="'30px'" class="plusButton"/>
  </vk-card>
</template>

<script>
import VkIconsPlus from '@vuikit/icons/lib/plus'

export default {
  name: 'AddTabCard',
  components: {
    VkIconsPlus,
  }
}
</script>

<style scoped>

</style>