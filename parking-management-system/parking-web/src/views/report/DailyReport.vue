<template>
  <div class="report-page">
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>今日收入</template>
          <div class="stat-value">¥ {{ summary.todayRevenue || 0 }}</div>
          <div class="stat-label">入场: {{ summary.todayEntry || 0 }} / 出场: {{ summary.todayExit || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>本月收入</template>
          <div class="stat-value">¥ {{ summary.monthRevenue || 0 }}</div>
          <div class="stat-label">月卡: {{ summary.monthlyRevenue || 0 }} / 临停: {{ summary.tempRevenue || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>当前在场</template>
          <div class="stat-value">{{ summary.currentParked || 0 }}</div>
          <div class="stat-label">剩余车位: {{ summary.availableSpaces || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>收费报表</span>
          <el-tabs v-model="activeTab" @tab-change="handleTabChange">
            <el-tab-pane label="日报" name="daily" />
            <el-tab-pane label="月报" name="monthly" />
          </el-tabs>
        </div>
      </template>

      <div v-if="activeTab === 'daily'">
        <el-form inline :model="dailyForm">
          <el-form-item label="日期">
            <el-date-picker v-model="dailyForm.date" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" @change="loadDailySummary" />
          </el-form-item>
        </el-form>
        <el-table :data="dailyData" stripe style="width: 100%">
          <el-table-column prop="date" label="日期" />
          <el-table-column prop="totalVehicles" label="总车次" />
          <el-table-column prop="entryCount" label="入场车次" />
          <el-table-column prop="exitCount" label="出场车次" />
          <el-table-column prop="monthlyCount" label="月卡续费" />
          <el-table-column prop="tempCount" label="临停收费" />
          <el-table-column prop="revenue" label="收入(元)">
            <template #default="{ row }">
              <span style="color: #67c23a; font-weight: bold">¥ {{ row.revenue || 0 }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-else>
        <el-form inline :model="monthlyForm">
          <el-form-item label="年月">
            <el-date-picker v-model="monthlyForm.date" type="month" placeholder="选择月份" value-format="YYYY-MM" @change="loadMonthlySummary" />
          </el-form-item>
        </el-form>
        <el-table :data="monthlyData" stripe style="width: 100%">
          <el-table-column prop="yearMonth" label="月份" />
          <el-table-column prop="totalVehicles" label="总车次" />
          <el-table-column prop="monthlyCount" label="月卡续费" />
          <el-table-column prop="tempCount" label="临停收费" />
          <el-table-column prop="revenue" label="收入(元)">
            <template #default="{ row }">
              <span style="color: #67c23a; font-weight: bold">¥ {{ row.revenue || 0 }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import dayjs from 'dayjs'

const activeTab = ref('daily')
const summary = ref<any>({})
const dailyData = ref<any[]>([])
const monthlyData = ref<any[]>([])

const dailyForm = reactive({
  date: dayjs().format('YYYY-MM-DD')
})

const monthlyForm = reactive({
  date: dayjs().format('YYYY-MM')
})

const loadSummary = async () => {
  try {
    const res = await request.get('/report/v1/daily-summary')
    summary.value = res || {}
  } catch (error) {
    console.error('Failed to load summary:', error)
  }
}

const loadDailySummary = async () => {
  try {
    const res = await request.get('/report/v1/daily-summary', {
      params: { date: dailyForm.date }
    })
    dailyData.value = res ? [res] : []
  } catch (error) {
    console.error('Failed to load daily summary:', error)
  }
}

const loadMonthlySummary = async () => {
  try {
    const [year, month] = monthlyForm.date.split('-')
    const res = await request.get('/report/v1/monthly-summary', {
      params: { year, month }
    })
    monthlyData.value = res ? [res] : []
  } catch (error) {
    console.error('Failed to load monthly summary:', error)
  }
}

const handleTabChange = () => {
  if (activeTab.value === 'daily') {
    loadDailySummary()
  } else {
    loadMonthlySummary()
  }
}

onMounted(() => {
  loadSummary()
  loadDailySummary()
})
</script>

<style scoped>
.report-page {
  padding: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
