<template>
  <div class="app-container page-container">
    <el-card class="card-container">
      <div slot="header" class="card-header">
        <span>规则模板列表</span>
        <div class="header-actions">
          <el-input
            v-model="listQuery.name"
            placeholder="模板名称"
            style="width: 200px;"
            class="filter-item"
            @keyup.enter.native="handleFilter"
          />
          <el-select
            v-model="listQuery.type"
            placeholder="模板类型"
            clearable
            style="width: 150px"
            class="filter-item"
          >
            <el-option label="技能标签" value="SKILL" />
            <el-option label="经验标签" value="EXPERIENCE" />
            <el-option label="教育标签" value="EDUCATION" />
            <el-option label="综合标签" value="COMPREHENSIVE" />
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
            type="primary"
            icon="el-icon-plus"
            @click="handleCreate"
          >
            添加模板
          </el-button>
        </div>
      </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="模板名称">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTypeColor(scope.row.type)">
            {{ getTypeText(scope.row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" width="300">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用次数" width="110" align="center">
        <template slot-scope="scope">
          <el-tag type="info">{{ scope.row.usageCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="280" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="info" size="mini" @click="handlePreview(row)">
            预览
          </el-button>
          <el-button
            v-if="row.status === 'ACTIVE'"
            size="mini"
            type="warning"
            @click="handleModifyStatus(row, 'INACTIVE')"
          >
            禁用
          </el-button>
          <el-button
            v-else
            size="mini"
            type="success"
            @click="handleModifyStatus(row, 'ACTIVE')"
          >
            启用
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    </el-card>
    <!-- 编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="60%">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="name">
              <el-input v-model="temp.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板类型" prop="type">
              <el-select v-model="temp.type" placeholder="请选择类型">
                <el-option label="技能标签" value="SKILL" />
                <el-option label="经验标签" value="EXPERIENCE" />
                <el-option label="教育标签" value="EDUCATION" />
                <el-option label="综合标签" value="COMPREHENSIVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="temp.description"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="请输入模板描述"
          />
        </el-form-item>
        <el-form-item label="规则模板" prop="template">
          <el-input
            v-model="temp.template"
            :autosize="{ minRows: 10, maxRows: 20}"
            type="textarea"
            placeholder="请输入Drools规则模板"
          />
        </el-form-item>
        <el-form-item label="参数配置" prop="parameters">
          <el-input
            v-model="temp.parameters"
            :autosize="{ minRows: 5, maxRows: 10}"
            type="textarea"
            placeholder="请输入参数配置（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" placeholder="请选择状态">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog title="模板预览" :visible.sync="previewVisible" width="70%">
      <div v-if="previewData">
        <h3>{{ previewData.name }}</h3>
        <p><strong>类型：</strong>{{ getTypeText(previewData.type) }}</p>
        <p><strong>描述：</strong>{{ previewData.description }}</p>
        <h4>规则模板：</h4>
        <pre style="background: #f5f5f5; padding: 15px; border-radius: 4px; overflow-x: auto;">{{ previewData.template }}</pre>
        <h4>参数配置：</h4>
        <pre style="background: #f5f5f5; padding: 15px; border-radius: 4px; overflow-x: auto;">{{ previewData.parameters }}</pre>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getRuleTemplates, createRuleTemplate, updateRuleTemplate, deleteRuleTemplate } from '@/api'

export default {
  name: 'RuleTemplates',
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        type: undefined
      },
      temp: {
        id: undefined,
        name: '',
        type: '',
        description: '',
        template: '',
        parameters: '',
        status: 'ACTIVE'
      },
      dialogFormVisible: false,
      previewVisible: false,
      previewData: null,
      dialogStatus: '',
      textMap: {
        update: '编辑模板',
        create: '创建模板'
      },
      rules: {
        name: [{ required: true, message: '模板名称是必填项', trigger: 'blur' }],
        type: [{ required: true, message: '模板类型是必填项', trigger: 'change' }],
        template: [{ required: true, message: '规则模板是必填项', trigger: 'blur' }],
        status: [{ required: true, message: '状态是必填项', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getRuleTemplates(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        type: '',
        description: '',
        template: '',
        parameters: '',
        status: 'ACTIVE'
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createRuleTemplate(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateRuleTemplate(tempData.id, tempData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该模板, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRuleTemplate(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      })
    },
    handlePreview(row) {
      this.previewData = row
      this.previewVisible = true
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    getTypeText(type) {
      const typeMap = {
        'SKILL': '技能标签',
        'EXPERIENCE': '经验标签',
        'EDUCATION': '教育标签',
        'COMPREHENSIVE': '综合标签'
      }
      return typeMap[type] || type
    },
    getTypeColor(type) {
      const colorMap = {
        'SKILL': 'primary',
        'EXPERIENCE': 'success',
        'EDUCATION': 'warning',
        'COMPREHENSIVE': 'info'
      }
      return colorMap[type] || 'info'
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
</style>
