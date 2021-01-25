<script>
const ContactData = () => import(/* webpackChunkName: "modals-chunk" */ "@/components/ContactData");

export default {
  name: 'Schedule',
  props: {
    editingShop: {
      type: Boolean,
      default: false,
      required: true
    },
    schedule: {
      type: String,
      default: '',
    },
    isOnline: {
      type: Boolean,
      default: false,
    },
    phone: {
      type: String,
      default: '',
    },
    address: {
      type: String,
      default: '',
    },
    email: {
      type: String,
      default: '',
    },
    websiteLink: {
      type: String,
      default: '',
    },
    county: {
      type: String,
      default: '',
    },
  },
  components: {
    ContactData
  },
  data() {
    return {
      scheduleCopy: '',
      showContact: false,
    }
  },
  mounted() {
    this.scheduleCopy = this.schedule
  },
  watch: {
    scheduleCopy: function(newScheduleCopy, oldScheduleCopy) {
      if(newScheduleCopy !== this.schedule)
        this.$emit('change_schedule', newScheduleCopy)
    },
    schedule: function(newSchedule, oldSchedule) {
      if(newSchedule !== this.scheduleCopy)
        this.scheduleCopy = newSchedule;
    }
  }
}
</script>

<template>
  <div>
    <article class="uk-article" style="margin: 0;">
      <vk-card padding="small" class="uk-height-1-1 uk-flex schedule-card-container">
        <div class="uk-flex uk-flex-column">
          <div class="header">
            <h2>Program de lucru</h2>
            <h5 v-if="isOnline">* Magazinul este online, orarul fiind destinat suportului</h5>
          </div>
          <textarea v-if="editingShop"
            v-model="scheduleCopy"
            maxlength="255" 
            class="uk-textarea custom-textarea-enabled" 
          />
          <p v-else-if="scheduleCopy == null" class="custom-schedule-paragraph no-schedule">Magazinul nu a adaugat un program de lucru</p>
          <p v-else class="custom-schedule-paragraph">{{scheduleCopy}}</p>
          <vk-button class="uk-margin" @click="showContact = true" type="primary">Contacteaza-ne</vk-button>
        </div>
      </vk-card>
    </article>
    
      <vk-modal :stuck="true" center :show.sync="showContact">
        <vk-modal-close></vk-modal-close>
        <vk-modal-title>DATE DE CONTACT</vk-modal-title>
        <ContactData
          :phone="phone"
          :address="address"
          :email="email"
          :websiteLink="websiteLink"
          :county="county"
        />
        <p class="uk-text-right">
          <vk-button @click="showContact = false" class="uk-margin-small-right">Inchide</vk-button>
        </p>
      </vk-modal>
  </div>
</template>

<style lang="scss" scoped>
.custom-textarea-enabled {
	background-color: rgba(112, 196, 43, 0.144);
	color: rgb(31, 31, 31);
	border: none;
	border-bottom: 2px solid #6FC42B;
	text-align: center;
  white-space: pre-line;
  max-width: 350px;
  min-width: 350px;
}

.custom-schedule-paragraph {
  background-color: transparent;
	color: rgb(31, 31, 31);
	border: none;
	text-align: center;
  white-space: pre-line;
  min-height: auto;
  resize: none;
}

.header {
  h2 {
    color: #666; 
    font-weight: 600;
    margin: 0;
  }
  h5 {
    color: #6fc42b;
    margin-top: 5px;
    font-weight: 400;
  }
}

.no-schedule {
  color: #666
}

.schedule-card-container {
  place-content: center;
  align-items: center;
}

@media(max-width: 640px) {
  .custom-textarea-enabled {
    max-width: 200px;
    min-width: 200px;
  }
}
</style>