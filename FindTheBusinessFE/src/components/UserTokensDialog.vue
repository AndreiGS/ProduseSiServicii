<template>
  <div id="user-tokens-sections" uk-modal bg-close="false">
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Credite magazine</h2>
      </div>
      <div class="uk-modal-body">
        <table>
          <tr>
            <td><p class="info-p">Mici (5 produse/servicii - GRATIS) : {{(small != null) ? small : 'Negasit'}}</p></td>
            <td><button style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" @click="buyCredit('small')" class="edit-button uk-button-primary"><span uk-icon="icon: plus; ratio: 0.8"></span></button></td>
          </tr>
          <tr>
            <td><p class="info-p">Medii (15 produse/servicii - GRATIS) : {{(medium != null) ? medium : 'Negasit'}}</p></td>
            <td><button style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" @click="buyCredit('medium')" class="edit-button uk-button-primary"><span uk-icon="icon: plus; ratio: 0.8"></span></button></td>
          </tr>
          <tr>
            <td><p class="info-p">Mari (40 produse/servicii - GRATIS) : {{(large != null) ? large : 'Negasit'}}</p></td>
            <td><button style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" @click="buyCredit('large')" class="edit-button uk-button-primary"><span uk-icon="icon: plus; ratio: 0.8"></span></button></td>
          </tr>
          <tr>
            <td><p class="info-p">Hyper (100 produse/servicii - GRATIS) : {{(unlimited != null) ? unlimited : 'Negasit'}}</p></td>
            <td><button style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" @click="buyCredit('unlimited')" class="edit-button uk-button-primary"><span uk-icon="icon: plus; ratio: 0.8"></span></button></td>
          </tr>
        </table>
      </div>
      <div class="uk-modal-footer uk-text-right">
				<div class="uk-hidden@s">
          <button @click="hideModal()" class="uk-button uk-button-default uk-button-small" type="button">Inchide</button>
        </div>
        <div class="uk-visible@s">
          <button @click="hideModal()" class="uk-button uk-button-default" type="button">Inchide</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'UserTokenDialog',
  data() {
    return {
      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080',

      small: null,
      medium: null,
      large: null,
      unlimited: null
    }
  },
  props: {
    smallTokens: null,
    mediumTokens: null,
    largeTokens: null,
    unlimitedTokens: null
  },
  methods: {
    hideModal() {
      UIkit.modal("#user-tokens-sections").hide();
    },
    async buyCredit(type) {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
      await axios({
        url: this.backend+'/api/user/addShopToken?type='+type,
        method: 'post',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
        },
        withCredentials: true
      })
        .then((response) => {
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);
          
          let data = {
            balanceSubtracted: response.data.balanceSubtracted,
            type: type
          }
          this.$emit('add_shop_token', data)

          if(type == 'small') 
            this.small++;
          else if(type == 'medium')
            this.medium++;
          else if(type == 'large')
            this.large++;
          else
            this.unlimited++;

          UIkit.notification({message: 'Am adaugat un credit in profilul dumneavoastra. Va multumim!', status: 'success'})
        })
        .catch((error) => {
          if(error.response == null) {
            UIkit.notification({message: 'Am adaugat un credit in profilul dumneavoastra. Daca acesta nu apare, va rugam sa reincarcati pagina. Va multumim!', status: 'danger'})
            return
          }

          if(error.response.status == 403) {
            UIkit.notification({message: 'Nu aveti destul credit in contul dumneavoastra', status: 'danger'})
            return
          }
          UIkit.notification({message: 'Nu am putut adauga creditul in contul dumneavoastra', status: 'danger'})
        })
        .finally(() => {
          this.loading = false
          clearTimeout(timeoutVar)
        })
    },
    setValues() {
      this.small = this.smallTokens
      this.medium = this.mediumTokens
      this.large = this.largeTokens
      this.unlimited = this.unlimitedTokens
    },
    
  },
  watch: {
    $props: {
      handler() {
        this.setValues();
      },
      deep: true,
      immediate: true,
    },
  }
}
</script>

<style lang="scss" scoped>
.info-p {
  margin: 0;
}
.edit-button {
  outline: 0;
  border: none;
  border-radius: 10px;
  height: 30px;
  width: 30px;
  margin: 0px;
  margin-left: 10px;
}
</style>