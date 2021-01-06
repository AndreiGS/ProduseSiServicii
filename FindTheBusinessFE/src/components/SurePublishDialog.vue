<template>
  <div id="sure-publish-sections" bg-close="false" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Publicare magazin</h2>
      </div>
      <div class="uk-modal-body">
        <p>Magazinului dumneavoastra i-a expirat creditul. Daca apasati pe butonul "Continua" si aveti destule credite, acestui magazin i se va atribui un nou credit. In cazul in care nu aveti credite veti primi un mesaj</p>
        <p v-if="loading==true">Se incarca</p>
      </div>
      <div class="uk-modal-footer uk-text-right">
        <div class="uk-hidden@s">
					<button @click="hideModal()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button">Inchide</button>
					<button @click="publishShop()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Continua</button>
				</div>
				<div class="uk-visible@s">
					<button @click="hideModal()" class="uk-button uk-button-default custom-dialog-button" type="button">Inchide</button>
					<button @click="publishShop()" class="uk-button uk-button-primary custom-dialog-button" type="button">Continua</button>
				</div>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios'

export default {
  name: 'SurePublishDialog',
  data() {
    return {
      loading: false,

      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  props: {
    shopIdToModify: null
  },
  methods: {
    hideModal() {
      UIkit.modal('#sure-publish-sections').hide()
    },
    async publishShop() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
      await axios({
        url: this.backend+'/api/shops/changePublished?id='+this.shopIdToModify+'&sure=true',
        method: 'post',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get("CSRF-TOKEN"),
          'X-REFRESH-TOKEN': this.$cookie.get("REFRESH-TOKEN")
        },
        withCredentials: true
      })
        .then((response) => {
          UIkit.notification({message: 'Ati publicat magazinul cu succes!', status: 'success'})

          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

          this.hideModal()

          let published = {
            id: this.shopIdToModify,
            isPublished: response.data.published
          }

          this.$emit('change_published', published)
        }) 
        .catch((error) => {
          if(error.response.status == 401) {
            UIkit.notification({message: 'Din pacate in acest moment nu aveti destule credite pentru acest tip de magazin in cont.', status: 'danger'})
						return 
					}
          UIkit.notification({message: 'Nu am putut modifica starea magazinului. Va rugam sa asteptati si/sau sa reincercati', status: 'danger'})
        })
        .finally(() => {
          this.loading = false;
          clearTimeout(timeoutVar)
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