<template>
  <div :class="windowWidth > 960 ? 'uk-padding-large' : ''">
    <div v-if="loading == true">
      <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
        Se incarca
      </div>
    </div>
    <div v-else>
      <div v-if="error==true">
        <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
          Nu am putut gasi fatada de magazin. Va rugam sa reincercati
        </div>
      </div>
      <div v-else>
        <TutorialLayout :isShowingDialog="isShowingDialog" />

        <div v-if="windowWidth < 640 || store.largePhoto != null || isOwner" class="uk-inline" style="width: 100%; margin-bottom: 15px;">
          <img loading="lazy" v-if="(newImage != null && newImage.includes('base64')) || (cannotFindImage == false && ((windowWidth >= 640 && store.largePhoto != null) || (windowWidth < 640 && store.smallPhoto != null)))" :src="((windowWidth >= 640 && newImage == null) || (windowWidth < 640 && newSmallImage == null)) ? ((windowWidth >= 640) ? store.largePhoto : store.smallPhoto) : ((windowWidth >= 640) ? newImage : newSmallImage)" class="cover-image" :class="(editingShop == true) ? 'filter-image' : ''" :alt="store.name" @error="cannotFindImage = true">
          
          <div v-else-if="isOwner" class="cover-image no-image-div">
            <p :style="cannotFindImage == true || ((windowWidth >= 640) ? store.largePhoto == null : store.smallPhoto == null) ? 'visibility: visible' : 'visibility: hidden'" v-if="editingShop == false" class="uk-overlay uk-position-center overlay-storefront" style="color: white;">Nicio imagine mare gasita</p>
          </div>
          
          <div class="uk-overlay uk-position-bottom-left overlay-storefront uk-flex uk-flex-column">
            <vk-label v-if="editingShop == true" class="uk-hidden@s" style="background: #E82901!important; margin-bottom: 5px;">In editare</vk-label>
            <vk-label v-if="store.promotedInHome || store.promotedInSearches">PROMOVAT</vk-label>
          </div>

          <div v-if="editingShop == false" class="uk-overlay uk-position-top-right overlay-storefront">
            <button style="cursor: pointer;" v-on:click="copyShareLink()" class="copy-link-button uk-button-primary uk-flex uk-flex-middle uk-align-center">
              <span style="margin-right: 5px" uk-icon="icon: social; ratio: 0.8"></span>
              <p style="margin: 0">Link distribuire</p>
            </button>
          </div>
          <div v-if="isOwner == true" class="uk-overlay uk-position-bottom-right overlay-storefront">
            <button uk-tooltip="Editeaza fatada de magazinul" style="cursor: pointer;" v-if="editingShop == false" @click="changeEditState()" :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 19 ? 'in-focus' : ''" class="edit-button uk-button-primary"><span uk-icon="icon: pencil; ratio: 0.8"></span></button>
            <div v-else class="edit-shop-button-container">
              <button uk-tooltip="Renunta la editarea fatadei de magazin" style="cursor: pointer;" @click="discardEdit()" class="edit-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
              <button uk-tooltip="Finalizeaza editarea fatadei de magazin" style="cursor: pointer;" @click="saveEdit()" class="edit-button uk-button-primary"><span uk-icon="icon: check; ratio: 0.8"></span></button>
            </div>
          </div>
          <div v-if="isOwner == true && editingShop == true" :style="(this.editingShop==true) ? 'cursor: pointer;' : ''" style="color: white;" class="uk-overlay uk-position-center overlay-storefront change-image-div">
            <div @click="windowWidth >= 640 ? showImageEdit = true : showSmallImageEdit = true">
              <span uk-icon="camera" ratio="2"></span>
              <p>Schimba imaginea {{windowWidth >= 640 ? 'mare' : 'mica'}}</p>
            </div>
            <button v-if="windowWidth >= 640 && (store.largePhoto != null || newImage != null)" style="cursor: pointer;" v-on:click="deleteLargeImage()" class="copy-link-button uk-button-primary uk-flex uk-flex-middle uk-align-center">
              <span style="margin-right: 5px" uk-icon="icon: trash; ratio: 0.8"></span>
              <p style="margin: 0">Sterge imaginea mare</p>
            </button>
          </div>

          <!--DESKTOP-->
          <div v-if="editingShop == true" class="uk-overlay uk-position-top-center overlay-storefront uk-visible@s">
            <vk-label style="background: #E82901!important">In editare</vk-label>
          </div>

          <div v-if="isOwner == true && editingShop == true" class="uk-visible@s change-contact-button-div">
            <div class="uk-overlay uk-position-top-left overlay-storefront">
              <button @click="changeTutorialStep(true)" :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 20 ? 'in-focus' : ''" style="cursor: pointer; padding: 5px" class="change-contact-button uk-button-primary uk-flex uk-flex-middle uk-align-center" href="#change-contact-sections" uk-toggle>
                <span style="margin-right: 5px" uk-icon="icon: refresh; ratio: 0.8"></span>
                <p style="margin: 0">Schimba detalii contact</p>
              </button>
            </div>
            <div class="uk-overlay uk-position-top-right overlay-storefront uk-flex uk-flex-row uk-flex-middle">
              <p style="margin: 0; margin-right: 10px">Reinnoire automata</p>
              <label style="cursor: pointer;" class="switch">
                <input type="checkbox" @click="wantsAutomaticTokenRefreshChanged()" v-model="store.hasAutomaticTokenRefresh">
                <span class="slider round"></span>
              </label>
            </div>
          </div>

          <!--MOBILE-->
          <div v-if="isOwner == true && editingShop == true" class="uk-overlay uk-position-top-left overlay-storefront change-contact-button-div uk-hidden@s">
            <button @click="changeTutorialStep(true)" :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 20 ? 'in-focus' : ''" class="change-contact-button uk-button-primary uk-flex uk-flex-middle" href="#change-contact-sections" uk-toggle style="padding: 5px">
              <span style="margin-right: 5px" uk-icon="icon: refresh; ratio: 0.8"></span>
              <p style="margin: 0">Schimba detalii contact</p>
            </button>
            <div class="uk-flex uk-flex-row uk-flex-middle">
              <p style="margin: 0; margin-right: 10px">Reinnoire automata</p>
              <label style="cursor: pointer;" class="switch">
                <input type="checkbox" @click="wantsAutomaticTokenRefreshChanged()" v-model="store.hasAutomaticTokenRefresh">
                <span class="slider round"></span>
              </label>
            </div>
          </div>
        </div>

        <vk-grid :style="(store.largePhoto == null || windowWidth < 640) ? '' : 'margin-top: 15px'" matched>
          <div class="shop-info-container uk-width-2-3@xl">
            <article class="uk-article" style="height: 100%;">
              <vk-grid style="height: 100%" matched>
                <div v-if="windowWidth >= 640 && (store.largePhoto == null || isOwner)" :style="(windowWidth >= 640) ? 'padding: 0;' : ''" class="uk-inline" :class="(windowWidth >= 640 && (store.largePhoto == null || isOwner)) ? 'uk-width-1-2' : ''">
                  <img v-if="cannotFindSmallImage == false" loading="lazy" :src="newSmallImage || store.smallPhoto" class="cover-image cover-small-image" :class="(editingShop == true) ? 'filter-image' : ''" :alt="store.name" @error="cannotFindSmallImage = true">
                  
                  <div v-else class="cover-image no-image-div">
                    <p class="uk-overlay uk-position-center overlay-storefront" style="color: white;">Nicio imagine mici gasita</p>
                  </div>
                  
                  <div v-if="store.largePhoto == null && !isOwner" class="uk-overlay uk-position-top-left overlay-storefront">
                    <button style="cursor: pointer;" v-on:click="copyShareLink()" class="copy-link-button uk-button-primary uk-flex uk-flex-middle">
                      <span style="margin-right: 5px" uk-icon="icon: social; ratio: 0.8"></span>
                      <p style="margin: 0">Link distribuire</p>
                    </button>
                  </div>

                  <div @click="showSmallImageEdit = true" v-if="isOwner == true && editingShop == true" :style="(this.editingShop==true) ? 'cursor: pointer;' : ''" style="color: white;" class="uk-overlay uk-position-center overlay-storefront change-image-div">
                    <span uk-icon="camera" ratio="2"></span>
                    <p>Schimba imaginea mica</p>
                  </div>
                </div>
                <div :class="(windowWidth >= 640 && (store.largePhoto == null || isOwner)) ? 'uk-width-1-2' : 'uk-width-2-2 uk-padding-remove'">
                  <div v-if="editingShop == false" class="info-container">
                    <p class="uk-text-lead" style="text-transform: uppercase; text-align: left; word-wrap: break-word;"> {{store.name}} </p>
                    <p style="text-align: left; word-wrap: break-word; white-space: pre-line;"> {{displayedDescription}} </p>
                  </div>
                  
                  <div v-else class="edit-info-container">
                    <input uk-tooltip="Titlu (maxim 40 caractere)" type="text" maxlength="40" v-model="store.name" style="font-size: 24px; text-transform: uppercase;" placeholder="Titlu" class="uk-textarea custom-textarea-enabled" />
                    <textarea uk-tooltip="Descriere (maxim 255 caractere)" maxlength="255" class="uk-textarea custom-textarea-enabled description-text-area" type="text" placeholder="Descriere" v-model="store.description"></textarea>
                  </div>
                  
                  <div v-if="needsCrop == true" class="uk-text-left">
                    <button style="cursor: pointer;" class="custom-showmore-button" v-if="isShowingFullDesc == false" @click="getFullDescription()">Arata toata descrierea</button>
                    <button style="cursor: pointer;" class="custom-showmore-button" v-else @click="cutDescription()">Arata mai putin</button>
                  </div>
                </div>
              </vk-grid>
            </article>
          </div>

          <div class="uk-width-1-3@xl" style="padding: 0">
            <article class="uk-article" style="margin-bottom: 10px;">
              <vk-card padding="small" class="uk-height-1-1 uk-flex comment-card-container">
                <div>
                  <p style="margin-bottom: 0 !important;">Magazinul a fost evaluat ca: <b>{{priceText}}</b></p>
                  <div>
                    <p :class="getDollarClass(1)">$ </p>
                    <p :class="getDollarClass(2)">$ </p>
                    <p :class="getDollarClass(3)">$ </p>
                    <StarRating
                        class="uk-flex uk-flex-center"
                        :rating="store.rating"
                        :star-size="25"
                        :show-rating="false"
                        :increment="0.1"
                        :read-only="true"
                    />
                  </div>
                  <br>
                  <vk-button v-if="editingShop == false" @click="isOwner==true ? showTab(store.tabs.length+2) : showTab(store.tabs.length+1)">VEZI COMENTARIILE</vk-button>
                </div>
              </vk-card>
            </article>
            <Schedule 
              :editingShop="editingShop"
              :schedule="store.schedule"
              :isOnline="store.county == 'Online' ? true : false"
              :address="store.address"
              :email="store.email"
              :county="store.county"
              :websiteLink="store.websiteLink"
              :phone="store.phone"

              @change_schedule="changeSchedule($event)"
            />
          </div>
        </vk-grid>

        <div class="uk-margin-medium-top">
          <vk-tabs align="center" :active-tab.sync="activeTab" @update:activeTab="activeTabChanged">
            <vk-tabs-item title="Toate" :disabled="this.$store.getters.getTutorialStep == 22">
              <ProductCardGrid
                @hide_modal="closeDialog"
                @show_modal="showDialog"
                :isOwner="isOwner"
                :allTabs="store.tabs"
                :tab="'all'"
                :phone="store.phone"
                :email="store.email"
                :address="store.address"
                :websiteLink="store.websiteLink"
                :county="store.county"
                :editingShop="editingShop"
                :foundItemsIds="foundItemsIds"
                :isSearching="isSearchingData"
              />
            </vk-tabs-item>
            <vk-tabs-item v-for="(tab, index) in store.tabs" :key="index+1" :title="tab.name" :disabled="$store.getters.getTutorialStep == 25">
              <ProductCardGrid
                :isOwner="isOwner"
                :allTabs="store.tabs"
                :tab="tab.name"
                :phone="store.phone"
                :email="store.email"
                :address="store.address"
                :websiteLink="store.websiteLink"
                :county="store.county"
                :editingShop="editingShop"
                :foundItemsIds="foundItemsIds"
                :isSearching="isSearchingData"
              />
            </vk-tabs-item>
            <vk-tabs-item v-if="isOwner" title="File" :disabled="this.$store.getters.getTutorialStep == 25">
              <TabsLayout
                v-on:add_new_tab="addNewTab()"
                v-on:delete_tab="deleteTab($event)"
                v-on:new_tab_saved="saveNewTab($event)"
                @show_modal="showDialog"
                :tabs="store.tabs"
                :newTabAdded="newTabAdded"
              /> 
            </vk-tabs-item>
            <vk-tabs-item title="Comentarii" :disabled="this.$store.getters.getTutorialStep == 22 || this.$store.getters.getTutorialStep == 25">
              <CommentCardGrid :isOwner="isOwner" v-on:reload_rating="reloadRating()" :code="this.code"/>
            </vk-tabs-item>
            
          </vk-tabs>
          
        </div>
        <ChangeContactDetailsDialog 
          v-on:contacts_changed="changeContacts($event)" 
          @hide_modal="closeDialog($event)"

          :oldPhone="store.phone" 
          :oldEmail="store.email" 
          :oldWebsiteLink="store.websiteLink" 
          :oldAddress="store.address"
          :oldCounty="store.county"
          />

        <vk-modal-full :stuck="true" :show.sync="showImageEdit">
          <vk-modal-full-close></vk-modal-full-close>
          <vk-modal-title>Poza mare magazin</vk-modal-title>

          <CropperDialogComponent 
            v-on:hide_modal="hideModal()"
            v-on:change_image="changeImage($event)"

            :oldImage="store.photo" 
            :aspectRatio="32/9"
            :minHeight="1000"
            :minWidth="1000*3.55"
            :from="'storefront_large'"
          />
        </vk-modal-full>

        <vk-modal-full :stuck="true" :show.sync="showSmallImageEdit">
          <vk-modal-full-close></vk-modal-full-close>
          <vk-modal-title>Poza mica magazin</vk-modal-title>

          <CropperDialogComponent 
            v-on:hide_modal="hideModal()"
            v-on:change_image="changeImage($event)"

            :oldImage="store.photo" 
            :aspectRatio="4/3"
            :minHeight="1000"
            :minWidth="1000*1.33"
            :from="'storefront_small'"
          />
        </vk-modal-full>
      </div>
    </div>
    
  </div>
</template>

<script>
const StarRating = () => import('vue-star-rating')  
const ProductCardGrid = () => import(/* webpackChunkName: "storefront-chunk" */ "../components/ProductCardGrid");
const CommentCardGrid = () => import(/* webpackChunkName: "storefront-chunk" */ "../components/CommentCardGrid");
const TabsLayout = () => import(/* webpackChunkName: "storefront-chunk" */ "../components/TabsLayout");
const ChangeContactDetailsDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ "@/components/ChangeContactDetailsDialog");
const Schedule = () => import(/* webpackChunkName: "storefront-chunk" */ "@/components/Schedule.vue")
const TutorialLayout = () => import(/* webpackChunkName: "others-chunk" */ '@/components/TutorialLayout.vue');
import CropperDialogComponent from '@/components/CropperDialogComponent.vue'
import axios from 'axios';

export default {
    name: "Storefront",
    components: {
        CommentCardGrid,
        ProductCardGrid,
        StarRating,
        TabsLayout,
        ChangeContactDetailsDialog,
        CropperDialogComponent,
        Schedule,
        TutorialLayout
    },
    props: {
      code: null,
      owner: null,
    },
    data () {
        return {
            store: {
              name: null,
              description: null,
              email: null,
              rating: null,
              price: null,
              websiteLink: null,
              address: null,
              phone: null,
              type: null,
              smallPhoto: null,
              largePhoto: null,
              tabs: null,
              schedule: null,
              hasAutomaticTokenRefresh: null
            },
            scheduleCopy: null,
            nameCopy: null,
            descriptionCopy: null,
            largePhotoCopy: null,
            newImage: null,
            newSmallImage: null,
            priceText: null,
            activeTab: 0,
            loading: false,
            error: false,
            displayedDescription: null,
            isShowingFullDesc: false,
            needsCrop: false,
            isOwner: false,
            editingShop: false,
            newTabAdded: false,
            backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080',
            frontend: process.env.VUE_APP_FRONTEND || 'http://localhost:8081',
            foundItemsIds: null,

            isSearchingData: null,
            
            showImageEdit: false,
            showSmallImageEdit: false,
            cannotFindImage: false,
            cannotFindSmallImage: false,
            isShowingDialog: false,
        }
    },
    metaInfo() {
      return { 
        title: `${(this.store.name != null) ? this.store.name + ' - ProduseSiServicii.ro' : 'ProduseSiServicii.ro'}`,
        meta: [
            { name: 'description', content:  (this.store.description != null) ? this.store.description.substring(0, Math.min(this.store.description.length, 200)) : 'Descriere'},
            { name: 'author', content: 'ProduseSiServicii.ro'},
            { name: 'keywords', content: 'produse, Produse, PRODUSE, servicii, Servicii, SERVICII, produse si servicii, produse sau servicii, produsesiservicii.ro, pfa, srl, instalator, horeca, mancare, reparator, masaj, mecanic'},
            { property: 'og:title', content: this.store.name + " - ProduseSiServicii.ro"},
            { property: 'og:site_name', content: 'ProduseSiServicii.ro'},
            { property: 'og:type', content: 'website'},    
            { name: 'robots', content: 'index,follow'} 
        ]
      }
    },
    methods: {
        deleteLargeImage() {
          if(this.windowWidth < 640) {
            return
          }
          if(this.newImage != null) {
            this.newImage = null
            return;
          }
          this.store.largePhoto = null
        },
        scroll() {
          window.scrollTo({
            top: document.body.scrollHeight,
            behavior: 'smooth',
          })
        },
        activeTabChanged() {
          if(this.$store.getters.getHasCompletedTutorial) {
            return
          }
          if(this.$store.getters.getIsWithinTutorial)
            this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
          this.scroll()
          this.isShowingDialog = false;
        },
        closeDialog(isComingFromChangeContactDialog) {
          if(isComingFromChangeContactDialog && !this.$store.getters.getHasCompletedTutorial) {
            this.scroll()
            this.editingShop = false
            return
          }
          this.isShowingDialog = false
        },
        showDialog() {
          this.isShowingDialog = true
        },
        changeTutorialStep(isShowingDialog) {
          if(this.$store.getters.getIsWithinTutorial)
            this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
          this.isShowingDialog = isShowingDialog
        },
        changeSchedule(newSchedule) {
          this.store.schedule = newSchedule
        },
        fallbackCopyTextToClipboard(text) {
          var textArea = document.createElement("textarea");
          textArea.value = text;
          
          // Avoid scrolling to bottom
          textArea.style.top = "0";
          textArea.style.left = "0";
          textArea.style.position = "fixed";

          document.body.appendChild(textArea);
          textArea.focus();
          textArea.select();

          document.body.removeChild(textArea);
        },
        copyTextToClipboard(text) {     
	        if (!navigator.clipboard) {
            this.fallbackCopyTextToClipboard(text);
       	    throw "Cannot copy link to clipboard";
            return;
          }
          navigator.clipboard.writeText(text);
        },
        copyShareLink() {
          try {
            this.copyTextToClipboard(this.frontend+this.$router.currentRoute.path)
            UIkit.notification({message: 'Link copiat in clipboard', status: 'success'})
          } catch(error) {
            UIkit.notification({message: 'Link-ul nu a putut fi copiat in clipboard', status: 'danger'})
          }
        },
        hideModal() {
          this.showImageEdit = false;
          this.showSmallImageEdit = false;
        },
        changeImage(data) {
          this.cannotFindImage = false
          if(data.from == 'storefront_large')
            this.newImage = data.newImage.image
          else
            this.newSmallImage = data.newImage.image
          this.hideModal();
        },
        async wantsAutomaticTokenRefreshChanged() {
          var timeoutVar = setTimeout(() => { this.loading = true; }, 1500);
          await axios({
            url: this.backend+'/api/shops/changeHasAutomaticTokenRefresh?id='+this.$route.params.id,
            method: 'post',
            withCredentials: true,
            headers: {
              'X-CSRF-TOKEN': this.$cookies.get('CSRF-TOKEN'),
              'X-REFRESH-TOKEN': this.$cookies.get('REFRESH-TOKEN')
            }
          })
            .then((response) => {
              
              this.$cookies.set("CSRF-TOKEN", response.data.csrfToken);
              this.$cookies.set("REFRESH-TOKEN", response.data.refreshToken);
              
              UIkit.notification({message: 'Am modificat actualizarea automata a creditului pentru acest magazin', status: 'success'});
            })
            .catch((error) => {
              if(error.response != null) {
                UIkit.notification({message: 'Nu am putut modifica actualizarea automata a creditului pentru acest magazin. Va rugam sa reincercati', status: 'danger'});
                this.store.hasAutomaticTokenRefresh = !this.store.hasAutomaticTokenRefresh
              }
            })
            .finally(() => {
              this.loading = false;
              clearTimeout(timeoutVar);
            })
        },
        changeContacts(contacts) {
          this.store.phone = contacts.phone
          this.store.websiteLink = contacts.websiteLink
          this.store.email = contacts.email
          this.store.address = contacts.address
          this.store.county = contacts.county
        },
        saveNewCategory(tab) {
          this.store.tabs.unshift({
            id: tab.id,
            name: tab.name
          })
        },
        categoryHasBeenChanged() {
          this.showTab(this.store.tabs.length + 1)
        },
        async addNewTab() {
          if(this.newTabAdded == true) {
            UIkit.notification({message: 'Ati adaugat deja o noua categorie', status: 'danger'})
            return
          }

          if(this.store.tabs == null)
            this.store.tabs = []

          this.store.tabs.unshift({
              name: "Fila noua",
              id: 'not-set'
            }
          )

          this.newTabAdded = true

          this.categoryHasBeenChanged();
        },
        deleteTab(tab) {
          var removeIndex = this.store.tabs.map(function(item) { return item.id == tab.oldId; }).indexOf(true);
          this.store.tabs.splice(removeIndex, 1)
          
          if(tab.oldId == 'not-set')
            this.newTabAdded = false;

          this.categoryHasBeenChanged();
        },
        saveNewTab(tab) {
          this.deleteTab(tab)
          this.saveNewCategory(tab)
          this.categoryHasBeenChanged();
        },
        changeEditState() {
          this.editingShop=!this.editingShop
          this.changeTutorialStep(false)
        },
        getPriceType() {
            if (this.store.price >= 1 && this.store.price < 1.5) {
              this.priceText = "Ieftin"
              return 
            }
            if (this.store.price >= 1.5 && this.store.price < 2.5){
              this.priceText = "Mediu"
              return 
            } 
            if (this.store.price >= 2.5) {
              this.priceText = "Scump"
              return 
            }

            this.priceText = "Neevaluat"
        },
        getDollarClass(x) {
            if(x==1){
              if(this.priceText != "Neevaluat" && this.priceText != null)
                return "dollar-green"
              return "dollar-gray"
            }
            if(x==2){
              if(this.priceText == "Mediu" || this.priceText == "Scump")
                return "dollar-green"
              return "dollar-gray"
            }
            if(x==3){
              if(this.priceText == "Scump")
                return "dollar-green"
              return "dollar-gray"
            }
        },
        showTab (x) {
          this.activeTab = x;
        },
        changeTab(element) {
          var x = window.scrollX, y = window.scrollY;
          element.focus();
          window.scrollTo(x, y);
        },
        async reloadRating() {
          await axios({
            method: 'get',
            url: this.backend+'/api/shops/getRatingAndPrice?id='+this.$route.params.id
          })
            .then((response) => {
              this.store.price = response.data.price;
              this.store.rating = response.data.rating;
              this.getPriceType();
            })
            .catch((error) => {
            })
        },
        allLowercase(tab) {
          return tab.toLowerCase()
        },
        cutDescription() {
          if(this.displayedDescription.length <= 462) {
            this.needsCrop = false
            return
          }

          this.displayedDescription = this.displayedDescription.substring(0, 458) + "..."
          this.isShowingFullDesc = false
        },
        getFullDescription() {
          this.displayedDescription = this.store.description
          this.isShowingFullDesc = true
        },
        async checkIfOwner() {
          var timeoutVar = setTimeout(() => { this.loading = true; }, 1500);
          await axios({
            url: this.backend+'/api/shops/checkIfOwner?id='+this.$route.params.id,
            method: 'post',
            headers: {
              'X-CSRF-TOKEN': this.$cookies.get('CSRF-TOKEN'),
              'X-REFRESH-TOKEN': this.$cookies.get('REFRESH-TOKEN')
            },
            withCredentials: true
          })
            .then((response) => {
              this.isOwner = true;

              this.$cookies.set("CSRF-TOKEN", response.data.csrfToken);
					    this.$cookies.set("REFRESH-TOKEN", response.data.refreshToken);
            })
            .catch((error) => {
              this.isOwner = false;

              if(this.$route.query.owner) {
                let query = Object.assign({}, this.$route.query);
                delete query.owner;
                this.$router.replace({ query });
              }
            })
            .finally(() => {
              clearTimeout(timeoutVar)
            })
        },
        async getShopDetails() {
          var timeoutVar = setTimeout(() => { this.loading = true; }, 1500);
          await axios({
            method: 'get',
            url: this.backend+'/api/shops/getShopDetailsById?id='+this.$route.params.id,
          })
            .then((response) => {
              this.store = response.data;
              this.displayedDescription = this.store.description
              this.nameCopy=this.store.name;
              this.scheduleCopy=this.store.schedule
              this.descriptionCopy=this.store.description;
              this.largePhotoCopy=this.store.largePhoto;

              this.cutDescription();
              this.getPriceType();

              if(this.code!=null) {
                if(this.isOwner==true)
                  this.showTab(this.store.tabs.length+2)
                else
                  this.showTab(this.store.tabs.length+1)

                return
              }
            })
            .catch((error) => {
              this.error = true;
            })
            .finally(() => {
              this.loading = false;
              clearTimeout(timeoutVar)
            })
        },
        discardEdit() {
          this.store.schedule=this.scheduleCopy
          this.store.name=this.nameCopy;
          this.store.description=this.descriptionCopy;
          this.store.largePhoto=this.largePhotoCopy;
          this.newImage=null;
          this.newSmallImage=null;
          this.changeEditState();
        },
        async saveEdit() {
          if(this.store.schedule == this.scheduleCopy &&
             this.store.name ==  this.nameCopy && 
             this.store.description == this.descriptionCopy && 
             this.newImage == null && this.newSmallImage == null && this.store.largePhoto != null) {
               this.discardEdit()
               return;
             }
             

          if(this.windowWidth >= 640)
            await this.saveEditToDb('LARGE');
          else
            await this.saveEditToDb('SMALL');
          this.changeEditState();
        },
        async saveEditToDb(imageType) {
          var timeoutVar = setTimeout(() => { this.loading = true; }, 1500);
          await axios({
            url: `${this.backend}/api/shops/changeStorefrontImage?id=${this.$route.params.id}&image_type=${imageType}`,
            method: 'post',
            headers: {
              'X-CSRF-TOKEN': this.$cookies.get('CSRF-TOKEN'),
              'X-REFRESH-TOKEN': this.$cookies.get('REFRESH-TOKEN')
            },
            data: {
              hasDeletedLargeImage: this.store.largePhoto == null && this.newImage == null,
              newImage: this.newImage,
              newSmallImage: this.newSmallImage,
              name: this.store.name,
              description: this.store.description,
              schedule: this.store.schedule,
            },
            withCredentials: true
          })
            .then((response) => {
              this.$cookies.set("CSRF-TOKEN", response.data.csrfToken);
              this.$cookies.set("REFRESH-TOKEN", response.data.refreshToken);

              this.store.largePhoto = response.data.newImageURL
              this.store.smallPhoto = response.data.newSmallImageURL
              this.nameCopy=this.store.name;
              this.scheduleCopy=this.store.schedule
              this.descriptionCopy=this.store.description;
              this.displayedDescription=this.store.description;
              this.newImage = null;
              this.newSmallImage = null;

              this.cutDescription();

              UIkit.notification({message: 'Modificarile au fost salvate', status: 'success'})
            })  
            .catch(async (error) => await this.saveEditCatch(error))
            .finally(() => {
              this.loading = false;
              clearTimeout(timeoutVar);
            })
        },
        async saveEditCatch(error) {
          if(error.response.status == 400) {
            UIkit.notification({message: 'Modificarile nu au putut fi salvate', status: 'danger'})
            await this.getShopDetails();
            return
          }
          if(error.response.status == 403) {
            this.$store.dispatch('changeLogged', '')
            this.$cookies.remove('REFRESH-TOKEN')
            this.$cookies.remove('CSRF-TOKEN')
            return
          }
          UIkit.notification({message: 'Modificarile nu au putut fi salvate', status: 'danger'})
        },
        warnThatContactsAreNotSet() {
          if(this.isOwner == true) {
            if(this.store.address == null && this.store.websiteLink == null && this.store.email == null && this.store.phone == null && this.$store.getters.getHasCompletedTutorial) {
              UIkit.notification({message: 'Nu uitati sa adaugati cel putin un contact al fatadei de magazin', status: 'danger'})
            }
          }
        },
        isSearching() {
          var url = window.location.href
          if(url.includes("itemsIds")) {
            return true;
          }
          return false
        },
    },
    async created() {
      if(this.$route.query.state !== undefined) {
        this.$router.push({path: "store/"+this.$route.query.state.replace("{storeid=\"", "").slice(0, -2), query: this.$route.query});
      }
      this.isSearchingData = this.isSearching()

      let ids = this.$route.query.itemsIds;
      if(ids != undefined) {
        this.foundItemsIds = ids
      }

      if(this.$route.query.owner == undefined) {
        await this.getShopDetails();
        return;
      }

      if(this.$route.query.owner == 'false' || (this.$store.getters.getLogged == '' && this.$route.query.owner == 'true')) {
        let query = Object.assign({}, this.$route.query);
        delete query.owner;
        this.$router.replace({ query });

        await this.getShopDetails();
        return;
      }

      if(this.$store.getters.getLogged != '')
        await this.checkIfOwner();
      
      await this.getShopDetails();

      this.warnThatContactsAreNotSet();
    },
    watch: {
      'showImageEdit': {
        handler() {
          this.showImageEdit === true ? document.body.style.overflow = 'hidden' : document.body.style.overflow = 'unset';
        },
        deep: true,
        immediate: true,
      },
      windowWidth: function(newV, oldV) {
        if((oldV < 768 && newV >= 768) || (oldV >= 768 && newV < 768)) {
          this.cannotFindImage = false;
          this.newImage = null;
        }
      }
    }
}
</script>

<style lang="scss" scoped>
.no-image-div {
  background-color: rgb(136, 136, 136);
  min-height: 250px
}

.cn {
  display: table-cell;
  width: 500px;
  height: 500px;
  vertical-align: middle;
  text-align: center;
}

.inner {
  display: inline-block;
  width: 200px; height: 200px;
}

.cover-image {
  min-height: 250px !important;
  width: 100%;
  object-fit: cover;
  
}
.header {
  padding-left: 0 !important;
}
.overlay-storefront {
  padding: 10px 10px !important;
}
.dollar-green {
  color: #5A9E23;
  font-size: 35px;
  font-weight: 100;
  display: inline;
  margin-right: 5px;
}
.dollar-gray {
  color: rgba(0, 0, 0, 0.42);
  font-size: 30px;
  font-weight: 100;
  display: inline;
  margin-right: 5px;
}
.rating {
  display: block !important;
}

.custom-textarea-enabled {
	background-color: rgba(112, 196, 43, 0.144);
	color: rgb(31, 31, 31);
	border: none;
	border-bottom: 2px solid #6FC42B;
	text-align: left;
  white-space: pre-line;
}

.custom-showmore-button {
  border: none;
  background-color: transparent;
  color: #6FC42B;
  padding: 0
}
.edit-button {
  outline: 0;
  border: none;
  border-radius: 10px;
  width: 45px;
  height: 30px;
}
.filter-image {
  filter: brightness(30%);
}
.change-image-div {
  p {
    margin-top: 10px; 
    margin-bottom: 0; 
    color: white;
  }
  span {
    color: white;
  }
}
.change-contact-button {
  margin-bottom: 10px;
	border-radius: 10px;
	padding: 5px 10px!important;
  outline: 0;
  border: none;
}
.change-contact-button-div {
  color: white;
}

.copy-link-button {
  outline: 0;
  border: none;
	border-radius: 10px;
	padding: 5px 10px!important;
}

.edit-info-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.description-text-area {
  min-width: 100%; 
  height: 100%; 
  margin-top: 10px;
}

.edit-shop-button-container {
  display: flex;
  flex-direction: row;

  button {
    margin: 0 5px;
  }
}

.shop-info-container {
  padding-left: 0px;
}

.comment-card-container {
  place-content: center;
  align-items: center;
}

/*Switch*/
.switch {
  position: relative;
  display: inline-block;
  width: 40px;
  height: 25px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 16px;
  width: 16px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #6FC42B;
}

input:focus + .slider {
  box-shadow: 0 0 1px #6FC42B;
}

input:checked + .slider:before {
  -webkit-transform: translateX(16px);
  -ms-transform: translateX(16px);
  transform: translateX(16px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

/* --------- */

@media (min-width: 3000px) {
  .cover-image {
    max-height: 1000px !important;
    width: 100%;
    object-fit: cover;
  }
}

@media (max-width: 960px) {
  .edit-info-container {
    margin: 0 0 0 10px;
    padding-right: 0;
  }
  .info-container {
    margin: 0 10px;
  }
}

@media (max-width: 639px) {
  .edit-info-container {
    margin: 0 10px;
    padding-right: 0;
  }
}
</style>
