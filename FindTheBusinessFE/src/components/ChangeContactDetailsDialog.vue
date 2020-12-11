<template>
  <div id="change-contact-sections" uk-modal>
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
        
        <region-select id="RegionSelectContact" className="uk-select custom-input" :regionName="true" v-model="contacts.county" country="RO" :placeholder="(contacts.county == null) ? 'Judet' : contacts.county" />

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
					<button @click="hideModal()" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button">Inchide</button>
					<button @click="saveChanges()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Salveaza</button>
				</div>
				<div class="uk-visible@s">
					<button class="uk-button uk-button-default custom-dialog-button" type="button" @click="hideModal()">Inchide</button>
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
      
    },
    async saveContactToDb() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

      await axios({
        url: this.backend+'/api/shops/changeContactData?id='+this.$route.params.id,
        method: 'patch',
        headers: {
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN'),
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN')
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
          this.$cookie.set('CSRF-TOKEN', response.data.csrfToken, 7);
          this.$cookie.set('REFRESH-TOKEN', response.data.refreshToken, 7);

          this.$emit('contacts_changed', this.contacts)
          this.hideModal()

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
</style>