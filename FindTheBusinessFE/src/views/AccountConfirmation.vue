<template>
	<div :class="windowWidth > 960 ? 'uk-padding-large' : ''">
    <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
			<div v-if="loading">Se incarca</div>
			<div v-else>
				<div v-if="error==400">
					Email-ul de confirmare a expirat. Va rugam sa va reinregistrati
					<!--TODO buton pentru retrimitere email--> 
				</div>
				<div v-else-if="error>0 && error!=400">A aparut o eroare. Daca dupa o reincercare aceasta persista, va rugam sa ne contactati!</div>
				<div v-else>Contul a fost validat! Bine ati venit!</div>
			</div>
    </div>
	</div>
</template>

<script>
import axios from 'axios'

export default {
	name: 'AccountConfirmation',
	props: {
		code: ''
	},
	data() {
		return {
			loading: false,
			error: 0,

			backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
		}
	},
	metaInfo() {
    return { 
      title: "Confirmare - ProduseSiServicii.ro",
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
	async created() {
		var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

		let csrfToken=this.$cookies.get('CSRF-TOKEN') || Math.floor(Math.random() * 10000000);

		this.$cookies.set("CSRF-TOKEN", csrfToken)
		await axios({
			method: 'patch',
			url: this.backend+"/api/authentication/confirmEmail?code="+this.code, 
			headers: {
				'X-CSRF-TOKEN': csrfToken
			},
			withCredentials: true
		})
			.then(response => {
				if(response.status == 200) {
					this.error = 0
				} else {
					this.error = 404;
				}
			})
			.catch(error => {
				this.error = error.response.status;
			})
			.finally(() => {
				this.loading = false;
				clearTimeout(timeoutVar)
			})
	}
}
</script>

<style scoped>

</style>