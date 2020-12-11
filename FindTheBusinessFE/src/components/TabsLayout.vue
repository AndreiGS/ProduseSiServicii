<template>
  <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
    <AddTabCard class="add-style" v-on:add_new_tab="addNewTab()"/>
    
    <div v-if="tabs.length > 0">
      <Tab style="margin-bottom: 15px"
        v-on:new_tab_saved="saveNewTab($event)" 
        v-on:delete_tab="deleteTab($event)" 
        v-on:delete_existing_tab="deleteTab($event)"
        v-for="(tab, index) in allTabs" :key="index" :tab="tab"
      />
    </div>
    <div v-else class="not-found">Nu exista nicio categorie personala</div>
  </div>  
</template>

<script>
const Tab = () => import(/* webpackChunkName: "storefront-chunk" */ '@/components/Tab.vue')
const AddTabCard = () => import(/* webpackChunkName: "storefront-chunk" */ '@/components/AddTabCard.vue')

export default {
  name: 'TabsLayout',
  components: {
    Tab,
    AddTabCard
  },
  props: {
    tabs: null,
    newTabAdded: null
  },
  data() {
    return {
      allTabs: null,
    }
  },
  methods: {
    addNewTab() {
      this.$emit('add_new_tab')
    },
    deleteTab(tab) {
      this.$emit('delete_tab', tab)
    },
    saveNewTab(tab) {
      this.$emit('new_tab_saved', tab)
    },
  },
  mounted() {
    this.allTabs = this.tabs
  }
}
</script>

<style scoped>
.not-found {
  font-size: 20px;
}

.add-style {
  margin-bottom: 15px;
}
</style>