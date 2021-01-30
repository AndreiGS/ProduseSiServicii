<template>
  <div :class="windowWidth > 960 ? 'uk-padding-large' : ''">
    <div v-if="loading==true || error!=0" class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
      <div v-if="loading">
        Se incarca
      </div>
      <div v-else-if="error!=0">
        <div v-if="error==403">
          Email-ul de schimbare a parolei a expirat
          <!--TODO buton pentru retrimitere email--> 
        </div>
        <div v-if="error==423">
          Parola apare printre parolele scapate pe internet in trecut! Va rugam sa incercati alta parola
          <!--TODO buton pentru retrimitere email--> 
        </div>
        <div v-else-if="error==411">
          Parola trebuie sa aiba minim 8 caractere!
        </div>
        <div v-else-if="error==426">
          Parola este prea slaba! Utilizati o parola care contine atat litere mici si mari cat si cifre si caractere speciale (@ . /)
        </div>
        <div v-else>
          Nu am putut sa va schimbam parola. Va rugam sa va conectati si sa reincercati!
        </div>
      </div>
    </div>
    <div class="uk-align-center uk-width-2-5@s uk-width-1-3@m uk-margin-xlarge@s uk-margin-small@xs">
      <input class="uk-input custom-input-enabled" type="text" placeholder="Parola noua" v-model="newPassword" maxlength="63">
      <Password 
        v-model="newPassword" 
        :strength-meter-only="true" 
        class="password-strength-meter" 
        :secureLength="8"
      />

      <button style="cursor: pointer; padding: 5px" v-on:click="changePassword()" class="change-password-button uk-button-primary uk-flex uk-flex-middle uk-align-center">
        <span uk-icon="icon: refresh; ratio: 0.8"></span>
        <p style="font-family: 'Montserrat'; margin: 5px;">Schimba parola</p>
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
const Password = () => import(/* webpackChunkName: "others-chunk" */ 'vue-password-strength-meter') 

export default {
  name: 'PasswordChangingConfirmation',
  props: {
    code: '',
  },
  components: {
    Password
  },
  data() {
    return {
      newPassword: '',
      loading: false,
      error: 0,

      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  methods: {
    async changePassword() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

      let csrfToken=Math.floor(Math.random() * 10000000);
			
			this.$cookies.set("CSRF-TOKEN", csrfToken)

      await axios({
        method: 'post',
        url: this.backend+"/api/user/changePassword?code="+this.code,
        headers: {
          "X-CSRF-TOKEN": csrfToken,
        },
        data: {
          password: this.newPassword
        },
        withCredentials: true
      })
        .then(response => {
          this.error=0;
          this.newPassword = '';
          UIkit.notification({message: 'Parola a fost modificata cu succes', status: 'success'})
          this.$router.push('/');
        })
        .catch(error => {
          this.error = error.response.status;
          UIkit.notification({message: 'Parola nu a putut fi modificata', status: 'danger'})
        })
        .finally(() => {
          this.loading = false;
          clearTimeout(timeoutVar)
        })
    }
  },
  metaInfo() {
    return { 
      title: "Parola - ProduseSiServicii.ro",
      meta: [
          { name: 'robots', content: 'noindex' },
          { name: 'description', content:  'Aceasta platforma are scopul de a ajuta comerciantii in a-si gasi clienti, iar clientii in a gasi produse si servicii direct de la producator.'},
          { property: 'og:title', content: "ProduseSiServicii.ro - Gaseste-ti clientii si comerciantii"},
          { property: 'og:site_name', content: 'ProduseSiServicii.ro'},
          { property: 'og:type', content: 'website'},    
          { name: 'robots', content: 'index,follow'} 
      ]
    }
  },
}
</script>

<style scoped>
.custom-input-enabled {
	background-color: rgba(112, 196, 43, 0.144);
	color: black;
	border: none;
	border-bottom: 2px solid #6FC42B;
	text-align: center;
}

.change-password-button {
  outline: 0;
  border: none;
	margin-top: 10px;
	border-radius: 10px;
	padding: 5px 10px!important;
}
</style>