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
        <div class="uk-inline" style="width: 100%">
          <img loading="lazy" v-if="cannotFindImage == false && ((windowWidth >= 768 && store.largePhoto != null) || (windowWidth < 768 && store.smallPhoto != null))" :src="(newImage == null) ? ((windowWidth >= 768) ? store.largePhoto : store.smallPhoto) : newImage" class="cover-image" :class="(editingShop == true) ? 'filter-image' : ''" :alt="store.name" @error="cannotFindImage = true">
          
          <div v-else class="cover-image no-image-div">
            <p :style="cannotFindImage == true || ((windowWidth >= 768) ? store.largePhoto == null : store.smallPhoto == null) ? 'visibility: visible' : 'visibility: hidden'" v-if="editingShop == false" class="uk-overlay uk-position-center overlay" style="color: white;">Nicio imagine gasita</p>
          </div>
          
          <div class="uk-overlay uk-position-bottom-left overlay uk-flex uk-flex-column">
            <vk-label v-if="editingShop == true" class="uk-hidden@s" style="background: #E82901!important; margin-bottom: 5px;">In editare</vk-label>
            <vk-label v-if="store.promotedInHome || store.promotedInSearches">PROMOVAT</vk-label>
          </div>

          <div v-if="editingShop == false" class="uk-overlay uk-position-top-right overlay">
            <button style="cursor: pointer;" v-on:click="copyShareLink()" class="copy-link-button uk-button-primary uk-flex uk-flex-middle uk-align-center">
              <span style="margin-right: 5px" uk-icon="icon: social; ratio: 0.8"></span>
              <p style="margin: 0">Link distribuire</p>
            </button>
          </div>
          <div v-if="isOwner == true" class="uk-overlay uk-position-bottom-right overlay">
            <button uk-tooltip="Editeaza fatada de magazinul" style="cursor: pointer;" v-if="editingShop == false" @click="changeEditState()" class="edit-button uk-button-primary"><span uk-icon="icon: pencil; ratio: 0.8"></span></button>
            <div v-else class="edit-shop-button-container">
              <button uk-tooltip="Renunta la editarea fatadei de magazin" style="cursor: pointer;" @click="discardEdit()" class="edit-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
              <button uk-tooltip="Finalizeaza editarea fatadei de magazin" style="cursor: pointer;" @click="saveEdit()" class="edit-button uk-button-primary"><span uk-icon="icon: check; ratio: 0.8"></span></button>
            </div>
          </div>
          <div @click="showImageEdit = true" v-if="isOwner == true && editingShop == true" :style="(this.editingShop==true) ? 'cursor: pointer;' : ''" style="color: white;" class="uk-overlay uk-position-center overlay change-image-div">
            <span uk-icon="camera" ratio="2"></span>
            <p>Schimba imaginea {{windowWidth >= 768 ? 'mare' : 'mica'}}</p>
          </div>

          <!--DESKTOP-->
          <div v-if="editingShop == true" class="uk-overlay uk-position-top-center overlay uk-visible@s">
            <vk-label style="background: #E82901!important">In editare</vk-label>
          </div>

          <div v-if="isOwner == true && editingShop == true" class="uk-visible@s change-contact-button-div">
            <div class="uk-overlay uk-position-top-left overlay">
              <button style="cursor: pointer; padding: 5px" class="change-contact-button uk-button-primary uk-flex uk-flex-middle uk-align-center" href="#change-contact-sections" uk-toggle>
                <span style="margin-right: 5px" uk-icon="icon: refresh; ratio: 0.8"></span>
                <p style="margin: 0">Schimba detalii contact</p>
              </button>
            </div>
            <div class="uk-overlay uk-position-top-right overlay uk-flex uk-flex-row uk-flex-middle">
              <p style="margin: 0; margin-right: 10px">Reinnoire automata</p>
              <label style="cursor: pointer;" class="switch">
                <input type="checkbox" @click="wantsAutomaticTokenRefreshChanged()" v-model="store.hasAutomaticTokenRefresh">
                <span class="slider round"></span>
              </label>
            </div>
          </div>

          <!--MOBILE-->
          <div v-if="isOwner == true && editingShop == true" class="uk-overlay uk-position-top-left overlay change-contact-button-div uk-hidden@s">
            <button class="change-contact-button uk-button-primary uk-flex uk-flex-middle" href="#change-contact-sections" uk-toggle style="padding: 5px">
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

        <vk-grid style="margin-top: 15px" matched>
          <div class="shop-info-container uk-width-2-3@m">
            <article class="uk-article" style="height: 100%;">

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
            </article>
          </div>

          <div class="uk-width-1-3@m" style="padding: 0">
            <article class="uk-article" style="margin-bottom: 10px;">
              <vk-card padding="small">
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
              </vk-card>
            </article>
            <Schedule 
              :editingShop="editingShop"
              :schedule="store.schedule"
              :isOnline="store.county == 'Online' ? true : false"

              @change_schedule="changeSchedule($event)"
            />
          </div>
        </vk-grid>

        

        <div class="uk-margin-medium-top">
          <vk-tabs align="center" :active-tab.sync="activeTab">
            <vk-tabs-item title="Toate">
              <ProductCardGrid
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
            <vk-tabs-item v-for="(tab, index) in store.tabs" :key="index+1" :title="tab.name">
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
            <vk-tabs-item v-if="isOwner" title="File">
              <TabsLayout
                v-on:add_new_tab="addNewTab()"
                v-on:delete_tab="deleteTab($event)"
                v-on:new_tab_saved="saveNewTab($event)"
                :tabs="store.tabs"
              /> 
            </vk-tabs-item>
            <vk-tabs-item title="Comentarii">
              <CommentCardGrid :isOwner="isOwner" v-on:reload_rating="reloadRating()" :code="this.code"/>
            </vk-tabs-item>
            
          </vk-tabs>
          
        </div>
        <ChangeContactDetailsDialog 
          v-on:contacts_changed="changeContacts($event)" 

          :oldPhone="store.phone" 
          :oldEmail="store.email" 
          :oldWebsiteLink="store.websiteLink" 
          :oldAddress="store.address"
          :oldCounty="store.county"
          />

        <vk-modal-full :show.sync="showImageEdit">
          <vk-modal-full-close></vk-modal-full-close>
          <vk-modal-title>Poza magazin</vk-modal-title>

          <CropperDialogComponent 
            v-on:hide_modal="hideModal()"
            v-on:change_image="changeImage($event)"

            :oldImage="store.photo" 
            :aspectRatio="(windowWidth >= 768) ? 32/9 : 4/3"
            :minHeight="1000"
            :minWidth="1000*3.55"
            :from="'storefront'"
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
const ChangeContactDetailsDialog = () => import(/* webpackChunkName: "storefront-chunk" */ "@/components/ChangeContactDetailsDialog");
const Schedule = () => import(/* webpackChunkName: "storefront-chunk" */ "@/components/Schedule.vue")
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
              photo: null,
              tabs: null,
              schedule: null,
              hasAutomaticTokenRefresh: null
            },
            scheduleCopy: null,
            nameCopy: null,
            descriptionCopy: null,
            newImage: null,
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
            cannotFindImage: false
        }
    },
    metaInfo() {
      return { 
        title: `${(this.store.name != null) ? this.store.name + ' - ProduseSiServicii.ro' : 'ProduseSiServicii.ro'}`,
        meta: [
            { name: 'description', content:  (this.store.description != null) ? this.store.description.substring(0, Math.min(this.store.description.length, 200)) : 'Descriere'},
            { property: 'og:title', content: this.store.name + " - ProduseSiServicii.ro"},
            { property: 'og:site_name', content: 'ProduseSiServicii.ro'},
            { property: 'og:type', content: 'website'},    
            { name: 'robots', content: 'index,follow'} 
        ]
      }
    },
    methods: {
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
          this.showImageEdit = false
        },
        async changeImage(data) {
          this.cannotFindImage = false
          this.newImage = data.image
          this.hideModal();
        },
        async wantsAutomaticTokenRefreshChanged() {
          var timeoutVar = setTimeout(() => { this.loading = true; }, 1500);
          await axios({
            url: this.backend+'/api/shops/changeHasAutomaticTokenRefresh?id='+this.$route.params.id,
            method: 'post',
            withCredentials: true,
            headers: {
              'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
              'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
            }
          })
            .then((response) => {
              
              this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
              this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);
              
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
              'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
              'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
            },
            withCredentials: true
          })
            .then((response) => {
              this.isOwner = true;

              this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
					    this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);
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
          this.newImage=null;
          this.changeEditState();
        },
        async saveEdit() {
          if(this.windowWidth >= 768)
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
              'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
              'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
            },
            data: {
              newImage: this.newImage,
              name: this.store.name,
              description: this.store.description,
              schedule: this.store.schedule,
            },
            withCredentials: true
          })
            .then((response) => {
              this.$cookie.set("CSRF-TOKEN", response.data.csrfToken, 7);
              this.$cookie.set("REFRESH-TOKEN", response.data.refreshToken, 7);

              if(imageType == 'LARGE')
                this.store.largePhoto = response.data.newImageURL
              else
                this.store.smallPhoto = response.data.newImageURL

              this.nameCopy=this.store.name;
              this.scheduleCopy=this.store.schedule
              this.descriptionCopy=this.store.description;
              this.displayedDescription=this.store.description;
              this.newImage = null;

              this.cutDescription();

              UIkit.notification({message: 'Modificarile au fost salvate', status: 'success'})
            })  
            .catch((error) => {
              console.log(error)
              if(error.response.status == 400) {
                UIkit.notification({message: 'Modificarile nu au putut fi salvate', status: 'danger'})
                return
              }
              if(error.response.status == 403) {
                this.$store.dispatch('changeLogged', '')
                this.$cookie.delete('REFRESH-TOKEN')
                this.$cookie.delete('CSRF-TOKEN')
                return
              }
              UIkit.notification({message: 'Modificarile nu au putut fi salvate', status: 'danger'})
            })
            .finally(() => {
              this.loading = false;
              clearTimeout(timeoutVar);
            })
        },
        warnThatContactsAreNotSet() {
          if(this.isOwner == true) {
            if(this.store.address == null && this.store.websiteLink == null && this.store.email == null && this.store.phone == null) {
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
        if(oldV < 768 && newV >= 768)
          this.cannotFindImage = false;

        if(oldV >= 768 && newV < 768)
          this.cannotFindImage = false;
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
.overlay {
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
  padding-right: 10px;
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
    padding: 10px 0;
    margin: 0 10px;
  }
  .info-container {
    margin: 0 10px;
  }
}

</style>
