const state = {
  employeeList: [],
  currentEmployee: null,
  taggedEmployees: [],
  loading: false,
  pagination: {
    currentPage: 1,
    pageSize: 20,
    total: 0,
  },
  taggingResults: [],
  trainingRecommendations: [],
}

const mutations = {
  SET_EMPLOYEE_LIST(state, employees) {
    state.employeeList = employees
  },
  SET_CURRENT_EMPLOYEE(state, employee) {
    state.currentEmployee = employee
  },
  SET_TAGGED_EMPLOYEES(state, employees) {
    state.taggedEmployees = employees
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  SET_PAGINATION(state, pagination) {
    state.pagination = Object.assign({}, state.pagination, pagination)
  },
  SET_TAGGING_RESULTS(state, results) {
    state.taggingResults = results
  },
  SET_TRAINING_RECOMMENDATIONS(state, recommendations) {
    state.trainingRecommendations = recommendations
  },
  UPDATE_EMPLOYEE_TAGS(state, { employeeId, tags }) {
    const employee = state.employeeList.find((emp) => emp.id === employeeId)
    if (employee) {
      employee.currentTags = tags
    }
    const taggedEmployee = state.taggedEmployees.find((emp) => emp.id === employeeId)
    if (taggedEmployee) {
      taggedEmployee.currentTags = tags
    }
  },
}

const actions = {
  async fetchEmployeeList({ commit }, params = {}) {
    commit("SET_LOADING", true)
    try {
      const response = await this.$api.employeeTagging.getEmployeeList(params)
      commit("SET_EMPLOYEE_LIST", response.data || [])
      commit("SET_PAGINATION", {
        currentPage: response.currentPage || 1,
        pageSize: response.pageSize || 20,
        total: response.total || 0,
      })
      return response
    } catch (error) {
      console.error("获取员工列表失败:", error)
      throw error
    } finally {
      commit("SET_LOADING", false)
    }
  },

  async fetchEmployeeProfile({ commit }, employeeId) {
    commit("SET_LOADING", true)
    try {
      const response = await this.$api.employeeTagging.getEmployeeProfile(employeeId)
      commit("SET_CURRENT_EMPLOYEE", response.data)
      return response
    } catch (error) {
      console.error("获取员工档案失败:", error)
      throw error
    } finally {
      commit("SET_LOADING", false)
    }
  },

  async tagEmployee({ commit }, { employee, mode = "auto", tagCodes = [], reason = "" }) {
    try {
      let response
      if (mode === "auto") {
        response = await this.$api.employeeTagging.tagEmployeeWithDynamicRules(employee)
      } else {
        response = await this.$api.employeeTagging.addManualTag(employee.id, {
          tagCodes,
          reason,
        })
      }

      commit("UPDATE_EMPLOYEE_TAGS", {
        employeeId: employee.id,
        tags: response.data.currentTags || [],
      })

      return response
    } catch (error) {
      console.error("员工打标签失败:", error)
      throw error
    }
  },

  async batchTagEmployees({ commit }, { employees, mode = "auto", tagCodes = [], reason = "" }) {
    try {
      const employeeIds = employees.map((emp) => emp.id)
      let response

      if (mode === "auto") {
        response = await this.$api.employeeTagging.batchTagWithDynamicRules(employees)
      } else {
        response = await this.$api.employeeTagging.batchAddManualTags(employeeIds, {
          tagCodes,
          reason,
        })
      }

      // 更新本地状态
      employeeIds.forEach((employeeId) => {
        const employeeData = response.data.find((item) => item.employeeId === employeeId)
        const tags = employeeData ? employeeData.tags : []
        commit("UPDATE_EMPLOYEE_TAGS", { employeeId, tags })
      })

      commit("SET_TAGGING_RESULTS", response.data || [])
      return response
    } catch (error) {
      console.error("批量打标签失败:", error)
      throw error
    }
  },

  async runDemo({ commit }) {
    commit("SET_LOADING", true)
    try {
      const response = await this.$api.employeeTagging.runFullDemo()
      commit("SET_TAGGED_EMPLOYEES", response.taggedEmployees || [])
      commit("SET_EMPLOYEE_LIST", response.taggedEmployees || [])
      return response
    } catch (error) {
      console.error("运行演示失败:", error)
      throw error
    } finally {
      commit("SET_LOADING", false)
    }
  },

  async fetchTrainingRecommendations({ commit }, employeeId) {
    try {
      const response = await this.$api.employeeTagging.getTrainingRecommendations(employeeId)
      commit("SET_TRAINING_RECOMMENDATIONS", response.data || [])
      return response
    } catch (error) {
      console.error("获取培训推荐失败:", error)
      throw error
    }
  },
}

const getters = {
  employeeList: (state) => state.employeeList,
  currentEmployee: (state) => state.currentEmployee,
  taggedEmployees: (state) => state.taggedEmployees,
  loading: (state) => state.loading,
  pagination: (state) => state.pagination,
  taggingResults: (state) => state.taggingResults,
  trainingRecommendations: (state) => state.trainingRecommendations,

  getEmployeeById: (state) => (id) => {
    return state.employeeList.find((emp) => emp.id === id) || state.taggedEmployees.find((emp) => emp.id === id)
  },

  getActiveTagsByEmployee: (state) => (employeeId) => {
    const employee =
      state.employeeList.find((emp) => emp.id === employeeId) ||
      state.taggedEmployees.find((emp) => emp.id === employeeId)
    if (!employee || !employee.currentTags) return []

    return employee.currentTags.filter((tag) => {
      if (!tag.active) return false
      if (tag.expireTime && new Date(tag.expireTime) < new Date()) return false
      return true
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}
