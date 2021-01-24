<template>
  <div :class="windowWidth > 960 ? 'uk-padding-large' : ''">
    <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
      <div v-if="loading">
        Se incarca
      </div>
      <div v-else>
				<div v-if="error!=0">
					<div v-if="error==403">
						Email-ul de schimbare a datelor a expirat
						<!--TODO buton pentru retrimitere email--> 
					</div>
					<div v-else>
						Nu am putut sa va schimbam datele. Va rugam sa va conectati si sa reincercati!
					</div>
				</div>
				<div v-else>Informatiile dumneavoastra au fost modificate</div>
			</div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ChangedInformations',
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

		let csrfToken = this.$cookie.get("CSRF-TOKEN")
    let refreshToken = this.$cookie.get("REFRESH-TOKEN")
    
		await axios({
			method: 'patch',
			url: this.backend+"/api/user/changeInfo?code="+this.code, 
			headers: {
				'X-CSRF-TOKEN': csrfToken,
				'X-REFRESH-TOKEN': refreshToken
			},
			withCredentials: true
		})
			.then(response => {
				this.error=0;
			})
			.catch(error => {
				this.$store.dispatch('changeLogged', '');
				this.$cookie.delete('CSRF-TOKEN')
				this.$cookie.delete('REFRESH-TOKEN')
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