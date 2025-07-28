<template>
   <div class="tag-cate-list page-container">
    <el-card class="card-container">
       <div slot="header" class="card-header">
        <span>标签分类</span>
        <div class="header-actions">
          <el-input
            v-model="listQuery.name"
            placeholder="分类名称"
            style="width: 200px;"
            class="filter-item"
            @keyup.enter.native="handleFilter"
          />
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
            添加分类
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
        <el-table-column label="分类名称">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column label="描述" width="300">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>
        <el-table-column label="标签数量" width="110" align="center">
          <template slot-scope="scope">
            <el-tag type="info">{{ scope.row.tagCount || 0 }}</el-tag>
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
        <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleUpdate(row)">
              编辑
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


    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="temp.description"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="请输入分类描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择状态">
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
  </div>
</template>

<script>
import { getTagCategories, createTagCategory, updateTagCategory, deleteTagCategory } from '@/api'

export default {
  name: 'TagCategories',
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined
      },
      temp: {
        id: undefined,
        name: '',
        description: '',
        status: 'ACTIVE'
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑分类',
        create: '创建分类'
      },
      rules: {
        name: [{ required: true, message: '分类名称是必填项', trigger: 'blur' }],
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
      getTagCategories(this.listQuery).then(response => {
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
        description: '',
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
          createTagCategory(this.temp).then(() => {
            this.list.unshift(this.temp)
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
          updateTagCategory(tempData.id, tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTagCategory(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        })
      })
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/common.scss';
.tag-cate-list {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-actions {
      display: flex;
      align-items: center;
      gap: 10px;
    }
  }
}
</style>
