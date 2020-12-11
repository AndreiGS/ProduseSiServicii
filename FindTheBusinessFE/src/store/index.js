import Vue from 'vue'
import Vuex from 'vuex'
import Vuikit from 'vuikit'
import VuikitIcons from "@vuikit/icons";
import VueWindowSize from 'vue-window-size'

import '../assets/theme'
 
Vue.use(Vuex)
Vue.use(Vuikit)
Vue.use(VuikitIcons)
Vue.use(VueWindowSize)

export default new Vuex.Store({
  state: {
    logged: false,
  },
  mutations: {
    changeLogged(state, loginStatus) {
      state.logged = loginStatus;
      localStorage.setItem("logged", loginStatus);
    },
    initialiseStore(state) {
      state.logged = (localStorage.getItem("logged") != null) ? localStorage.getItem("logged") : false
    }
  },
  actions: {
    changeLogged(context, loginStatus) {
      context.commit('changeLogged', loginStatus);
    }
  },
  modules: {
  },
  getters: {
    getLogged(state) {
      return state.logged;
    },
  }
})
