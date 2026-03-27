<template>
  <div class="device-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>道闸设备列表</span>
          <el-button type="primary">添加设备</el-button>
        </div>
      </template>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="code" label="设备编码" />
        <el-table-column prop="name" label="设备名称" />
        <el-table-column prop="location" label="安装位置" />
        <el-table-column prop="totalLanes" label="车道数" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'online' ? 'success' : row.status === 'offline' ? 'danger' : 'warning'">
              {{ statusMap[row.status] }}
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

const statusMap: Record<string, string> = {
  online: '在线',
  offline: '离线',
  fault: '故障'
}

const tableData = ref([
  { id: 1, code: 'B001', name: '入口道闸1', location: '东门入口', totalLanes: 2, status: 'online' },
  { id: 2, code: 'B002', name: '入口道闸2', location: '东门入口', totalLanes: 2, status: 'online' },
  { id: 3, code: 'B003', name: '出口道闸1', location: '西门出口', totalLanes: 2, status: 'offline' },
  { id: 4, code: 'B004', name: '出口道闸2', location: '西门出口', totalLanes: 2, status: 'online' }
])
</script>

<style scoped>
.device-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
