<template>
	<div>
		<div v-if="loading==false">

			<TutorialLayout :isShowingDialog="isShowingDialog"/>

			<div v-if="error">
				<div :class="windowWidth > 960 ? 'uk-padding-large' : ''">
					<div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
						Nu sunteti conectat. Va rugam sa va conectati intai
					</div>
				</div>
			</div>

			<div v-else :class="windowWidth > 960 ? 'uk-padding-large' : ''">
				<!--DESKTOP-->
				<div class="uk-visible@s">
					<div class="uk-inline" style="width: 100%">
						<!--<img loading="lazy" src="../assets/cat.jpg" class="cover-image" alt="">-->

						<div class="uk-width-1-1 profile-details-background">
							<div class="uk-width-1-1 profile-details-holder">

							</div>
						</div>

						<div v-if="editing == true" class="uk-overlay uk-position-top-center profile-overlay">
							<vk-label style="background: #E82901!important">In editare</vk-label>
						</div>

						<div class="uk-overlay uk-position-center custom-mid-info-desktop">
							<textarea :uk-tooltip="(editing == true) ? 'Nume' : null" id="NameInputDesktop" class="uk-input custom-input-disabled" type="text" placeholder="Nume" v-model="editedName" disabled="true"></textarea>
							<button style="cursor: pointer;" v-if="editing" v-on:click="changePassword()" class="change-password-button uk-button-primary uk-flex uk-flex-middle uk-align-center">
								<span style="margin-right: 5px" uk-icon="icon: refresh; ratio: 0.8"></span>
								<p style="font-family: 'Montserrat'">Schimba parola</p>
							</button>
						</div>

						<div class="uk-overlay uk-position-bottom-left custom-info-desktop" style="padding-bottom: 26px; width: 30%;">
							<textarea :uk-tooltip="(editing == true) ? 'Email' : null" id="EmailInputDesktop" :class="(editing == true) ? 'uk-text-center uk-input custom-input-enabled' : 'uk-text-left uk-input custom-input-disabled'" type="email" placeholder="Email" v-model="editedEmail" disabled="true"></textarea>
						</div>
						<div class="uk-overlay uk-position-bottom-right custom-info-desktop" style="padding-right: 45px; margin-bottom: 30px;">
							<div class="uk-flex uk-flex-middle" style="float: right">
								<p style="color: #666!important;">Credit: {{user.balance}} RON</p>
								<!--<button uk-tooltip="title: Adauga credit; pos: top" style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" class="edit-button uk-button-primary" href="#add-balance-sections" uk-toggle><span uk-icon="icon: credit-card; ratio: 1"></span></button>-->
							</div>
						</div>

						<div :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 1 ? 'in-focus' : ''" class="uk-overlay uk-position-top-left custom-info-desktop custom-padding-edit-buttons" style="padding-top: 16px!important; text-align: left;">
							<button @click="tokensButton()" uk-tooltip="title: Credite pentru fatade de magazin; pos: right" style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" class="edit-button uk-button-primary" href="#user-tokens-sections" uk-toggle>
								<span uk-icon="icon: lifesaver; ratio: 1"></span>
							</button>
						</div>

						<div class="uk-overlay uk-position-top-right custom-padding-edit-buttons">
							<div class="uk-flex uk-flex-column">
								<div>
									<div v-if="!editing">
										<button uk-tooltip="title: Editeaza profilul; pos: left" style="cursor: pointer;" v-on:click="changeEdit()" class="edit-button uk-button-primary"><span uk-icon="icon: pencil; ratio: 0.8"></span></button>
									</div>
									<div v-if="editing">
										<button uk-tooltip="title: Anuleaza modificarile; pos: bottom" style="cursor: pointer;" v-on:click="discardChanges()" class="edit-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
										<button uk-tooltip="title: Salveaza modificarile; pos: bottom" style="cursor: pointer;" v-on:click="saveChanges()" class="edit-button uk-button-primary"><span uk-icon="icon: check; ratio: 0.8"></span></button>
									</div>
								</div>
								<div class="uk-text-right" style="margin-top: 10px;">
									<button v-if="editing" uk-tooltip="title: Sterge profilul; pos: bottom" style="cursor: pointer;" v-on:click="deleteUser()" class="edit-button uk-button-danger"><span uk-icon="icon: trash; ratio: 0.8"></span></button>
									<button uk-tooltip="title: Deconectare; pos: left" style="cursor: pointer;" v-on:click="logoutUser()" class="edit-button uk-button-primary"><span uk-icon="icon: sign-out; ratio: 0.8"></span></button>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!--MOBILE-->
				<div class="uk-hidden@s">
					<div class="uk-inline uk-width-1-1">
						<div class="uk-width-1-1 profile-details-background">
							<div class="uk-width-1-1 profile-details-holder">

							</div>
						</div>

						<div v-if="editing == true" class="uk-overlay uk-position-left-center profile-overlay">
							
						</div>

						<div class="uk-overlay uk-position-center custom-mid-info-mobile custom-padding-info">
							<textarea id="NameInputMobile" class="uk-input" :class="editing == false ? 'custom-input-disabled' : 'custom-input-enabled'" type="text" placeholder="Nume" v-model="editedName" :disabled="editing == false ? true : false"></textarea>
						</div>

						<div :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 1 ? 'in-focus' : ''" style="color: white; z-index: 10;" class="uk-overlay uk-position-bottom-right custom-info-desktop custom-padding-edit-buttons">
							<button @click="tokensButton()" style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" class="edit-button uk-button-primary" href="#user-tokens-sections" uk-toggle>
								<span uk-icon="icon: lifesaver; ratio: 1"></span>
							</button>
						</div>

						<div v-if="editing" class="uk-overlay uk-position-top-left change-password-mobile">
							<button style="cursor: pointer;" v-on:click="changePassword()" class="change-password-button uk-button-primary uk-flex uk-flex-middle uk-align-center">
								<span style="margin-right: 5px" uk-icon="icon: refresh; ratio: 0.8"></span>
								<p>Schimba parola</p>
							</button>
							<vk-label style="background: #E82901!important">In editare</vk-label>
						</div>

						<div class="uk-overlay uk-position-bottom-center custom-info-mobile custom-padding-info uk-width-1-1">
							<div>
								<textarea id="EmailInputMobile" class="uk-input custom-input-disabled" type="email" placeholder="Email" v-model="editedEmail" disabled="true"></textarea>
							</div>
							<div class="uk-flex uk-flex-center uk-flex-middle credit-mobile" style="margin-bottom: 5px;">
								<p class="uk-text-break" style="color: #666!important;">Credit: {{user.balance}} RON</p>
								<!--<button style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" class="edit-button uk-button-primary" href="#add-balance-sections" uk-toggle><span uk-icon="icon: credit-card; ratio: 1"></span></button>-->
							</div>
						</div>

						<div class="uk-overlay uk-position-top-right custom-padding-edit-buttons">
							<div class="uk-flex uk-flex-column">
								<div>
									<div v-if="!editing">
										<button uk-tooltip="title: Editeaza profilul; pos: left" style="cursor: pointer;" v-on:click="changeEdit()" class="edit-button uk-button-primary"><span uk-icon="icon: pencil; ratio: 0.8"></span></button>
									</div>
									<div v-if="editing">
										<button uk-tooltip="title: Anuleaza modificarile; pos: bottom" style="cursor: pointer;" v-on:click="discardChanges()" class="edit-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
										<button uk-tooltip="title: Salveaza modificarile; pos: bottom" style="cursor: pointer;" v-on:click="saveChanges()" class="edit-button uk-button-primary"><span uk-icon="icon: check; ratio: 0.8"></span></button>
									</div>
								</div>
								<div class="uk-text-right" style="margin-top: 10px;">
									<button v-if="editing" uk-tooltip="title: Sterge profilul; pos: bottom" style="cursor: pointer;" v-on:click="deleteUser()" class="edit-button uk-button-danger"><span uk-icon="icon: trash; ratio: 0.8"></span></button>
									<button uk-tooltip="title: Deconectare; pos: left" style="cursor: pointer;" v-on:click="logoutUser()" class="edit-button uk-button-primary"><span uk-icon="icon: sign-out; ratio: 0.8"></span></button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<br>

				<ShopCardGrid 
					v-on:delete_shop="deleteShop($event)" 
					v-on:new_shop_added="newShopAdded($event)" 
					v-on:change_published="changePublished($event)"
					v-on:discard_new_shop="discardNewShop()" 
					v-on:add_shop="addShop()" 
					v-on:refresh_page="refreshPage()" 
					v-on:show_modal="showDialog()" 
					v-on:close_modal="closeDialog()" 
					@change_balance="changeBalance($event)"
					@promote_shop="promoteShop($event)"
					:shops="user.shops" 
					:subcategories="subcategories"
					:categories="categories"
					:hasAddedShop="shopAdded"/>
			</div>

			<AddBalanceDialog v-on:change_balance="changeBalance($event)" />
			<DeleteUserDialog />
			<UserTokensDialog 
				v-on:add_shop_token="addShopToken($event)"
				@close_dialog="closeDialog()"
				:smallTokens="user.smallTokens"	
				:mediumTokens="user.mediumTokens"	
				:largeTokens="user.largeTokens"	
				:unlimitedTokens="user.unlimitedTokens"	
			/>

		</div>

		<div v-else :class="windowWidth > 960 ? 'uk-padding-large' : ''">
			<div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
				Se incarca
			</div>
		</div>

	</div>
</template>

<script>
import axios from 'axios'
const ShopCardGrid = () => import(/* webpackChunkName: "profile-chunk" */ '@/components/ShopCardGrid.vue');
const AddBalanceDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/AddBalanceDialog.vue');
const DeleteUserDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/DeleteUserDialog.vue');
const UserTokensDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/UserTokensDialog.vue');
const TutorialLayout = () => import(/* webpackChunkName: "others-chunk" */ '@/components/TutorialLayout.vue');

export default {
	name: "Profile",
	data() {
		return {
			isShowingDialog: false,
			error: false,
			loading: false,
			user: {
				email: '',
				name: '',
				balance: 0,
				shops: [],
				smallTokens: null,
				mediumTokens: null,
				largeTokens: null,
				unlimitedTokens: null,
				noOfShopsAllowed: 5,
			},
			subcategories: [],
			categories: [],
			editedEmail: '',
			editedName: '',
			editing: false,
			shopAdded: false,

			backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
		}
	},
	components: {
		ShopCardGrid,
		DeleteUserDialog,
		AddBalanceDialog,
		UserTokensDialog,
	  TutorialLayout,
	},
	metaInfo() {
    return { 
      title: "Profil - ProduseSiServicii.ro",
      meta: [
          { name: 'description', content:  'Aceasta platforma are scopul de a ajuta comerciantii in a-si gasi clienti, iar clientii in a gasi produse si servicii direct de la producator.'},
          { property: 'og:title', content: "ProduseSiServicii.ro - Gaseste-ti clientii si comerciantii"},
          { property: 'og:site_name', content: 'ProduseSiServicii.ro'},
          { property: 'og:type', content: 'website'},    
          { name: 'robots', content: 'index,follow'} 
      ]
    }
  },
	methods: {
		tokensButton() {
			this.isShowingDialog = true
			if(this.$store.getters.getIsWithinTutorial)
        this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
		},
		closeDialog() {
			this.isShowingDialog = false;
		},
		showDialog() {
			this.isShowingDialog = true;
		},
		addShopToken(data) {
			this.user.balance -= data.balanceSubtracted

			if(data.type == 'small') {
				this.user.smallTokens++;
				return
			}
			if(data.type == 'medium') {
				this.user.mediumTokens++;
				return
			}
			if(data.type == 'large') {
				this.user.largeTokens++;
				return
			}
			this.user.unlimitedTokens++;
			return
		},
		changePublished(data) {
			this.user.shops.forEach(shop => {
				if(shop.id == data.id) {
					shop.published = data.isPublished
					return
				}
			});
		},
		promoteShop(promoteData) {
			this.user.shops.forEach(shop => {
				if(shop.id == promoteData.shopId) {
					if(promoteData.where == 'SEARCHES')
						shop.promotedDaysInSearchesRemaining += promoteData.days
					else
						shop.promotedDaysInHomeRemaining += promoteData.days
					return
				}
			});
		},
		async getUserProfile() {
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
			await axios({
				method: 'get',
				url: this.backend+'/api/user/getUserProfileInfo',
				headers: {
					'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
				},
				withCredentials: true
			})
				.then(response => {
					this.error=false;
					this.user.email = response.data.email;
					this.user.name = response.data.name;
					this.user.balance = response.data.balance;
					this.user.shops = response.data.shops;
					this.user.noOfShopsAllowed = response.data.noOfShopsAllowed
					this.subcategories = response.data.subcategories;
					this.categories = response.data.categories;
					this.user.smallTokens = response.data.smallTokens;
					this.user.mediumTokens = response.data.mediumTokens;
					this.user.largeTokens = response.data.largeTokens;
					this.user.unlimitedTokens = response.data.unlimitedTokens;

					this.editedEmail = this.user.email;
					this.editedName = this.user.name;

					if(this.user.shops.length > 0) {
						this.$store.dispatch('changeHasCompletedTutorial', true);
					}
					
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);
				})
				.catch(error => {
					this.error = true;

					this.$store.dispatch('changeLogged', '');
					this.$cookie.delete('CSRF-TOKEN')
					this.$cookie.delete('REFRESH-TOKEN')
				})
				.finally(() => {
					this.loading = false;
					clearTimeout(timeoutVar)
				})
		},
		changeInputToEditable(input) {
			input.disabled = false;
			input.className = "uk-input custom-input-enabled";
		},
		changeInputToDisabled(input) {
			input.disabled = true;
			input.className = "uk-input custom-input-disabled";
		},
		changeEdit() {
			this.editing = !this.editing;
			if(this.editing) {
				//DESKTOP ENABLE
				this.changeInputToEditable(document.getElementById('NameInputDesktop'));
				this.changeInputToEditable(document.getElementById('EmailInputDesktop'));

				//MOBILE ENABLE
				this.changeInputToEditable(document.getElementById('NameInputMobile'));
				this.changeInputToEditable(document.getElementById('EmailInputMobile'));
			} else {
				//DESKTOP DISABLE
				this.changeInputToDisabled(document.getElementById('NameInputDesktop'));
				this.changeInputToDisabled(document.getElementById('EmailInputDesktop'));

				//MOBILE DISABLE
				this.changeInputToDisabled(document.getElementById('NameInputMobile'));
				this.changeInputToDisabled(document.getElementById('EmailInputMobile'));
			}	
		},
		async logoutUser() {
			await axios({
				method: 'post',
				url: this.backend+'/api/authentication/logout',
				headers: {
					'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN'),
					'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN')
				},
				withCredentials: true
			})
				.finally(() => {
					this.error=0;
					UIkit.notification({message: 'V-ati deconectat cu succes', status: 'success'})
					this.$cookie.delete("CSRF-TOKEN");
					this.$cookie.delete("REFRESH-TOKEN");

					this.$store.dispatch('changeLogged', '');

					this.$router.push("/");
				})
		},
		newShopAdded(shop){
			if(shop.oldId.includes('not-set')) {
				this.shopAdded = false
			}
			
			for(let i=0; i<this.user.shops.length; i++) {
				if(this.user.shops[i].id == shop.oldId) {
					if(shop.shopSize == "SMALL")
						this.user.smallTokens--;
					else if(shop.shopSize == "MEDIUM")
						this.user.mediumTokens--;
					else if(shop.shopSize == "LARGE")
						this.user.largeTokens--;
					else if(shop.shopSize == "UNLIMITED")
						this.user.unlimitedTokens--;

					this.user.shops[i].name = shop.title
					this.user.shops[i].description = shop.description
					this.user.shops[i].id = shop.id
					this.user.shops[i].smallPhoto = shop.image
					this.user.shops[i].subcategories = shop.subcategories
					this.user.shops[i].categories = shop.category
					this.user.shops[i].maximumSize = shop.shopSize
					this.user.shops[i].published = shop.published

					break;
				}
			}
		},
		async saveChanges() {
			if(this.editedEmail == this.user.email && this.editedName == this.user.name) {
				this.discardChanges();
				return;
			}

			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
			let errored = false

			await axios({
				method: 'post',
				url: this.backend+'/api/user/sendEmailForChangingInformation',
				headers: {
					'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN'),
					'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN')
				},
				data: {
					newEmail: this.editedEmail,
					newName: this.editedName
				},
				withCredentials: true
			})
				.then(response => {
					this.error=0;

          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

					UIkit.notification({message: 'Email-ul pentru confirmare a fost trimis', status: 'success'})
				})
				.catch(error => {
					errored = true
					UIkit.notification({message: 'Email-ul pentru confirmare nu a putut fi trimis', status: 'danger'})
				})
				.finally(() => {
					this.loading = false;
					clearTimeout(timeoutVar)
				})
				if(errored == false)
						this.changeEdit();
		},
		discardChanges() {
			this.editedEmail = this.user.email;
			this.editedName = this.user.name;

			this.editing = false;

			//DESKTOP DISABLE
			this.changeInputToDisabled(document.getElementById('NameInputDesktop'));
			this.changeInputToDisabled(document.getElementById('EmailInputDesktop'));

			//MOBILE DISABLE
			this.changeInputToDisabled(document.getElementById('NameInputMobile'));
			this.changeInputToDisabled(document.getElementById('EmailInputMobile'));
		},
		async changePassword() {
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

			await axios({
        method: 'post',
        url: this.backend+"/api/user/sendEmailForChangingPassword",
        headers: {
          "X-CSRF-TOKEN": this.$cookie.get("CSRF-TOKEN"),
          "X-REFRESH-TOKEN": this.$cookie.get("REFRESH-TOKEN"),
        },
        withCredentials: true
      })
        .then(response => {
					this.loading = false;
					this.error=0;


          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

					UIkit.notification({message: 'Email-ul pentru confirmare a fost trimis', status: 'success'})
					this.editing = false;
        })
        .catch(error => {
					UIkit.notification({message: 'Email-ul pentru confirmare nu a putut fi trimis', status: 'danger'})
					this.loading = false;
					clearTimeout(timeoutVar)
        })
		},
		changeBalance(balanceToAdd) {
			this.user.balance += balanceToAdd
		},
		async refreshPage() {
			await this.getUserProfile();
		},
		addShop() {
			if(this.shopAdded == true) {
				UIkit.notification({message: "Ati adaugat deja o fatada de magazin. Dupa ce o salvati puteti adauga una noua!", status: 'warning'})
				return;
			}
			console.log(this.user.noOfShopsAllowed, this.user.shops.length)
			if(this.user.noOfShopsAllowed < this.user.shops.length+1) {
				UIkit.notification({message: 'Nu puteti sa adaugati fatadei de magazin deoarece ati atins numarul maxim de fatade de magazin. Daca doriti sa mai adaugati magazin va rugam sa ne scrieti un email!', status: 'danger'})
        return
			}
			this.shopAdded = true;
			this.user.shops.unshift(
				{
					id: 'not-set' + Math.floor(Math.random() * 10000) + 1,
					description: 'Descriere',
					name: 'Titlu',
					smallPhoto: null,
					rating: 0,
					type: 'NOT_PROMOTED',
					maximumSize: 'FREE'
				}
			)
		},
		discardNewShop() {
			var removeIndex = this.user.shops.map(function(item) { return item.id.includes('not-set'); }).indexOf(true);
			this.user.shops.splice(removeIndex, 1)
			this.shopAdded = false;
		},
		deleteShop(id) {
			try {
				var removeIndex = this.user.shops.map(function(item) { return item.id; }).indexOf(id);
				this.user.shops.splice(removeIndex, 1)
			} catch(e) {
				UIkit.notification({message: 'Fatada de magazin a fost stearss, dar modificarea va aparea la reincarcarea paginii', status: 'warning'})
				console.log(e)
			}
		},
		deleteUser() {
			UIkit.modal("#delete-user-sections").show()
		}
	},
	async mounted() { 
		await this.getUserProfile()
	}
}
</script>

<style lang="scss" scoped>
.cover-image {
	max-height: 350px!important;
	width: 100%;
	object-fit: cover;
	filter: brightness(30%);
}

#NameInputDesktop, #EmailInputDesktop {
	margin: 0;
}

.custom-mid-info-desktop {
	font-size: 30px;
}
.custom-info-desktop {
	font-size: 20px;
}

.custom-mid-info-mobile {
	font-size: 20px;
}
.custom-info-mobile {
	font-size: 15px;
}

p {
	padding: 0;
	margin: 0;
	color: white;
}

.edit-button {
	outline: 0;
  border: none;
  border-radius: 10px;
  width: 45px;
  height: 30px;
	margin: 0 5px; 
}

.custom-padding-edit-buttons {
	padding: 20px!important;
}

.custom-padding-info {
	padding: 20px 10px;
}

.change-password-button {
	margin-top: 10px;
	border-radius: 10px;
	padding: 5px 10px!important;
}


.custom-input-disabled {
	background-color: transparent;
	color: #666;
	border: none;
	text-align: center;
}

.custom-input-disabled, .custom-input-enabled {
	margin-bottom: 20px; 
	width: 95%;
	min-width: 95%;
	max-width: 95%;
	overflow: hidden;
	line-height: inherit;
	height: auto!important;
	min-height: 30px;
	max-height: 100px;
	resize: none;
}

.custom-input-enabled {
	background-color: rgba(112, 196, 43, 0.144);
	color: #666;
	border: none;
	border-bottom: 2px solid #6FC42B;
	text-align: center;
}

.change-password-mobile {
	margin: 0px!important;
	padding: 20px!important;
	button {
		margin: 0px!important;
	}
}

.credit-mobile {
	margin-top: 10px;
}

.profile-details-background {
	height: 300px;
	background: rgba(111,196,43,0.2);
	padding: 15px;
}

.profile-details-holder {
	background: white;
	height: 100%;
}

@media (max-width: 350px) {
	.edit-button {
		width: 35px;
		height: 30px;
		margin-left: 5px;
	}
	.credit-mobile {
		margin-top: 5px;
	}
	.uk-input {
		height: 30px;
	}
	#NameInputMobile {
		margin-bottom: 10px;
		width: 90%;
	}
}

@media (min-width: 2000px) {
	.cover-image {
		max-height: 700px!important;
	}
}
</style>