import { getRuleList, createRule, updateRule, deleteRule, testRule, getRuleTemplates } from "@/api"

const state = {
  ruleList: [],
  ruleTemplates: [],
  currentRule: null,
  testResult: null,
  loading: false,
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0,
  },
}

const mutations = {
  SET_RULE_LIST(state, data) {
    state.ruleList = data.list || []
    state.pagination.total = data.total || 0
  },
  SET_RULE_TEMPLATES(state, templates) {
    state.ruleTemplates = templates
  },
  SET_CURRENT_RULE(state, rule) {
    state.currentRule = rule
  },
  SET_TEST_RESULT(state, result) {
    state.testResult = result
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  SET_PAGINATION(state, pagination) {
    state.pagination = { ...state.pagination, ...pagination }
  },
  ADD_RULE(state, rule) {
    state.ruleList.unshift(rule)
    state.pagination.total++
  },
  UPDATE_RULE(state, updatedRule) {
    const index = state.ruleList.findIndex((rule) => rule.id === updatedRule.id)
    if (index !== -1) {
      state.ruleList.splice(index, 1, updatedRule)
    }
  },
  REMOVE_RULE(state, ruleId) {
    const index = state.ruleList.findIndex((rule) => rule.id === ruleId)
    if (index !== -1) {
      state.ruleList.splice(index, 1)
      state.pagination.total--
    }
  },
}

const actions = {
  async fetchRuleList({ commit }, params = {}) {
    commit("SET_LOADING", true)
    try {
      const response = await getRuleList(params)
      commit("SET_RULE_LIST", response.data)
      return response
    } catch (error) {
      console.error("获取规则列表失败:", error)
      throw error
    } finally {
      commit("SET_LOADING", false)
    }
  },

  async fetchRuleTemplates({ commit }) {
    try {
      const response = await getRuleTemplates()
      commit("SET_RULE_TEMPLATES", response.data)
      return response
    } catch (error) {
      console.error("获取规则模板失败:", error)
      throw error
    }
  },

  async createRule({ commit }, ruleData) {
    try {
      const response = await createRule(ruleData)
      commit("ADD_RULE", response.data)
      return response
    } catch (error) {
      console.error("创建规则失败:", error)
      throw error
    }
  },

  async updateRule({ commit }, { id, data }) {
    try {
      const response = await updateRule(id, data)
      commit("UPDATE_RULE", response.data)
      return response
    } catch (error) {
      console.error("更新规则失败:", error)
      throw error
    }
  },

  async deleteRule({ commit }, ruleId) {
    try {
      await deleteRule(ruleId)
      commit("REMOVE_RULE", ruleId)
    } catch (error) {
      console.error("删除规则失败:", error)
      throw error
    }
  },

  async testRule({ commit }, { ruleId, testData }) {
    try {
      const response = await testRule(ruleId, testData)
      commit("SET_TEST_RESULT", response.data)
      return response
    } catch (error) {
      console.error("测试规则失败:", error)
      throw error
    }
  },

  setCurrentRule({ commit }, rule) {
    commit("SET_CURRENT_RULE", rule)
  },

  setPagination({ commit }, pagination) {
    commit("SET_PAGINATION", pagination)
  },
}

const getters = {
  ruleList: (state) => state.ruleList,
  ruleTemplates: (state) => state.ruleTemplates,
  currentRule: (state) => state.currentRule,
  testResult: (state) => state.testResult,
  loading: (state) => state.loading,
  pagination: (state) => state.pagination,
  activeRules: (state) => state.ruleList.filter((rule) => rule.status === "ACTIVE"),
  rulesByType: (state) => {
    const result = {}
    state.ruleList.forEach((rule) => {
      if (!result[rule.type]) {
        result[rule.type] = []
      }
      result[rule.type].push(rule)
    })
    return result
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}
