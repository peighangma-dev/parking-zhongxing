<template>
  <div class="pass-record-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>通行记录</span>
          <el-button type="success" @click="handleExport">导出</el-button>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="车牌号">
          <el-input v-model="queryForm.plateNumber" placeholder="请输入车牌号" clearable />
        </el-form-item>
        <el-form-item label="通行类型">
          <el-select v-model="queryForm.passType" placeholder="请选择" clearable>
            <el-option label="入场" value="in" />
            <el-option label="出场" value="out" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" />
        <el-table-column prop="passType" label="通行类型">
          <template #default="{ row }">
            <el-tag :type="row.passType === 'in' ? 'success' : 'warning'">
              {{ row.passType === 'in' ? '入场' : '出场' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="passStatus" label="通行状态">
          <template #default="{ row }">
            <el-tag :type="row.passStatus === 'normal' ? 'success' : 'danger'">
              {{ row.passStatus === 'normal' ? '正常' : '异常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recognitionConfidence" label="识别置信度">
          <template #default="{ row }">
            {{ row.recognitionConfidence ? (row.recognitionConfidence * 100).toFixed(1) + '%' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="feeCalculated" label="费用">
          <template #default="{ row }">
            {{ row.feeCalculated ? '¥' + row.feeCalculated : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="passTime" label="通行时间" />
        <el-table-column label="抓拍图片" width="100">
          <template #default="{ row }">
            <el-image v-if="row.imageUrl" :src="row.imageUrl" :preview-src-list="[row.imageUrl]" style="width: 40px; height: 40px" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
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
import dayjs from 'dayjs'

const queryForm = reactive({
  plateNumber: '',
  passType: ''
})

const dateRange = ref<string[]>([])
const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const loadData = async () => {
  try {
    const startTime = dateRange.value?.[0]
    const endTime = dateRange.value?.[1]
    const res = await request.get('/report/v1/pass-records/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        plateNumber: queryForm.plateNumber || undefined,
        passType: queryForm.passType || undefined,
        startTime: startTime || undefined,
        endTime: endTime || undefined
      }
    })
    tableData.value = res.records || []
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
  queryForm.plateNumber = ''
  queryForm.passType = ''
  dateRange.value = []
  handleQuery()
}

const handleExport = async () => {
  try {
    const startTime = dateRange.value?.[0]
    const endTime = dateRange.value?.[1]
    const res = await request.get('/report/v1/pass-records/export', {
      params: {
        plateNumber: queryForm.plateNumber || undefined,
        passType: queryForm.passType || undefined,
        startTime: startTime || undefined,
        endTime: endTime || undefined
      }
    })
    ElMessage.success('导出成功，共 ' + (res?.length || 0) + ' 条记录')
  } catch (error) {
    console.error('Failed to export:', error)
    ElMessage.error('导出失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.pass-record-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
