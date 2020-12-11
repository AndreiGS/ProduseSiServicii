<template>
  <div id="app">
    <NewNewNewNavbar />
    <CookiesPopup v-if="hasAcceptedCookies == false" @set_has_accepted_cookies_true="setHasAcceptedCookiesTrue"/>
    <router-view/>
  </div>
</template>

<script>
const NewNewNewNavbar = () => import(/* webpackChunkName: "others-chunk" */ '@/components/NewNewNewNavbar.vue')
const CookiesPopup = () => import(/* webpackChunkName: "others-chunk" */ '@/components/CookiesPopup.vue')

export default {
  name: 'App',
  components: {
    NewNewNewNavbar,
    CookiesPopup
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
      title: "Produse&Servicii.ro",
      meta: [
          { name: 'description', content:  'Aceasta platforma are scopul de a ajuta comerciantii in a-si gasi clienti, iar clientii in a gasi produse direct de la producator sau diferite servicii.'},
          { property: 'og:title', content: "Produse&Servicii.ro - Gaseste-ti clientii si comerciantii"},
          { property: 'og:site_name', content: 'Produse&Servicii.ro'},
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
</style>
