<template>
  <vk-card padding="small" class="card uk-card-hover">
    <div v-if="loading == false">
      <div class="uk-inline uk-text-left" style="width: 100%">
        <input maxlength="20" v-model="tabData.tabName" class="uk-input custom-input-width" :class="setTabClass()"  type="text" placeholder="Nume categorie" :disabled="(editingTab == true) ? false : true">
        
        <div v-if="editingTab == false">
          <a uk-tooltip="Sterge fila" v-if="tab.id.includes('not-set') == false" style="cursor: pointer;" @click="deleteExistingTab()" class="uk-form-icon uk-form-icon-flip second-button" uk-icon="icon: trash"></a>
          <a uk-tooltip="Editeaza fila" style="cursor: pointer;" class="uk-form-icon uk-form-icon-flip first-button" @click="changeEdit()" uk-icon="icon: pencil"></a>
          
        </div>
        <div v-else>
          <a uk-tooltip="Sterge fila" v-if="tab.id.includes('not-set') == false" style="cursor: pointer;" @click="deleteExistingTab()" class="uk-form-icon uk-form-icon-flip third-button" uk-icon="icon: trash"></a>
          <a uk-tooltip="Salveaza modificarile" style="cursor: pointer;" :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 24 ? 'in-focus' : ''" class="uk-form-icon uk-form-icon-flip second-button" @click="saveChanges()" uk-icon="icon: check"></a>
          <a uk-tooltip="Anuleaza modificarile" style="cursor: pointer;"  class="uk-form-icon uk-form-icon-flip first-button" @click="discardChanges()" uk-icon="icon: ban"></a>
        </div>
      </div>
    </div>
    <div v-else>
      Se incarca
    </div>
  </vk-card>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Tab',
  props: {
    tab: null,
  },
  data() {
    return {
      tabData: {
        tabName: null,
        id: null
      },
      tabToEmit: {
        name: null,
        oldId: null,
        id: null
      },
      oldTabName: null,
      editingTab: false,
      loading: false,

      backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
    }
  },
  methods: {
    setTabClass() {
      let className = !this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 20 ? 'in-focus ' : ''
    
      className += (this.editingTab == true) ? 'custom-input-enabled' : 'custom-input-disabled';

      return className;
    },
    changeEdit() {
      this.editingTab = !this.editingTab
    },
    async deleteExistingTab() {
      this.tabData.tabName = this.oldTabName

      this.tabToEmit.name = this.tabData.tabName;
      this.tabToEmit.oldId = this.tab.id;
      this.tabToEmit.id = this.tabData.id;
      await this.deleteExistingTabFromDb();
      
    },
    async deleteExistingTabFromDb() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
      
      await axios({
        url: this.backend+'/api/tabs/deleteTab?shopId='+this.$route.params.id+'&tabId='+this.tabToEmit.id,
        method: 'delete',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
        },
        withCredentials: true
      })
        .then((response) => {
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

          UIkit.notification({message: 'Categoria a fost stearsa', status: 'success'})

          this.$emit('delete_existing_tab', this.tabToEmit);
        })
        .catch((error) => {
          UIkit.notification({message: 'Nu am putut sterge categoria. Va rugam reincercati', status: 'danger'})
        })
        .finally(() => {
          this.loading = false
          clearTimeout(timeoutVar)
        })
    },
    discardChanges() {
      this.tabData.tabName = this.oldTabName
      this.changeEdit();

      this.tabToEmit.name = this.tabData.tabName;
      this.tabToEmit.oldId = this.tab.id;
      this.tabToEmit.id = this.tabData.id;
      if(this.tabData.id == 'not-set')
        this.$emit("delete_tab", this.tabToEmit);
    },
    async saveChanges() {
      if(this.tabData.tabName == 'Toate' || this.tabData.tabName == 'Categorii' || this.tabData.tabName == 'Comentarii') {
        UIkit.notification({message: 'Nu puteti da acest nume unei categorii', status: 'danger'})
        return;
      }

      await this.saveTabToDb();
    },
    async saveTabToDb() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

      await axios({
        url: this.backend+'/api/tabs/postTab?shopId='+this.$route.params.id+'&tabId='+this.tabData.id,
        method: 'post',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN'),
        },
        data: {
          name: this.tabData.tabName
        },
        withCredentials: true
      })
        .then((response) => {
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

          this.tabToEmit.oldId = this.tab.id
          this.tabToEmit.id = response.data.tabResponseDto.id
          this.tabToEmit.name = response.data.tabResponseDto.name

          this.$emit('new_tab_saved', this.tabToEmit)
          this.oldTabName = this.tabData.tabName
          this.tabData.id = response.data.tabResponseDto.id

          this.changeEdit();
          this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
          this.$emit('show_modal')
          
          if(this.$store.getters.getHasCompletedTutorial)
            UIkit.notification({message: 'Noua categorie a fost salvata', status: 'success'})
        })
        .catch((error) => {
          if(error.response.status == 500) {
            UIkit.notification({message: 'Nu am putut salva categoria. Va rugam sa reincercati', status: 'danger'})
            return;
          }
          if(error.response.status == 406) {
            UIkit.notification({message: 'Exista deja o categorie cu acelasi nume in acest magazin', status: 'danger'})
            return;
          }
          if(error.response.status == 490) {
            UIkit.notification({message: 'Numele adaugat este prea lung', status: 'danger'})
            return;
          }
          UIkit.notification({message: 'Nu am putut salva noua categorie. Va rugam sa reincercati', status: 'danger'})
        })
        .finally(() => {
          this.loading = false
          clearTimeout(timeoutVar)
        })
    }
  },
  mounted() {
    this.tabData.tabName = this.tab.name
    this.tabData.id = this.tab.id
    this.oldTabName = this.tabData.tabName
    this.editingTab = (this.tabData.id == 'not-set') ? true : false

    if(!this.$store.getters.getHasCompletedTutorial) {
      window.scrollTo({
        top: document.body.scrollHeight,
        behavior: 'smooth',
      })
    }
  }
}
</script>

<style scoped>
.custom-input-disabled {
	background-color: transparent;
	border: none;
  color: #222;
}

.custom-input-enabled {
	background-color: rgba(112, 196, 43, 0.144);
	border: none;
	border-bottom: 2px solid #6FC42B;
  color: #222;
}

.first-button {
  margin-right: 0px;
}

.second-button {
  margin-right: 60px;
}

.third-button {
  margin-right: 120px;
}

.custom-input-width {
    width: 75%;
}

@media(max-width: 760px) {
  .custom-input-width {
      width: 70%;
  }
}

@media(max-width: 640px) {
  .first-button {
    margin-right: 90px;
  }

  .second-button {
    margin-right: 45px;
  }

  .custom-input-width {
    width: 65%;
  }
}

@media(max-width: 460px) {
  .custom-input-width {
    width: 50%;
  }
}

@media(max-width: 330px) {
  .first-button {
    margin-right: 60px;
  }

  .second-button {
    margin-right: 30px;
  }
}
</style>