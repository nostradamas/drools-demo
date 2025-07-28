import Vue from "vue"
import Vuex from "vuex"
import tagLibrary from "./modules/tagLibrary"
import ruleManagement from "./modules/ruleManagement"
import employeeTagging from "./modules/employeeTagging"

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    tagLibrary,
    ruleManagement,
    employeeTagging,
  },
  state: {
    sidebar: {
      opened: true,
    },
  },
  mutations: {
    TOGGLE_SIDEBAR: (state) => {
      state.sidebar.opened = !state.sidebar.opened
    },
  },
  actions: {
    toggleSideBar({ commit }) {
      commit("TOGGLE_SIDEBAR")
    },
  },
})

export default store
