<script>
import OfflineMixin from '@/mixins/offline.js'

export default {
  mixins: [OfflineMixin]
}
</script>

<template>
  <div id="modal-offline" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
        <div class="uk-modal-header">
          <h2 class="uk-modal-title">Fara retea</h2>
        </div>
        <div class="uk-modal-body">
          <p>Pentru utilizarea tuturor functionalitatilor aplicatiei este necesara o conexiune la internet</p>
          <br>
          <p>Totusi, puteti utiliza aplicatia in continuare, fara a obtine toate datele adaugate de alti utilizatori de la ultima utilizare a aplicatiei de catre dumeneavoastra</p>
        </div>
        <div class="uk-modal-footer uk-text-right">
          <div class="uk-hidden@s">
            <button @click="okButton" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Bine</button>
          </div>
          <div class="uk-visible@s">
            <button @click="okButton" class="uk-button uk-button-primary custom-dialog-button" type="button">Bine</button>
          </div>
        </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.custom-dialog-button{
	margin-right: 2px;
}

#modal-offline {
  z-index: 10000;
}
</style>