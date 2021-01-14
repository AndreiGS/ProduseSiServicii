<template>
  <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
    <vk-grid v-if="shops.length>0">
      <div class="uk-width-5-5@m">
        <div class="cards">
          <AddShopCard 
            v-if="!hasAddedShop" 
            v-on:add_shop="addShop" 
            :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 3 ? 'in-focus' : ''" 
            style="margin-bottom: 10px"
          />
          <ShopCard 
            v-on:new_shop_added="$emit('new_shop_added', $event)" 
            v-on:change_published="$emit('change_published', $event)"
            v-on:discard_new_shop="$emit('discard_new_shop')" 
            v-on:show_modal="$emit('show_modal')" 
            v-on:close_modal="$emit('close_modal')" 
            v-on:refresh_shop="showRefresh($event)" 
            v-on:promote_shop="showPromote($event)" 
            v-on:delete_shop_db="showDelete($event)" 
            v-on:cannot_change_publish="surePublish($event)" 
            v-on:change_image="changeImage($event)"
            v-on:delete_new_image="$emit('delete_new_image', $event)"

            v-for="shop in shops" 
              :key="shop.id"
              :oldShopSize="shop.maximumSize"
              :oldIsPublished="shop.published"
              :shopId="shop.id"
              :oldTitle="shop.name"
              :oldDescription="shop.description"
              :rating="shop.rating"
              :isPromotedInHome="shop.promotedInHome"
              :isPromotedInSearches="shop.promotedInSearches"
              :promotedDaysInHomeRemaining="shop.promotedDaysInHomeRemaining"
              :promotedDaysInSearchesRemaining="shop.promotedDaysInSearchesRemaining"
              :shopSubcategoriesProp="shop.subcategories"
              :oldShopCategories="shop.categories"
              :subcategories="subcategories"
              :categories="categories"
              :newImage="shop.newImage"
              :oldImage="shop.smallPhoto"
          /><!--shop image-->
        </div>
      </div>
      <!--<vk-card class="uk-width-1-5@m" type="primary" style="padding-left: 0 !important;">
        <vk-card-title> RECLAME </vk-card-title>
      </vk-card>-->
    </vk-grid>
    <div v-else>
      <AddShopCard v-on:add_shop="addShop" :class="!this.$store.getters.getHasCompletedTutorial && this.$store.getters.getTutorialStep == 3 ? 'in-focus' : ''"  style="margin-bottom: 10px"/>
      <div class="not-found">Nu ai niciun magazin adaugat in cont!</div>
    </div>

    <RefreshStoreDialog v-on:refresh_page="$emit('refresh_page')" @close_modal="$emit('close_modal')" :shopIdToModify="shopId"/>
    <PromoteStoreDialog v-on:refresh_page="$emit('refresh_page')" @change_balance="$emit('change_balance', $event)" @close_modal="$emit('close_modal')" :shopIdToModify="shopId"/>
    <DeleteShopDialog v-on:delete_shop="$emit('delete_shop', $event)" :shopIdToModify="shopId"/>
    <SurePublishDialog v-on:change_published="$emit('change_published', $event)" :shopIdToModify="shopId"/> 
  </div>
</template>

<script>
const ShopCard = () => import(/* webpackChunkName: "cards-chunk" */ "./ShopCard");
const RefreshStoreDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/RefreshStoreDialog.vue');
const PromoteStoreDialog = () => import(/* webpackChunkName: "profile-chunk" */ '@/components/PromoteStoreDialog.vue');
const AddShopCard = () => import(/* webpackChunkName: "cards-chunk" */ '@/components/AddShopCard')
const DeleteShopDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/DeleteShopDialog')
const SurePublishDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/SurePublishDialog.vue')

export default {
    name: "ShopCardGrid",
    components: {
      ShopCard,
      RefreshStoreDialog,
      PromoteStoreDialog, 
      AddShopCard,
      DeleteShopDialog,
      SurePublishDialog,
    },
    data() {
      return {
        shopId: null,
        data: {
          id: null,
          image: null
        },
      }
    },
    props: {
      shops: null,
      subcategories: null,
      categories: null,
      hasAddedShop: false,
    },
    methods: {
      showRefresh(id) {
        this.shopId = id;
        UIkit.modal("#refresh-shop-sections").show();
      },
      showPromote(id) {
        this.shopId = id;
        UIkit.modal("#promote-shop-sections").show();
      },
      showDelete(id) {
        this.shopId = id;
        UIkit.modal("#delete-shop-sections").show();
      },
      surePublish(id) {
        this.shopId = id;
        UIkit.modal("#sure-publish-sections").show();
      },
      addShop() {
        if(this.$store.getters.getIsWithinTutorial){
          this.$store.dispatch('changeTutorialStep', this.$store.getters.getTutorialStep+1)
          window.scrollTo({
            top: document.body.scrollHeight,
            behavior: 'smooth'
          })
        }
        this.$emit('add_shop')
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
</style>