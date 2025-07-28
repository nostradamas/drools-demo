import axios from "axios"
import { Message } from "element-ui"

// 创建axios实例
const service = axios.create({
  baseURL: "http://localhost:8080/api", // 后端API地址
  timeout: 10000, // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    console.log(error)
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data
    return res
  },
  (error) => {
    console.log("err" + error)
    Message({
      message: error.message,
      type: "error",
      duration: 5 * 1000,
    })
    return Promise.reject(error)
  },
)

// API接口定义
const api = {
  // 标签库管理
  tagLibrary: {
    // 获取所有标签
    getAllTags: () => service.get("/tag-library-management/tags"),
    // 根据编码获取标签
    getTagByCode: (code) => service.get(`/tag-library-management/tags/${code}`),
    // 根据维度获取标签
    getTagsByDimension: (dimension) => service.get(`/tag-library-management/tags/dimension/${dimension}`),
    // 根据分类获取标签
    getTagsByCategory: (category) => service.get(`/tag-library-management/tags/category/${category}`),
    // 搜索标签
    searchTags: (keyword) => service.get("/tag-library-management/tags/search", { params: { keyword } }),
    // 添加标签
    addTag: (data) => service.post("/tag-library-management/tags", data),
    // 更新标签
    updateTag: (code, data) => service.put(`/tag-library-management/tags/${code}`, data),
    // 删除标签
    deleteTag: (code) => service.delete(`/tag-library-management/tags/${code}`),
    // 批量操作标签
    batchOperateTags: (data) => service.post("/tag-library-management/tags/batch", data),
    // 获取标签统计
    getTagStatistics: () => service.get("/tag-library-management/statistics"),
    // 获取所有分类
    getAllCategories: () => service.get("/tag-library-management/categories"),
    // 导出标签库
    exportTagLibrary: () => service.get("/tag-library-management/export"),
    // 导入标签库
    importTagLibrary: (data) => service.post("/tag-library-management/import", data),
  },

  // 规则管理
  ruleManagement: {
    // 获取所有规则
    getAllRules: () => service.get("/rule-management/rules"),
    // 根据编码获取规则
    getRule: (ruleCode) => service.get(`/rule-management/rules/${ruleCode}`),
    // 根据标签获取规则
    getRulesByTag: (tagCode) => service.get(`/rule-management/rules/tag/${tagCode}`),
    // 添加规则
    addRule: (data) => service.post("/rule-management/rules", data),
    // 更新规则
    updateRule: (ruleCode, data) => service.put(`/rule-management/rules/${ruleCode}`, data),
    // 删除规则
    deleteRule: (ruleCode) => service.delete(`/rule-management/rules/${ruleCode}`),
    // 启用/禁用规则
    toggleRule: (ruleCode, data) => service.post(`/rule-management/rules/${ruleCode}/toggle`, data),
    // 测试规则
    testRule: (ruleCode, data) => service.post(`/rule-management/rules/${ruleCode}/test`, data),
    // 预览规则
    previewRule: (data) => service.post("/rule-management/rules/preview", data),
    // 验证规则
    validateRule: (data) => service.post("/rule-management/rules/validate", data),
    // 获取所有模板
    getAllTemplates: () => service.get("/rule-management/templates"),
    // 获取模板参数
    getTemplateParameters: (template) => service.get(`/rule-management/templates/${template}/parameters`),
    // 获取规则统计
    getRuleStatistics: () => service.get("/rule-management/statistics"),
    // 批量操作规则
    batchOperateRules: (data) => service.post("/rule-management/rules/batch", data),
    // 初始化默认规则
    initializeDefaultRules: () => service.post("/rule-management/rules/initialize"),
    // 导出规则
    exportRules: () => service.get("/rule-management/export"),
    // 导入规则
    importRules: (data) => service.post("/rule-management/import", data),
  },

  // 员工标签
  employeeTagging: {
    // 使用动态规则打标签
    tagEmployeeWithDynamicRules: (data) => service.post("/integrated-tagging/tag-employee-dynamic", data),
    // 批量打标签
    batchTagWithDynamicRules: (data) => service.post("/integrated-tagging/batch-tag-dynamic", data),
    // 完整演示
    runFullDemo: () => service.get("/integrated-tagging/full-demo"),
    // 系统状态检查
    getSystemStatus: () => service.get("/integrated-tagging/system-status"),
    // 手动添加标签
    addManualTag: (employeeId, data) => service.post(`/enhanced-tagging/employee/${employeeId}/add-tag`, data),
    // 获取员工标签统计
    getEmployeeTagStatistics: (employeeId) => service.get(`/enhanced-tagging/employee/${employeeId}/tag-stats`),
    // 按维度获取员工标签
    getEmployeeTagsByDimension: (employeeId) =>
      service.get(`/enhanced-tagging/employee/${employeeId}/tags-by-dimension`),
    // 获取培训推荐
    getTrainingRecommendations: (employeeId) =>
      service.get(`/enhanced-tagging/employee/${employeeId}/training-recommendations`),
  },
}

export default api
