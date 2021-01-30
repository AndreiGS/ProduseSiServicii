<template>
	<div id="login-sections" uk-modal>
		<div class="uk-modal-dialog uk-margin-auto-vertical">
			<button v-on:click="clearTextFiles()" class="uk-modal-close-default" type="button" uk-close></button>
			<div class="uk-modal-header">
				<h2 class="uk-modal-title">Conectare</h2>
			</div>
			<div class="uk-modal-body uk-flex" uk-overflow-auto>
        <div class="uk-width-1-5 uk-visible@s"></div>
				<div class="uk-flex uk-flex-column uk-width-3-5@s">
					<div class="uk-inline">
							<span class="uk-form-icon" uk-icon="icon: mail"></span>
							<input v-model="email" class="uk-input custom-input uk-form-width-large" type="email" placeholder="Email">
					</div>

					<div class="uk-inline">
							<span class="uk-form-icon" uk-icon="icon: lock"></span>
							<input v-model="password" class="uk-input custom-input uk-form-width-large" :type="isPasswordHidden ? 'password' : 'text'" placeholder="Parola">
							<a v-on:click="changeIsPasswordHidden(false)" v-if="isPasswordHidden" class="uk-form-icon uk-form-icon-flip change-password-button" uk-icon="icon: plus"></a>
							<a v-on:click="changeIsPasswordHidden(true)" v-else class="uk-form-icon uk-form-icon-flip change-password-button" uk-icon="icon: minus"></a>
					</div>

					<p @click="resetPassword()" class="forgot-password__button">Am uitat parola</p>

					<div style="margin: 5px;">
						<div v-if="!loading">
							<div v-if="error==403">Contul dumneavoastra a fost blocat!</div>
							<div v-else-if="error==401">Contul dumneavoastra nu a fost activat! Verificati-va email-ul mai intai</div>
							<div v-else-if="error==406 || error==400">Email sau parola gresita</div>
							<div v-else-if="logged!=''">Te-ai autentificat cu succes</div>
						</div>
						<div v-else>Se incarca</div>
					</div>
        </div>
        <div class="uk-width-1-5 uk-visible@s"></div>
			</div>
			<div class="uk-modal-footer uk-text-right">
				<div class="uk-hidden@s">
					<button v-on:click="clearTextFiles()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button" href="#register-sections" uk-toggle>Inregistrare</button>
					<button v-on:click="login()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Conectare</button>
				</div>

				<div class="uk-visible@s">
					<button v-on:click="clearTextFiles()" class="uk-button uk-button-default custom-dialog-button" type="button" href="#register-sections" uk-toggle>Inregistrare</button>
					<button v-on:click="login()" class="uk-button uk-button-primary custom-dialog-button" type="button">Conectare</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import axios from 'axios'

export default {
	name: "LoginDialog",
	data() {
		return {
			loading: false,
			error: 0,
			email: '',
			password: '',
			isPasswordHidden: true,
			logged: '',

			backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
		}
	},
	methods: {
		async login() {
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

			let csrfToken=Math.floor(Math.random() * 10000000);
			
			this.$cookies.set("CSRF-TOKEN", csrfToken)

			await axios({
				  method: 'post',
					url: this.backend+"/api/authentication/login", 
					data: {
						email: this.email,
						password: this.password
					},
					headers: {
						'X-CSRF-TOKEN': csrfToken
					},
					withCredentials: true
				})
				.then(response => {
					this.error = 0;
					/*this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 
														{
															expires: 1000*60*60*24*7, 
															domain: 'localhost', 
															path: '/api',
														}
													);
					this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 
														{
															expires: 1000*60*60*24*7, 
															domain: 'localhost', 
															path: '/api'
														}
													);*/

					this.$cookies.set("CSRF-TOKEN", response.data.csrfToken);
					this.$cookies.set("REFRESH-TOKEN", response.data.refreshToken);
					this.$store.dispatch('changeLogged', response.data.userRole);

					if(this.$router.currentRoute.path == "/profile")
						this.$router.go();
					else
						this.$router.push("/profile")

					UIkit.modal(document.getElementById("login-sections")).hide();
					this.clearTextFiles();
				})
				.catch(error => {
					if(error.response != undefined)
						this.error = error.response.status;
					else
						this.error = 0
				})
				.finally(() => {
					this.loading = false;
					clearTimeout(timeoutVar)
				})
		},
		resetPassword() {
			if(this.$router.currentRoute.path == "/resetPassword")
				this.$router.go();
			else
				this.$router.push("/resetPassword")

			UIkit.modal(document.getElementById("login-sections")).hide();
			this.clearTextFiles();
		},
		changeIsPasswordHidden(value) {
			this.isPasswordHidden = value;
		},
		clearTextFiles() {
			this.password = '';
			this.name = '';
			this.email = '';
		}
	},
	created() {
        return this.$store.watch(
            (state)=>{
                this.logged = this.$store.getters.getLogged
            },
            (newValue, oldValue)=>{
            })
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
.change-password-button {
	:focus {
		border: none!important;
		outline: none!important;
	}
}

.forgot-password__button {
	cursor: pointer;
	color: #6fc42b;
	font-weight: 500;
	font-size: 25;
	padding: 5px;
	width: fit-content;
	margin: 0;
	text-decoration: none;

	&:hover {
		color: #5A9E23;
		font-weight: 600;
	}

	p {
		margin: 0;
	}
}

</style>