<script>
import UpdateMixin from '@/mixins/update.js'

export default {
  name: 'UpdateAvailableDialog',
  mixins: [
    UpdateMixin
  ]
}
</script>

<template>
  <div id="modal-update" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
        <div class="uk-modal-header">
          <h2 class="uk-modal-title">Actualizare</h2>
        </div>
        <div class="uk-modal-body">
          <p>Apasati butonul de mai jos pentru a descarca ultima versiune</p>
        </div>
        <div class="uk-modal-footer uk-text-right">
          <div class="uk-hidden@s">
            <button @click="refreshApp" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Actualizare</button>
          </div>
          <div class="uk-visible@s">
            <button @click="refreshApp" class="uk-button uk-button-primary custom-dialog-button" type="button">Actualizare</button>
          </div>
        </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.custom-dialog-button{
	margin-right: 2px;
}

#modal-update {
  z-index: 10000;
}
</style>