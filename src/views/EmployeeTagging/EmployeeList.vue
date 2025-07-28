<template>
  <div class="employee-list page-container">
    <el-card class="card-container">
      <div slot="header" class="card-header">
        <span>员工列表</span>
        <div class="header-actions">
            <el-input v-model="searchForm.name" placeholder="请输入姓名"></el-input>
            <el-select v-model="searchForm.department" placeholder="请选择部门">
              <el-option label="技术部" value="技术部"></el-option>
              <el-option label="市场部" value="市场部"></el-option>
              <el-option label="人事部" value="人事部"></el-option>
            </el-select>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
            <el-button type="primary" @click="showTaggingDialog">批量打标签</el-button>
            <el-button type="success" @click="runDemo">运行演示</el-button>
        </div>
      </div>

      <el-table
        :data="employeeList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="employeeNo" label="员工编号" width="120"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="age" label="年龄" width="80"></el-table-column>
        <el-table-column prop="department" label="部门" width="120"></el-table-column>
        <el-table-column prop="position" label="职位" width="150"></el-table-column>
        <el-table-column label="工作经验" width="100">
          <template slot-scope="scope">
            {{ scope.row.experienceInfo?.totalWorkYears || 0 }}年
          </template>
        </el-table-column>
        <el-table-column label="学历" width="100">
          <template slot-scope="scope">
            {{ scope.row.experienceInfo?.education || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="绩效评分" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPerformanceType(scope.row.evaluationInfo?.currentPerformanceScore)">
              {{ scope.row.evaluationInfo?.currentPerformanceScore || 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前标签" min-width="200">
          <template slot-scope="scope">
            <div class="tag-container">
              <el-tag
                v-for="tag in getActiveTags(scope.row.currentTags)"
                :key="tag.tagCode"
                size="small"
                :type="getDimensionType(tag.dimension)"
                style="margin-right: 5px; margin-bottom: 5px;"
              >
                {{ tag.tagName }}
              </el-tag>
              <span v-if="!getActiveTags(scope.row.currentTags).length" class="no-tags">暂无标签</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewProfile(scope.row)">查看档案</el-button>
            <el-button size="mini" type="primary" @click="tagSingleEmployee(scope.row)">打标签</el-button>
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

    <!-- 批量打标签对话框 -->
    <el-dialog
      title="批量打标签"
      :visible.sync="taggingDialogVisible"
      width="800px"
      @close="resetTaggingForm"
    >
      <div class="tagging-content">
        <div class="selected-employees">
          <h4>选中的员工 ({{ selectedEmployees.length }}人)</h4>
          <div class="employee-chips">
            <el-tag
              v-for="employee in selectedEmployees"
              :key="employee.id"
              closable
              @close="removeSelectedEmployee(employee)"
              style="margin-right: 10px; margin-bottom: 10px;"
            >
              {{ employee.name }} - {{ employee.position }}
            </el-tag>
          </div>
        </div>
        
        <el-divider></el-divider>
        
        <div class="tagging-options">
          <el-radio-group v-model="taggingMode">
            <el-radio label="auto">自动打标签（使用规则引擎）</el-radio>
            <el-radio label="manual">手动添加标签</el-radio>
          </el-radio-group>
          
          <div v-if="taggingMode === 'manual'" class="manual-tagging" style="margin-top: 20px;">
            <el-select
              v-model="selectedTagCodes"
              multiple
              placeholder="选择要添加的标签"
              style="width: 100%;"
            >
              <el-option
                v-for="tag in availableTags"
                :key="tag.code"
                :label="tag.name"
                :value="tag.code"
              >
                <span style="float: left">{{ tag.name }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ tag.description }}</span>
              </el-option>
            </el-select>
            
            <el-input
              v-model="manualTagReason"
              type="textarea"
              placeholder="请输入添加标签的原因"
              :rows="3"
              style="margin-top: 15px;"
            ></el-input>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="taggingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="executeTagging" :loading="taggingLoading">
          {{ taggingMode === 'auto' ? '自动打标签' : '添加标签' }}
        </el-button>
      </div>
    </el-dialog>

    <!-- 标签结果对话框 -->
    <el-dialog
      title="打标签结果"
      :visible.sync="resultDialogVisible"
      width="1000px"
    >
      <div class="tagging-results">
        <el-alert
          :title="`成功为 ${taggingResults.length} 名员工打标签`"
          type="success"
          :closable="false"
          style="margin-bottom: 20px;"
        ></el-alert>
        
        <el-collapse v-model="activeResultPanels">
          <el-collapse-item
            v-for="result in taggingResults"
            :key="result.id"
            :title="`${result.name} - ${result.position}`"
            :name="result.id"
          >
            <div class="employee-result">
              <div class="result-section">
                <h4>新增标签</h4>
                <div class="new-tags">
                  <el-tag
                    v-for="tag in getNewTags(result.currentTags)"
                    :key="tag.tagCode"
                    :type="getDimensionType(tag.dimension)"
                    style="margin-right: 8px; margin-bottom: 8px;"
                  >
                    {{ tag.tagName }}
                    <el-tooltip :content="tag.reason" placement="top">
                      <i class="el-icon-info" style="margin-left: 5px;"></i>
                    </el-tooltip>
                  </el-tag>
                </div>
              </div>
              
              <div class="result-section">
                <h4>标签统计</h4>
                <el-descriptions :column="3" size="small" border>
                  <el-descriptions-item label="能力标签">
                    {{ getTagCountByDimension(result.currentTags, 'ABILITY') }}
                  </el-descriptions-item>
                  <el-descriptions-item label="履历标签">
                    {{ getTagCountByDimension(result.currentTags, 'EXPERIENCE') }}
                  </el-descriptions-item>
                  <el-descriptions-item label="考评标签">
                    {{ getTagCountByDimension(result.currentTags, 'EVALUATION') }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'EmployeeList',
  data() {
    return {
      searchForm: {
        name: '',
        department: ''
      },
      employeeList: [],
      selectedEmployees: [],
      loading: false,
      currentPage: 1,
      pageSize: 20,
      total: 0,
      
      taggingDialogVisible: false,
      taggingMode: 'auto',
      selectedTagCodes: [],
      manualTagReason: '',
      availableTags: [],
      taggingLoading: false,
      
      resultDialogVisible: false,
      taggingResults: [],
      activeResultPanels: []
    }
  },
  
  mounted() {
    this.loadEmployeeList()
    this.loadAvailableTags()
  },
  
  methods: {
    async loadEmployeeList() {
      this.loading = true
      try {
        // 使用演示数据
        const response = await this.$api.employeeTagging.runFullDemo()
        this.employeeList = response.taggedEmployees || []
        this.total = this.employeeList.length
      } catch (error) {
        console.error('加载员工列表失败:', error)
        this.$message.error('加载员工列表失败')
      } finally {
        this.loading = false
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
    
    handleSelectionChange(selection) {
      this.selectedEmployees = selection
    },
    
    showTaggingDialog() {
      if (this.selectedEmployees.length === 0) {
        this.$message.warning('请选择要打标签的员工')
        return
      }
      this.taggingDialogVisible = true
    },
    
    async tagSingleEmployee(employee) {
      try {
        this.taggingLoading = true
        const response = await this.$api.employeeTagging.tagEmployeeWithDynamicRules(employee)
        
        this.taggingResults = [response]
        this.activeResultPanels = [response.id]
        this.resultDialogVisible = true
        
        // 更新列表中的员工数据
        const index = this.employeeList.findIndex(emp => emp.id === employee.id)
        if (index !== -1) {
          this.$set(this.employeeList, index, response)
        }
        
        this.$message.success('打标签成功')
      } catch (error) {
        console.error('打标签失败:', error)
        this.$message.error('打标签失败')
      } finally {
        this.taggingLoading = false
      }
    },
    
    async executeTagging() {
      if (this.selectedEmployees.length === 0) {
        this.$message.warning('请选择要打标签的员工')
        return
      }
      
      if (this.taggingMode === 'manual' && this.selectedTagCodes.length === 0) {
        this.$message.warning('请选择要添加的标签')
        return
      }
      
      try {
        this.taggingLoading = true
        
        if (this.taggingMode === 'auto') {
          // 自动打标签
          const response = await this.$api.employeeTagging.batchTagWithDynamicRules(this.selectedEmployees)
          this.taggingResults = response || []
        } else {
          // 手动添加标签
          const results = []
          for (const employee of this.selectedEmployees) {
            for (const tagCode of this.selectedTagCodes) {
              await this.$api.employeeTagging.addManualTag(employee.id, {
                tagCode,
                reason: this.manualTagReason || '手动添加'
              })
            }
            results.push(employee)
          }
          this.taggingResults = results
        }
        
        this.activeResultPanels = this.taggingResults.map(result => result.id)
        this.taggingDialogVisible = false
        this.resultDialogVisible = true
        
        // 刷新员工列表
        this.loadEmployeeList()
        
        this.$message.success(`成功为 ${this.taggingResults.length} 名员工打标签`)
      } catch (error) {
        console.error('批量打标签失败:', error)
        this.$message.error('批量打标签失败')
      } finally {
        this.taggingLoading = false
      }
    },
    
    async runDemo() {
      try {
        this.loading = true
        const response = await this.$api.employeeTagging.runFullDemo()
        
        this.employeeList = response.taggedEmployees || []
        this.total = this.employeeList.length
        
        this.$message.success('演示运行成功，已为所有员工重新打标签')
      } catch (error) {
        console.error('运行演示失败:', error)
        this.$message.error('运行演示失败')
      } finally {
        this.loading = false
      }
    },
    
    viewProfile(employee) {
      this.$router.push(`/employee-tagging/profile/${employee.id}`)
    },
    
    removeSelectedEmployee(employee) {
      const index = this.selectedEmployees.findIndex(emp => emp.id === employee.id)
      if (index !== -1) {
        this.selectedEmployees.splice(index, 1)
      }
    },
    
    resetTaggingForm() {
      this.taggingMode = 'auto'
      this.selectedTagCodes = []
      this.manualTagReason = ''
    },
    
    getActiveTags(tags) {
      if (!tags) return []
      return tags.filter(tag => tag.active && !this.isTagExpired(tag))
    },
    
    getNewTags(tags) {
      if (!tags) return []
      return tags.filter(tag => tag.active && tag.source === 'RULE_ENGINE')
    },
    
    getTagCountByDimension(tags, dimension) {
      if (!tags) return 0
      return tags.filter(tag => tag.active && tag.dimension === dimension).length
    },
    
    isTagExpired(tag) {
      if (!tag.expireTime) return false
      return new Date(tag.expireTime) < new Date()
    },
    
    getPerformanceType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return 'primary'
      if (score >= 70) return 'warning'
      return 'danger'
    },
    
    getDimensionType(dimension) {
      const typeMap = {
        'ABILITY': 'primary',
        'EXPERIENCE': 'success',
        'EVALUATION': 'warning'
      }
      return typeMap[dimension] || 'info'
    },
    
    handleSizeChange(val) {
      this.pageSize = val
      this.loadEmployeeList()
    },
    
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadEmployeeList()
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/common.scss';

.employee-list {
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
