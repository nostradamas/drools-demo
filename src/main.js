import Vue from "vue"
import App from "./App.vue"
import router from "./router"
import store from "./store"
import ElementUI from "element-ui"
import api from "./api" // 引入 api
import "element-ui/lib/theme-chalk/index.css"
import "element-ui/lib/theme-chalk/display.css" // 添加这一行，确保所有图标样式都被加载

Vue.config.productionTip = false

Vue.use(ElementUI, { size: "small" })

Vue.prototype.$api = api // 注册 $api

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app")
