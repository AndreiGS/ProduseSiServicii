<template>
  <div id="change-contact-sections" bg-close="false" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Detalii contact</h2>
      </div>
      <div class="uk-modal-body">
        <input v-model="contacts.phone" class="uk-input custom-input" type="text" placeholder="Telefon">
        <input v-model="contacts.address" class="uk-input custom-input" type="text" placeholder="Adresa">
        <input v-model="contacts.email" class="uk-input custom-input" type="text" placeholder="Email">
        <input v-model="contacts.websiteLink" class="uk-input custom-input" type="text" placeholder="Link website">
        
        <div class="uk-flex uk-flex-row">
          <region-select id="RegionSelectContact" className="uk-select custom-input" :regionName="true" v-model="contacts.county" country="RO" :placeholder="(contacts.county == null) ? 'Judet' : contacts.county" />
          <button style="cursor: pointer;" @click="contacts.county = null" class="reset-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
        </div>
        

        <!--<div class="uk-text-center">
          <h3>Detalii actuale</h3>
          <p>Numar telefon: {{(oldPhone != null) ? oldPhone : '-'}}</p>
          <p>Adresa: {{(oldAddress != null) ? oldAddress : '-'}}</p>
          <p>Email: {{(oldEmail != null) ? oldEmail : '-'}}</p>
          <p>Website: {{(oldWebsiteLink != null) ? oldWebsiteLink : '-'}}</p>
          <p>Judet: {{(oldCounty != null) ? oldCounty : '-'}}</p>
        </div>-->
        <p class="uk-text-center" v-if="loading==true">Se incarca</p>
      </div>
      <div class="uk-modal-footer uk-text-right">
        <div class="uk-hidden@s">
					<button @click="hideModal()" :class="!this.$store.getters.getHasCompletedTutorial ? 'uk-hidden' : ''" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button">Inchide</button>
					<button @click="saveChanges()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Salveaza</button>
				</div>
				<div class="uk-visible@s">
					<button :class="!this.$store.getters.getHasCompletedTutorial ? 'uk-hidden' : ''" class="uk-button uk-button-default custom-dialog-button" type="button" @click="hideModal()">Inchide</button>
					<button @click="saveChanges()" class="uk-button uk-button-primary custom-dialog-button" type="button">Salveaza</button>
				</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ChangeContactDetailsDialog',
  props: {
    oldPhone: null,
    oldWebsiteLink: null,
    oldEmail: null,
    oldAddress: null,
    oldCounty: null
  },
  data() {
    return {
      loading: false,

      contacts: {
        address: null,
        websiteLink: null,
        phone: null,
        email: null,
        county: null
      },

      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  methods: {
    hideModal() {
      UIkit.modal('#change-contact-sections').hide()
    },
    changeContactDetailsToNullIfNecessary() {
      if(this.contacts.address == '') 
        this.contacts.address = null
      if(this.contacts.websiteLink == '') 
        this.contacts.websiteLink = null
      if(this.contacts.phone == '') 
        this.contacts.phone = null
      if(this.contacts.email == '') 
        this.contacts.email = null
      if(this.contacts.county == '') 
        this.contacts.county = null
    },
    async saveChanges() {
      this.changeContactDetailsToNullIfNecessary()
      await this.saveContactToDb();
      this.$emit('hide_modal', true)
      if(this.$store.getters.getIsWithinTutorial)
        this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
    },
    async saveContactToDb() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

      await axios({
        url: this.backend+'/api/shops/changeContactData?id='+this.$route.params.id,
        method: 'patch',
        headers: {
          'X-REFRESH-TOKEN': this.$cookies.get('REFRESH-TOKEN'),
          'X-CSRF-TOKEN': this.$cookies.get('CSRF-TOKEN')
				},
        data: {
          address: this.contacts.address,
          websiteLink: this.contacts.websiteLink,
          phone: this.contacts.phone,
          email: this.contacts.email,
          county: this.contacts.county
        },
        withCredentials: true
      })
        .then((response) => {
          this.$cookies.set('CSRF-TOKEN', response.data.csrfToken);
          this.$cookies.set('REFRESH-TOKEN', response.data.refreshToken);

          this.$emit('contacts_changed', this.contacts)
          this.hideModal()
          if(this.$store.getters.getHasCompletedTutorial)
            UIkit.notification({message: 'Datele de contact au fost actualizate', status: 'success'})
        })
        .catch((error) => {
          UIkit.notification({message: 'Datele de contact nu au putut fi actualizate. Va rugam reincercati', status: 'danger'})
        })
        .finally(() => {
          this.loading = false
          clearTimeout(timeoutVar)
        })
    },
    addOnlineOption() {
			const el = document.getElementById("RegionSelectContact");
			var option = document.createElement("option");
			option.text = "Online";
			option.value = "Online";
      el.appendChild(option)
		}
  },
  mounted(){
    this.addOnlineOption();
  },
  watch: {
      $props: {
        handler() {
          this.contacts.address = this.oldAddress
          this.contacts.websiteLink = this.oldWebsiteLink
          this.contacts.county = this.oldCounty
          this.contacts.phone = this.oldPhone
          this.contacts.email = this.oldEmail
        },
        deep: true,
        immediate: true,
      },
    }
}
</script>

<style scoped>
.custom-dialog-button{
	margin-right: 2px;
}

.custom-input {
  margin: 5px;
}

.reset-button {
	outline: 0;
  border: none;
	border-radius: 10px;
  width: 45px;
	height: 30px;
	align-self: center;
	margin: 0 10px;
}
</style>