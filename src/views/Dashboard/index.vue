<template>
  <div class="dashboard page-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="card-container stat-card">
          <div class="stat-item">
            <div class="stat-icon tag-icon">
              <i class="el-icon-collection-tag"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalTags || 0 }}</div>
              <div class="stat-label">标签总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="card-container stat-card">
          <div class="stat-item">
            <div class="stat-icon rule-icon">
              <i class="el-icon-setting"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalRules || 0 }}</div>
              <div class="stat-label">规则总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="card-container stat-card">
          <div class="stat-item">
            <div class="stat-icon employee-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalEmployees || 0 }}</div>
              <div class="stat-label">员工总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="card-container stat-card">
          <div class="stat-item">
            <div class="stat-icon execution-icon">
              <i class="el-icon-magic-stick"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalExecutions || 0 }}</div>
              <div class="stat-label">规则执行次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="card-container">
          <div slot="header" class="card-header">
            <span>标签分布统计</span>
          </div>
          <div id="tagChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="card-container">
          <div slot="header" class="card-header">
            <span>规则执行统计</span>
          </div>
          <div id="ruleChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="card-container">
          <div slot="header" class="card-header">
            <span>系统状态</span>
            <div class="header-actions">
              <el-button style="float: right; padding: 3px 0" type="text" @click="refreshSystemStatus">刷新</el-button>
            </div>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="标签库状态">
              <el-tag :type="systemStatus.tagLibraryHealth === 'HEALTHY' ? 'success' : 'danger'">
                {{ systemStatus.tagLibraryHealth === 'HEALTHY' ? '正常' : '异常' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="规则引擎状态">
              <el-tag :type="systemStatus.ruleEngineHealth === 'HEALTHY' ? 'success' : 'danger'">
                {{ systemStatus.ruleEngineHealth === 'HEALTHY' ? '正常' : '异常' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="活跃标签数">{{ systemStatus.activeTags || 0 }}</el-descriptions-item>
            <el-descriptions-item label="活跃规则数">{{ systemStatus.activeRules || 0 }}</el-descriptions-item>
            <el-descriptions-item label="最后更新时间">{{ systemStatus.lastUpdateTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="系统运行状态">
              <el-tag type="success">运行中</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      statistics: {},
      systemStatus: {},
      tagChart: null,
      ruleChart: null
    }
  },
  mounted() {
    this.loadDashboardData()
    this.initCharts()
  },
  methods: {
    async loadDashboardData() {
      try {
        // 获取标签统计
        const tagStats = await this.$api.tagLibrary.getTagStatistics()
        // 获取规则统计
        const ruleStats = await this.$api.ruleManagement.getRuleStatistics()
        // 获取系统状态
        const systemStatus = await this.$api.employeeTagging.getSystemStatus()
        
        this.statistics = {
          totalTags: tagStats.totalTags,
          totalRules: ruleStats.totalRules,
          totalEmployees: 100, // 模拟数据
          totalExecutions: ruleStats.totalExecutions
        }
        
        this.systemStatus = {
          tagLibraryHealth: 'HEALTHY',
          ruleEngineHealth: 'HEALTHY',
          activeTags: tagStats.totalTags,
          activeRules: ruleStats.activeRules,
          lastUpdateTime: new Date().toLocaleString()
        }
        console.log('系统状态:', systemStatus)
        this.updateCharts(tagStats, ruleStats)
      } catch (error) {
        console.error('加载仪表盘数据失败:', error)
        this.$message.error('加载数据失败')
      }
    },
    
    initCharts() {
      this.tagChart = echarts.init(document.getElementById('tagChart'))
      this.ruleChart = echarts.init(document.getElementById('ruleChart'))
    },
    
    updateCharts(tagStats, ruleStats) {
      // 标签分布图
      const tagOption = {
        title: {
          text: '标签维度分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            name: '标签数量',
            type: 'pie',
            radius: '50%',
            data: [
              { value: tagStats.dimensionStats? tagStats.dimensionStats.ABILITY : 0, name: '能力素质' },
              { value: tagStats.dimensionStats? tagStats.dimensionStats.EXPERIENCE : 0, name: '履历经验' },
              { value: tagStats.dimensionStats? tagStats.dimensionStats.EVALUATION : 0, name: '考评结果' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      
      // 规则模板分布图
      const ruleOption = {
        title: {
          text: '规则模板分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: Object.keys(ruleStats.templateStats || {})
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '规则数量',
            type: 'bar',
            data: Object.values(ruleStats.templateStats || {}),
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      
      this.tagChart.setOption(tagOption)
      this.ruleChart.setOption(ruleOption)
    },
    
    refreshSystemStatus() {
      this.loadDashboardData()
      this.$message.success('系统状态已刷新')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  .stat-card {
    .stat-item {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        
        i {
          font-size: 24px;
          color: white;
        }
        
        &.tag-icon {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.rule-icon {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.employee-icon {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.execution-icon {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .stat-content {
        .stat-number {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
          line-height: 1;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }
}
</style>
