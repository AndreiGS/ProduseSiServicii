<template>
	<div id="search-sections" uk-modal>
		<div class="uk-modal-dialog uk-margin-auto-vertical">
			<button class="uk-modal-close-default" type="button" uk-close></button>
			<div v-if="loading == false">
				<div class="uk-modal-header">
					<h2 class="uk-modal-title">Cauta</h2>
				</div>
				<div class="uk-modal-body" uk-overflow-auto>

					<div>
						<h4 class="uk-text-center">Nume</h4>
						<div class="uk-flex uk-flex-row uk-flex-middle">
							<div class="uk-inline uk-width-1-1">
								<a class="uk-form-icon" href="#" uk-icon="icon: search"></a>
								<input v-model="selectedSearch" class="uk-input" type="text">
							</div>
							<button style="cursor: pointer;" @click="selectedSearch = null" class="reset-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
						</div>
					</div>
					<hr>
					<SearchDropdownInput
						:title="'Categorii'"
						:items="categories"
						:dropdownName="'category'"
						v-on:item_changed="categoryChanged($event)"
					/>
					<hr>
					<SearchDropdownInput
						:title="'Subcategorii'"
						:items="findSubcategoriesByCategoryId()"
						:dropdownName="'subcategory'"
						v-on:item_changed="selectedSubcategory = $event"
					/>

					<hr>

					<div>
						<h4 class="uk-text-center">Judet</h4>
						<div class="uk-flex uk-flex-row uk-flex-middle">
							<region-select style="cursor: pointer;"  id="RegionSelect" className="uk-select uk-width-1-1" defaultRegion="RO" :regionName="true" v-model="selectedCounty" country="RO" placeholder="Neselectat" />
      				<button style="cursor: pointer;" @click="setNullCountySelector()" class="reset-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
						</div>
					</div>

					<hr>

					<div>
						<h4 class="uk-text-center">Rating minim</h4>
						<div class="uk-flex uk-flex-column uk-width-1-1">
							<StarRating class="uk-flex-center"
								v-model="selectedRating"
								:show-rating="false" 
								:star-size="30" 
								:increment="0.5"
							/>
							<button style="cursor: pointer; margin-top: 10px;" @click="selectedRating = null" class="reset-button uk-button-danger"><span uk-icon="icon: ban; ratio: 0.8"></span></button>
						</div>
					</div>

					<hr>

				</div>
				<div class="uk-modal-footer uk-text-right">
					<div class="uk-visible@s">
						<button class="uk-button uk-button-default uk-modal-close custom-dialog-button" type="button">Inchideti</button>
						<button v-on:click="search()" class="uk-button uk-button-primary custom-dialog-button" type="button">Cautati</button>
					</div>
					<div class="uk-hidden@s">
						<button class="uk-button uk-button-default uk-modal-close custom-dialog-button uk-button-small" type="button">Inchideti</button>
						<button v-on:click="search()" class="uk-button uk-button-primary custom-dialog-button uk-button-small" type="button">Cautati</button>
					</div>
				</div>
			</div>
			<div v-else>
				<h2 class="uk-text-center">Se incarca</h2>
			</div>
			
		</div>
	</div>
</template>

<script>
import axios from 'axios'

const SearchDropdownInput = () => import(/* webpackChunkName: "home-chunk" */ "@/components/SearchDropdownInput.vue")
const StarRating = () => import('vue-star-rating')

export default {
	name: "SearchDialog",
	components: {
		SearchDropdownInput,
		StarRating
	},
	data() {
		return {
			selectedCategory: 'none',
			selectedSubcategory:'none',
			selectedSearch: null,
			selectedCounty: null,
			selectedRating: null,

			categories: null,
			subcategories: null,
			loading: false,

			backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
		}
	},
	methods: {
		categoryChanged(category) {
			this.selectedCategory = category; 
			this.selectedSubcategory = 'none';
		},
		setNullCountySelector() {
			var regionSelectInput = document.getElementById("RegionSelect")
			regionSelectInput.selectedIndex = 0
			this.selectedCounty = null;
		},
		async getCategoriesAndSubcategories() {
			var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
			await axios({
				url: this.backend+'/api/search/getCategoriesAndSubcategories',
				method: 'get'
			})
				.then((response) => {
					this.categories = response.data.categories
					this.subcategories = response.data.subcategories
				})
				.catch((error) => {
				})
				.finally(() => {
					this.loading = false
					clearTimeout(timeoutVar)
				})
		},
		findSubcategoriesByCategoryId() {
			if(this.subcategories == null)
				return

			let subcategoriesToReturn = [];

			this.subcategories.forEach(subcategory => {
				if(subcategory.categoryId == this.selectedCategory)
					subcategoriesToReturn.push(subcategory)
			});

			return subcategoriesToReturn
		},
		search() {
			let frontend = process.env.VUE_APP_FRONTEND

			let search = (this.selectedSearch == null || this.selectedSearch == '') ? "none" : this.selectedSearch
			let subcategory = this.selectedSubcategory
			let category = this.selectedCategory
			let rating = this.selectedRating == null ? -1 : this.selectedRating
			let county = (this.selectedCounty != "" && this.selectedCounty != null) ? this.selectedCounty : "none"

			if(category == 'none')
				subcategory = 'none'

			if(search == 'none' && subcategory == 'none' && category == 'none' && rating == -1 && county == "none") {
				if(this.$router.currentRoute.path != "/")
					this.$router.push({name: 'Home'})
				UIkit.modal("#search-sections").hide()
				return
			}

			if(this.$router.currentRoute.fullPath != '/search?search='+search+'&subcategory='+subcategory+'&category='+category+'&rating='+rating+'&county='+county) {
				this.$router.push({
					name: 'Search',
					query: {
						search: search,
						subcategory: subcategory,
						category: category,
						rating: rating,
						county: county
					}
				})
			}

			/*this.selectedSearch = null
			this.selectedRating = null
			this.selectedCounty = ''
			this.selectedCategory = 'none'
			this.selectedSubcategory = 'none'

			var regionSelectInput = document.getElementById("RegionSelect")
			regionSelectInput.selectedIndex = 0*/

			UIkit.modal("#search-sections").hide()
		},
		addOnlineOption() {
			const el = document.getElementById("RegionSelect");
			var option = document.createElement("option");
			option.text = "Online";
			option.value = "Online";
			el?.appendChild(option)
		}
	},
	async mounted() {
		await this.getCategoriesAndSubcategories();
		this.findSubcategoriesByCategoryId();
		this.addOnlineOption();
	}
}
</script>

<style scoped>
.custom-dialog-button{
	margin-right: 2px;
}
.reset-button {
	outline: 0;
  border: none;
	border-radius: 10px;
  width: 45px;
	height: 30px;
	align-self: center;
	margin: 0 10px;
}
</style>