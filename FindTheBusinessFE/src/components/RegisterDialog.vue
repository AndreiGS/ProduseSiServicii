<template>
  <div id="register-sections" bg-close="false" uk-modal>
		<div class="uk-modal-dialog uk-margin-auto-vertical">

			<button v-on:click="clearTextFiles()" class="uk-modal-close-default" type="button" uk-close></button>

			<div class="uk-modal-header">
				<h2 class="uk-modal-title">Inregistrare</h2>
			</div>
			<div class="uk-modal-body uk-flex" uk-overflow-auto>
        <div class="uk-width-1-5"></div>
				<div class="uk-flex uk-flex-column uk-width-3-5@s">
					<div class="uk-inline">
							<span class="uk-form-icon" uk-icon="icon: user"></span>
							<input v-model="name" class="uk-input custom-input uk-form-width-large" type="text" placeholder="Numele firmei">
					</div>
					
					<div class="uk-inline">
							<span class="uk-form-icon" uk-icon="icon: mail"></span>
							<input v-model="email" class="uk-input custom-input uk-form-width-large" type="email" placeholder="Email">
					</div>

					<div class="uk-inline">
							<span class="uk-form-icon" uk-icon="icon: lock"></span>
							<input v-model="password" maxlength="63" class="uk-input custom-input uk-form-width-large" :type="isPasswordHidden ? 'password' : 'text'" placeholder="Parola">
							<a v-on:click="changeIsPasswordHidden(false)" v-if="isPasswordHidden" class="uk-form-icon uk-form-icon-flip change-password-button" uk-icon="icon: plus"></a>
							<a v-on:click="changeIsPasswordHidden(true)" v-else class="uk-form-icon uk-form-icon-flip change-password-button" uk-icon="icon: minus"></a>
					</div>

					<Password 
						v-model="password" 
						:strength-meter-only="true" 
						class="password-strength-meter" 
						:secureLength="8"
					/>

					<div class="uk-inline">
							<span class="uk-form-icon" uk-icon="icon: lock"></span>
							<input v-model="confirmationPassword" maxlength="63" class="uk-input custom-input uk-form-width-large" :type="isConfirmationPasswordHidden ? 'password' : 'text'" placeholder="Confirma parola">
							<a v-on:click="changeIsConfirmationPasswordHidden(false)" v-if="isConfirmationPasswordHidden" class="uk-form-icon uk-form-icon-flip change-password-button" uk-icon="icon: plus"></a>
							<a v-on:click="changeIsConfirmationPasswordHidden(true)" v-else class="uk-form-icon uk-form-icon-flip change-password-button" uk-icon="icon: minus"></a>
					</div>

					<label style="margin: 5px"><input v-model="termsChecked" style="margin-right: 5px;" class="uk-checkbox" type="checkbox">Sunt de acord cu <router-link :to="{name: 'TermsAndConditions'}" target="_blank">Termenii si Conditiile</router-link></label>

					<div style="margin: 5px">
						<div v-if="!loading">
							<div v-if="error==409">Email-ul exista deja in baza de date a aplicatiei!</div>
							<div v-else-if="error==423">Parola apare printre parolele scapate pe internet in trecut! Va rugam sa incercati alta parola</div>
							<div v-else-if="error==403">Termenii si conditiile nu au fost acceptate!</div>
							<div v-else-if="error==406">Parolele nu coincid!</div>
							<div v-else-if="error==411">Parola trebuie sa aiba minim 8 caractere!</div>
							<div v-else-if="error==426">Parola este prea slaba! Utilizati o parola care contine atat litere mici si mari cat si cifre si caractere speciale (@ . /)</div>
							<div v-else-if="error!=0">Actiunea dumneavoastra nu a putut fi completata. Incercati din nou!</div>
						</div>
						<div v-else-if="loading">Se incarca</div>
					</div>
				</div>
        <div class="uk-width-1-5"></div>
			</div>
			<div class="uk-modal-footer uk-text-right">
				<div class="uk-hidden@s">
					<button v-on:click="clearTextFiles()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button" href="#login-sections" uk-toggle>Conectare</button>
					<button v-on:click="register()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Inregistrare</button>
				</div>
				<div class="uk-visible@s">
					<button v-on:click="clearTextFiles()" class="uk-button uk-button-default custom-dialog-button" type="button" href="#login-sections" uk-toggle>Conectare</button>
					<button v-on:click="register()" class="uk-button uk-button-primary custom-dialog-button" type="button">Inregistrare</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import axios from 'axios'
const Password = () =>  import(/* webpackChunkName: "others-chunk" */ 'vue-password-strength-meter')

export default {
  name: "RegisterDialog",
	components: {
		Password
	},
  data() {
    return {
			loading: false,
			error: 0,
      email: '',
      password: '',
			name: '',
			confirmationPassword: '',
			isPasswordHidden: true,
			isConfirmationPasswordHidden: true,
			termsChecked: false,

			backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  methods: {
		async register() {
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

			let csrfToken=Math.floor(Math.random() * 10000000);

			this.$cookie.set("CSRF-TOKEN", csrfToken, 7)

			await axios({
				  method: 'post',
					url: this.backend+"/api/authentication/register", 
					data: {
            name: this.name,
						email: this.email,
						password: this.password,
						hasCheckedTerms: this.termsChecked,
						confirmationPassword: this.confirmationPassword
					},
					headers: {
						'X-CSRF-TOKEN': csrfToken,
					},
					withCredentials: true
				})
				.then(response => {
					this.loading = false;
					this.error = 0;

          UIkit.modal(document.getElementById("register-sections")).hide();
					UIkit.modal(document.getElementById("login-sections")).show();
					this.clearTextFiles();
				})
				.catch(error => {
					this.error = error.response.status;
				})
				.finally(() => {
					this.loading = false;
					clearTimeout(timeoutVar)
				})
		},
		changeIsPasswordHidden(value) {
			this.isPasswordHidden = value;
		},
		changeIsConfirmationPasswordHidden(value) {
			this.isConfirmationPasswordHidden = value;
		},
		clearTextFiles() {
			this.isConfirmationPasswordHidden = true;
			this.isPasswordHidden = true;
			this.confirmationPassword = '';
			this.password = '';
			this.name = '';
			this.email = '';
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

.password-strength-meter {
	margin: 0 5px 0 10px;
}
</style>