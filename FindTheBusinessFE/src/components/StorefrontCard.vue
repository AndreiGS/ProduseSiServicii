<template>
  <div class="card">
    <vk-card padding="small" class="uk-card-hover uk-flex"> 
      <div class="uk-flex uk-flex-stretch uk-flex-column" style="width: 100%">
        <div>
          <vk-label class="top-right" v-if="promoted" slot="badge">PROMOVAT</vk-label>
          <img loading="lazy" :style="(hasImageLoaded == false) ? 'visibility: hidden' : 'visibility: visible'" v-if="cannotFindImage == false && image != null" :src="image" alt="" class="card-image"  @onerror="cannotFindImage = true" @load="onImgLoad">

          <div v-else class="card-image no-image-div uk-inline">
            <p style="color: white;" class="uk-overlay uk-position-center overlay">Nicio imagine gasita</p>
          </div>

          <p class="card-text uk-text-center wrap">
            {{title}}
          </p> 
          <p class="uk-text-left uk-text-center wrap" style="white-space: pre-line;">{{displayedDescription}}</p>
          <div v-if="needsCrop == true" style="margin-bottom: 20px;">
            <button style="cursor: pointer;"  class="custom-showmore-button" v-if="isShowingFullDesc == false" @click="getFullDescription()">Arata toata descrierea</button>
            <button style="cursor: pointer;"  class="custom-showmore-button" v-else @click="cutDescription()">Arata mai putin</button>
          </div>
        </div>
        <div class="uk-flex uk-flex-bottom" style="height: 100%">
          <div class="uk-flex-column" style="width: 100%">
            <StarRating style="margin-bottom: 20px;" :rating="rating" :show-rating="false" :star-size="20" :increment="0.1" :read-only="true"/>
            <router-link :to="{ name: 'Storefront', params: { id: id }, query: { itemsIds: getFoundItemsIds() }}"><vk-button class="custom-button"  style="width: 100%" type="primary">VEZI</vk-button></router-link>
          </div>
        </div>
      </div>
    </vk-card>
  </div>
</template>

<!--
COMPONENT CALL:

<vk-grid class="uk-grid-match">
      <StorefrontCard
          title="Magazinul lui Gigel"
          description="Gigel vine doar produse proaspete si de inalta calitate! Produsele sunt BIO 100%!"
          :rating="3.7"
          :image="require('../assets/cat.jpg')"
          :promoted="true"
      />
      <StorefrontCard
          title="Magazinul lui Gigel2"
          description="Gigel vine doar produse proaspete si de inalta calitate! Produsele sunt BIO 100%!"
          :rating="3.7"
          :image="require('../assets/woman.jpg')"
          :promoted="true"
      />
    </vk-grid>
-->

<script>
import StarRating from 'vue-star-rating'

export default {
    name: "StorefrontCard",
    props: {
      id: null,
      title: null,
      description: null,
      rating: null,
      image: null,
      promoted: false,
      foundItemsIds: null,
    },
    data() {
      return {
        displayedDescription: this.description,
        isShowingFullDesc: false,
        needsCrop: true,
        cannotFindImage: false,
        hasImageLoaded: false,
      }
    },
    components: {
        StarRating
    },
    methods: {
      onImgLoad() {
        this.hasImageLoaded = true;
      },
      cutDescription() {
        if(this.displayedDescription.length <= 80) {
          this.needsCrop = false
          return
        }

        this.displayedDescription = this.displayedDescription.substring(0, 76) + "..."
        this.isShowingFullDesc = false
      },
      getFullDescription() {
        this.displayedDescription = this.description
        this.isShowingFullDesc = true
      },
      getFoundItemsIds() {
        if(this.foundItemsIds == null)
          return undefined

        let ids = []

        this.foundItemsIds.forEach(item => {
          ids.push(item.id)
        });

        return ids;
      },
      
    },
    mounted() {
      this.cutDescription();
    }
}
</script>

<style scoped>
.card {
  margin-bottom: 15px;
}
.card-text {
  text-align: left;
  font-weight: bold;
  margin-top: 5px !important;
}

.card-image {
  width: 100%;
  object-fit: cover
}

.wrap {
  word-break: break-word;
}

.custom-showmore-button {
  border: none;
  background-color: transparent;
  color: #6FC42B
}
  
.vue-star-rating {
	display: inline-block!important;
}

.uk-card-body {
  display: flex!important;
  width: 100%!important;
}

.top-right {
  position: absolute;
  top: 30px;
  right: 30px;
}

.no-image-div {
  background-color: rgb(136, 136, 136);
}

.custom-button {
  outline: 0;
  border: none;
}
</style>