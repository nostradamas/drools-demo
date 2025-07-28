const state = {
  tagList: [],
  categoryList: [],
  currentTag: null,
  loading: false,
  pagination: {
    currentPage: 1,
    pageSize: 20,
    total: 0,
  },
}

const mutations = {
  SET_TAG_LIST(state, tags) {
    state.tagList = tags
  },
  SET_CATEGORY_LIST(state, categories) {
    state.categoryList = categories
  },
  SET_CURRENT_TAG(state, tag) {
    state.currentTag = tag
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  SET_PAGINATION(state, pagination) {
    state.pagination = Object.assign({}, state.pagination, pagination)
  },
  ADD_TAG(state, tag) {
    state.tagList.unshift(tag)
  },
  UPDATE_TAG(state, updatedTag) {
    const index = state.tagList.findIndex((tag) => tag.id === updatedTag.id)
    if (index !== -1) {
      state.tagList.splice(index, 1, updatedTag)
    }
  },
  DELETE_TAG(state, tagId) {
    const index = state.tagList.findIndex((tag) => tag.id === tagId)
    if (index !== -1) {
      state.tagList.splice(index, 1)
    }
  },
  ADD_CATEGORY(state, category) {
    state.categoryList.unshift(category)
  },
  UPDATE_CATEGORY(state, updatedCategory) {
    const index = state.categoryList.findIndex((cat) => cat.id === updatedCategory.id)
    if (index !== -1) {
      state.categoryList.splice(index, 1, updatedCategory)
    }
  },
  DELETE_CATEGORY(state, categoryId) {
    const index = state.categoryList.findIndex((cat) => cat.id === categoryId)
    if (index !== -1) {
      state.categoryList.splice(index, 1)
    }
  },
}

const actions = {
  async fetchTagList({ commit }, params = {}) {
    commit("SET_LOADING", true)
    try {
      const response = await this.$api.tagLibrary.getTagList(params)
      commit("SET_TAG_LIST", response.data || [])
      commit("SET_PAGINATION", {
        currentPage: response.currentPage || 1,
        pageSize: response.pageSize || 20,
        total: response.total || 0,
      })
      return response
    } catch (error) {
      console.error("获取标签列表失败:", error)
      throw error
    } finally {
      commit("SET_LOADING", false)
    }
  },

  async fetchCategoryList({ commit }) {
    try {
      const response = await this.$api.tagLibrary.getCategoryList()
      commit("SET_CATEGORY_LIST", response.data || [])
      return response
    } catch (error) {
      console.error("获取分类列表失败:", error)
      throw error
    }
  },

  async createTag({ commit }, tagData) {
    try {
      const response = await this.$api.tagLibrary.createTag(tagData)
      commit("ADD_TAG", response.data)
      return response
    } catch (error) {
      console.error("创建标签失败:", error)
      throw error
    }
  },

  async updateTag({ commit }, { id, tagData }) {
    try {
      const response = await this.$api.tagLibrary.updateTag(id, tagData)
      commit("UPDATE_TAG", response.data)
      return response
    } catch (error) {
      console.error("更新标签失败:", error)
      throw error
    }
  },

  async deleteTag({ commit }, tagId) {
    try {
      await this.$api.tagLibrary.deleteTag(tagId)
      commit("DELETE_TAG", tagId)
    } catch (error) {
      console.error("删除标签失败:", error)
      throw error
    }
  },

  async createCategory({ commit }, categoryData) {
    try {
      const response = await this.$api.tagLibrary.createCategory(categoryData)
      commit("ADD_CATEGORY", response.data)
      return response
    } catch (error) {
      console.error("创建分类失败:", error)
      throw error
    }
  },

  async updateCategory({ commit }, { id, categoryData }) {
    try {
      const response = await this.$api.tagLibrary.updateCategory(id, categoryData)
      commit("UPDATE_CATEGORY", response.data)
      return response
    } catch (error) {
      console.error("更新分类失败:", error)
      throw error
    }
  },

  async deleteCategory({ commit }, categoryId) {
    try {
      await this.$api.tagLibrary.deleteCategory(categoryId)
      commit("DELETE_CATEGORY", categoryId)
    } catch (error) {
      console.error("删除分类失败:", error)
      throw error
    }
  },
}

const getters = {
  tagList: (state) => state.tagList,
  categoryList: (state) => state.categoryList,
  currentTag: (state) => state.currentTag,
  loading: (state) => state.loading,
  pagination: (state) => state.pagination,

  getTagsByCategory: (state) => (categoryId) => {
    return state.tagList.filter((tag) => tag.categoryId === categoryId)
  },

  getActiveTags: (state) => {
    return state.tagList.filter((tag) => tag.active)
  },

  getTagByCode: (state) => (code) => {
    return state.tagList.find((tag) => tag.code === code)
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}
