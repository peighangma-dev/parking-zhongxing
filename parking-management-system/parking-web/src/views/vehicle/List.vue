<template>
  <div class="vehicle-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>车辆列表</span>
          <div>
            <el-input v-model="searchKey" placeholder="请输入车牌号" style="width: 200px; margin-right: 10px" />
            <el-button type="primary">搜索</el-button>
            <el-button type="success">添加车辆</el-button>
          </div>
        </div>
      </template>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" />
        <el-table-column prop="plateColor" label="车牌颜色" />
        <el-table-column prop="vehicleBrand" label="车辆品牌" />
        <el-table-column prop="vehicleType" label="车型" />
        <el-table-column prop="vehicleTypeCat" label="分类">
          <template #default="{ row }">
            <el-tag :type="getCategoryType(row.vehicleTypeCat)">
              {{ getCategoryName(row.vehicleTypeCat) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'normal' ? 'success' : 'info'">
              {{ row.status === 'normal' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default>
            <el-button link type="primary" size="small">编辑</el-button>
            <el-button link type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const searchKey = ref('')

const categoryMap: Record<string, string> = {
  monthly: '月卡车',
  temp: '临时车',
  vip: 'VIP',
  blacklist: '黑名单'
}

const categoryTypeMap: Record<string, string> = {
  monthly: 'success',
  temp: 'warning',
  vip: 'danger',
  blacklist: 'danger'
}

const tableData = ref([
  { id: 1, plateNumber: '京A12345', plateColor: '蓝色', vehicleBrand: '宝马', vehicleType: '轿车', vehicleTypeCat: 'monthly', status: 'normal' },
  { id: 2, plateNumber: '京B67890', plateColor: '黄色', vehicleBrand: '大众', vehicleType: 'SUV', vehicleTypeCat: 'temp', status: 'normal' },
  { id: 3, plateNumber: '京C11111', plateColor: '绿色', vehicleBrand: '奔驰', vehicleType: '轿车', vehicleTypeCat: 'vip', status: 'normal' }
])

const getCategoryName = (cat: string) => categoryMap[cat] || cat
const getCategoryType = (cat: string) => categoryTypeMap[cat] || 'info'
</script>

<style scoped>
.vehicle-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
