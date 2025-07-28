<template>
  <div class="rule-list page-container">
    <el-card class="search-table-wrapper">
      <div slot="header" class="card-header">
        <span>规则列表</span>
        <div class="header-actions">
          <el-button type="primary" @click="showAddDialog">添加规则</el-button>
          <el-button type="success" @click="initializeDefaultRules">初始化默认规则</el-button>
          <el-button type="warning" @click="handleBatchOperation('activate')" :disabled="selectedRules.length === 0">批量启用</el-button>
          <el-button type="info" @click="handleBatchOperation('deactivate')" :disabled="selectedRules.length === 0">批量禁用</el-button>
          <el-button type="danger" @click="handleBatchOperation('delete')" :disabled="selectedRules.length === 0">批量删除</el-button>
        </div>
      </div>

      <div class="search-container">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="规则名称">
            <el-input v-model="searchForm.ruleName" placeholder="请输入规则名称"></el-input>
          </el-form-item>
          <el-form-item label="规则模板">
            <el-select v-model="searchForm.ruleTemplate" placeholder="请选择规则模板">
              <el-option 
                v-for="template in templates"
                :key="template"
                :label="getTemplateName(template)"
                :value="template"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        :data="ruleList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="ruleCode" label="规则编码" width="180"></el-table-column>
        <el-table-column prop="ruleName" label="规则名称" width="200"></el-table-column>
        <el-table-column prop="tagCode" label="关联标签" width="150">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.tagCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ruleTemplate" label="规则模板" width="150">
          <template slot-scope="scope">
            <el-tag type="primary" size="small">{{ getTemplateName(scope.row.ruleTemplate) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" sortable></el-table-column>
        <el-table-column prop="executionCount" label="执行次数" width="100" sortable></el-table-column>
        <el-table-column prop="active" label="状态" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.active"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="testRule(scope.row)">测试</el-button>
            <el-button size="mini" type="info" @click="previewRule(scope.row)">预览</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right;"
      ></el-pagination>
    </el-card>

    <!-- 添加/编辑规则对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="resetForm"
    >
      <el-form
        :model="ruleForm"
        :rules="ruleRules"
        ref="ruleForm"
        label-width="120px"
      >
        <el-form-item label="规则编码" prop="ruleCode">
          <el-input v-model="ruleForm.ruleCode" :disabled="isEdit" placeholder="请输入规则编码"></el-input>
        </el-form-item>
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="ruleForm.ruleName" placeholder="请输入规则名称"></el-input>
        </el-form-item>
        <el-form-item label="关联标签" prop="tagCode">
          <el-select v-model="ruleForm.tagCode" placeholder="请选择关联标签" style="width: 100%;">
            <el-option
              v-for="tag in availableTags"
              :key="tag.code"
              :label="tag.name"
              :value="tag.code"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="规则模板" prop="ruleTemplate">
          <el-select v-model="ruleForm.ruleTemplate" placeholder="请选择规则模板" style="width: 100%;" @change="onTemplateChange">
            <el-option
              v-for="template in templates"
              :key="template"
              :label="getTemplateName(template)"
              :value="template"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="规则条件" prop="conditions" v-if="ruleForm.ruleTemplate && ruleForm.ruleTemplate !== 'CUSTOM'">
          <div class="conditions-editor">
            <div v-if="ruleForm.ruleTemplate === 'EXPERIENCE_BASED'" class="condition-group">
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="最小年限">
                    <el-input-number v-model="conditionsForm.minYears" :min="0" style="width: 100%;"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="最大年限">
                    <el-input-number v-model="conditionsForm.maxYears" :min="0" style="width: 100%;"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="操作符">
                    <el-select v-model="conditionsForm.operator" style="width: 100%;">
                      <el-option label=">=" value=">="></el-option>
                      <el-option label=">" value=">"></el-option>
                      <el-option label="=" value="=="></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            
            <div v-if="ruleForm.ruleTemplate === 'SKILL_BASED'" class="condition-group">
              <el-form-item label="必需技能">
                <el-select v-model="conditionsForm.requiredSkills" multiple placeholder="选择必需技能" style="width: 100%;">
                  <el-option v-for="skill in commonSkills" :key="skill" :label="skill" :value="skill"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="匹配类型">
                <el-radio-group v-model="conditionsForm.matchType">
                  <el-radio label="ANY">任意匹配</el-radio>
                  <el-radio label="ALL">全部匹配</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
            
            <div v-if="ruleForm.ruleTemplate === 'PERFORMANCE_BASED'" class="condition-group">
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="最低分数">
                    <el-input-number v-model="conditionsForm.minScore" :min="0" :max="100" style="width: 100%;"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="最高分数">
                    <el-input-number v-model="conditionsForm.maxScore" :min="0" :max="100" style="width: 100%;"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="操作符">
                    <el-select v-model="conditionsForm.operator" style="width: 100%;">
                      <el-option label=">=" value=">="></el-option>
                      <el-option label=">" value=">"></el-option>
                      <el-option label="=" value="=="></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            
            <div v-if="ruleForm.ruleTemplate === 'EDUCATION_BASED'" class="condition-group">
              <el-form-item label="学历要求">
                <el-checkbox-group v-model="conditionsForm.educationLevels">
                  <el-checkbox label="本科">本科</el-checkbox>
                  <el-checkbox label="硕士">硕士</el-checkbox>
                  <el-checkbox label="博士">博士</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>
            
            <div v-if="ruleForm.ruleTemplate === 'LEADERSHIP_BASED'" class="condition-group">
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="最低领导力评分">
                    <el-input-number v-model="conditionsForm.minLeadershipScore" :min="0" :max="100" style="width: 100%;"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="最少管理年限">
                    <el-input-number v-model="conditionsForm.minManagementYears" :min="0" style="width: 100%;"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="最小团队规模">
                    <el-input-number v-model="conditionsForm.minTeamSize" :min="0" style="width: 100%;"></el-input-number>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="规则内容" prop="ruleContent" v-if="ruleForm.ruleTemplate === 'CUSTOM'">
          <el-input
            type="textarea"
            v-model="ruleForm.ruleContent"
            placeholder="请输入自定义规则内容（DRL格式）"
            :rows="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            type="textarea"
            v-model="ruleForm.description"
            placeholder="请输入规则描述"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="ruleForm.priority" :min="1" :max="10" style="width: 100%;"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="previewCurrentRule" :loading="previewing">预览规则</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </div>
    </el-dialog>

    <!-- 规则预览对话框 -->
    <el-dialog
      title="规则预览"
      :visible.sync="previewDialogVisible"
      width="800px"
    >
      <div class="rule-preview">
        <h4>生成的规则内容：</h4>
        <pre class="rule-content">{{ previewContent }}</pre>
        <div class="validation-result">
          <el-tag :type="previewValid ? 'success' : 'danger'">
            {{ previewValid ? '规则语法正确' : '规则语法错误' }}
          </el-tag>
        </div>
      </div>
    </el-dialog>

    <!-- 规则测试对话框 -->
    <el-dialog
      title="规则测试"
      :visible.sync="testDialogVisible"
      width="600px"
    >
      <div class="rule-test">
        <el-form :model="testForm" label-width="100px">
          <el-form-item label="选择员工">
            <el-select v-model="testForm.employeeId" placeholder="选择测试员工" style="width: 100%;">
              <el-option
                v-for="employee in testEmployees"
                :key="employee.id"
                :label="`${employee.name} - ${employee.position}`"
                :value="employee.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        
        <div v-if="testResult" class="test-result">
          <h4>测试结果：</h4>
          <el-alert
            :title="testResult.message"
            :type="testResult.success ? 'success' : 'error'"
            :closable="false"
          ></el-alert>
          <div v-if="testResult.success" class="result-details">
            <p><strong>触发规则数：</strong>{{ testResult.firedRules }}</p>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="executeTest" :loading="testing">执行测试</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'RuleList',
  data() {
    return {
      searchForm: {
        ruleName: '',
        ruleTemplate: ''
      },
      ruleList: [],
      selectedRules: [],
      loading: false,
      currentPage: 1,
      pageSize: 20,
      total: 0,
      
      dialogVisible: false,
      dialogTitle: '添加规则',
      isEdit: false,
      submitting: false,
      
      ruleForm: {
        ruleCode: '',
        ruleName: '',
        tagCode: '',
        ruleTemplate: '',
        conditions: '',
        description: '',
        priority: 5,
        ruleContent: '',
        createdBy: 'admin'
      },
      
      conditionsForm: {
        minYears: 0,
        maxYears: 50,
        operator: '>=',
        requiredSkills: [],
        matchType: 'ANY',
        minScore: 0,
        maxScore: 100,
        educationLevels: [],
        minLeadershipScore: 70,
        minManagementYears: 1,
        minTeamSize: 3
      },
      
      ruleRules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        tagCode: [
          { required: true, message: '请选择关联标签', trigger: 'change' }
        ],
        ruleTemplate: [
          { required: true, message: '请选择规则模板', trigger: 'change' }
        ]
      },
      
      templates: [],
      availableTags: [],
      commonSkills: ['Java', 'Python', 'JavaScript', 'React', 'Vue', 'Spring', 'MySQL', '数据库', '微服务', '分布式', 'Redis', 'CSS', 'HTML'],
      
      previewDialogVisible: false,
      previewContent: '',
      previewValid: false,
      previewing: false,
      
      testDialogVisible: false,
      testForm: {
        employeeId: ''
      },
      testEmployees: [],
      testResult: null,
      testing: false,
      currentTestRule: null
    }
  },
  
  mounted() {
    this.loadRuleList()
    this.loadTemplates()
    this.loadAvailableTags()
    this.loadTestEmployees()
  },
  
  methods: {
    async loadRuleList() {
      this.loading = true
      try {
        const response = await this.$api.ruleManagement.getAllRules()
        this.ruleList = response || []
        this.total = this.ruleList.length
      } catch (error) {
        console.error('加载规则列表失败:', error)
        this.$message.error('加载规则列表失败')
      } finally {
        this.loading = false
      }
    },
    
    async loadTemplates() {
      try {
        const response = await this.$api.ruleManagement.getAllTemplates()
        this.templates = response || []
      } catch (error) {
        console.error('加载模板失败:', error)
      }
    },
    
    async loadAvailableTags() {
      try {
        const response = await this.$api.tagLibrary.getAllTags()
        this.availableTags = response.filter(tag => tag.active) || []
      } catch (error) {
        console.error('加载可用标签失败:', error)
      }
    },
    
    async loadTestEmployees() {
      try {
        const response = null;await this.$api.employeeTagging.runFullDemo()
        this.testEmployees = response.taggedEmployees || []
      } catch (error) {
        console.error('加载测试员工失败:', error)
      }
    },
    
    handleSelectionChange(selection) {
      this.selectedRules = selection
    },
    
    async handleBatchOperation(operation) {
      if (this.selectedRules.length === 0) {
        this.$message.warning('请选择要操作的规则')
        return
      }
      
      const operationText = {
        activate: '启用',
        deactivate: '禁用',
        delete: '删除'
      }
      
      try {
        await this.$confirm(`确定要${operationText[operation]}选中的${this.selectedRules.length}个规则吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const ruleCodes = this.selectedRules.map(rule => rule.ruleCode)
        const response = await this.$api.ruleManagement.batchOperateRules({
          ruleCodes,
          operation
        })
        
        if (response.success) {
          this.$message.success(response.message)
          this.loadRuleList()
        } else {
          this.$message.error(response.message)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量操作失败:', error)
          this.$message.error('批量操作失败')
        }
      }
    },
    
    async handleStatusChange(rule) {
      try {
        const response = await this.$api.ruleManagement.toggleRule(rule.ruleCode, {
          active: rule.active
        })
        
        if (response.success) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error('状态更新失败')
          rule.active = !rule.active // 回滚状态
        }
      } catch (error) {
        console.error('更新状态失败:', error)
        this.$message.error('更新状态失败')
        rule.active = !rule.active // 回滚状态
      }
    },
    
    async initializeDefaultRules() {
      try {
        await this.$confirm('确定要初始化默认规则吗？这将添加一些预定义的规则。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })
        
        const response = await this.$api.ruleManagement.initializeDefaultRules()
        
        if (response.success) {
          this.$message.success('默认规则初始化成功')
          this.loadRuleList()
        } else {
          this.$message.error('默认规则初始化失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('初始化默认规则失败:', error)
          this.$message.error('初始化默认规则失败')
        }
      }
    },
    
    showAddDialog() {
      this.dialogTitle = '添加规则'
      this.isEdit = false
      this.dialogVisible = true
    },
    
    showEditDialog(rule) {
      this.dialogTitle = '编辑规则'
      this.isEdit = true
      this.ruleForm = { ...rule }
      
      // 解析条件
      if (rule.conditions) {
        try {
          this.conditionsForm = { ...this.conditionsForm, ...JSON.parse(rule.conditions) }
        } catch (error) {
          console.error('解析规则条件失败:', error)
        }
      }
      
      this.dialogVisible = true
    },
    
    onTemplateChange() {
      // 重置条件表单
      this.conditionsForm = {
        minYears: 0,
        maxYears: 50,
        operator: '>=',
        requiredSkills: [],
        matchType: 'ANY',
        minScore: 0,
        maxScore: 100,
        educationLevels: [],
        minLeadershipScore: 70,
        minManagementYears: 1,
        minTeamSize: 3
      }
    },
    
    async previewCurrentRule() {
      try {
        this.previewing = true
        
        // 构建条件JSON
        const conditions = JSON.stringify(this.conditionsForm)
        
        const previewData = {
          ruleCode: this.ruleForm.ruleCode,
          ruleName: this.ruleForm.ruleName,
          tagCode: this.ruleForm.tagCode,
          ruleTemplate: this.ruleForm.ruleTemplate,
          conditions: conditions,
          priority: this.ruleForm.priority
        }
        
        const response = await this.$api.ruleManagement.previewRule(previewData)
        
        if (response.success) {
          this.previewContent = response.data.ruleContent
          this.previewValid = response.data.isValid
          this.previewDialogVisible = true
        } else {
          this.$message.error('预览规则失败')
        }
      } catch (error) {
        console.error('预览规则失败:', error)
        this.$message.error('预览规则失败')
      } finally {
        this.previewing = false
      }
    },
    
    previewRule(rule) {
      this.previewContent = rule.ruleContent || '规则内容为空'
      this.previewValid = true
      this.previewDialogVisible = true
    },
    
    testRule(rule) {
      this.currentTestRule = rule
      this.testResult = null
      this.testDialogVisible = true
    },
    
    async executeTest() {
      if (!this.testForm.employeeId) {
        this.$message.warning('请选择要测试的员工')
        return
      }
      this.testing = true
      try {
        const response = await this.$api.ruleManagement.testRule({
          ruleCode: this.currentTestRule.ruleCode,
          employeeId: this.testForm.employeeId
        })
        
        if (response.success) {
          this.testResult = response.data
          this.$message.success('规则测试成功')
        } else {
          this.$message.error('规则测试失败')
          this.testResult = null
        }
      } catch (error) {
        console.error('规则测试失败:', error)
        this.$message.error('规则测试失败')
      } finally {
        this.testing = false
      }
    },
    handleDelete(rule) {
      this.$confirm(`确定要删除规则 "${rule.ruleName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(async () => {
        try {
          const response = await this.$api.ruleManagement.deleteRule(rule.ruleCode)
          if (response.success) {
            this.$message.success('规则删除成功')
            this.loadRuleList()
          } else {
            this.$message.error('规则删除失败')
          }
        } catch (error) {
          console.error('删除规则失败:', error)
          this.$message.error('删除规则失败')
        }
      })
      .catch(() => {});
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      this.loadRuleList()
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.loadRuleList()
    },
    getTemplateName(template) {
      const templateNames = {
        EXPERIENCE_BASED: '基于经验',
        SKILL_BASED: '基于技能',
        PERFORMANCE_BASED: '基于绩效',
        EDUCATION_BASED: '基于学历',      
        LEADERSHIP_BASED: '基于领导力',
        CUSTOM: '自定义'
      }
      return templateNames[template] || '未知模板'
    },
    resetForm() {
      this.ruleForm = {
        ruleCode: '',
        ruleName: '',
        tagCode: '',
        ruleTemplate: '',
        conditions: '',
        description: '', 
        priority: 5,
        ruleContent: '',
        createdBy: 'admin'
      }
      this.conditionsForm = {
        minYears: 0,
        maxYears: 50,
        operator: '>=',
        requiredSkills: [],
        matchType: 'ANY',
        minScore: 0,
        maxScore: 100,
        educationLevels: [],
        minLeadershipScore: 70, 
        minManagementYears: 1,
        minTeamSize: 3
      }
      this.dialogVisible = false
      this.previewDialogVisible = false
      this.testDialogVisible = false
      this.previewContent = ''
      this.previewValid = false
      this.previewing = false
      this.submitting = false
      this.testing = false
      this.testResult = null
      this.currentTestRule = null
    },
    async handleSubmit() {
      this.submitting = true
      try {
        await this.$refs.ruleForm.validate()
        const conditions = JSON.stringify(this.conditionsForm)
        const ruleData = {
          ...this.ruleForm,
          conditions: conditions,
          ruleContent: this.ruleForm.ruleTemplate === 'CUSTOM' ? this.ruleForm.ruleContent : ''
        }   
        let response
        if (this.isEdit) {
          response = await this.$api.ruleManagement.updateRule(ruleData)
        } else {
          response = await this.$api.ruleManagement.addRule(ruleData)
        }
        if (response.success) {
          this.$message.success(this.isEdit ? '规则更新成功' : '规则添加成功')
          this.resetForm()
          this.loadRuleList()
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('提交规则失败:', error)
        this.$message.error('提交规则失败')
      } finally { 
        this.submitting = false
      }
    }
  }   
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/common.scss';

.rule-list {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-actions {
      display: flex;
      align-items: center;
    }
  }
}
</style>
