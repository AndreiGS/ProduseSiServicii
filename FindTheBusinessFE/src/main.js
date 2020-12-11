import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueCookie from 'vue-cookie'
import axios from 'axios'
import vueCountryRegionSelect from 'vue-country-region-select'
import VueMeta from 'vue-meta'

Vue.config.productionTip = false

Vue.use(vueCountryRegionSelect)
Vue.use(VueCookie);

new Vue({
  async beforeCreate() { 
    this.$store.commit('initialiseStore');

    var url = window.location.href
    if(url.includes("confirmEmail?code=")) {
      let params = new URL(url).searchParams
      this.$router.push({ path: 'confirmEmail', query: { code:  params.get('code')} })
    }
    else if(url.includes("changeInfo?code=")) {
      let params = new URL(url).searchParams
      this.$router.push({ path: 'changed', query: { code:  params.get('code')} })
    }
    else if(url.includes("changePassword?code=")) {
      let params = new URL(url).searchParams
      this.$router.push({ path: 'changePassword', query: { code:  params.get('code')} })
    }
    else if(url.includes("profile")) {
      return
    }

    if(this.$store.getters.getLogged != '' && this.$store.getters.getLogged != null && this.$store.getters.getLogged != undefined) {
      await axios({
        url: (process.env.VUE_APP_BACKEND || 'http://localhost:8080') + '/api/authentication/checkIdentity',
        method: 'post',
        headers: {
          'X-CSRF-TOKEN': this.$cookie.get('CSRF-TOKEN'),
          'X-REFRESH-TOKEN': this.$cookie.get('REFRESH-TOKEN')
        },
        withCredentials: true,
      })
        .then((response) => {
          this.$cookie.set('CSRF-TOKEN', response.data.csrfToken, 7);
					this.$cookie.set('REFRESH-TOKEN', response.data.refreshToken, 7);
        })
        .catch(() => {
          this.$store.dispatch('changeLogged', '')
          this.$cookie.delete('CSRF-TOKEN')
          this.$cookie.delete('REFRESH-TOKEN')

          if(this.$route.query.owner) {
            let query = Object.assign({}, this.$route.query);
            delete query.owner;
            this.$router.replace({ query });
          }
          
        })
    }
  },
  router,
  store,
  render: h => h(App)
}).$mount('#app')

Vue.use(VueMeta)
