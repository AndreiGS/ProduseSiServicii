<template>
  <vk-card padding="small" class="card" @click="$emit('add_shop')">
    <vk-icon icon="plus" ratio="2" class="plusButton"/>
  </vk-card>
</template>

<script>
export default {
  name: 'AddShopCard'
}
</script>

<style lang="scss" scoped>
.plusButton {
  transition: transform 0.2s ease-in-out;
}
</style>