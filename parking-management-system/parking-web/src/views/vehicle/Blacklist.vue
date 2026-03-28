<template>
  <div class="blacklist-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>黑名单管理</span>
          <el-button type="danger" @click="handleAdd">添加黑名单</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" />
        <el-table-column prop="plateColor" label="车牌颜色" />
        <el-table-column prop="vehicleBrand" label="车辆品牌" />
        <el-table-column prop="reason" label="加入原因" />
        <el-table-column prop="operatorName" label="操作人" />
        <el-table-column prop="createdAt" label="加入时间" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="success" link @click="handleRemove(row)">移出</el-button>
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

    <el-dialog v-model="dialogVisible" title="添加黑名单" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="车牌号">
          <el-input v-model="form.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车牌颜色">
          <el-select v-model="form.plateColor" placeholder="请选择">
            <el-option label="蓝色" value="蓝色" />
            <el-option label="黄色" value="黄色" />
            <el-option label="绿色" value="绿色" />
            <el-option label="白色" value="白色" />
            <el-option label="黑色" value="黑色" />
          </el-select>
        </el-form-item>
        <el-form-item label="车辆品牌">
          <el-input v-model="form.vehicleBrand" placeholder="请输入车辆品牌" />
        </el-form-item>
        <el-form-item label="加入原因">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const form = reactive({
  plateNumber: '',
  plateColor: '',
  vehicleBrand: '',
  reason: ''
})

const loadData = async () => {
  try {
    const res = await request.get('/vehicle/v1/blacklist/page', {
      params: {
        current: pagination.current,
        size: pagination.size
      }
    })
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

const handleAdd = () => {
  form.plateNumber = ''
  form.plateColor = ''
  form.vehicleBrand = ''
  form.reason = ''
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await request.post('/vehicle/v1/blacklist', {
      plateNumber: form.plateNumber,
      plateColor: form.plateColor,
      vehicleBrand: form.vehicleBrand,
      reason: form.reason
    })
    ElMessage.success('添加成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to add:', error)
  }
}

const handleRemove = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要将该车辆移出黑名单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/vehicle/v1/blacklist/${row.id}`)
    ElMessage.success('移出成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to remove:', error)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.blacklist-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
