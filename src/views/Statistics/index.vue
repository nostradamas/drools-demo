<template>
  <div class="app-container page-container">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :span="6">
        <el-card class="card-container">
          <div slot="header" class="card-header">
            <span>总员工数</span>
            <i class="el-icon-user" />
          </div>
          <div class="card-content">
            <div class="card-number">{{ statistics.totalEmployees }}</div>
            <div class="card-desc">
              <span class="card-trend up">+{{ statistics.newEmployeesThisMonth }}</span>
              本月新增
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <div class="card-container">
          <div class="card-header">
            <span>标签总数</span>
            <i class="el-icon-collection-tag" />
          </div>
          <div class="card-content">
            <div class="card-number">{{ statistics.totalTags }}</div>
            <div class="card-desc">
              <span class="card-trend up">+{{ statistics.newTagsThisMonth }}</span>
              本月新增
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="card-container">
          <div class="card-header">
            <span>规则总数</span>
            <i class="el-icon-setting" />
          </div>
          <div class="card-content">
            <div class="card-number">{{ statistics.totalRules }}</div>
            <div class="card-desc">
              <span class="card-trend">{{ statistics.activeRules }}</span>
              个激活
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="card-container">
          <div class="card-header">
            <span>打标签次数</span>
            <i class="el-icon-data-analysis" />
          </div>
          <div class="card-content">
            <div class="card-number">{{ statistics.totalTaggingCount }}</div>
            <div class="card-desc">
              <span class="card-trend up">+{{ statistics.taggingCountToday }}</span>
              今日新增
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 标签使用统计图表 -->
      <el-col :span="12">
        <div class="card-container">
          <div class="card-header">
            <span>标签使用统计</span>
            <el-button-group style="float: right;">
              <el-button size="mini" @click="refreshTagChart">刷新</el-button>
              <el-button size="mini" @click="exportTagData">导出</el-button>
            </el-button-group>
          </div>
          <div id="tagChart" style="height: 400px;" />
        </div>
      </el-col>
      
      <!-- 部门员工分布图表 -->
      <el-col :span="12">
        <div class="card-container">
          <div class="card-header">
            <span>部门员工分布</span>
            <el-button-group style="float: right;">
              <el-button size="mini" @click="refreshDeptChart">刷新</el-button>
              <el-button size="mini" @click="exportDeptData">导出</el-button>
            </el-button-group>
          </div>
          <div id="deptChart" style="height: 400px;" />
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 规则执行统计 -->
      <el-col :span="24">
        <div class="card-container">
          <div class="card-header">
            <span>规则执行统计</span>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="float: right;"
              @change="handleDateChange"
            />
          </div>
          <div id="ruleChart" style="height: 400px;" />
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 热门标签排行 -->
      <el-col :span="12">
        <div class="card-container">
          <div class="card-header">
            <span>热门标签排行</span>
          </div>
          <el-table :data="hotTags" style="width: 100%">
            <el-table-column prop="rank" label="排名" width="80" align="center" />
            <el-table-column prop="name" label="标签名称" />
            <el-table-column prop="count" label="使用次数" width="100" align="center">
              <template slot-scope="scope">
                <el-tag type="primary">{{ scope.row.count }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="growth" label="增长率" width="100" align="center">
              <template slot-scope="scope">
                <span :class="scope.row.growth > 0 ? 'card-trend up' : 'card-trend down'">
                  {{ scope.row.growth > 0 ? '+' : '' }}{{ scope.row.growth }}%
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      
      <!-- 系统活跃度 -->
      <el-col :span="12">
        <div class="card-container">
          <div class="card-header">
            <span>系统活跃度</span>
          </div>
          <div class="activity-list">
            <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
              <div class="activity-icon">
                <i :class="getActivityIcon(activity.type)" />
              </div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-time">{{ activity.time }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getStatistics, getTagUsageStats, getDepartmentStats, getRuleExecutionStats, getHotTags, getRecentActivities } from '@/api'

export default {
  name: 'Statistics',
  data() {
    return {
      statistics: {
        totalEmployees: 0,
        newEmployeesThisMonth: 0,
        totalTags: 0,
        newTagsThisMonth: 0,
        totalRules: 0,
        activeRules: 0,
        totalTaggingCount: 0,
        taggingCountToday: 0
      },
      hotTags: [],
      recentActivities: [],
      dateRange: [],
      tagChart: null,
      deptChart: null,
      ruleChart: null
    }
  },
  mounted() {
    this.initData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.tagChart) {
      this.tagChart.dispose()
    }
    if (this.deptChart) {
      this.deptChart.dispose()
    }
    if (this.ruleChart) {
      this.ruleChart.dispose()
    }
  },
  methods: {
    async initData() {
      try {
        // 获取基础统计数据
        const statsResponse = await getStatistics()
        this.statistics = statsResponse.data

        // 获取热门标签
        const hotTagsResponse = await getHotTags()
        this.hotTags = hotTagsResponse.data.map((tag, index) => ({
          ...tag,
          rank: index + 1
        }))

        // 获取最近活动
        const activitiesResponse = await getRecentActivities()
        this.recentActivities = activitiesResponse.data
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    
    initCharts() {
      this.$nextTick(() => {
        this.initTagChart()
        this.initDeptChart()
        this.initRuleChart()
      })
    },
    
    async initTagChart() {
      const chartDom = document.getElementById('tagChart')
      this.tagChart = echarts.init(chartDom)
      
      try {
        const response = await getTagUsageStats()
        const option = {
          title: {
            text: '标签使用分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          series: [
            {
              name: '标签使用次数',
              type: 'pie',
              radius: '50%',
              data: response.data,
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
        this.tagChart.setOption(option)
      } catch (error) {
        console.error('获取标签统计数据失败:', error)
      }
    },
    
    async initDeptChart() {
      const chartDom = document.getElementById('deptChart')
      this.deptChart = echarts.init(chartDom)
      
      try {
        const response = await getDepartmentStats()
        const option = {
          title: {
            text: '部门员工分布',
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
            data: response.data.map(item => item.name)
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '员工数量',
              data: response.data.map(item => item.value),
              type: 'bar',
              itemStyle: {
                color: '#409EFF'
              }
            }
          ]
        }
        this.deptChart.setOption(option)
      } catch (error) {
        console.error('获取部门统计数据失败:', error)
      }
    },
    
    async initRuleChart() {
      const chartDom = document.getElementById('ruleChart')
      this.ruleChart = echarts.init(chartDom)
      
      try {
        const response = await getRuleExecutionStats(this.dateRange)
        const option = {
          title: {
            text: '规则执行趋势',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: response.data.dates
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '执行次数',
              data: response.data.counts,
              type: 'line',
              smooth: true,
              itemStyle: {
                color: '#67C23A'
              }
            }
          ]
        }
        this.ruleChart.setOption(option)
      } catch (error) {
        console.error('获取规则执行统计数据失败:', error)
      }
    },
    
    refreshTagChart() {
      this.initTagChart()
    },
    
    refreshDeptChart() {
      this.initDeptChart()
    },
    
    handleDateChange() {
      this.initRuleChart()
    },
    
    exportTagData() {
      this.$message.success('标签数据导出功能开发中...')
    },
    
    exportDeptData() {
      this.$message.success('部门数据导出功能开发中...')
    },
    
    getActivityIcon(type) {
      const iconMap = {
        'tag': 'el-icon-collection-tag',
        'rule': 'el-icon-setting',
        'employee': 'el-icon-user',
        'system': 'el-icon-monitor'
      }
      return iconMap[type] || 'el-icon-info'
    }
  }
}
</script>

<style scoped>
.card-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

.card-content {
  text-align: center;
}

.card-number {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.card-desc {
  color: #909399;
  font-size: 14px;
}

.card-trend {
  font-weight: bold;
}

.card-trend.up {
  color: #67C23A;
}

.card-trend.down {
  color: #F56C6C;
}

.activity-list {
  max-height: 300px;
  overflow-y: auto;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #EBEEF5;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #F5F7FA;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.activity-icon i {
  font-size: 18px;
  color: #409EFF;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.activity-time {
  font-size: 12px;
  color: #909399;
}
</style>
