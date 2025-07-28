import Vue from "vue"
import Router from "vue-router"
import Layout from "@/components/Layout"

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: "/",
      component: Layout,
      redirect: "/dashboard",
      children: [
        {
          path: "dashboard",
          name: "Dashboard",
          component: () => import("@/views/Dashboard/index"),
          meta: { title: "仪表盘", icon: "el-icon-s-home" },
        },
      ],
    },
    {
      path: "/tag-library",
      component: Layout,
      redirect: "/tag-library/list",
      name: "TagLibrary",
      meta: { title: "标签库管理", icon: "el-icon-collection-tag" },
      children: [
        {
          path: "list",
          name: "TagList",
          component: () => import("@/views/TagLibrary/TagList"),
          meta: { title: "标签列表", icon: "el-icon-price-tag" },
        },
        {
          path: "categories",
          name: "TagCategories",
          component: () => import("@/views/TagLibrary/TagCategories"),
          meta: { title: "标签分类", icon: "el-icon-menu" },
        },
      ],
    },
    {
      path: "/rule-management",
      component: Layout,
      redirect: "/rule-management/list",
      name: "RuleManagement",
      meta: { title: "规则管理", icon: "el-icon-setting" },
      children: [
        {
          path: "list",
          name: "RuleList",
          component: () => import("@/views/RuleManagement/RuleList"),
          meta: { title: "规则列表", icon: "el-icon-document" },
        },
        {
          path: "templates",
          name: "RuleTemplates",
          component: () => import("@/views/RuleManagement/RuleTemplates"),
          meta: { title: "规则模板", icon: "el-icon-files" },
        },
      ],
    },
    {
      path: "/employee-tagging",
      component: Layout,
      redirect: "/employee-tagging/list",
      name: "EmployeeTagging",
      meta: { title: "员工标签", icon: "el-icon-user" },
      children: [
        {
          path: "list",
          name: "EmployeeList",
          component: () => import("@/views/EmployeeTagging/EmployeeList"),
          meta: { title: "员工列表", icon: "el-icon-user" },
        },
        {
          path: "tagging",
          name: "TaggingOperation",
          component: () => import("@/views/EmployeeTagging/TaggingOperation"),
          meta: { title: "打标签操作", icon: "el-icon-magic-stick" },
        },
        {
          path: "profile/:id",
          name: "EmployeeProfile",
          component: () => import("@/views/EmployeeTagging/EmployeeProfile"),
          meta: { title: "员工档案", icon: "el-icon-user-solid" },
          hidden: true,
        },
      ],
    },
    {
      path: "/statistics",
      component: Layout,
      children: [
        {
          path: "index",
          name: "Statistics",
          component: () => import("@/views/Statistics"),
          meta: { title: "统计分析", icon: "el-icon-pie-chart" },
        },
      ],
    },
  ],
})
