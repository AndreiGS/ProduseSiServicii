<template>
  <div id="promote-shop-sections" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Promovare magazin</h2>
      </div>
      <div class="uk-modal-body">
        <p>Daca alegeti aceasta optiune, 60 RON vor fi retrasi din contul dumneavoastra pentru ca magazinul sa ajunga pe paginile de inceput ale site-ului si sa apara cat mai sus in cautarile utilizatorilor</p>
        <hr>

        <vk-grid divided class="uk-child-width-expand@s">
          <div class="promote-button uk-first-column">
            <vk-button @click="promoteStore('HOME')" class="custom-button" type="primary">Promoveaza pe ecranul "Acasa"</vk-button>
            <p>Acest tip de promovare plaseaza magazinul pe ecranul de pornire al website-ului</p>
          </div>

          <div class="promote-button">
            <vk-button @click="promoteStore('SEARCHES')" class="custom-button" type="primary">Promoveaza in timpul cautarilor</vk-button>
            <p>Acest tip de promovare plaseaza toate magazinele astfel promovate pe primele pozitii in cautarile clientilor</p>
          </div>
        </vk-grid>
      </div>
      
      <div class="uk-modal-footer uk-text-right">
        <div class="uk-hidden@s">
          <button class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button" @click="hideModal()">Inchide</button>
        </div>
        <div class="uk-visible@s">
          <button class="uk-button uk-button-default custom-dialog-button" type="button" @click="hideModal()">Inchide</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  name: 'PromoteStoreDialog',
  props: {
    shopIdToModify: null
  },
  data() {
    return {
      loading: false,

      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  methods: {
    hideModal() {
      UIkit.modal('#promote-shop-sections').hide()
    },
    async promoteStore(type) {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
      await axios({
        url: `${this.backend}/api/shops/promoteShop?id=${this.shopIdToModify}&type=${type}`,
        method: 'patch',
        withCredentials: true,
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN'),
        }
      })
        .then((response) => {
          UIkit.notification({message: 'Ati promovat magazinul cu succes!', status: 'success'})

          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

          UIkit.modal("#promote-shop-sections").hide();

          this.$emit("refresh_page");
        }) 
        .catch((error) => {
          if(error.response.status == 400)
            UIkit.notification({message: 'Din pacate in acest moment nu aveti destul credit in cont. Puteti sa introduceti credit in aceasta fereastra', status: 'danger'})
          else if(error.response.status == 403)
            UIkit.notification({message: 'Acest magazin nu va apartine. Incercati sa reincarcati pagina', status: 'danger'})
          else
            UIkit.notification({message: 'Nu am reusit sa promovam magazinul. Reincercati sau trimiteti-ne un mesaj la: xxxxxxx', status: 'danger'})
        })
        .finally(() => {
          this.loading = false;
          clearTimeout(timeoutVar)
        }) 
    }
  }
}
</script>

<style lang="scss" scoped>
.custom-dialog-button{
	margin-right: 2px;
}

.promote-button {
  text-align: center;
  display: flex;
  flex-direction: column;

  .custom-button {
    padding: 5px 10px;
  }

  p{
    margin: 10px;
  }

}
</style>