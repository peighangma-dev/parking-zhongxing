<template>
  <div class="rate-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>费率规则</span>
          <el-button type="primary" @click="handleAdd">新增规则</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="ruleName" label="规则名称" />
        <el-table-column prop="vehicleType" label="车辆类型">
          <template #default="{ row }">
            <el-tag>{{ getVehicleTypeName(row.vehicleType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="freeMinutes" label="免费时长(分钟)" />
        <el-table-column prop="firstHourFee" label="首小时费用(元)" />
        <el-table-column prop="subsequentFee" label="续费(元/小时)" />
        <el-table-column prop="dailyCap" label="日封顶(元)" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'enabled' ? 'success' : 'danger'">
              {{ row.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="规则名称">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="车辆类型">
          <el-select v-model="form.vehicleType" placeholder="请选择">
            <el-option label="临时车" value="temp" />
            <el-option label="月卡车" value="monthly" />
            <el-option label="VIP" value="vip" />
          </el-select>
        </el-form-item>
        <el-form-item label="免费时长(分钟)">
          <el-input-number v-model="form.freeMinutes" :min="0" />
        </el-form-item>
        <el-form-item label="首小时费用(元)">
          <el-input-number v-model="form.firstHourFee" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="续费(元/小时)">
          <el-input-number v-model="form.subsequentFee" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="日封顶(元)">
          <el-input-number v-model="form.dailyCap" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" active-value="enabled" inactive-value="disabled" />
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
import { ElMessage } from 'element-plus'

const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增规则')
const form = reactive({
  id: null as number | null,
  ruleName: '',
  vehicleType: 'temp',
  freeMinutes: 15,
  firstHourFee: 5,
  subsequentFee: 3,
  dailyCap: 50,
  status: 'enabled'
})

const vehicleTypeMap: Record<string, string> = {
  temp: '临时车',
  monthly: '月卡车',
  vip: 'VIP'
}

const getVehicleTypeName = (type: string) => vehicleTypeMap[type] || type

const loadData = async () => {
  try {
    const res = await request.get('/payment/v1/rates/page', {
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
  dialogTitle.value = '新增规则'
  form.id = null
  form.ruleName = ''
  form.vehicleType = 'temp'
  form.freeMinutes = 15
  form.firstHourFee = 5
  form.subsequentFee = 3
  form.dailyCap = 50
  form.status = 'enabled'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑规则'
  form.id = row.id
  form.ruleName = row.ruleName
  form.vehicleType = row.vehicleType
  form.freeMinutes = row.freeMinutes
  form.firstHourFee = row.firstHourFee
  form.subsequentFee = row.subsequentFee
  form.dailyCap = row.dailyCap
  form.status = row.status
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/payment/v1/rates/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/payment/v1/rates/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/payment/v1/rates', form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to submit:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.rate-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
