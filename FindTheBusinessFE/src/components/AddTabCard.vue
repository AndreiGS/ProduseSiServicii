<template>
  <vk-card padding="small" class="card" @click="$emit('add_new_tab')">
    <vk-icon icon="plus" ratio="2" class="plusButton"/>
  </vk-card>
</template>

<script>
export default {
  name: 'AddTabCard'
}
</script>

<style scoped>

</style>