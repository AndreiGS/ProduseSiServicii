<script>
import axios from 'axios'

export default {
  name: 'ResetPassword',
  data() {
    return {
      isLoading: false,
      hasError: 0,
      email: '',
      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  methods: {
    async sendEmail() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

			let csrfToken=Math.floor(Math.random() * 10000000);
			
			this.$cookie.set("CSRF-TOKEN", csrfToken, 7)

      await axios({
        method: 'post',
        url: `${this.backend}/api/authentication/sendResetPasswordEmail`, 
        data: {
          email: this.email,
        },
        headers: {
          'X-CSRF-TOKEN': csrfToken
        },
        withCredentials: true,
      })
        .then(() => {
          UIkit.notification({message: `Am trimis un email pentru modificarea parolei la adresa: ${this.email}`, status: 'success'})
          this.$router.push('/')
        })
        .catch(() => {
          UIkit.notification({message: `Nu am putut trimite un email pentru modificarea parolei la adresa: ${this.email}`, status: 'danger'})
          this.hasError = true;
        })
        .finally(() => {
          clearTimeout(timeoutVar);
        })
    }
  },
  metaInfo() {
    return { 
      title: "Am uitat parola - ProduseSiServicii.ro",
      meta: [
          { name: 'description', content:  'Aceasta platforma are scopul de a ajuta comerciantii in a-si gasi clienti, iar clientii in a gasi produse direct de la producator sau diferite servicii.'},
          { property: 'og:title', content: "ProduseSiServicii.ro - Gaseste-ti clientii si comerciantii"},
          { property: 'og:site_name', content: 'ProduseSiServicii.ro'},
          { property: 'og:type', content: 'website'},    
          { name: 'robots', content: 'index,follow'} 
      ]
    }
  },
}
</script>

<template>
  <div :class="windowWidth > 960 ? 'uk-padding-large' : ''">
    <div v-if="isLoading || hasError" class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
      <div v-if="isLoading">
        Se incarca
      </div>
      <div v-else-if="hasError">
        <div v-if="hasError">
          Email-ul nu a putut fi trimis
        </div>
      </div>
    </div>
    <div class="uk-align-center uk-width-2-5@s uk-width-1-3@m uk-margin-xlarge@s uk-margin-small@xs">
      <input class="uk-input custom-input-enabled" type="email" placeholder="Email-ul contului" v-model="email" maxlength="255">

      <button style="cursor: pointer; padding: 5px" v-on:click="sendEmail()" class="change-password-button uk-button-primary uk-flex uk-flex-middle uk-align-center">
        <span uk-icon="icon: push; ratio: 0.8"></span>
        <p style="font-family: 'Montserrat'; margin: 5px;">Trimite email</p>
      </button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
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

.password-strength-meter {
	margin: 0 5px 0 10px;
}
</style>