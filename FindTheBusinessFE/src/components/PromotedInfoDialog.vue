<template>
  <div :id="'promote-info-sections'+this.id" bg-close="false" uk-modal>
    <div class="uk-modal-dialog uk-margin-auto-vertical">
      <button class="uk-modal-close-default" type="button" uk-close></button>
      <div class="uk-modal-header">
        <h2 class="uk-modal-title">Promovare magazin</h2>
      </div>
      <div class="uk-modal-body">
        <div>
          <p v-if="isPromotedInHome">{{`Magazinul este promovat in pagina acasa inca ${promotedDaysInHomeRemaining} zile`}}</p>
          <p v-else>Magazinul nu este promovat in pagina "Acasa"</p>
        </div>
        <div>
          <p v-if="isPromotedInSearches">{{`Magazinul este promovat in cautarile utilizatorilor inca ${promotedDaysInSearchesRemaining} zile`}}</p>
          <p v-else>Magazinul nu este promovat in cautarile utilizatorilor</p>
        </div>
      </div>
      <div class="uk-modal-footer uk-text-right">
        <div class="uk-hidden@s">
          <button v-if="this.$store.getters.getHasCompletedTutorial" class="uk-button uk-button-default custom-dialog-button uk-button-small" type="button" @click="hideModal()">Inchide</button>
          <button class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button" @click="openPromotePanel()">Promoveaza</button>
        </div>
        <div class="uk-visible@s">
          <button v-if="this.$store.getters.getHasCompletedTutorial" class="uk-button uk-button-default custom-dialog-button" type="button" @click="hideModal()">Inchide</button>
          <button v class="uk-button uk-button-primary custom-dialog-button" type="button" @click="openPromotePanel()">Promoveaza</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PromotedInfoDialog',
  props: {
    isPromotedInHome: {
      type: Boolean,
      default: false,
    },
    isPromotedInSearches: {
      type: Boolean,
      default: false,
    },
    promotedDaysInHomeRemaining: {
      type: Number,
      default: 0
    },
    promotedDaysInSearchesRemaining: {
      type: Number,
      default: 0
    },
    id: {
      type: String,
      default: ''
    }
  },
  methods: {
    openPromotePanel() {
      this.hideModal();
      this.$emit('promote_shop', this.id);
      if(this.$store.getters.getIsWithinTutorial)
        this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
    },
    hideModal() {
      console.log("hide");
      UIkit.modal('#promote-info-sections'+this.id).hide();
    }
  }
}
</script>

<style>

</style>