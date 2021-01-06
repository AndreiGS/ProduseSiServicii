<template>
  <div id="delete-user-sections" bg-close="false" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Stergere cont</h2>
      </div>
      <div class="uk-modal-body">
        <p>Aceasta optiune va sterge atat contul dumeavoastra, cat si toate datele legate de acesta</p>
        <p v-if="loading==true">Se incarca</p>
      </div>
      <div class="uk-modal-footer uk-text-right">
        <div class="uk-hidden@s">
					<button @click="hideModal()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button">Inchide</button>
					<button @click="deleteUser()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Sterge</button>
				</div>
				<div class="uk-visible@s">
					<button class="uk-button uk-button-default custom-dialog-button" type="button" @click="hideModal()">Inchide</button>
					<button @click="deleteUser()" class="uk-button uk-button-primary custom-dialog-button" type="button">Sterge</button>
				</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'DeleteUserDialog',
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
      UIkit.modal('#delete-user-sections').hide()
    },
    async deleteUser() {
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

			await axios({
        url: this.backend+'/api/user/deleteProfile',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN'),
        },
				withCredentials: true,
				method: 'delete'
			})
				.then((response) => {
					this.$cookie.delete('REFRESH-TOKEN')
					this.$cookie.delete('CSRF-TOKEN')
          this.$store.dispatch('changeLogged', '');
          this.$router.push("/")
          
          this.hideModal();
					UIkit.notification({message: 'Contul dumneavoastra a fost sters. Ne veti lipsi :(', status: 'success'})
				})
				.catch((error) => {
					UIkit.notification({message: 'Nu am putut sterge contul dumneavoastra', status: 'danger'})
				})
				.finally(() => {
          this.loading = false;
          clearTimeout(timeoutVar)
				})
		}
  }
}
</script>

<style>
.custom-dialog-button{
	margin-right: 2px;
}
</style>