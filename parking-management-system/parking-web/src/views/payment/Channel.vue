<template>
  <div class="channel-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>支付渠道列表</span>
          <el-button type="primary">添加渠道</el-button>
        </div>
      </template>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="channelCode" label="渠道编码" />
        <el-table-column prop="channelName" label="渠道名称" />
        <el-table-column prop="feeRate" label="手续费率">
          <template #default="{ row }">
            {{ (row.feeRate * 100).toFixed(2) }}%
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'enabled' ? 'success' : 'info'">
              {{ row.status === 'enabled' ? '启用' : '禁用' }}
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

const tableData = ref([
  { id: 1, channelCode: 'WECHAT', channelName: '微信支付', feeRate: 0.0025, sortOrder: 1, status: 'enabled' },
  { id: 2, channelCode: 'ALIPAY', channelName: '支付宝', feeRate: 0.0025, sortOrder: 2, status: 'enabled' },
  { id: 3, channelCode: 'CASH', channelName: '现金支付', feeRate: 0, sortOrder: 3, status: 'enabled' },
  { id: 4, channelCode: 'ETC', channelName: 'ETC电子支付', feeRate: 0.0010, sortOrder: 4, status: 'enabled' }
])
</script>

<style scoped>
.channel-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
