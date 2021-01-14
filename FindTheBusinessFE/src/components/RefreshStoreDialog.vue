<template>
  <div id="refresh-shop-sections" bg-close="false" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Reincarcare magazin</h2>
      </div>
      <div class="uk-modal-body">
        <p>Daca alegeti aceasta optiune, 20 RON vor fi retrasi din contul dumneavoastra pentru ca magazinul sa ajunga cat mai sus in lista de magazine la cautare sau in cazul unui magazin promovat, pe prima pagina a site-ului</p>
        <p v-if="loading==true">Se incarca</p>
      </div>
      <div class="uk-modal-footer uk-text-right">
        <div class="uk-hidden@s">
					<button @click="hideModal()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button">Inchide</button>
					<button @click="refreshStore()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Reincarca</button>
				</div>
				<div class="uk-visible@s">
					<button class="uk-button uk-button-default custom-dialog-button" type="button" @click="hideModal()">Inchide</button>
					<button @click="refreshStore()" class="uk-button uk-button-primary custom-dialog-button" type="button">Reincarca</button>
				</div>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios'

export default {
  name: 'RefreshStoreDialog',
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
      UIkit.modal('#refresh-shop-sections').hide()
      this.changeTutorialStep();

      if(!this.$store.getters.getHasCompletedTutorial)
        this.$emit('close_modal')
    },
    changeTutorialStep() {
      if(this.$store.getters.getIsWithinTutorial)
        this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
    },
    async refreshStore() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
      await axios({
        url: this.backend+'/api/shops/refreshShop?id='+this.shopIdToModify, //TODO
        method: 'patch',
        withCredentials: true,
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN'),
        }
      })
        .then((response) => {
          UIkit.notification({message: 'Ati reincarcat magazinul cu succes!', status: 'success'})


          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

          this.hideModal()

          if(this.$store.getters.getHasCompletedTutorial)
            this.$emit("refresh_page");
        }) 
        .catch((error) => {
          if(error.response.status == 400)
            UIkit.notification({message: 'Din pacate in acest moment nu aveti destul credit in cont. Va rugam sa adaugati credit pentru a putea efectua operatiunea', status: 'danger'})
          else if(error.response.status == 403)
            UIkit.notification({message: 'Acest magazin nu va apartine. Incercati sa reincarcati pagina', status: 'danger'})
          else
            UIkit.notification({message: 'Nu am reusit sa reincarcam magazinul. Reincercati sau trimiteti-ne un mesaj la: xxxxxxx', status: 'danger'})
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