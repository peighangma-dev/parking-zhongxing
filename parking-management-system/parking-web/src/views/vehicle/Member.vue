<template>
  <div class="member-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>月卡管理</span>
          <el-button type="success">开通月卡</el-button>
        </div>
      </template>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" />
        <el-table-column prop="memberType" label="套餐类型">
          <template #default="{ row }">
            {{ getMemberTypeName(row.memberType) }}
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="生效日期" />
        <el-table-column prop="endDate" label="到期日期" />
        <el-table-column prop="balance" label="账户余额">
          <template #default="{ row }">
            ¥ {{ row.balance.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default>
            <el-button link type="primary" size="small">续期</el-button>
            <el-button link type="warning" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const memberTypeMap: Record<string, string> = {
  monthly: '月卡',
  seasonal: '季卡',
  annual: '年卡',
  vip: 'VIP'
}

const statusMap: Record<string, string> = {
  active: '有效',
  expired: '已过期',
  cancelled: '已取消'
}

const statusTypeMap: Record<string, string> = {
  active: 'success',
  expired: 'danger',
  cancelled: 'info'
}

const tableData = ref([
  { id: 1, plateNumber: '京A12345', memberType: 'monthly', startDate: '2026-01-01', endDate: '2026-12-31', balance: 0, status: 'active' },
  { id: 2, plateNumber: '京A22222', memberType: 'seasonal', startDate: '2026-01-01', endDate: '2026-03-31', balance: 100, status: 'active' },
  { id: 3, plateNumber: '京A33333', memberType: 'annual', startDate: '2025-06-01', endDate: '2026-05-31', balance: 0, status: 'expired' }
])

const getMemberTypeName = (type: string) => memberTypeMap[type] || type
const getStatusName = (status: string) => statusMap[status] || status
const getStatusType = (status: string) => statusTypeMap[status] || 'info'
</script>

<style scoped>
.member-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
