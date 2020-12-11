<template>
  <div>
    <h4 class="uk-text-center">{{title}}</h4>
    <div class="uk-flex uk-flex-row uk-flex-middle">
      <select :id="`${dropdownName}--select`" style="cursor: pointer;"  @change="itemChanged()" v-model="selectedItem" class="uk-select">
        <option value="none">Neselectat</option>
        <option v-for="item in items" :key="item.id" :value="item.id">{{item.name}}</option>
      </select>
      <button style="cursor: pointer;" @click="selectedItem = 'none'; itemChanged()" class="reset-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
    </div>
  </div>
</template>

<script>
export default {
  name: "SearchDropdownInput",
  props: {
    title: null,
    items: null,
    dropdownName: null
  },
  data() {
    return {
      selectedItem: 'none'
    }
  },
  methods: {
    itemChanged() {
      this.$emit('item_changed', this.selectedItem)
    }
  }
}
</script>

<style scoped>
.reset-button {
  outline: 0;
  border: none;
	border-radius: 10px;
  width: 45px;
	height: 30px;
	align-self: center;
	margin: 0 10px;
}
</style>