<template>
  <div class="sidebar-wrapper">
    <div class="sidebar-logo">
      <h3>员工标签管理系统</h3>
    </div>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="false"
        :background-color="'#2b2f3a'"
        :text-color="'#bfcbd9'"
        :unique-opened="false"
        :active-text-color="'#409EFF'"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="route in routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import variables from '@/assets/css/variables.scss'

export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters([
      'sidebar'
    ]),
    routes() {
      return this.$router.options.routes
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    variables() {
      return variables
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-wrapper {
  height: 100%;
  
  .sidebar-logo {
    height: 50px;
    line-height: 50px;
    text-align: center;
    background: #2b2f3a;
    
    h3 {
      color: #fff;
      font-weight: 600;
      font-size: 16px;
      margin: 0;
    }
  }
}
</style>
