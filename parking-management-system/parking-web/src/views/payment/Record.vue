<template>
  <div class="record-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>交易记录</span>
          <el-button type="success" @click="handleExport">导出</el-button>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="订单号">
          <el-input v-model="queryForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="车牌号">
          <el-input v-model="queryForm.plateNumber" placeholder="请输入车牌号" clearable />
        </el-form-item>
        <el-form-item label="支付状态">
          <el-select v-model="queryForm.paymentStatus" placeholder="请选择" clearable>
            <el-option label="待支付" value="pending" />
            <el-option label="已支付" value="paid" />
            <el-option label="已取消" value="cancelled" />
            <el-option label="已退款" value="refunded" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="plateNumber" label="车牌号" />
        <el-table-column prop="channelName" label="支付渠道" />
        <el-table-column prop="amountDue" label="应付金额">
          <template #default="{ row }">
            <span style="color: #e6a23c">¥ {{ row.amountDue || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="amountPaid" label="实付金额">
          <template #default="{ row }">
            <span style="color: #67c23a; font-weight: bold">¥ {{ row.amountPaid || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="支付状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.paymentStatus)">
              {{ getStatusName(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentTime" label="支付时间" />
        <el-table-column prop="transactionId" label="交易流水号" show-overflow-tooltip />
      </el-table>
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const statusMap: Record<string, string> = {
  pending: '待支付',
  paid: '已支付',
  cancelled: '已取消',
  refunded: '已退款'
}

const queryForm = reactive({
  orderNo: '',
  plateNumber: '',
  paymentStatus: ''
})

const tableData = ref<any[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const getStatusName = (status: string) => statusMap[status] || status
const getStatusType = (status: string) => {
  const map: Record<string, string> = { pending: 'warning', paid: 'success', cancelled: 'info', refunded: 'danger' }
  return map[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await request.get('/payment/v1/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        orderNo: queryForm.orderNo || undefined,
        plateNumber: queryForm.plateNumber || undefined,
        paymentStatus: queryForm.paymentStatus || undefined
      }
    })
    const records = res.records || []
    tableData.value = records.map((r: any) => ({
      ...r,
      channelName: r.channelName || r.paymentChannelId || '-'
    }))
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

const handleQuery = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  queryForm.orderNo = ''
  queryForm.plateNumber = ''
  queryForm.paymentStatus = ''
  handleQuery()
}

const handleExport = async () => {
  try {
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('Failed to export:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.record-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
