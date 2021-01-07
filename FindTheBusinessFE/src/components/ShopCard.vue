<template>
  <div>
    <vk-card padding="small" class="card uk-card-hover">
      <vk-grid v-if="loading == false">
        <div class="card-image-custom-width">
          <div class="uk-card uk-card-body">
            <!--<div v-if="isPromotedInHome" class="uk-card-badge uk-label" style="right: 10px; top: 10px;">PROMOVAT{{promotedDaysInHomeRemaining != null ? ` ${promotedDaysInHomeRemaining} zile "ACASA"` : null}}</div>
            <div v-if="isPromotedInSearches" class="uk-card-badge uk-label" style="right: 10px; top: 10px;">PROMOVAT{{promotedDaysInSearchesRemaining != null ? ` ${promotedDaysInSearchesRemaining} zile "CAUTARI"` : null}}</div>-->

            <button @click="openPromotedInfoPanel()" v-if="isPromotedInHome || isPromotedInSearches" uk-tooltip="Informatii promovare" style="cursor: pointer;" class="promote-info-button uk-button-primary uk-card-badge uk-label"><span uk-icon="icon: info; ratio: 0.8"></span></button>

            <div @click="showEditImageModal()" class="uk-inline uk-width-1-1" :style="(this.editingShop==true) ? 'cursor: pointer;' : ''">
              
              <img loading="lazy" v-if="(newImage != null && newImage.includes('base64')) || (cannotFindImage == false && (oldImage != null || newImage != null))" :src="(newImage != null) ? newImage : oldImage" class="card-image" :class="(this.editingShop==true) ? 'changing-image' : ''" :alt="title" @error="cannotFindImage = true;" @load="onImgLoad">
          
              <div v-else class="no-image-div">
                <p v-if="editingShop == false" class="uk-overlay uk-position-center overlay" style="color: white;">Nicio imagine gasita</p>
              </div>
            
              <div v-if="this.editingShop == true" class="uk-position-center change-image-overlay">
                <span uk-icon="camera" ratio="2"></span>
                <p>Schimba imaginea</p>
              </div>
            </div>
          </div> 
        </div>
        <div class="card-body-custom-width" style="padding-left: 0!important; text-align: left;">
          <vk-grid style="height: 100%; padding-left: 0!important;">
            <div class="uk-width-2-3@s uk-width-3-5@m uk-width-2-3@l uk-width-2-3@xl" style="height: 100%; padding-left: 0px;">
              <div class="uk-flex uk-visible@s">
                <vk-card-title style="margin-bottom: 0px; width: 95%;">
                  <textarea v-if="editingShop == true" :uk-tooltip="(editingShop == true) ? 'Titlu (maxim 40 caractere)' : null" maxlength="40" :id="'TitleInputDesktop'+id" class="uk-input textarea-title" :class="(editingShop == false) ? 'custom-input-disabled' : 'custom-input-enabled'" type="text" placeholder="Titlu" v-model="title" :disabled="(editingShop == true) ? false : true"></textarea>
                  <h3 v-else style="margin: 0; padding-left: 10px; font-weight: 400;">{{title}}</h3>
                </vk-card-title>
              </div>
              <div style="justify-content: center" class="uk-flex uk-hidden@s">
                <vk-card-title style="margin-bottom: 0px; width: 100%;">
                  <textarea v-if="editingShop == true" :uk-tooltip="(editingShop == true) ? 'Titlu (maxim 40 caractere)' : null" maxlength="40" style="text-align: center;" :id="'TitleInputMobile'+id" class="uk-input textarea-title" :class="(editingShop == false) ? 'custom-input-disabled' : 'custom-input-enabled'" type="text" placeholder="Titlu" v-model="title" :disabled="(editingShop == true) ? false : true"></textarea>
                  <h3 class="uk-text-center" v-else style="margin: 0; padding-left: 0; font-weight: 400;">{{title}}</h3>
                </vk-card-title>
              </div>

              <div class="uk-text-center uk-hidden@s">
                <textarea uk-tooltip="Descriere (maxim 255 caractere)" maxlength="255" style="text-align: center; padding-left: 0px" class="uk-textarea custom-textarea-enabled text-area-props textarea-desc" v-if="editingShop == true" :id="'DescriptionInputMobile'+id" type="text" placeholder="Descriere" v-model="description"></textarea>
                <p :id="'ph-desc-mobile'+id" class="description-p-props-mobile" style="text-align: center; white-space: pre-line;" v-else>{{description}}</p>
              </div>
              <div class="uk-visible@s">
                <textarea uk-tooltip="Descriere (maxim 255 caractere)" maxlength="255" class="uk-textarea custom-textarea-enabled text-area-props textarea-desc" v-if="editingShop == true" :id="'DescriptionInputDesktop'+id" type="text" placeholder="Descriere" v-model="description"></textarea>
                <p :id="'ph-desc-desktop'+id" class="description-p-props-desktop" style="padding-left: 10px; margin-bottom: 0px; white-space: pre-line;" v-else>{{description}}</p>
              </div>

              <div class="showmore-button-container uk-text-center uk-hidden@s" v-if="editingShop == false">
                <button style="cursor: pointer;" class="custom-showmore-button" v-if="isShowingFullDesc == false" @click="getFullDescription()">Arata toata descrierea</button>
                <button style="cursor: pointer;" class="custom-showmore-button" v-else @click="cutDescription()">Arata mai putin</button>
              </div>
              <div style="padding-left: 10px; margin-top: 26px" class="showmore-button-container uk-visible@s" v-if="editingShop == false">
                <button style="cursor: pointer;" class="custom-showmore-button" v-if="isShowingFullDesc == false" @click="getFullDescription()">Arata toata descrierea</button>
                <button style="cursor: pointer;" class="custom-showmore-button" v-else @click="cutDescription()">Arata mai putin</button>
              </div>

              <div class="uk-hidden@s">
                <div v-if="editingShop == false" class="shop-type-mobile">
                  <div class="uk-text-center">
                    <button uk-tooltip="title: Schimba starea fatadei de magazin; pos: top" style="cursor: pointer; margin-bottom: 10px" @click="changePublished()" :class="isPublished == true ? 'uk-button-primary' : 'uk-button-danger'" class="change-published-button uk-flex uk-flex-middle uk-align-center">
                      <p style="font-family: 'Montserrat'">{{isPublished == true ? 'PUBLICAT' : 'NEPUBLICAT'}}</p>
                    </button>
                    <p style="margin-bottom: 10px; margin-top: 0">Tip: {{getShopSizeName()}}</p>
                  </div>
                  <StarRating v-if="id.includes('not-set') == false" class="rating-mobile" :rating="rating" :show-rating="false" :star-size="20" :increment="0.1" :read-only="true"/>
                </div>
                <div style="margin-top: 20px" v-else>
                  <select uk-tooltip="Schimba marimea fatadei de magazin" style="cursor: pointer; border: rgb(197, 166, 11) 2px solid;" v-model="shopSize" class="uk-select subcategory-custom-select">
                    <option value="FREE">Gratis (1 produs)</option>
                    <option value="SMALL">Mic (5 produse)</option>
                    <option value="MEDIUM">Mediu (15 produse)</option>
                    <option value="LARGE">Mare (40 produse)</option>
                    <option value="UNLIMITED">Extra (100 produse)</option>
                  </select>

                  <button class="uk-button uk-button-primary custom-change-subcategories-button" @click="showChangeSubcategories = true">Modifica categorii</button>
                </div>

                
              </div>

              <!--<div v-if="editingShop == true" style="margin-bottom: 20px" class="uk-hidden@s">
                <div class="uk-text-center" style="margin: 20px 0 10px 0">
                  Subcategorii
                </div>
                <div v-if="id.includes('not-set') == false" class="uk-flex uk-flex-column">
                  <select uk-tooltip="Schimba subcategoria" style="cursor: pointer;" v-for="(subcategory, index) in shopSubcategories" :id="id+index" :key="index" class="uk-select subcategory-custom-select">
                    <option :value="subcategory.name">{{subcategory.name}}</option>
                    <option value="none">Neselectat</option>
                    <option v-for="(subcategoryFromAll, index) in getSubcategoriesToDisplay(subcategory.name)" :key="index" :value="subcategoryFromAll.name">{{subcategoryFromAll.name}}</option>
                  </select>
                  <select uk-tooltip="Schimba subcategoria" style="cursor: pointer;" class="uk-select subcategory-custom-select" v-for="(number, index) in getArrayToCompleteOptionsArray()" :id="id+(index+((shopSubcategories != undefined) ? shopSubcategories.length : 0))" :key="index+((shopSubcategories != undefined) ? shopSubcategories.length : 0)">
                    <option value="none">Neselectat</option>
                    <option v-for="(subcategoryFromAll, index) in subcategories" :key="index" :value="subcategoryFromAll.name">{{subcategoryFromAll.name}}</option>
                  </select>
                </div>
                <div v-else class="uk-flex uk-flex-column">
                  <select uk-tooltip="Schimba subcategoria" style="cursor: pointer;" v-for="(number, index) in [0, 1, 2]" :id="id+index" :key="index" class="uk-select subcategory-custom-select">
                    <option value="none">Neselectat</option>
                    <option v-for="(subcategoryFromAll, index) in subcategories" :key="index" :value="subcategoryFromAll.name">{{subcategoryFromAll.name}}</option>
                  </select>
                </div>
              </div>-->

              <div class="edit-buttons-container uk-hidden@s" style="margin-bottom: 15px">
                <div v-if="editingShop == false">
                  <button uk-tooltip="Reincarca fatada de magazin" style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" @click="$emit('refresh_shop', id)" class="edit-button-shop-card uk-button-primary"><span uk-icon="icon: refresh; ratio: 0.8" class="edit-button-icon"></span></button>
                  <button uk-tooltip="Promoveaza fatada de magazin" style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" @click="$emit('promote_shop', id)" class="edit-button-shop-card uk-button-primary"><span uk-icon="icon: bolt; ratio: 1" class="edit-button-icon"></span></button>
                  <button uk-tooltip="Editeaza fatada de magazin" style="cursor: pointer; margin-right: 10px;" class="edit-button-shop-card uk-button-primary" v-on:click="editShop()"><span uk-icon="icon: pencil; ratio: 0.8" class="edit-button-icon"></span></button>
                </div>
                <div v-else>
									<button uk-tooltip="Sterge fatada de magazin" style="cursor: pointer;" @click="$emit('delete_shop_db', id)" v-if="id.includes('not-set') == false" class="custom-action-button uk-button-danger"><span style="margin: 3px" uk-icon="icon: trash; ratio: 0.8"></span></button>
                  <button uk-tooltip="Salveaza modificarile" style="cursor: pointer;" v-on:click="saveChanges()" class="custom-action-button uk-button-primary"><span style="margin: 3px" uk-icon="icon: check; ratio: 0.8"></span></button>
									<button uk-tooltip="Anuleaza modificarile" style="cursor: pointer; margin-right: 10px" v-on:click="discardChanges()" class="custom-action-button uk-button-danger"><span style="margin: 3px" uk-icon="icon: ban; ratio: 0.8"></span></button>
                </div>
              </div>

              <router-link :to="{ name: 'Storefront', params: { id: id }, query: { owner: true }}"><vk-button v-if="id.includes('not-set') == false && editingShop == false" class="uk-hidden@s custom-button" type="primary">VEZI MAGAZIN</vk-button></router-link>
            </div>

            <!--DESKTOP-->
            <div class="uk-width-1-3@s uk-width-2-5@m uk-width-1-3@l uk-width-1-5@xl uk-visible@s" style="padding: 0">
              <div class="edit-buttons-container">
                <div v-if="editingShop == false">
                  <button uk-tooltip="Reincarca fatada de magazin" style="cursor: pointer; margin-left: 0px; background: linear-gradient(45deg, #d6e218, #777400);" @click="$emit('refresh_shop', id)" class="edit-button-shop-card uk-button-primary"><span uk-icon="icon: refresh; ratio: 0.8" class="edit-button-icon"></span></button>
                  <button uk-tooltip="Promoveaza fatada de magazin" style="cursor: pointer; background: linear-gradient(45deg, #d6e218, #777400);" @click="$emit('promote_shop', id)" class="edit-button-shop-card uk-button-primary"><span uk-icon="icon: bolt; ratio: 1" class="edit-button-icon"></span></button>
                  <button uk-tooltip="Editeaza fatada de magazin" style="cursor: pointer;" class="edit-button-shop-card uk-button-primary" v-on:click="editShop()"><span uk-icon="icon: pencil; ratio: 0.8" class="edit-button-icon"></span></button>
                </div>
                <div v-else>
                  <button uk-tooltip="Sterge fatada de magazin" style="cursor: pointer; margin-left: 0px" @click="$emit('delete_shop_db', id)" v-if="id.includes('not-set') == false" class="custom-action-button uk-button-danger"><span style="margin: 3px" uk-icon="icon: trash; ratio: 0.8"></span></button>
                  <button uk-tooltip="Salveaza modificarile" style="cursor: pointer;"  v-on:click="saveChanges()" class="custom-action-button uk-button-primary"><span style="margin: 3px" uk-icon="icon: check; ratio: 0.8"></span></button>
                  <button uk-tooltip="Anuleaza modificarile" style="cursor: pointer;"  v-on:click="discardChanges()" class="custom-action-button uk-button-danger"><span style="margin: 3px" uk-icon="icon: ban; ratio: 0.8"></span></button>
                </div>
              </div>

              <div v-if="editingShop == true">
                <select uk-tooltip="title: Schimba marimea fatadei de magazin; pos: left" style="cursor: pointer; border: rgb(197, 166, 11) 2px solid;" v-model="shopSize" class="uk-select subcategory-custom-select shop-type-select">
                  <option value="FREE">Gratis (1 produs)</option>
                  <option value="SMALL">Mic (5 produse)</option>
                  <option value="MEDIUM">Mediu (15 produse)</option>
                  <option value="LARGE">Mare (40 produse)</option>
                  <option value="UNLIMITED">Extra (100 produse)</option>
                </select>
                <button class="uk-button uk-button-primary custom-change-subcategories-button uk-flex uk-flex-middle uk-align-center" @click="showChangeSubcategories = true"><span style="margin: 3px" uk-icon="icon: settings; ratio: 1"></span><p style="margin: 0">Categorii</p></button>
              </div>

              <div v-if="editingShop == false">
                <div class="shop-type">
                  <button uk-tooltip="title: Schimba starea fatadei de magazin; pos: left" style="cursor: pointer;"  @click="changePublished()" :class="isPublished == true ? 'uk-button-primary' : 'uk-button-danger'" class="change-published-button uk-flex uk-flex-middle uk-align-center">
                    <p style="font-family: 'Montserrat'">{{isPublished == true ? 'PUBLICAT' : 'NEPUBLICAT'}}</p>
                  </button>
                  <p>Tip: {{getShopSizeName()}}</p>
                </div>
                <StarRating v-if="id.includes('not-set') == false" class="custom-star" :rating="rating" :show-rating="false" :star-size="20" :increment="0.1" :read-only="true"/>
                <router-link :to="{ name: 'Storefront', params: { id: id }, query: { owner: true }}"><vk-button v-if="id.includes('not-set') == false && editingShop == false" class="uk-visible@s custom-button" type="primary">VEZI MAGAZIN</vk-button></router-link>
              </div>

              <!--<div v-else>
                <div class="subcategories-title uk-width-1-5 uk-text-center">
                  Subcategorii
                </div>
                <div v-if="id.includes('not-set') == false" class="uk-flex uk-flex-column uk-width-1-5 subcategories-panel">
                  <select uk-tooltip="Schimba subcategoria" style="cursor: pointer;" v-for="(subcategory, index) in shopSubcategories" v-model="subcategoriesToPost[index]" :id="id+index" :key="index" class="uk-select subcategory-custom-select">
                    <option :value="subcategory.name">{{subcategory.name}}</option>
                    <option value="none">Neselectat</option>
                    <option v-for="(subcategoryFromAll, index) in getSubcategoriesToDisplay(subcategory.name)" :key="index" :value="subcategoryFromAll.name">{{subcategoryFromAll.name}}</option>
                  </select>
                  <select uk-tooltip="Schimba subcategoria" style="cursor: pointer;" class="uk-select subcategory-custom-select" v-for="(number, index) in getArrayToCompleteOptionsArray()" v-model="subcategoriesToPost[index+((shopSubcategories != undefined) ? shopSubcategories.length : 0)]" :id="id+(index+((shopSubcategories != undefined) ? shopSubcategories.length : 0))" :key="index+((shopSubcategories != undefined) ? shopSubcategories.length : 0)">
                    <option value="none">Neselectat</option>
                    <option v-for="(subcategoryFromAll, index) in subcategories" :key="index" :value="subcategoryFromAll.name">{{subcategoryFromAll.name}}</option>
                  </select>
                </div>
                <div v-else class="uk-flex uk-flex-column uk-width-1-5 subcategories-panel">
                  <select uk-tooltip="Schimba subcategoria" style="cursor: pointer;" v-for="(number, index) in [0, 1, 2]" v-model="subcategoriesToPost[index]" :id="id+index" :key="index" class="uk-select subcategory-custom-select">
                    <option value="none">Neselectat</option>
                    <option v-for="(subcategoryFromAll, index) in subcategories" :key="index" :value="subcategoryFromAll.name">{{subcategoryFromAll.name}}</option>
                  </select>
                </div>
              </div>-->
            </div>
          </vk-grid>
        </div>

        <PromotedInfoDialog
          :id="id"
          :isPromotedInHome="isPromotedInHome"
          :isPromotedInSearches="isPromotedInSearches"
          :promotedDaysInHomeRemaining="promotedDaysInHomeRemaining"
          :promotedDaysInSearchesRemaining="promotedDaysInSearchesRemaining"

          @promote_shop="$emit('promote_shop', id)"
        />

        <vk-modal-full :stuck="true" :show.sync="showImageEdit">
          <vk-modal-full-close></vk-modal-full-close>
          <vk-modal-title>Poza fatada de magazin</vk-modal-title>
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
            :from="'shopcard'"
          />
        </vk-modal-full>

        <vk-modal :stuck="true" overflow-auto center :show.sync="showChangeSubcategories">
          <vk-modal-title slot="header" style="white-space: pre-line;">Schimba subcategoriile magazinului {{'\n' + title}}</vk-modal-title>
          
          <div style="margin-bottom: 20px;">
            <h3>Categorie</h3>
            <select uk-tooltip="Schimba categoria" style="cursor: pointer;" v-model="shopCategory" class="uk-select subcategory-custom-select">
              <option :value="{id: 'none', name: 'none'}">Neselectat</option>
              <option v-for="(categoryFromAll, index) in categories" :key="index" :value="categoryFromAll">{{categoryFromAll.name}}</option>
            </select>
          </div>

          <div>
            <h3>Subcategorie</h3>
            <select uk-tooltip="Schimba subcategoria" style="cursor: pointer;" v-for="(select, index) in getArrayToCompleteOptionsArray()" v-model="subcategoriesToPost[index]" :key="select.id || index" class="uk-select subcategory-custom-select">
              <option value="none">Neselectat</option>
              <option v-for="(subcategoryFromAll, optionIndex) in findSubcategoriesByCategoryId(index)" :key="optionIndex" :value="subcategoryFromAll.name">{{subcategoryFromAll.name}}</option>
            </select>
          </div>

          <div slot="footer" class="uk-text-right">
            <vk-button :class="windowWidth <= 640 ? 'uk-button-small' : ''" style="margin-right: 2px;" @click="setShopCategoriesModelArray">Reseteaza</vk-button>
            <vk-button :class="windowWidth <= 640 ? 'uk-button-small' : ''" @click="showChangeSubcategories = false" type="primary">OK</vk-button>
          </div>
        </vk-modal>
        
      </vk-grid>

      <div v-else>Se incarca</div>
    </vk-card>
    
  </div>
</template>

<script>
const ContactData = () => import (/* webpackChunkName: "profile-chunk" */ "./ContactData");
const PromotedInfoDialog = () => import (/* webpackChunkName: "profile-chunk" */ "./PromotedInfoDialog");
import CropperDialogComponent from '@/components/CropperDialogComponent.vue'
import StarRating from 'vue-star-rating'
import axios from 'axios'

export default {
  name: "ShopCard",
  components: {
      ContactData,
      StarRating,
      CropperDialogComponent,
      PromotedInfoDialog,
  },
  props: {
      shopId: null,
      oldTitle: null,
      oldDescription: null,
      rating: null,
      oldImage: null,
      isPromotedInSearches: false,
      isPromotedInHome: false,
      subcategories: null,
      categories: null,
      shopSubcategoriesProp: null,
      oldShopCategories: {
        type: Object,
        default: function() {
          return {
            id: 'none',
            name: 'none'
          }
        }
      },
      oldShopSize: null,
      oldIsPublished: null,
      promotedDaysInHomeRemaining: null,
      promotedDaysInSearchesRemaining: null,
  },
  data () {
      return {
        id: this.shopId,

        showContact: false,
        editingShop: false,

        title: this.oldTitle,
        description: this.oldDescription,

        shopSubcategories: this.shopSubcategoriesProp,

        loading: false,
        subcategoriesToPost: [],
        shopCategory: this.oldShopCategories,

        displayedDescription: null,

        isShowingFullDesc: false,

        backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080',

        shopSize: this.oldShopSize,
        isPublished: this.oldIsPublished,

        showImageEdit: false,
        showChangeSubcategories: false,
        newImage: null,
        cannotFindImage: false,
        hasImageLoaded: false
      }
  },
  methods: {
    shouldShowSubcategory(subcategory, index) {
      if(this.subcategoriesToPost[index] == subcategory.name)
        return true;

      return subcategory.categoryId == this.shopCategory.id && !this.subcategoriesToPost.includes(subcategory.name)
    },
    findSubcategoriesByCategoryId(index) {
			if(this.subcategories == null || this.shopCategory.id == null)
				return

			return this.subcategories.filter(subcategory => this.shouldShowSubcategory(subcategory, index)).sort((s1, s2) => {
        return s1.name > s2.name ? 1 : s1.name < s2.name ? -1 : 0;
      });

			/*this.subcategories.forEach(subcategory => {
				if(subcategory.categoryId == this.shopCategory.id)
					subcategoriesToReturn.push(subcategory)
			});

			return subcategoriesToReturn*/
		},
    openPromotedInfoPanel() {
      UIkit.modal("#promote-info-sections").show();
    },
    onImgLoad() {
      this.hasImageLoaded = true;
    },
    hideModal() {
      this.showImageEdit = false
    },
    changeImage(data) {
      this.cannotFindImage = false
      this.newImage = data.newImage.image 
      this.hideModal();
    },
    getShopSizeName() {
      if(this.shopSize == 'FREE')
        return 'GRATIS'
      if(this.shopSize == 'SMALL')
        return 'MIC'
      if(this.shopSize == 'MEDIUM')
        return 'MEDIU'
      if(this.shopSize == 'LARGE')
        return 'MARE'
      if(this.shopSize == 'UNLIMITED')
        return 'NELIMITAT'

      return 'NEGASIT'
    },
    getSubcategoriesToDisplay(subcategoryToPass) {
      return this.subcategories.filter((subcategory) => {
        return subcategory.name != subcategoryToPass ? true : false;
      })
    },
    getArrayToCompleteOptionsArray() {
      let array = this.shopSubcategories?.slice() || []
      let length = array.length;

      for(let i = 0; i<3-length; i++)
        array.push(i);
      return array
    },
    setShopCategoriesModelArray() {
      this.shopSubcategories = this.shopSubcategoriesProp?.slice() || []
      Object.assign(this.shopCategory, this.oldShopCategories)
      let length = 0;

      if(this.shopSubcategories != undefined)
        length = this.shopSubcategories.length;

      if(this.shopSubcategories == undefined || this.shopSubcategories == null) {
        this.shopSubcategories = []
      }

      for(let i=0; i<3; i++) {
        if(this.shopSubcategories[i] != undefined) {
          this.$set(this.subcategoriesToPost, i, this.shopSubcategories[i].name)
        } else {
          this.subcategoriesToPost[i] = 'none'
        }
      }
    },
    async saveChanges() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

      var subcategories = new Set(this.subcategoriesToPost)

      subcategories.forEach((subcategory) => {
        if(subcategory == 'none') {
          subcategories.delete(subcategory);
        }
      });

      await axios({
        url: this.backend+'/api/shops/saveShop?id='+this.id,
        method: 'post',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get("CSRF-TOKEN"),
          'X-REFRESH-TOKEN': this.$cookie.get("REFRESH-TOKEN")
        },
        data: {
          base64SmallImage: this.newImage,
          subcategories: Array.from(subcategories),
          category: this.shopCategory,
          name: this.title,
          description: this.description,
          shopSize: this.shopSize
        },
        withCredentials: true
      })
        .then((response) => {
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

          this.id = response.data.shopId;
          let imageUrl = response.data.smallPhotoUrl;

          this.shopSubcategories = [];
          
          subcategories.forEach(element => {
            this.shopSubcategories.push({
              "name": element
            })
          });

          let shopToSave = {
            oldId: this.shopId,
            id: this.id,
            title: this.title,
            description: this.description,
            image: imageUrl,
            subcategories: this.shopSubcategories,
            shopSize: this.shopSize,
            category: this.shopCategory,
            published: true
          }

          this.$emit('new_shop_added', shopToSave)

          this.editingShop = false;

          UIkit.notification({message: "Nu uita sa adaugi detaliile de contact ale fatadei de magazin pentru a putea fi usor de gasit si de contactat", status: "warning"})
        })
        .catch((error) => {
          if(error.response.status == 413) {
            UIkit.notification({message: 'Imaginea adaugata are o marime prea mare', status: 'danger'})
            return 
          }

          if(error.response.status == 400) {
            UIkit.notification({message: 'Nu puteti sa schimbati marimea fatadei de magazin deoarece acesta are prea multe produse', status: 'danger'})
            return 
          }

          if(error.response.status == 409) {
            UIkit.notification({message: 'Nu puteti sa adaugati fatadei de magazin deoarece ati atins numarul maxim de fatade de magazin. Daca doriti sa mai adaugati magazin va rugam sa ne scrieti un email!', status: 'danger'})
            return 
          }

          if(error.response.status == 401) {
            UIkit.notification({message: 'Nu dispuneti de creditul necesar tipului de fatada de magazin selectat', status: 'danger'})
            return 
          }

          if(error.response.status == 406) {
            UIkit.notification({message: 'Nu ati adaugat nicio poza fatadei de magazin', status: 'danger'})
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
            
          UIkit.notification({message: 'Nu am putut efectua modificarile', status: 'danger'})
          
        })
        .finally(() => {
          this.loading = false;
          clearTimeout(timeoutVar)
        }) 
    },
    showEditImageModal() {
      if(this.editingShop == true) this.showImageEdit = true;
    },
    discardChanges() {
      if(this.id.includes('not-set')) {
        this.$emit("discard_new_shop")
        return;
      }

      this.title = this.oldTitle
      this.description = this.oldDescription
      this.shopSize = this.oldShopSize
      this.setShopCategoriesModelArray();
      this.newImage = null;

      //this.resetDescription();

      this.editShop()
    },
    editShop() {
      this.editingShop = !this.editingShop;
    },
    resetDescription() {
      this.displayedDescription = this.description;

      let charNo = window.innerWidth / 9

      if(this.displayedDescription.length > charNo)
        this.displayedDescription = this.displayedDescription.substring(0, charNo-4) + '...'
    },
    checkIfTextOverflows(screen) {
      let element = document.getElementById('ph-desc-'+screen+this.id)
      return element.scrollHeight > element.clientHeight || element.scrollWidth > element.clientWidth;
    },
    cutDescription() {
      let elementDesktop = document.getElementById('ph-desc-desktop'+this.id)
      let elementMobile = document.getElementById('ph-desc-mobile'+this.id)
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
      let elementDesktop = document.getElementById('ph-desc-desktop'+this.id)
      let elementMobile = document.getElementById('ph-desc-mobile'+this.id)
      elementDesktop.classList.remove("description-p-props-desktop")
      elementDesktop.classList.add("description-p-props-desktop-showing-full-desc")
      elementMobile.classList.remove("description-p-props-mobile")

      elementDesktop.style.marginTop = '10px'
      elementMobile.style.marginTop = '10px'
      elementDesktop.style.wordBreak = 'break-word'
      elementMobile.style.wordBreak = 'break-word'

      this.isShowingFullDesc = true;
    },
    async changePublished() {
      var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);

      await axios({
        url: this.backend+'/api/shops/changePublished?id='+this.id+'&sure=false',
        method: 'post',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get("CSRF-TOKEN"),
          'X-REFRESH-TOKEN': this.$cookie.get("REFRESH-TOKEN")
        },
        withCredentials: true
      })
        .then((response) => {
          this.isPublished = response.data.published
          this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
          this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

          let published = {
            id: this.id,
            isPublished: this.isPublished
          }

          this.$emit('change_published', published)
        })
        .catch((error) => {
          if(error.response.status == 409) {
            this.$emit('cannot_change_publish', this.id)
            return
          }

          UIkit.notification({message: 'Nu am putut modifica starea fatadei de magazin. Va rugam sa asteptati si/sau sa reincercati', status: 'danger'})
        })
        .finally(() => {
          this.loading = false
          clearTimeout(timeoutVar)
        })
    }
     
  },
  mounted() {
    this.shopSize = this.oldShopSize || 'NEGASIT';
    this.isPublished = this.oldIsPublished || 'NEGASIT';

    if(this.id.includes('not-set'))
      this.shopSize = 'FREE'

    if(this.id.includes('not-set')) {
      this.editShop()
    }
    this.setShopCategoriesModelArray();

    this.categories.sort((s1, s2) => {
        return s1.name > s2.name ? 1 : s1.name < s2.name ? -1 : 0;
    });
  },
  watch: {
    $props: {
      handler() {
        this.isPublished = this.oldIsPublished
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
    },
    'shopCategory': {
      handler() {
        if(this.shopCategory.id == this.oldShopCategories.id)
          return;

        this.subcategoriesToPost.fill('none');
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
}

.textarea-desc {
  min-height: 105px;
}

.shop-type {
  p {
    margin-top: 5px;
    margin-bottom: 5px;
    text-align: right;
  }

  button {
    margin: 0;
    margin-bottom: 5px;
  }

  position: absolute;
  right: 20px;
  top: 55px;
}

.change-published-button {
  border: none;
  outline: 0;
	margin-top: 10px;
	border-radius: 7.5px;
	padding: 5px 10px!important;
  height: 23px;

  p {
    margin: 0
  }
}

.card {
  margin-bottom: 10px;
}

.subcategories-panel {
  height: 70px;
  position: absolute; 
  top: 105px; 
  right: 20px; 
  overflow: auto
}

.shop-type-select {
  height: 30px!important;
  border-radius: 10px;
  position: absolute;
  top: 50px;
  right: 20px;
  width: 125px;
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

.edit-button-icon {
  margin: 3px;
}

.edit-button-shop-card {
  outline: 0;
  border: none;
	margin: 0 0 0 10px;
	border-radius: 10px;
}

.custom-change-subcategories-button {
  outline: 0;
  border: none;
  border-radius: 10px;
  margin-top: 10px;
  position: absolute; 
  top: 80px; 
  right: 20px;
  max-width: 125px;
  text-transform: capitalize;
  padding: 0 20px;
}

.promote-info-button {
  outline: 0;
  border: none;
  right: 10px; 
  top: 10px;
  span {
    padding: 5px;
  }
}

.changing-image {
  filter: brightness(30%);
  
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

.subcategory-custom-select {
  margin-top: 3px;
}

.shop-type-mobile {
  margin-top: 20px;
  .rating-mobile {
    justify-content: center;
  }
}

.custom-action-button {
  border: 0;
  outline: 0;
	margin: 0 0 0 10px;
	border-radius: 10px;
}

.text-area-props{
  word-break: break-word; 
  margin-top: 10px; 
  height: 105px;
}

.no-image-div {
  background-color: rgb(136, 136, 136);
  height: 155px;
  width: 100%;
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
  max-height: 75px;
  overflow: hidden;
}

.custom-showmore-button {
  border: none;
  outline: 0;
  background-color: transparent;
  color: #6FC42B;
  padding: 0;
  font-family: 'Montserrat'; 
}

.card-image-custom-width {
  padding-left: 0!important;
  height: 155px;
  .uk-card {
    padding: 0;
    height: 100%;
    .uk-inline {
      height: 100%;
    }
  } 
}

.card-image {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.subcategories-title {
  position: absolute; 
  top: 85px; 
  right: 20px;
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
  .subcategories-panel {
    height: 150px;
  }
  .showmore-button-container {
    margin-top: 43.2px!important
  }
  .card-image-custom-width {
    height: 300px;
    width: 410px!important;
  }

  .card-body-custom-width {
    width: calc(100% - 410px)!important;
  }
  .no-image-div {
    height: 300px;
  }
  .textarea-desc {
    min-height: 185px;
  }
}

@media (max-width: 660px) {
  .card-image-custom-width {
    .uk-card {
      margin-right: 5px
    } 
  }
  .shop-type-select, .custom-change-subcategories-button {
    width: 110px;
  }
  .subcategories-panel {
    width: 110px;
  }
  .subcategories-title {
    right: 15px;
  }
  .custom-action-button {
    margin-left: 5px;
  }
}

@media (min-width: 640px) {
  .custom-button {
    position: absolute;
    bottom: 20px;
    right: 20px;
  }
  .custom-star {
    position: absolute;
    top: 110px;
    right: 20px;
  }
  .edit-buttons-container{
    text-align: right;
    position: absolute;
    right: 20px;
    top: 20px;
  }
  .showmore-button-container {
    margin-top: 18px
  }
  .card-image-custom-width {
    width: 216.6px;
    .uk-card {
      margin-right: 10px
    } 
  }

  .card-body-custom-width {
    width: calc(100% - 216.6px);
  }
}

@media (width: 640px) {
  .card-image-custom-width {
    width: 211.6px;
  }

  .card-body-custom-width {
    width: calc(100% - 211.6px);
  }
}

@media (max-width: 639px) {
  .custom-button {
    width: 100%;
  }
  .edit-buttons-container{
    text-align: center;
    margin: 20px 0px;
  }
  .card-image-custom-width {
    width: 100%;
    height: 100%;
  }

  .card-body-custom-width {
    width: 100%;
  }

  .custom-change-subcategories-button {
    position: initial;
    max-width: revert;
    width: 50%;
    margin: 10px 25%;
  }
}

@media (max-width: 450px) {
  .custom-change-subcategories-button {
    width: 100%;
    margin: 10px 0;
  }
}
</style>