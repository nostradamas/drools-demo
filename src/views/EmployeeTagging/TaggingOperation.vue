<template>
  <div class="app-container page-container">
    <el-card class="card-container">
      <div slot="header" class="card-header">
        <span>员工标签操作</span>
        <div class="header-actions">
          <el-input
            v-model="listQuery.name"
            placeholder="员工姓名"
            style="width: 200px;"
            class="filter-item"
            @keyup.enter.native="handleFilter"
          />
          <el-select
            v-model="listQuery.department"
            placeholder="部门"
            clearable
            style="width: 150px"
            class="filter-item"
          >
            <el-option label="技术部" value="技术部" />
            <el-option label="产品部" value="产品部" />
            <el-option label="市场部" value="市场部" />
            <el-option label="人事部" value="人事部" />
          </el-select>
          <el-button
            class="filter-item"
            type="primary"
            icon="el-icon-search"
            @click="handleFilter"
          >
            搜索
          </el-button>
          <el-button
            class="filter-item"
            style="margin-left: 10px;"
            type="success"
            icon="el-icon-plus"
            @click="handleBatchTag"
          >
            批量打标签
          </el-button>
          <el-button
            class="filter-item"
            type="warning"
            icon="el-icon-refresh"
            @click="handleAutoTag"
          >
            自动打标签
          </el-button>
        </div>
      </div>
    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
      />
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="姓名">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="部门" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.department }}</span>
        </template>
      </el-table-column>
      <el-table-column label="职位" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.position }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前标签" width="300">
        <template slot-scope="scope">
          <el-tag
            v-for="tag in scope.row.tags"
            :key="tag.id"
            :type="getTagType(tag.category)"
            size="mini"
            style="margin: 2px;"
            closable
            @close="handleRemoveTag(scope.row, tag)"
          >
            {{ tag.name }}
          </el-tag>
          <el-button
            v-if="!scope.row.tags || scope.row.tags.length === 0"
            type="text"
            size="mini"
            @click="handleAddTag(scope.row)"
          >
            添加标签
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleAddTag(row)">
            打标签
          </el-button>
          <el-button type="info" size="mini" @click="handleViewProfile(row)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    </el-card>
    <!-- 打标签对话框 -->
    <el-dialog title="员工打标签" :visible.sync="tagDialogVisible" width="60%">
      <div v-if="currentEmployee">
        <h3>{{ currentEmployee.name }} - {{ currentEmployee.position }}</h3>
        <el-divider />
        
        <div style="margin-bottom: 20px;">
          <h4>选择标签分类：</h4>
          <el-radio-group v-model="selectedCategory" @change="handleCategoryChange">
            <el-radio-button
              v-for="category in tagCategories"
              :key="category.id"
              :label="category.id"
            >
              {{ category.name }}
            </el-radio-button>
          </el-radio-group>
        </div>

        <div style="margin-bottom: 20px;">
          <h4>可选标签：</h4>
          <el-checkbox-group v-model="selectedTags">
            <el-checkbox
              v-for="tag in availableTags"
              :key="tag.id"
              :label="tag.id"
              style="margin: 5px;"
            >
              {{ tag.name }}
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <div>
          <h4>当前标签：</h4>
          <el-tag
            v-for="tag in currentEmployee.tags"
            :key="tag.id"
            :type="getTagType(tag.category)"
            style="margin: 2px;"
            closable
            @close="handleRemoveTag(currentEmployee, tag)"
          >
            {{ tag.name }}
          </el-tag>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="tagDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="handleConfirmTag">
          确认打标签
        </el-button>
      </div>
    </el-dialog>

    <!-- 批量打标签对话框 -->
    <el-dialog title="批量打标签" :visible.sync="batchTagDialogVisible" width="50%">
      <div>
        <p>已选择 {{ selectedEmployees.length }} 名员工</p>
        <el-divider />
        
        <div style="margin-bottom: 20px;">
          <h4>选择要添加的标签：</h4>
          <el-select
            v-model="batchSelectedTags"
            multiple
            placeholder="请选择标签"
            style="width: 100%;"
          >
            <el-option
              v-for="tag in allTags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
            />
          </el-select>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchTagDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="handleConfirmBatchTag">
          确认批量打标签
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getEmployeeList, getTagCategories, getTagsByCategory, addEmployeeTag, removeEmployeeTag, batchTagEmployees, autoTagEmployee } from '@/api'

export default {
  name: 'TaggingOperation',
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        department: undefined
      },
      selectedEmployees: [],
      currentEmployee: null,
      tagCategories: [],
      availableTags: [],
      allTags: [],
      selectedCategory: null,
      selectedTags: [],
      batchSelectedTags: [],
      tagDialogVisible: false,
      batchTagDialogVisible: false
    }
  },
  created() {
    this.getList()
    this.getTagCategories()
    this.getAllTags()
  },
  methods: {
    getList() {
      this.listLoading = true
      getEmployeeList(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    getTagCategories() {
      getTagCategories().then(response => {
        this.tagCategories = response.data
        if (this.tagCategories.length > 0) {
          this.selectedCategory = this.tagCategories[0].id
          this.handleCategoryChange(this.selectedCategory)
        }
      })
    },
    getAllTags() {
      // 获取所有标签用于批量操作
      getTagsByCategory().then(response => {
        this.allTags = response.data
      })
    },
    handleCategoryChange(categoryId) {
      getTagsByCategory(categoryId).then(response => {
        this.availableTags = response.data
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSelectionChange(val) {
      this.selectedEmployees = val
    },
    handleAddTag(row) {
      this.currentEmployee = row
      this.selectedTags = row.tags ? row.tags.map(tag => tag.id) : []
      this.tagDialogVisible = true
    },
    handleRemoveTag(employee, tag) {
      this.$confirm('确定要移除这个标签吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeEmployeeTag(employee.id, tag.id).then(() => {
          // 更新本地数据
          const index = employee.tags.findIndex(t => t.id === tag.id)
          if (index !== -1) {
            employee.tags.splice(index, 1)
          }
          this.$message({
            type: 'success',
            message: '标签移除成功!'
          })
        })
      })
    },
    handleConfirmTag() {
      if (this.selectedTags.length === 0) {
        this.$message.warning('请选择至少一个标签')
        return
      }
      
      const promises = this.selectedTags.map(tagId => {
        return addEmployeeTag(this.currentEmployee.id, tagId)
      })
      
      Promise.all(promises).then(() => {
        this.$message({
          type: 'success',
          message: '标签添加成功!'
        })
        this.tagDialogVisible = false
        this.getList()
      }).catch(() => {
        this.$message.error('标签添加失败')
      })
    },
    handleBatchTag() {
      if (this.selectedEmployees.length === 0) {
        this.$message.warning('请先选择员工')
        return
      }
      this.batchTagDialogVisible = true
    },
    handleConfirmBatchTag() {
      if (this.batchSelectedTags.length === 0) {
        this.$message.warning('请选择至少一个标签')
        return
      }
      
      const employeeIds = this.selectedEmployees.map(emp => emp.id)
      batchTagEmployees(employeeIds, this.batchSelectedTags).then(() => {
        this.$message({
          type: 'success',
          message: '批量打标签成功!'
        })
        this.batchTagDialogVisible = false
        this.getList()
      }).catch(() => {
        this.$message.error('批量打标签失败')
      })
    },
    handleAutoTag() {
      if (this.selectedEmployees.length === 0) {
        this.$message.warning('请先选择员工')
        return
      }
      
      this.$confirm('确定要为选中的员工自动打标签吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const promises = this.selectedEmployees.map(emp => autoTagEmployee(emp.id))
        Promise.all(promises).then(() => {
          this.$message({
            type: 'success',
            message: '自动打标签完成!'
          })
          this.getList()
        })
      })
    },
    handleViewProfile(row) {
      this.$router.push(`/employee-tagging/profile/${row.id}`)
    },
    getTagType(category) {
      const typeMap = {
        'SKILL': 'primary',
        'EXPERIENCE': 'success',
        'EDUCATION': 'warning',
        'COMPREHENSIVE': 'info'
      }
      return typeMap[category] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/common.scss';
.app-container {
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
.filter-container {
  margin-bottom: 20px;
}

.filter-item {
  margin-right: 10px;
}
</style>
