<template>
  <div class="home">
    <CardLayout />
  </div>
</template>

<script>
const CardLayout = () => import(/* webpackChunkName: "home-chunk" */ '@/components/CardLayout.vue')

export default {
  name: 'Home',
  components: {
    CardLayout,
  },
  metaInfo() {
    return { 
      title: "Acasa - ProduseSiServicii.ro",
      meta: [
          { name: 'description', content:  'Aceasta platforma are scopul de a ajuta comerciantii in a-si gasi clienti, iar clientii in a gasi produse si servicii cat mai usor si rapid.'},
          { name: 'author', content: 'ProduseSiServicii.ro'},
          { name: 'keywords', content: 'produse, Produse, PRODUSE, servicii, Servicii, SERVICII, produse si servicii, produse sau servicii, produsesiservicii.ro, pfa, srl, instalator, horeca, mancare, reparator, masaj, mecanic'},
          { property: 'og:title', content: "ProduseSiServicii.ro - Gaseste-ti clientii si comerciantii"},
          { property: 'og:site_name', content: 'ProduseSiServicii.ro'},
          { property: 'og:type', content: 'website'},    
          { name: 'robots', content: 'index,follow'} 
      ]
    }
  }
}
</script>
