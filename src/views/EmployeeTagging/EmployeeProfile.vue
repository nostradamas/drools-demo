<template>
  <div class="employee-profile page-container">
    <el-card v-if="employee" class="card-container">
      <div slot="header" class="profile-header">
        <div class="employee-basic">
          <div class="avatar">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="basic-info">
            <h2>{{ employee.name }}</h2>
            <p>{{ employee.position }} | {{ employee.department }}</p>
            <p>员工编号: {{ employee.employeeNo }}</p>
          </div>
        </div>
        <div class="header-actions">
          <el-button @click="$router.go(-1)">返回</el-button>
          <el-button type="primary" @click="refreshTags">刷新标签</el-button>
          <el-button type="success" @click="showAddTagDialog">添加标签</el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="card-container info-card">
            <div slot="header" class="card-header">基本信息</div>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="年龄">{{ employee.age }}岁</el-descriptions-item>
              <el-descriptions-item label="性别">{{ employee.gender || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="入职时间">{{ formatDate(employee.hireDate) }}</el-descriptions-item>
              <el-descriptions-item label="工作年限">{{ employee.experienceInfo?.totalWorkYears || 0 }}年</el-descriptions-item>
              <el-descriptions-item label="学历">{{ employee.experienceInfo?.education || '-' }}</el-descriptions-item>
              <el-descriptions-item label="毕业院校">{{ employee.experienceInfo?.graduateSchool || '-' }}</el-descriptions-item>
              <el-descriptions-item label="专业">{{ employee.experienceInfo?.major || '-' }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="card-container info-card">
            <div slot="header" class="card-header">能力评估</div>
            <div class="ability-scores">
              <div class="score-item">
                <span class="score-label">领导力</span>
                <el-progress :percentage="employee.abilityInfo?.leadershipScore || 0" :show-text="true"></el-progress>
              </div>
              <div class="score-item">
                <span class="score-label">沟通能力</span>
                <el-progress :percentage="employee.abilityInfo?.communicationScore || 0" :show-text="true"></el-progress>
              </div>
              <div class="score-item">
                <span class="score-label">创新能力</span>
                <el-progress :percentage="employee.abilityInfo?.innovationScore || 0" :show-text="true"></el-progress>
              </div>
              <div class="score-item">
                <span class="score-label">学习能力</span>
                <el-progress :percentage="employee.abilityInfo?.learningAbility || 0" :show-text="true"></el-progress>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="card-container info-card">
            <div slot="header" class="card-header">绩效考评</div>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="当前绩效">
                <el-tag :type="getPerformanceType(employee.evaluationInfo?.currentPerformanceScore)">
                  {{ employee.evaluationInfo?.currentPerformanceScore || 0 }}分
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="绩效等级">{{ employee.evaluationInfo?.performanceLevel || '-' }}</el-descriptions-item>
              <el-descriptions-item label="潜力评级">{{ employee.evaluationInfo?.potentialRating || 0 }}/5</el-descriptions-item>
              <el-descriptions-item label="团队协作">{{ employee.evaluationInfo?.teamCollaborationScore || 0 }}分</el-descriptions-item>
              <el-descriptions-item label="成长趋势">
                <el-tag :type="getTrendType(employee.evaluationInfo?.growthTrend)">
                  {{ employee.evaluationInfo?.growthTrend || '-' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="最近考评">{{ formatDate(employee.evaluationInfo?.lastEvaluationDate) }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <el-card class="card-container info-card">
            <div slot="header" class="card-header">技能清单</div>
            <div class="skills-section">
              <h4>技术技能</h4>
              <div class="skill-tags">
                <el-tag
                  v-for="skill in employee.abilityInfo?.technicalSkills || []"
                  :key="skill"
                  style="margin-right: 8px; margin-bottom: 8px;"
                >
                  {{ skill }}
                  <span v-if="employee.abilityInfo?.skillLevels[skill]" class="skill-level">
                    ({{ employee.abilityInfo.skillLevels[skill] }}/5)
                  </span>
                </el-tag>
              </div>
              
              <h4 style="margin-top: 20px;">软技能</h4>
              <div class="skill-tags">
                <el-tag
                  v-for="skill in employee.abilityInfo?.softSkills || []"
                  :key="skill"
                  type="success"
                  style="margin-right: 8px; margin-bottom: 8px;"
                >
                  {{ skill }}
                </el-tag>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card class="card-container info-card">
            <div slot="header" class="card-header">项目经验</div>
            <div class="projects-section">
              <div
                v-for="project in employee.experienceInfo?.projects || []"
                :key="project.projectName"
                class="project-item"
              >
                <h4>{{ project.projectName }}</h4>
                <p><strong>角色:</strong> {{ project.role }}</p>
                <p><strong>持续时间:</strong> {{ project.durationMonths }}个月</p>
                <p><strong>技术栈:</strong> {{ project.technology }}</p>
                <p><strong>成果:</strong> {{ project.achievement }}</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px;">
        <el-col :span="24">
          <el-card class="card-container tags-card">
            <div slot="header" class="card-header tags-header">
              <span>员工标签</span>
              <div class="tag-stats">
                <el-tag type="info" size="small">总计: {{ getActiveTags().length }}</el-tag>
                <el-tag type="primary" size="small">能力: {{ getTagsByDimension('ABILITY').length }}</el-tag>
                <el-tag type="success" size="small">履历: {{ getTagsByDimension('EXPERIENCE').length }}</el-tag>
                <el-tag type="warning" size="small">考评: {{ getTagsByDimension('EVALUATION').length }}</el-tag>
              </div>
            </div>

            <el-tabs v-model="activeTagTab">
              <el-tab-pane label="全部标签" name="all">
                <div class="tag-dimension-section">
                  <div class="tags-container">
                    <div
                      v-for="tag in getActiveTags()"
                      :key="tag.tagCode"
                      class="tag-item"
                    >
                      <el-tag
                        :type="getDimensionType(tag.dimension)"
                        :color="getTagColor(tag.tagCode)"
                        style="color: white;"
                        closable
                        @close="removeTag(tag)"
                      >
                        {{ tag.tagName }}
                      </el-tag>
                      <div class="tag-meta">
                        <span class="tag-source">{{ getSourceText(tag.source) }}</span>
                        <span class="tag-confidence">置信度: {{ Math.round(tag.confidence * 100) }}%</span>
                        <span class="tag-time">{{ formatDate(tag.assignedTime) }}</span>
                      </div>
                      <div class="tag-reason">{{ tag.reason }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>

              <el-tab-pane label="能力素质" name="ability">
                <div class="tag-dimension-section">
                  <div class="tags-container">
                    <div
                      v-for="tag in getTagsByDimension('ABILITY')"
                      :key="tag.tagCode"
                      class="tag-item"
                    >
                      <el-tag type="primary" closable @close="removeTag(tag)">
                        {{ tag.tagName }}
                      </el-tag>
                      <div class="tag-meta">
                        <span class="tag-source">{{ getSourceText(tag.source) }}</span>
                        <span class="tag-confidence">置信度: {{ Math.round(tag.confidence * 100) }}%</span>
                      </div>
                      <div class="tag-reason">{{ tag.reason }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>

              <el-tab-pane label="履历经验" name="experience">
                <div class="tag-dimension-section">
                  <div class="tags-container">
                    <div
                      v-for="tag in getTagsByDimension('EXPERIENCE')"
                      :key="tag.tagCode"
                      class="tag-item"
                    >
                      <el-tag type="success" closable @close="removeTag(tag)">
                        {{ tag.tagName }}
                      </el-tag>
                      <div class="tag-meta">
                        <span class="tag-source">{{ getSourceText(tag.source) }}</span>
                        <span class="tag-confidence">置信度: {{ Math.round(tag.confidence * 100) }}%</span>
                      </div>
                      <div class="tag-reason">{{ tag.reason }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>

              <el-tab-pane label="考评结果" name="evaluation">
                <div class="tag-dimension-section">
                  <div class="tags-container">
                    <div
                      v-for="tag in getTagsByDimension('EVALUATION')"
                      :key="tag.tagCode"
                      class="tag-item"
                    >
                      <el-tag type="warning" closable @close="removeTag(tag)">
                        {{ tag.tagName }}
                      </el-tag>
                      <div class="tag-meta">
                        <span class="tag-source">{{ getSourceText(tag.source) }}</span>
                        <span class="tag-confidence">置信度: {{ Math.round(tag.confidence * 100) }}%</span>
                      </div>
                      <div class="tag-reason">{{ tag.reason }}</div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="margin-top: 20px;">
        <el-col :span="24">
          <el-card>
            <div slot="header">培训推荐</div>
            <div class="training-recommendations">
              <el-tag
                v-for="recommendation in trainingRecommendations"
                :key="recommendation"
                type="info"
                style="margin-right: 10px; margin-bottom: 10px;"
              >
                {{ recommendation }}
              </el-tag>
              <span v-if="!trainingRecommendations.length" class="no-recommendations">
                暂无培训推荐
              </span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 添加标签对话框 -->
    <el-dialog
      title="添加标签"
      :visible.sync="addTagDialogVisible"
      width="500px"
    >
      <el-form :model="addTagForm" :rules="addTagRules" ref="addTagForm" label-width="80px">
        <el-form-item label="选择标签" prop="tagCode">
          <el-select v-model="addTagForm.tagCode" placeholder="请选择标签" style="width: 100%;">
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
        </el-form-item>
        <el-form-item label="添加原因" prop="reason">
          <el-input
            type="textarea"
            v-model="addTagForm.reason"
            placeholder="请输入添加标签的原因"
            :rows="3"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addTagDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addTag" :loading="addingTag">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import moment from 'moment'

export default {
  name: 'EmployeeProfile',
  data() {
    return {
      employee: null,
      loading: false,
      activeTagTab: 'all',
      trainingRecommendations: [],
      
      addTagDialogVisible: false,
      addTagForm: {
        tagCode: '',
        reason: ''
      },
      addTagRules: {
        tagCode: [
          { required: true, message: '请选择标签', trigger: 'change' }
        ],
        reason: [
          { required: true, message: '请输入添加原因', trigger: 'blur' }
        ]
      },
      availableTags: [],
      addingTag: false
    }
  },
  
  mounted() {
    this.loadEmployeeProfile()
    this.loadTrainingRecommendations()
    this.loadAvailableTags()
  },
  
  methods: {
    async loadEmployeeProfile() {
      const employeeId = this.$route.params.id
      this.loading = true
      
      try {
        // 使用演示数据
        const response = await this.$api.employeeTagging.runFullDemo()
        const employees = response.taggedEmployees || []
        this.employee = employees.find(emp => emp.id == employeeId) || employees[0]
      } catch (error) {
        console.error('加载员工档案失败:', error)
        this.$message.error('加载员工档案失败')
      } finally {
        this.loading = false
      }
    },
    
    async loadTrainingRecommendations() {
      const employeeId = this.$route.params.id
      try {
        const response = await this.$api.employeeTagging.getTrainingRecommendations(employeeId)
        this.trainingRecommendations = response || []
      } catch (error) {
        console.error('加载培训推荐失败:', error)
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
    
    async refreshTags() {
      if (!this.employee) return
      
      try {
        const response = await this.$api.employeeTagging.tagEmployeeWithDynamicRules(this.employee)
        this.employee = response
        this.$message.success('标签刷新成功')
      } catch (error) {
        console.error('刷新标签失败:', error)
        this.$message.error('刷新标签失败')
      }
    },
    
    showAddTagDialog() {
      this.addTagDialogVisible = true
    },
    
    async addTag() {
      try {
        await this.$refs.addTagForm.validate()
        this.addingTag = true
        
        await this.$api.employeeTagging.addManualTag(this.employee.id, {
          tagCode: this.addTagForm.tagCode,
          reason: this.addTagForm.reason
        })
        
        this.$message.success('标签添加成功')
        this.addTagDialogVisible = false
        this.addTagForm = { tagCode: '', reason: '' }
        
        // 刷新员工数据
        this.loadEmployeeProfile()
      } catch (error) {
        if (error !== false) {
          console.error('添加标签失败:', error)
          this.$message.error('添加标签失败')
        }
      } finally {
        this.addingTag = false
      }
    },
    
    async removeTag(tag) {
      try {
        await this.$confirm(`确定要移除标签"${tag.tagName}"吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        // 这里应该调用删除标签的API
        this.$message.success('标签移除成功')
        
        // 从当前标签列表中移除
        const index = this.employee.currentTags.findIndex(t => t.tagCode === tag.tagCode)
        if (index !== -1) {
          this.employee.currentTags.splice(index, 1)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('移除标签失败:', error)
          this.$message.error('移除标签失败')
        }
      }
    },
    
    getActiveTags() {
      if (!this.employee || !this.employee.currentTags) return []
      return this.employee.currentTags.filter(tag => tag.active && !this.isTagExpired(tag))
    },
    
    getTagsByDimension(dimension) {
      return this.getActiveTags().filter(tag => tag.dimension === dimension)
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
    
    getTrendType(trend) {
      const typeMap = {
        '上升': 'success',
        '稳定': 'primary',
        '下降': 'danger'
      }
      return typeMap[trend] || 'info'
    },
    
    getDimensionType(dimension) {
      const typeMap = {
        'ABILITY': 'primary',
        'EXPERIENCE': 'success',
        'EVALUATION': 'warning'
      }
      return typeMap[dimension] || 'info'
    },
    
    getSourceText(source) {
      const sourceMap = {
        'RULE_ENGINE': '规则引擎',
        'MANUAL': '手动添加',
        'SYSTEM': '系统生成'
      }
      return sourceMap[source] || source
    },
    
    getTagColor(tagCode) {
      // 根据标签编码返回颜色，这里可以从标签库中获取
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      const hash = tagCode.split('').reduce((a, b) => {
        a = ((a << 5) - a) + b.charCodeAt(0)
        return a & a
      }, 0)
      return colors[Math.abs(hash) % colors.length]
    },
    
    formatDate(date) {
      if (!date) return '-'
      return moment(date).format('YYYY-MM-DD')
    }
  }
}
</script>

<style lang="scss" scoped>
.employee-profile {
  .profile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .employee-basic {
      display: flex;
      align-items: center;
      
      .avatar {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        
        i {
          font-size: 40px;
          color: white;
        }
      }
      
      .basic-info {
        h2 {
          margin: 0 0 10px 0;
          color: #303133;
        }
        
        p {
          margin: 5px 0;
          color: #606266;
        }
      }
    }
    
    .header-actions {
      display: flex;
      gap: 10px;
    }
  }
  
  .info-card {
    margin-bottom: 20px;
    
    .ability-scores {
      .score-item {
        margin-bottom: 20px;
        
        .score-label {
          display: block;
          margin-bottom: 8px;
          font-weight: 500;
          color: #303133;
        }
      }
    }
    
    .skills-section {
      h4 {
        margin-bottom: 10px;
        color: #303133;
      }
      
      .skill-tags {
        margin-bottom: 15px;
        
        .skill-level {
          font-size: 12px;
          opacity: 0.8;
        }
      }
    }
    
    .projects-section {
      .project-item {
        padding: 15px;
        border: 1px solid #EBEEF5;
        border-radius: 4px;
        margin-bottom: 15px;
        
        h4 {
          margin: 0 0 10px 0;
          color: #303133;
        }
        
        p {
          margin: 5px 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }
  
  .tags-card {
    .tags-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .tag-stats {
        display: flex;
        gap: 10px;
      }
    }
    
    .tag-dimension-section {
      .tags-container {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 15px;
        
        .tag-item {
          padding: 15px;
          border: 1px solid #EBEEF5;
          border-radius: 8px;
          background-color: #fafafa;
          
          .tag-meta {
            margin-top: 8px;
            display: flex;
            gap: 15px;
            font-size: 12px;
            color: #909399;
          }
          
          .tag-reason {
            margin-top: 5px;
            font-size: 13px;
            color: #606266;
            font-style: italic;
          }
        }
      }
    }
  }
  
  .training-recommendations {
    .no-recommendations {
      color: #909399;
      font-style: italic;
    }
  }
}
</style>
