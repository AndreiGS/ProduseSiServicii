<template>
  <ul class="uk-pagination center-ul" uk-margin>
    <li v-if="currentPage > 1"><a @click="$emit('change_page', currentPage-1)"><span uk-pagination-previous></span></a></li>

    <PaginationButton v-on:change_page="$emit('change_page', $event)" v-for="(arr, index) in getRange(currentPage, Math.min(totalPages, 2+currentPage-1))" 
                      :key="index" 
                      :pageNo="arr"/>
    <li v-if="totalPages-currentPage >= 2" class="uk-disable"><span>...</span></li>

    <li v-if="currentPage < totalPages"><a @click="$emit('change_page', currentPage+1)"><span uk-pagination-next></span></a></li>
    
  </ul>
</template>

<script>
const PaginationButton = () => import(/* webpackChunkName: "home-chunk" */ '@/components/PaginationButton.vue')

export default {
  name: 'Pagination',
  components: {
    PaginationButton
  },
  props: {
    currentPage: null,
    totalPages: null
  },
  methods: {
    getRange(min, max) {
      var array = [],
      j = 0;
      for(var i = min; i <= max; i++){
        array[j] = i;
        j++;
      }
      return array;
    }
  }
}
</script>

<style scoped>
.center-ul {
  justify-content: center;
  margin-left: -3%!important;
}
</style>