<template>
  <div id="app">
    <NewNewNewNavbar v-if="this.$store.getters.getHasCompletedTutorial"/>
    <CookiesPopup v-if="hasAcceptedCookies == false" @set_has_accepted_cookies_true="setHasAcceptedCookiesTrue" />
    <UpdateAvailableDialog />
    <OfflineDialog />
    <InstallDialog />
    <router-view/>
  </div>
</template>

<script>
const NewNewNewNavbar = () => import(/* webpackChunkName: "others-chunk" */ '@/components/NewNewNewNavbar.vue')
const CookiesPopup = () => import(/* webpackChunkName: "others-chunk" */ '@/components/CookiesPopup.vue')
const UpdateAvailableDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/UpdateAvailableDialog.vue')
const OfflineDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/OfflineDialog.vue')
const InstallDialog = () => import(/* webpackChunkName: "dialogs-chunk" */ '@/components/InstallDialog.vue')

export default {
  name: 'App',
  components: {
    NewNewNewNavbar,
    CookiesPopup,
    OfflineDialog,
    InstallDialog,
    UpdateAvailableDialog,
  },
  data() {
    return {
      hasAcceptedCookies: localStorage.getItem('has_accepted_cookies') || false
    }
  },
  methods: {
    setHasAcceptedCookiesTrue() {
      this.hasAcceptedCookies=true;
    }
  },
  metaInfo() {
    return { 
      title: "ProduseSiServicii.ro",
      meta: [
          { name: 'description', content:  'Aceasta platforma are scopul de a ajuta comerciantii in a-si gasi clienti, iar clientii in a gasi produse direct de la producator sau diferite servicii.'},
          { property: 'og:title', content: "ProduseSiServicii.ro - Gaseste-ti clientii si comerciantii"},
          { property: 'og:site_name', content: 'ProduseSiServicii.ro'},
          { property: 'og:type', content: 'website'},    
          { name: 'robots', content: 'index,follow'} 
      ]
    }
  }
}
</script>

<style lang="scss">
#app {
  font-family: 'Montserrat', Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.in-focus {
  z-index: 1011!important;
  position: relative;
  color: white!important;
}

.top-placement {
	position: fixed;
	top: 0;
  min-height: 20px;
	width: 98.5%;
  left: 0;
}

.bottom-placement {
	position: fixed;
	bottom: 0;
	min-height: 30px;
	width: 98.5%;
  left: 0;
}

.overlay {
	top: 0;
  left: 0;
  position: fixed;
  height: 100%;
  width: 100%;
  z-index: 1011;
  background: #0000006e;
}
</style>
