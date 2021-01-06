<template>
  <div id="add-balance-sections" bg-close="false" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
        <button class="uk-modal-close-default" type="button" uk-close></button>
        <div class="uk-modal-header">
          <h2 class="uk-modal-title">Adauga credite</h2>
        </div>
        <div class="uk-modal-body" uk-overflow-auto>
          <div class="uk-width-1-5 uk-visible@s"></div>
          <div class="uk-width-3-5@s uk-align-center">
							<div class="uk-inline" v-if="!paying">
                <span class="uk-form-icon" uk-icon="icon: cart"></span>
							  <input v-model.number="amount" class="uk-input custom-input uk-form-width-large" type="number" placeholder="Valoare(RON)">
              </div>

              <div v-else>
                <StripeElements 
                  ref="elementsRef"
                  :pk="publishableKey"
                  :amount="amount"
                  @token="tokenCreated"
                  @loading="loading = $event"
                >
                </StripeElements>
                <div class="uk-text-center">
                  <button style="margin-left: 5px;" class="uk-button uk-button-primary custom-dialog-button" @click="submit">Plateste {{amount}} RON</button>
                </div>
              </div>

              <p class="uk-text-center" v-if="loading">Se incarca</p>
              <p class="uk-text-center" v-if="error">A aparut o eroare</p>
          </div>
          <div class="uk-width-1-5 uk-visible@s"></div>
        </div>
        <div class="uk-modal-footer uk-text-right">
				<div class="uk-hidden@s">
					<button v-on:click="closePayment()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button">Inchide</button>
					<button v-if="!paying" v-on:click="addBalance(true)" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Cumpara</button>
          <button v-else v-on:click="addBalance(false)" class="uk-button uk-button-default custom-dialog-button" type="button">Inapoi</button>  
        </div>

				<div class="uk-visible@s">
					<button v-on:click="closePayment()" class="uk-button uk-button-default custom-dialog-button" type="button">Inchide</button>
					<button v-if="!paying" v-on:click="addBalance(true)" class="uk-button uk-button-primary custom-dialog-button" type="button">Cumpara</button>
          <button v-else v-on:click="addBalance(false)" class="uk-button uk-button-default custom-dialog-button" type="button">Inapoi</button>
				</div>
			</div>
    </div>
  </div>
</template>

<script>
import { StripeElements } from 'vue-stripe-checkout';
import axios from 'axios'

export default {
  name: "AddBalanceDialog",
  data() {
    return {
      amount: null,
      paying: false,

      loading: false,
      error: false,

      publishableKey: process.env.VUE_APP_STRIPE_PUBLIC_KEY, //TODO add environmental variable
      token: null,
      charge: null,
      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  components: {
    StripeElements,
  },
  methods: {
    addBalance(paying) {
      if(this.amount < 5) {
        return UIkit.notification({message: 'Nu puteti adauga mai putin de 5 RON', status: 'danger'})
      }

      if(Number.isInteger(this.amout)) {
        return UIkit.notification({message: 'Nu puteti adauga decat sume intregi (ex: 5)', status: 'danger'})
      }

      this.paying = paying;
      
    },
    closePayment() {
      UIkit.modal('#add-balance-sections').hide();
    },

    async submit () {
      await this.$refs.elementsRef.submit();
    },
    async tokenCreated (token) {
      this.token = token;
      this.charge = {
        source: token.id,
        amount: this.amount,
      }
      await this.sendTokenToServer();
    },
    async sendTokenToServer() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
      await axios({
        method: 'post',
        url: this.backend+'/api/user/charge',
        data: {
          tkn: this.charge.source,
          amount: this.charge.amount
        },
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get("CSRF-TOKEN"),
          'X-REFRESH-TOKEN': this.$cookie.get("REFRESH-TOKEN")
        },
        withCredentials: true
      })
        .then((response) => {
          UIkit.notification({message: 'Ati adaugat ' + response.data.amount/100 + ' RON', status: 'success'})
	  
	  
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);
          
          this.paying = false;
          this.amount = null;
          
          this.$emit('change_balance', response.data.amount/100)

          UIkit.modal('#add-balance-sections').hide();
        })
        .catch((error) => {  
          this.error = true;
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

.custom-input {
  margin: 5px;
}

</style>
