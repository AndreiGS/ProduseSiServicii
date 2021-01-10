<template>
	<div :class="windowWidth > 960 ? 'uk-padding-large' : ''">
		<div v-if="!loading">
			<!--<vk-card class="uk-width-1-1 ad" type="primary" style="padding-left: 0 !important;">
				<vk-card-title> RECLAME </vk-card-title>
			</vk-card>-->
			<div v-if="!errored">
				<div class="uk-grid-match custom-grid" uk-grid>
					<div v-if="isSearchingData == false" class="uk-width-1-1 uk-grid-match custom-grid" uk-grid>
						<StoreFrontCard class="uk-width-1-6@xl uk-width-1-4@l uk-width-1-2@s uk-width-1-1@xs custom-grid-card" v-for="shop in shops" 
							:key="shop.id" 
							:id="shop.id" 
							:rating="shop.rating" 
							:description="shop.description" 
							:title="shop.name"
							:image="shop.smallPhoto || require('../assets/logo.png')"
							:promoted="shop.promotedInHome"
						/>
					</div>
					<div v-else class="uk-width-1-1 uk-grid-match custom-grid" uk-grid>
						<StoreFrontCard class="uk-width-1-6@xl uk-width-1-4@l uk-width-1-2@s uk-width-1-1@xs custom-grid-card" v-for="shop in getSearchedShopsByPage(currentPage)" 
							:key="shop.shop.id" 
							:id="shop.shop.id" 
							:rating="shop.shop.rating"
							:description="shop.shop.description" 
							:title="shop.shop.name"
							:image="shop.shop.smallPhoto || require('../assets/logo.png')"
							:foundItemsIds="shop.itemsIds"
							:promoted="shop.shop.promotedInSearches"
						/>
					</div>
				</div>
				<Pagination v-if="totalPages > 1" v-on:change_page="changePage($event)" :currentPage="currentPage" :totalPages="totalPages"/>
				<!--<vk-card class="uk-width-1-1 ad" type="primary" style="padding-left: 0 !important;">
					<vk-card-title> RECLAME </vk-card-title>
				</vk-card>-->
			</div>
			<div v-else>
				<div class="uk-padding-small not-found" style="background-color: rgba(111,196,43,0.2)">
					Nu exista niciun magazin!
				</div>
			</div>
		</div>
		<div v-else>
    	<div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
				Se incarca
			</div>
    </div>
	</div>
</template>

<script>
import axios from 'axios'

const StoreFrontCard = () => import(/* webpackChunkName: "cards-chunk" */ '@/components/StorefrontCard.vue')
const Pagination = () => import(/* webpackChunkName: "home-chunk" */ '@/components/Pagination.vue')

export default {
  name: 'CardLayout',
  components: {
		StoreFrontCard,
		Pagination
  },
	data() {
		return {
			shops: null,

			loading: false,
			errored: false,

			totalPages: null,
			currentPage: 1,

			backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080',

			isSearchingData: false
		}
	},
  async mounted() {
		if(this.isSearching() == false) {
			await this.getShops()
			return
		}
		this.isSearchingData = true
		await this.getSearchedShops();
	},
	methods: {
	 	async getShops() {
			this.errored = false;
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
			await axios
				.get(this.backend+'/api/shops/getPromotedShops?page=' + (this.currentPage-1))
				.then(response => {
					this.shops = response.data.shops;
					this.totalPages = response.data.totalPages;

					if(this.shops.length == 0) {
						this.errored = true;
					}

				})
				.catch(error => {
					this.errored = true;
				})
				.finally(() => {
					this.loading = false
					clearTimeout(timeoutVar)
				})
		},
		async getSearchedShops() {
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
			
			await axios({
				url: this.backend + '/api/search/searchForItems?searchBarText='+this.$route.query.search+'&category='+this.$route.query.category+'&subcategory='+this.$route.query.subcategory+'&rating='+this.$route.query.rating+'&county='+this.$route.query.county,
				method: 'get'
			})
				.then((response) => {
					this.errored = false
					this.shops = response.data

					this.totalPages = this.calculateTotalPages(this.shops.length)

					if(this.shops.length == 0) {
						this.errored = true;
					}
				})
				.catch((error) => {
					this.shops = null
					this.errored = true
				})
				.finally(() => {
					this.loading = false
					clearTimeout(timeoutVar)
				})
		},
		calculateTotalPages(length) {
			return (Math.ceil(length/24));
		},
		async changePage(page) {
			this.currentPage = page;

			if(this.isSearching() == false) {
				await this.getShops()
				return
			}
		},
		getSearchedShopsByPage(page) {
			if(this.shops == null)
				return 

			let start = (page-1)*24;
			let end = start+24;

			return this.shops.slice(start, end)
		},
		isSearching(url) {
			if(url == null || url == undefined) {
				url = this.$router.currentRoute.path
			}
			if(url.includes("search")) {
				return true;
			}
			return false
		},
	},
	watch: {
    async $route (to, from) {
			this.shops = null
			
      if(this.isSearching(to.path) == true) {
				this.isSearchingData = true
				await this.getSearchedShops()
				return
			}
			this.isSearchingData = false
			
			await this.getShops()
		},
  }
}
</script>

<style scoped>
.ad {
	margin: 10px;
}
.custom-grid-card {
	padding: 10px!important;
}
.custom-grid {
	padding: 0!important;
}
.not-found {
  font-size: 20px;
  margin-top: 15px;
}
</style>