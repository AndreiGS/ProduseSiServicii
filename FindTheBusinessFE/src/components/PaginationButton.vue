<template>
  <li><a v-on:click="$emit('change_page', pageNo)">{{pageNo}}</a></li>
</template>

<script>
export default {
  name: 'PaginationButton',
  props: {
    pageNo: null
  }
}
</script>

<style>

</style>