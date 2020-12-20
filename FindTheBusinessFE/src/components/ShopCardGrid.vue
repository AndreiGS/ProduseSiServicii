<template>
  <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
    <vk-grid v-if="shops.length>0">
      <div class="uk-width-5-5@m">
        <div class="cards">
          <AddShopCard v-on:add_shop="$emit('add_shop')" style="margin-bottom: 10px"/>
          <ShopCard 
            v-on:new_shop_added="$emit('new_shop_added', $event)" 
            v-on:change_published="$emit('change_published', $event)"
            v-on:discard_new_shop="$emit('discard_new_shop')" 
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
              :subcategories="subcategories"
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
      <AddShopCard v-on:add_shop="$emit('add_shop')" style="margin-bottom: 10px"/>
      <div class="not-found">Nu ai niciun magazin adaugat in cont!</div>
    </div>

    <RefreshStoreDialog v-on:refresh_page="$emit('refresh_page')" :shopIdToModify="shopId"/>
    <PromoteStoreDialog v-on:refresh_page="$emit('refresh_page')" :shopIdToModify="shopId"/>
    <DeleteShopDialog v-on:delete_shop="$emit('delete_shop', $event)" :shopIdToModify="shopId"/>
    <SurePublishDialog v-on:change_published="$emit('change_published', $event)" :shopIdToModify="shopId"/> 
  </div>
</template>

<script>
const ShopCard = () => import(/* webpackChunkName: "profile-chunk" */ "./ShopCard");
const RefreshStoreDialog = () => import(/* webpackChunkName: "profile-chunk" */ '@/components/RefreshStoreDialog.vue');
const PromoteStoreDialog = () => import(/* webpackChunkName: "profile-chunk" */ '@/components/PromoteStoreDialog.vue');
const AddShopCard = () => import(/* webpackChunkName: "profile-chunk" */ '@/components/AddShopCard')
const DeleteShopDialog = () => import(/* webpackChunkName: "profile-chunk" */ '@/components/DeleteShopDialog')
const SurePublishDialog = () => import(/* webpackChunkName: "profile-chunk" */ '@/components/SurePublishDialog.vue')

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
      }
    }
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