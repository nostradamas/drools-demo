<template>
  <div class="tag-list page-container">
    <el-card class="card-container">
      <div slot="header" class="card-header">
        <span>标签列表</span>
        <div class="header-actions">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索标签"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="handleSearch"
          >
            <i slot="suffix" class="el-input__icon el-icon-search" @click="handleSearch"></i>
          </el-input>
          <el-button type="primary" @click="showAddDialog">添加标签</el-button>
          <el-button type="success" @click="handleBatchOperation('activate')" :disabled="selectedTags.length === 0">批量启用</el-button>
          <el-button type="warning" @click="handleBatchOperation('deactivate')" :disabled="selectedTags.length === 0">批量禁用</el-button>
          <el-button type="danger" @click="handleBatchOperation('delete')" :disabled="selectedTags.length === 0">批量删除</el-button>
        </div>
      </div>

      <el-table
        :data="tagList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="code" label="标签编码" width="150"></el-table-column>
        <el-table-column prop="name" label="标签名称" width="150">
          <template slot-scope="scope">
            <el-tag :color="scope.row.color" style="color: white;">
              {{ scope.row.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="category" label="分类" width="120">
          <template slot-scope="scope">
            {{ getCategoryName(scope.row.category) }}
          </template>
        </el-table-column>
        <el-table-column prop="dimension" label="维度" width="100">
          <template slot-scope="scope">
            <el-tag :type="getDimensionType(scope.row.dimension)" size="small">
              {{ getDimensionName(scope.row.dimension) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" sortable></el-table-column>
        <el-table-column prop="weight" label="权重" width="80" sortable></el-table-column>
        <el-table-column prop="active" label="状态" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.active"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row)">编辑</el-button>
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

    <!-- 添加/编辑标签对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="resetForm"
    >
      <el-form
        :model="tagForm"
        :rules="tagRules"
        ref="tagForm"
        label-width="100px"
      >
        <el-form-item label="标签编码" prop="code">
          <el-input v-model="tagForm.code" :disabled="isEdit" placeholder="请输入标签编码"></el-input>
        </el-form-item>
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="tagForm.name" placeholder="请输入标签名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            type="textarea"
            v-model="tagForm.description"
            placeholder="请输入标签描述"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="tagForm.category" placeholder="请选择分类" style="width: 100%;">
            <el-option
              v-for="category in categories"
              :key="category"
              :label="getCategoryName(category)"
              :value="category"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="tagForm.priority" :min="1" :max="10" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="权重" prop="weight">
          <el-input-number v-model="tagForm.weight" :min="0.1" :max="1" :step="0.1" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-color-picker v-model="tagForm.color"></el-color-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TagList',
  data() {
    return {
      tagList: [],
      selectedTags: [],
      searchKeyword: '',
      loading: false,
      currentPage: 1,
      pageSize: 20,
      total: 0,
      
      dialogVisible: false,
      dialogTitle: '添加标签',
      isEdit: false,
      submitting: false,
      
      tagForm: {
        code: '',
        name: '',
        description: '',
        category: '',
        priority: 5,
        weight: 0.5,
        color: '#409EFF'
      },
      
      tagRules: {
        code: [
          { required: true, message: '请输入标签编码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入标签名称', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ]
      },
      
      categories: []
    }
  },
  
  mounted() {
    this.loadTagList()
    this.loadCategories()
  },
  
  methods: {
    async loadTagList() {
      this.loading = true
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          keyword: this.searchKeyword.trim() || undefined
        }
        const response = await this.$api.tagLibrary.getTags(params)
        this.tagList = response.data || []
        this.total = response.total
      } catch (error) {
        console.error('加载标签列表失败:', error)
        this.$message.error('加载标签列表失败')
      } finally {
        this.loading = false
      }
    },
    
    async loadCategories() {
      try {
        const response = await this.$api.tagLibrary.getAllCategories()
        this.categories = response || []
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    },
    
    handleSearch() {
      if (this.searchKeyword.trim()) {
        this.searchTags()
      } else {
        this.loadTagList()
      }
    },
    
    async searchTags() {
      this.loading = true
      try {
        const response = await this.$api.tagLibrary.searchTags(this.searchKeyword)
        this.tagList = response || []
        this.total = this.tagList.length
      } catch (error) {
        console.error('搜索标签失败:', error)
        this.$message.error('搜索标签失败')
      } finally {
        this.loading = false
      }
    },
    
    handleSelectionChange(selection) {
      this.selectedTags = selection
    },
    
    async handleBatchOperation(operation) {
      if (this.selectedTags.length === 0) {
        this.$message.warning('请选择要操作的标签')
        return
      }
      
      const operationText = {
        activate: '启用',
        deactivate: '禁用',
        delete: '删除'
      }
      
      try {
        await this.$confirm(`确定要${operationText[operation]}选中的${this.selectedTags.length}个标签吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const tagCodes = this.selectedTags.map(tag => tag.code)
        const response = await this.$api.tagLibrary.batchOperateTags({
          tagCodes,
          operation
        })
        
        if (response.success) {
          this.$message.success(response.message)
          this.loadTagList()
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
    
    async handleStatusChange(tag) {
      try {
        const response = await this.$api.tagLibrary.updateTag(tag.code, {
          active: tag.active
        })
        
        if (response.success) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error('状态更新失败')
          tag.active = !tag.active // 回滚状态
        }
      } catch (error) {
        console.error('更新状态失败:', error)
        this.$message.error('更新状态失败')
        tag.active = !tag.active // 回滚状态
      }
    },
    
    showAddDialog() {
      this.dialogTitle = '添加标签'
      this.isEdit = false
      this.dialogVisible = true
    },
    
    showEditDialog(tag) {
      this.dialogTitle = '编辑标签'
      this.isEdit = true
      this.tagForm = { ...tag }
      this.dialogVisible = true
    },
    
    async handleSubmit() {
      try {
        await this.$refs.tagForm.validate()
        this.submitting = true
        
        let response
        if (this.isEdit) {
          response = await this.$api.tagLibrary.updateTag(this.tagForm.code, this.tagForm)
        } else {
          response = await this.$api.tagLibrary.addTag(this.tagForm)
        }
        
        if (response.success) {
          this.$message.success(response.message)
          this.dialogVisible = false
          this.loadTagList()
        } else {
          this.$message.error(response.message)
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时返回false
          console.error('提交失败:', error)
          this.$message.error('提交失败')
        }
      } finally {
        this.submitting = false
      }
    },
    
    async handleDelete(tag) {
      try {
        await this.$confirm(`确定要删除标签"${tag.name}"吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await this.$api.tagLibrary.deleteTag(tag.code)
        
        if (response.success) {
          this.$message.success('删除成功')
          this.loadTagList()
        } else {
          this.$message.error('删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    
    resetForm() {
      this.tagForm = {
        code: '',
        name: '',
        description: '',
        category: '',
        priority: 5,
        weight: 0.5,
        color: '#409EFF'
      }
      if (this.$refs.tagForm) {
        this.$refs.tagForm.resetFields()
      }
    },
    
    handleSizeChange(val) {
      this.pageSize = val
      this.loadTagList()
    },
    
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadTagList()
    },
    
    getCategoryName(category) {
      const categoryMap = {
        'TECHNICAL_ABILITY': '技术能力',
        'SOFT_SKILL': '软技能',
        'LEADERSHIP': '领导力',
        'COMMUNICATION': '沟通能力',
        'INNOVATION': '创新能力',
        'EXPERIENCE_LEVEL': '经验等级',
        'EDUCATION_BACKGROUND': '教育背景',
        'INDUSTRY_EXPERIENCE': '行业经验',
        'PROJECT_EXPERIENCE': '项目经验',
        'CERTIFICATION': '认证资质',
        'PERFORMANCE_LEVEL': '绩效等级',
        'POTENTIAL_ASSESSMENT': '潜力评估',
        'COMPETENCY_RATING': '胜任力评级',
        'TEAM_COLLABORATION': '团队协作',
        'GROWTH_TREND': '成长趋势'
      }
      return categoryMap[category] || category
    },
    
    getDimensionName(dimension) {
      const dimensionMap = {
        'ABILITY': '能力',
        'EXPERIENCE': '履历',
        'EVALUATION': '考评'
      }
      return dimensionMap[dimension] || dimension
    },
    
    getDimensionType(dimension) {
      const typeMap = {
        'ABILITY': 'primary',
        'EXPERIENCE': 'success',
        'EVALUATION': 'warning'
      }
      return typeMap[dimension] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/common.scss';

.tag-list {
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
