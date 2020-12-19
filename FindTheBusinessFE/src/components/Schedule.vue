<script>
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
  },
  data() {
    return {
      scheduleCopy: ''
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
    <article class="uk-article">
      <vk-card padding="small">
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
      </vk-card>
    </article>
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
</style>