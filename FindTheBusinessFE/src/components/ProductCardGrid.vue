<template>
  <div v-if="firstEnter == false" class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
    <div v-if="loading==true">
      Se incarca
    </div>
    <div v-else>
      <vk-grid>
        <div class="uk-width-5-5@m">
          <div class="cards">
            <AddItemCard v-if="tab == 'all' && isOwner == true && !itemAdded && (this.$store.getters.getTutorialStep == 26 || this.$store.getters.getHasCompletedTutorial)" style="margin-bottom: 15px" :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 22 ? 'in-focus' : ''" v-on:add_item="addItem()"/>
            <div v-if="products != null && products.length > 0">
              <div v-if="isSearching == true">
                <div v-if="foundItemsIds != null && foundItemsIds.length > 0">
                  <h3 style="margin-bottom: 10px; font-weight: 350">Articole gasite</h3>
                  <ProductCard 
                    :isOwner="isOwner" 

                    v-on:save_item="saveItem($event)" 
                    v-on:delete_item="deleteItem($event)" 
                    v-on:change_image="changeImage($event)"
                    v-on:delete_new_image="deleteNewImage($event)"

                    v-for="product in getProducts(true)" 
                      :key="product.id"
                      :editingShop="editingShop"
                      :allTabs="allTabs"
                      :tab="tab"
                      :oldId="product.id"
                      :oldTitle="product.title"
                      :oldDescription="product.description"
                      :oldTab="(product.tabName == null) ? 'none' : product.tabName"
                      :email="email"
                      :price="product.price"
                      :websiteLink="websiteLink"
                      :phone="phone"
                      :address="address"
                      :county="county"
                      :oldImage="product.photo"
                      :newImage="product.newImage"
                  />
                </div>
                
                <div v-if="foundItemsIds.length != products.length">
                  <h3 style="margin-top: 20px; margin-bottom: 10px; font-weight: 350" v-if="foundItemsIds != null && foundItemsIds.length > 0">Alte articole</h3>
                  <ProductCard 
                    :isOwner="isOwner" 

                    v-on:save_item="saveItem($event)" 
                    v-on:delete_item="deleteItem($event)"
                    v-on:change_image="changeImage($event)"
                    v-on:delete_new_image="deleteNewImage($event)"

                    v-for="product in getProducts(false)" 
                      :key="product.id"
                      :editingShop="editingShop"
                      :allTabs="allTabs"
                      :tab="tab"
                      :oldId="product.id"
                      :oldTitle="product.title"
                      :oldDescription="product.description"
                      :oldTab="(product.tabName == null) ? 'none' : product.tabName"
                      :price="product.price"
                      :email="email"
                      :websiteLink="websiteLink"
                      :phone="phone"
                      :address="address"
                      :county="county"
                      :oldImage="product.photo"
                      :newImage="product.newImage"
                  />
                </div>
              </div>
              <div v-else>
                <ProductCard 
                  :isOwner="isOwner" 

                  v-on:save_item="saveItem($event)"
                  v-on:delete_item="deleteItem($event)"
                  v-on:change_image="changeImage($event)"
                  v-on:delete_new_image="deleteNewImage($event)"
                  @hide_modal="$emit('hide_modal')"
                  @show_modal="$emit('show_modal')"

                  v-for="product in products"
                    :key="product.id"
                    :editingShop="editingShop"
                    :allTabs="allTabs"
                    :price="product.price"
                    :tab="tab"
                    :oldId="product.id"
                    :oldTitle="product.title"
                    :oldDescription="product.description"
                    :oldTab="(product.tabName == null) ? 'none' : product.tabName"
                    :email="email"
                    :websiteLink="websiteLink"
                    :phone="phone"
                    :address="address"
                    :county="county"
                    :oldImage="product.photo"
                    :newImage="product.newImage"
                />
              </div>
            </div>
            <div v-else-if="(this.products != null && this.products.length <= 0) || this.products == null" class="not-found">
              Nu s-a gasit niciun produs in aceasta fatada de magazin
            </div>
          </div>
        </div>
        <!--<vk-card class="uk-width-1-5@m" type="primary" style="padding-left: 0 !important;">
          <vk-card-title> RECLAME </vk-card-title>
        </vk-card>-->
      </vk-grid>
    </div>
  </div>
</template>

<script>
const ProductCard = () => import(/* webpackChunkName: "cards-chunk" */ "./ProductCard");
const AddItemCard = () => import(/* webpackChunkName: "cards-chunk" */ '@/components/AddItemCard')

import axios from 'axios'

export default {
    name: "ProductCardGrid",
    components: {
        ProductCard,
        AddItemCard,
    },
    data() {
      return {
        loading: false,
        products: [],
        itemAdded: false,
        firstEnter: true,
        data: {
          id: null,
          image: null
        },

        backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
      }
    },
    props: {
      allTabs: null,
      tab: null,
      //contact data
      phone: null,
      email: null,
      websiteLink: null,
      address: null,
      county: null,
      editingShop: null,

      isOwner: null,

      foundItemsIds: null,

      isSearching: null,
    },
    methods: {
      changeImage(data) {
        this.data.id = data.id
        this.data.image = data.image || require('../assets/logo.png')
      },
      saveNewImage(data) {
        this.products.forEach(product => {
          if(product.id == data.id) {
            this.$set(product, 'newImage', data.image)
            return;
          }
        });
      },
      deleteNewImage(id) {
        this.products.forEach(product => {
          if(product.id == id) {
            product.newImage = null
            return;
          }
        });
      },
      addItem() {
        if(this.itemAdded == true) {
          UIkit.notification({message: 'Ati adaugat deja un produs nou', status: 'danger'})

          return
        }

        this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)

        if(this.products == null)
          this.products = []

        this.products.unshift({
          'id': 'not-set',
          'title': 'Produs nou',
          'description': 'Descriere produs nou',
          'price': null,
          'photo': null,
          'tabName': 'none'
        })

        this.itemAdded = true;
      },
      saveItem(item) {
        this.deleteItem(item)

        this.products.unshift({
          'id': item.newId,
          'title': item.title,
          'description': item.description,
          'price': item.price,
          'photo': item.image,
          'tabName': item.tabName
        })
      },
      deleteItem(item) {
        var removeIndex = this.products.map(function(currItem) { return currItem.id.includes(item.id); }).indexOf(true);
        this.products.splice(removeIndex, 1)
        if(item.id == 'not-set')
          this.itemAdded = false;
      },
      async getItems() {
        let csrfToken = this.$cookie.get("CSRF-TOKEN")
        if(!csrfToken) {
          csrfToken=Math.floor(Math.random() * 10000000);
			    this.$cookie.set("CSRF-TOKEN", csrfToken, 7)
        } 

        var timeoutVar = setTimeout(() => { this.firstEnter = false; this.loading = true; }, 1000);
        await axios({
          url: this.backend+"/api/items/getItems?store="+this.$route.params.id,
          method: 'post',
          data: {
            foundItemsIds: this.foundItemsIds,
            tab: this.tab
          },
          headers: {
            'X-CSRF-TOKEN': csrfToken
          }
        })
          .then((response) => {
            this.products = response.data.items

            if(this.products.length == 0)
              this.products = []

              /*for(let i=0; i<this.products.length; i++)
                if(this.products[i].description > 123)
                  this.products[i].description = this.products[i].description.substring(0, 119)+"..."*/
          })
          .catch((error) => {
            this.products = []
          })
          .finally(() => {
            this.firstEnter = false
            this.loading = false;
            clearTimeout(timeoutVar)
          })
      },
      getProducts(searched) {
        if(this.isSearching == false)
          return this.products

        if(this.foundItemsIds == null) {
          return this.products;
        }

        let length = this.foundItemsIds.length
        if(length <= 0 ){
          return this.products;
        }

        if(searched == true) {
          return this.products.slice(0, length)
        }
        
        return this.products.slice(length);
      }
    },
    async created() {
      await this.getItems();
    },
    mounted() {
      if(!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 22) {
        console.log("yes")
        window.scrollTo({
          top: document.body.scrollHeight,
          behavior: 'smooth',
        })
      }
    },
}
</script>

<style>
@media(min-width: 960px) {
  .cards {
    margin-right: 10px;
  }
}

.not-found {
  font-size: 20px;
}

.found_item {
  color: red
}
</style>