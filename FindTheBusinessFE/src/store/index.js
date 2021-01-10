import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

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
