<template>
  <div class="dashboard">
    <h2>停车场管理系统</h2>
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card>
          <template #header>
            <span>今日入场</span>
          </template>
          <div class="stat-value">1,234</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <span>今日出场</span>
          </template>
          <div class="stat-value">1,156</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <span>当前在场</span>
          </template>
          <div class="stat-value">78</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <span>今日收入</span>
          </template>
          <div class="stat-value">¥ 15,680</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="stats-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>道闸状态</span>
          </template>
          <el-table :data="barrierStatus" style="width: 100%">
            <el-table-column prop="name" label="设备名称" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="row.status === 'online' ? 'success' : 'danger'">
                  {{ row.status === 'online' ? '在线' : '离线' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近交易</span>
          </template>
          <el-table :data="recentPayments" style="width: 100%">
            <el-table-column prop="plateNumber" label="车牌号" />
            <el-table-column prop="amount" label="金额" />
            <el-table-column prop="channel" label="支付方式" />
            <el-table-column prop="time" label="时间" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const barrierStatus = ref([
  { name: '入口道闸1', status: 'online' },
  { name: '入口道闸2', status: 'online' },
  { name: '出口道闸1', status: 'offline' },
  { name: '出口道闸2', status: 'online' }
])

const recentPayments = ref([
  { plateNumber: '京A12345', amount: '¥10.00', channel: '微信支付', time: '10:30' },
  { plateNumber: '京B67890', amount: '¥5.00', channel: '支付宝', time: '10:25' },
  { plateNumber: '京C11111', amount: '¥15.00', channel: '现金', time: '10:20' }
])
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.dashboard h2 {
  margin-bottom: 20px;
  color: #333;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
  padding: 20px 0;
}
</style>
