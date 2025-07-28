const getters = {
  // 标签库相关
  tagList: (state) => state.tagLibrary.tagList,
  tagCategories: (state) => state.tagLibrary.tagCategories,
  currentTag: (state) => state.tagLibrary.currentTag,
  tagLoading: (state) => state.tagLibrary.loading,

  // 规则管理相关
  ruleList: (state) => state.ruleManagement.ruleList,
  ruleTemplates: (state) => state.ruleManagement.ruleTemplates,
  currentRule: (state) => state.ruleManagement.currentRule,
  testResult: (state) => state.ruleManagement.testResult,
  ruleLoading: (state) => state.ruleManagement.loading,

  // 员工标签相关
  employeeList: (state) => state.employeeTagging.employeeList,
  currentEmployee: (state) => state.employeeTagging.currentEmployee,
  employeeTags: (state) => state.employeeTagging.employeeTags,
  employeeLoading: (state) => state.employeeTagging.loading,
}

export default getters
