<template>
  <div id="delete-shop-sections" bg-close="false" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Stergere magazin</h2>
      </div>
      <div class="uk-modal-body">
        <p>Aceasta optiune va sterge magazinul dumneavoastra din sistem. Astfel, daca doriti sa il readaugati, acesta va trebui rescris de la inceput</p>
        <p v-if="loading==true">Se incarca</p>
      </div>
      <div class="uk-modal-footer uk-text-right">
        <div class="uk-hidden@s">
					<button @click="hideModal()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button">Inchide</button>
					<button @click="deleteShop()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Sterge</button>
				</div>
				<div class="uk-visible@s">
					<button class="uk-button uk-button-default custom-dialog-button" type="button" @click="hideModal()">Inchide</button>
					<button @click="deleteShop()" class="uk-button uk-button-primary custom-dialog-button" type="button">Sterge</button>
				</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'DeleteShopDialog',
  props: {
    shopIdToModify: null,
  },
  data() {
    return {
      loading: null,

      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  methods: {
    hideModal() {
      UIkit.modal('#delete-shop-sections').hide()
    },
    async deleteShop() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

      await axios({
        url: this.backend+'/api/shops/deleteShop?id='+this.shopIdToModify,
        method: 'delete',
        withCredentials: true,
        headers: {
          'X-CSRF-TOKEN': this.$cookies.get("CSRF-TOKEN"),
          'X-REFRESH-TOKEN': this.$cookies.get("REFRESH-TOKEN")
        }
      })
        .then((response) => {
          this.$cookies.set("CSRF-TOKEN", response.data.csrfToken);
          this.$cookies.set("REFRESH-TOKEN", response.data.refreshToken);
          this.$emit("delete_shop", this.shopIdToModify);
        })
        .catch((error) => {
          UIkit.notification({message: 'Nu am putut sterge magazinul', status: 'danger'})
        })
        .finally(() => {
          this.loading = false;
          clearTimeout(timeoutVar)
          this.hideModal()
        })
    }
  }
}
</script>

<style scoped>
.custom-dialog-button{
	margin-right: 2px;
}
</style>