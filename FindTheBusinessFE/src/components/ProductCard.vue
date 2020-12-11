<template>
  <div>
    <vk-card  padding="small" class="card uk-card-hover">
      <div v-if="loading == true">
        Se incarca
      </div>
      <vk-grid v-else>
        <div class="card-image-custom-width">
          <div @click="if(editingItem == true) showImageEdit = true" class="uk-inline uk-width-1-1" :style="(this.editingItem==true) ? 'cursor: pointer;' : ''">

            <img loading="lazy" v-if="cannotFindImage == false && (oldImage != null || newImage != null)" :src="(newImage != null) ? newImage : oldImage" class="card-image" :class="(this.editingItem==true) ? 'changing-image' : ''" alt="" @onerror="this.cannotFindImage=true; this.hasImageLoaded=true" @load="onImgLoad">
            
            <div v-else class="no-image-div">
              <p v-if="editingItem == false" class="uk-overlay uk-position-center overlay" style="color: white;">Nicio imagine gasita</p>
            </div>

            <div v-if="this.editingItem == true" class="uk-position-center change-image-overlay">
              <span uk-icon="camera" ratio="2"></span>
              <p>Schimba imaginea</p>
            </div>
          </div>
        </div>
        <div style="padding-left: 0 !important; text-align: left;" class="card-body-custom-width">
          <vk-grid style="height: 100%">
            <div class="uk-width-2-3@s uk-width-3-5@m uk-width-2-3@l uk-width-3-4@xl" style="height: 100%; padding-left: 0px;">
              <div class="uk-flex uk-visible@s" style="word-wrap: break-word;">
                <vk-card-title style="margin-bottom: 0px; word-wrap: break-word; width: 100%;">
                  <textarea v-if="editingItem == true" :uk-tooltip="(editingItem == true) ? 'Titlu (maxim 40 caractere)' : null" maxlength="40" :id="'TitleInputDesktop'+item.id" class="uk-textarea textarea-title" :class="(editingItem == false) ? 'custom-input-disabled' : 'custom-input-enabled'" type="text" placeholder="Titlu" v-model="item.title" :disabled="(editingItem == true) ? false : true"></textarea>
                  <h3 v-else style="margin: 0; padding-left: 10px; font-weight: 400;">{{item.title}}</h3>
                </vk-card-title>
              </div>
              <div class="uk-flex uk-hidden@s" style="word-wrap: break-word; justify-content: center;">
                <vk-card-title style="margin-bottom: 0px; word-wrap: break-word; width: 100%;">
                  <textarea v-if="editingItem == true" :uk-tooltip="(editingItem == true) ? 'Titlu (maxim 40 caractere)' : null" maxlength="40" style="text-align: center; padding: 0" :id="'TitleInputMobile'+item.id" class="uk-textarea textarea-title" :class="(editingItem == false) ? 'custom-input-disabled' : 'custom-input-enabled'" type="text" placeholder="Titlu" v-model="item.title" :disabled="(editingItem == true) ? false : true"></textarea>
                  <h3 class="uk-text-center" v-else style="margin: 0; padding-left: 0; font-weight: 400;">{{item.title}}</h3>
                </vk-card-title>
              </div>

              <div class="uk-text-center uk-hidden@s">
                <textarea uk-tooltip="Descriere (maxim 255 caractere)" maxlength="255" style="text-align: center;" class="uk-textarea custom-textarea-enabled text-area-props" v-if="editingItem == true" :id="'DescriptionInputMobile'+item.id" type="text" placeholder="Descriere" v-model="item.description"></textarea>
                <p :id="'ph-desc-mobile'+item.id" class="description-p-props-mobile" style="text-align: center; white-space: pre-line;" v-else>{{item.description}}</p>
              </div>
              <div class="uk-visible@s">
                <textarea uk-tooltip="Descriere (maxim 255 caractere)" maxlength="255" class="uk-textarea custom-textarea-enabled text-area-props" v-if="editingItem == true" :id="'DescriptionInputDesktop'+item.id" type="text" placeholder="Descriere" v-model="item.description"></textarea>
                <p :id="'ph-desc-desktop'+item.id" class="description-p-props-desktop" style="padding-left: 10px; margin-bottom: 0px; white-space: pre-line;" v-else>{{item.description}}</p>
              </div>

              <div class="showmore-button-container uk-text-center uk-hidden@s" v-if="editingItem == false">
                <button style="cursor: pointer;" class="custom-showmore-button" v-if="isShowingFullDesc == false" @click="getFullDescription()">Arata toata descrierea</button>
                <button style="cursor: pointer;" class="custom-showmore-button" v-else @click="cutDescription()">Arata mai putin</button>
              </div>
              <div style="padding-left: 10px;" class="showmore-button-container uk-visible@s" v-if="editingItem == false">
                <button style="cursor: pointer;" class="custom-showmore-button" v-if="isShowingFullDesc == false" @click="getFullDescription()">Arata toata descrierea</button>
                <button style="cursor: pointer;" class="custom-showmore-button" v-else @click="cutDescription()">Arata mai putin</button>
              </div>

              <div class="uk-hidden@s">
                <div class="edit-buttons-container" v-if="isOwner == true">
                  <div v-if="editingItem == false">
                    <button uk-tooltip="Editeaza articol" @click="editItem()" style="cursor: pointer; margin-left: 0px;" class="edit-button-item-card uk-button-primary"><span uk-icon="icon: pencil; ratio: 0.8" class="edit-button-icon"></span></button>
                  </div>
                  <div v-else>
                    <button uk-tooltip="Sterge articol" @click="deleteItem()" style="cursor: pointer;margin-left: 0px" v-if="item.id.includes('not-set') == false" class="edit-button-item-card uk-button-danger"><span style="margin: 3px" uk-icon="icon: trash; ratio: 0.8"></span></button>
                    <button uk-tooltip="Salveaza modificarile" style="cursor: pointer;"  @click="saveItem()" class="edit-button-item-card uk-button-primary"><span style="margin: 3px" uk-icon="icon: check; ratio: 0.8"></span></button>
                    <button uk-tooltip="Anuleaza modificarile" style="cursor: pointer;" @click="discardItem()" class="edit-button-item-card uk-button-danger"><span style="margin: 3px" uk-icon="icon: ban; ratio: 0.8"></span></button>
                  </div>
                </div>
                <div style="margin-top: 10px">
                  <div v-if="editingItem == true">
                    <div class="uk-text-center">
                      Fila
                    </div>
                    <div>
                      <select uk-tooltip="Schimba fila" style="cursor: pointer;"  v-if="allTabs.length > 0" class="uk-select" v-model="item.tabName">
                        <option value="none">Neselectat</option>
                        <option v-for="(tab, index) in allTabs" :key="index" :value="tab.name">{{tab.name}}</option>
                      </select>
                      <div v-else class="uk-text-center">
                        Nu exista
                      </div>
                    </div>
                    <div class="uk-text-center" style="margin-top: 10px">
                      Pret <input uk-tooltip="(Optional)Pret aproximativ si cantitate/perioada (maxim 15 caractere)" maxlength="15" :id="'PriceInputMobile'+item.id" class="uk-input custom-input-enabled" type="text" placeholder="(Optional)-Pret/Cantitate(Perioada)" v-model="item.price">
                    </div>
                  </div>
                  <div v-else-if="editingItem == false && allLowercase(tab) == 'all'">
                    <div class="uk-text-center">
                      Fila: {{(item.tabName == null || item.tabName == 'none') ? '-' : item.tabName}}
                    </div>
                    <div class="uk-text-right price">
                      Pret: {{item.price !== null ? item.price : 'Neprecizat'}}
                    </div>
                  </div>
                </div>
                <vk-button v-if="editingItem == false" class="custom-button uk-hidden@s" @click="showContact = true" type="primary">Contact</vk-button>
              </div>
            </div>
            <div class="uk-width-1-3@s uk-width-2-5@m uk-width-1-3@l uk-width-1-4@xl uk-visible@s" style="padding: 0">
              <div v-if="isOwner == true" class="edit-buttons-container uk-visible@s">
                <div v-if="editingItem == false">
                  <button uk-tooltip="Editeaza articol" @click="editItem()" style="cursor: pointer; margin-left: 0px" class="edit-button-item-card edit-item-button uk-button-primary"><span uk-icon="icon: pencil; ratio: 0.8" class="edit-button-icon"></span></button>
                </div>
                <div v-else>
                  <button uk-tooltip="Sterge articol" @click="deleteItem()" style="cursor: pointer; margin-left: 0px" v-if="item.id.includes('not-set') == false" class="edit-button-item-card uk-button-danger"><span style="margin: 3px" uk-icon="icon: trash; ratio: 0.8"></span></button>
                  <button uk-tooltip="Salveaza modificarile" style="cursor: pointer;" @click="saveItem()" class="edit-button-item-card uk-button-primary"><span style="margin: 3px" uk-icon="icon: check; ratio: 0.8"></span></button>
                  <button uk-tooltip="Anuleaza modificarile" style="cursor: pointer;" @click="discardItem()" class="edit-button-item-card uk-button-danger"><span style="margin: 3px" uk-icon="icon: ban; ratio: 0.8"></span></button>
                </div>
              </div>
              <div style="position: absolute; right: 20px;" :style="(isOwner == true) ? 'top: 60px;' : 'top: 20px;'" class="uk-width-1-5@s uk-width-1-6@l">
                <div>
                  <div v-if="editingItem == true">
                    <div class="uk-text-right">
                      Pret <input uk-tooltip="(Optional)Pret aproximativ si cantitate/perioada (maxim 15 caractere)" maxlength="15" :id="'PriceInputDesktop'+item.id" class="uk-input custom-input-enabled" type="text" placeholder="(Optional)-Pret/Cantitate(Perioada)" v-model="item.price">
                    </div>
                    <div class="uk-text-right">
                      Fila
                    </div>
                    <div>
                      <select uk-tooltip="Schimba fila" style="cursor: pointer; height: 32px" v-if="allTabs.length > 0" class="uk-select" v-model="item.tabName">
                        <option value="none">Neselectat</option>
                        <option v-for="(tab, index) in allTabs" :key="index" :value="tab.name">{{tab.name}}</option>
                      </select>
                      <div v-else class="uk-text-right">
                        Nu exista
                      </div>
                    </div>
                  </div>
                  <div v-else-if="editingItem == false && allLowercase(tab) == 'all'">
                    <div class="uk-text-right price">
                      Pret: {{item.price !== null ? item.price : 'Neprecizat'}}
                    </div>
                    <div class="uk-text-right tab">
                      Fila: {{(item.tabName == null || item.tabName == 'none') ? '-' : item.tabName}}
                    </div>
                  </div>
                </div>
              </div>

              <vk-button v-if="editingItem == false" class="uk-visible@s custom-button" @click="showContact = true" type="primary">Contact</vk-button>
            </div>
            <!--<div class="uk-width-1-6@s uk-visible@s">
              <p style="font-size: 20px;"><b>{{ price }} lei</b></p>
            </div>-->
          </vk-grid>
        </div>
      </vk-grid>
    </vk-card>

    <vk-modal center :show.sync="showContact">
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

    <vk-modal-full :show.sync="showImageEdit">
      <vk-modal-full-close></vk-modal-full-close>
      <vk-modal-title>Poza produs</vk-modal-title>
      <!--<VueCropper style="height: 500px; text-align: center"
        ref="cropper"
        dragMode='move'
        alt="Imagine negasita"
        :src="(newImage != null) ? newImage : oldImage"
        :aspectRatio="4/3"
        :imgStyle="{
          height: '100%'
        }"
        :toggleDragModeOnDblclick="false"
      />-->
      <CropperDialogComponent 
        v-on:hide_modal="hideModal()"
        v-on:change_image="changeImage($event)"

        :oldImage="oldImage" 
        :aspectRatio="4/3"
        :minHeight="300"
        :minWidth="400"
        :from="'productcard'"
      />
    </vk-modal-full>
  </div>
</template>

<script>
import axios from 'axios'
const ContactData = () => import(/* webpackChunkName: "profile-chunk" */ "./ContactData");
import CropperDialogComponent from '@/components/CropperDialogComponent.vue'
import 'cropperjs/dist/cropper.css';


export default {
    name: "ProductCard",
    components: {
      ContactData,
      CropperDialogComponent
    },
    props: {
        //initial data
        oldId: null,
        oldTitle: null,
        oldDescription: null,
        oldImage: null,
        oldTab: null,

        //contact data
        phone: null,
        email: null,
        address: null,
        websiteLink: null,
        county: null,
        price: null,

        //tabs
        allTabs: null,
        tab: null,

        isOwner: null,
        editingShop: null,
    },
    data () {
        return {
          showImageEdit: false,

          showContact: false,
          editingItem: false,
          item: {
            id: null,
            title: null,
            description: null,
            tabName: 'none',
            price: null,
          },
          loading: false,
          isShowingFullDesc: false,

          newImage: null,
          cannotFindImage: false,
          hasImageLoaded: false,

          backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080' 
        }
    },
    methods: {
      onImgLoad() {
        this.hasImageLoaded = true;
      },
      //Edit image methods
      hideModal() {
        this.showImageEdit = false
      },
      changeImage(data) {
        this.newImage = data.image 
        this.cannotFindImage = false
        this.hideModal();
      },
      //---------------------------------

      editItem() {
        this.editingItem = !this.editingItem
      },
      discardItem() {
        if(this.item.id == 'not-set') {
          this.$emit('delete_item', this.item)
          return
        }

        this.$emit('delete_new_image', this.item.id);

        this.resetValues();
        this.editItem();
      },
      async deleteItem() {
        var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
        await axios({
          url: this.backend+'/api/items/deleteItem?itemId='+this.item.id+'&shopId='+this.$route.params.id,
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
            this.$emit('delete_item', this.item)
          })
          .catch((error) => {
            UIkit.notification({message: 'Nu am putut salva magazinul. Va rugam reincercati', status: 'danger'})
          })
          .finally(() => {
            this.loading = false
            clearTimeout(timeoutVar)
          })
      },
      async saveItem() {
        var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
        await axios({
          url: this.backend+'/api/items/addItem?shopId='+this.$route.params.id+'&itemId='+this.item.id,
          method: 'post',
          headers: {
            'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
            'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
          },
          data: {
            title: this.item.title,
            description: this.item.description,
            price: this.item.price,
            tabName: this.item.tabName,
            newImage: this.newImage
          },
          withCredentials: true
        })
          .then((response) => {
            this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
            this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

            let newTab = response.data.itemResponseDto.tabWithoutIdDto;

            let itemToSave = {
              id: this.oldId,
              newId: response.data.itemResponseDto.id,
              title: response.data.itemResponseDto.title,
              description: response.data.itemResponseDto.description,
              price: response.data.itemResponseDto.price,
              image: response.data.itemResponseDto.photo,
              tabName: (newTab == null) ? null : newTab.name
            }

            this.$emit('save_item', itemToSave)
            this.editItem();
          })
          .catch((error) => {
            if(error.response == null) {
              return
            }

            if(error.response.status == 406) {
              UIkit.notification({message: 'Nu ati adaugat nicio poza produsului', status: 'danger'})
              return
            }
            if(error.response.status == 490) {
              UIkit.notification({message: 'Numele adaugat este prea lung', status: 'danger'})
              return;
            }
            if(error.response.status == 491) {
              UIkit.notification({message: 'Descrierea adaugata este prea lunga', status: 'danger'})
              return;
            }
            if(error.response.status == 406) {
              this.$store.dispatch('changeLogged', '')
              this.$cookie.delete('REFRESH-TOKEN')
              this.$cookie.delete('CSRF-TOKEN')
              return
            }
            if(error.response.status == 400) {
              UIkit.notification({message: 'Nu puteti adauga produsul deoarece depasiti capacitatea maxima a acestuia', status: 'danger'})
              return
            }
            UIkit.notification({message: 'Nu am putut salva magazinul. Va rugam reincercati', status: 'danger'})
          })
          .finally(() => {
            this.loading = false
            clearTimeout(timeoutVar)
          })
      },
      resetValues() {
        this.item.id = this.oldId
        this.item.title = this.oldTitle
        this.item.description = this.oldDescription
        this.item.price = this.price
        this.newImage = null
        this.item.tabName = this.oldTab
      },
      allLowercase(tab) {
        return tab.toLowerCase()
      },
      cutDescription() {
        let elementDesktop = document.getElementById('ph-desc-desktop'+this.item.id)
        let elementMobile = document.getElementById('ph-desc-mobile'+this.item.id)
        elementDesktop.classList.remove("description-p-props-desktop-showing-full-desc")
        elementDesktop.classList.add("description-p-props-desktop")
        elementMobile.classList.add("description-p-props-mobile")

        elementDesktop.style.marginTop = null
        elementMobile.style.marginTop = null
        elementDesktop.style.wordBreak = null
        elementMobile.style.wordBreak = null

        this.isShowingFullDesc = false;
      },
      getFullDescription() {
        let elementDesktop = document.getElementById('ph-desc-desktop'+this.item.id)
        let elementMobile = document.getElementById('ph-desc-mobile'+this.item.id)
        elementDesktop.classList.remove("description-p-props-desktop")
        elementDesktop.classList.add("description-p-props-desktop-showing-full-desc")
        elementMobile.classList.remove("description-p-props-mobile")

        elementDesktop.style.marginTop = '10px'
        elementMobile.style.marginTop = '10px'
        elementDesktop.style.wordBreak = 'break-word'
        elementMobile.style.wordBreak = 'break-word'

        this.isShowingFullDesc = true;
      },
    },
    mounted() {
      this.resetValues();
      
      this.editingItem = (this.item.id == 'not-set') ? true : false
    },
    watch: {
      $props: {
        handler() {
          if(this.editingShop == false)
            this.editingItem = false;
        },
        deep: true,
        immediate: true,
      },
      'showImageEdit': {
        handler() {
          this.showImageEdit === true ? document.body.style.overflow = 'hidden' : document.body.style.overflow = 'unset';
        },
        deep: true,
        immediate: true,
      }
    }
}
</script>

<style lang="scss" scoped>
.textarea-title {
  min-height: 40px;
  height: 40px;
}

.textarea-desc {
  min-height: 105px;
}

.edit-button-item-card {
  outline: 0;
  border: none;
	margin: 0 0 0 10px;
	border-radius: 10px;
  
  :focus, :active {
    border: none
  }
}

.text-area-props{
  word-break: break-word; 
  margin-top: 10px; 
  height: 105px;
}

.change-image-overlay{
  p {
    margin-top: 10px; 
    margin-bottom: 0; 
    color: white;
  }
  span {
    color: white;
  }
}

.changing-image {
  filter: brightness(30%);
}

.custom-input-disabled {
	background-color: transparent;
	color: rgb(31, 31, 31);
	border: none;
	text-align: left;
}

.custom-input-enabled {
	background-color: rgba(112, 196, 43, 0.144);
	color: rgb(31, 31, 31);
	border: none;
	border-bottom: 2px solid #6FC42B;
	text-align: left;
}

.edit-button-icon {
  margin: 3px;
}

.custom-textarea-disabled {
	background-color: transparent;
	color: rgb(31, 31, 31);
	border: none;
	text-align: left;
  resize: none;
}

.custom-textarea-enabled {
	background-color: rgba(112, 196, 43, 0.144);
	color: rgb(31, 31, 31);
	border: none;
	border-bottom: 2px solid #6FC42B;
	text-align: left;
}

.description-p-props-desktop{
  margin-top: 10px; 
  word-break: break-word; 
  height: 65px; 
  overflow: hidden;
}

.description-p-props-desktop-showing-full-desc {
  min-height: 65px;
}

.description-p-props-mobile {
  margin-top: 10px; 
  word-break: break-word; 
  max-height: 65px;
  overflow: hidden;
}

.card {
  margin-bottom: 10px;
}
.dollar-green {
  color: #5A9E23;
  font-size: 35px;
  font-weight: 100;
  display: inline;
}
.dollar-gray {
  color: rgba(0, 0, 0, 0.42);
  font-size: 30px;
  display: inline;
}

.custom-showmore-button {
  border: none;
  background-color: transparent;
  color: #6FC42B;
  padding: 0;
  font-family: 'Montserrat'; 
}

.card-image-custom-width {
  padding-left: 0!important;
  height: 155px;
  .uk-inline {
    height: 100%;
  }
}

.card-image {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.showmore-button-container {
  margin-top: 28px
}

.no-image-div {
  background-color: rgb(136, 136, 136);
  height: 155px;
  width: 206.6px;
}

.price {
  font-weight: bold;
  font-size: 17px;
}

.tab {
  font-size: 17px;
}

@media (max-width: 639px) {
  .card-image-custom-width {
    width: 100%;
    height: 100%;
  }

  .card-body-custom-width {
    width: 100%;
  }
  .custom-button {
    width: 100%;
    margin-top: 10px;
  }
  .edit-buttons-container{
    text-align: center;
    margin-top: 20px;
  }
  .no-image-div {
    width: 100%;
  }
}

@media(width: 640px) {
  .custom-button {
    position: absolute;
    bottom: 20px;
    right: 20px;
    width: 20%;
  }
  .custom-star {
    float: right
  }
  .edit-item-button {
    float: right
  }
  .showmore-button-container {
    margin-top: 0px!important;
  }
  .description-p-props-desktop {
    height: 85px; 
  }
  .description-p-props-desktop-showing-full-desc {
    min-height: 85px;
  }

  .card-image-custom-width {
    margin-right: 5px;
    height: 155px;
    width: 206.6px;
  }

  .card-body-custom-width {
    margin-top: 0!important;
    width: calc(100% - 211.6px);
  }

  .no-image-div {
    width: 100%;
  }
}

@media (min-width: 641px) {
  .custom-button {
    position: absolute;
    bottom: 20px;
    right: 20px;
  }
  .custom-star {
    position: absolute;
    top: 60px;
    right: 20px;
  }
  .edit-buttons-container{
    text-align: right;
    position: absolute;
    right: 20px;
    top: 20px;
  }
  .card-image-custom-width {
    margin-right: 10px;
    width: 206.6px;
    height: 155px;
    .uk-card {
      margin-right: 10px
    } 
  }

  .card-body-custom-width {
    margin-top: 0!important;
    width: calc(100% - 216.6px);
  }
  .edit-item-button {
    float: right
  }
}

@media (min-width: 2000px) {
  .text-area-props{
    height: 250px!important;
  }
  .description-p-props-desktop{
    height: 185px; 
  }
  .description-p-props-desktop-showing-full-desc {
    min-height: 185px;
  }
  .showmore-button-container {
    margin-top: 0px!important
  }
  .card-image-custom-width {
    height: 300px;
    width: 400px;
  }

  .no-image-div {
    height: 300px;
    width: 400px;
  }

  .card-body-custom-width {
    width: calc(100% - 410px);
  }
  .showmore-button-container {
    margin-top: 43.2px!important
  }
}

@media (max-width: 1400px) {
  .price {
    font-size: 15px;
  }

  .tab {
    font-size: 15px;
  }
}

@media (max-width: 1200px) {
  .price {
    font-size: 13px;
  }

  .tab {
    font-size: 12px;
  }
}

@media (max-width: 660px) {
  .card-image-custom-width {
    .uk-card {
      margin-right: 5px
    } 
  }
  .edit-button-item-card {
    margin-left: 5px;
  }

  .price {
    font-size: 17px;
    margin-top: 15px;
  }
}

</style>