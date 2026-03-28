<template>
  <div class="vehicle-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>车辆列表</span>
          <el-button type="primary" @click="handleAdd">新增车辆</el-button>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="车牌号">
          <el-input v-model="queryForm.plateNumber" placeholder="请输入车牌号" clearable />
        </el-form-item>
        <el-form-item label="车辆分类">
          <el-select v-model="queryForm.vehicleTypeCat" placeholder="请选择" clearable>
            <el-option label="月卡车" value="monthly" />
            <el-option label="临时车" value="temp" />
            <el-option label="VIP" value="vip" />
            <el-option label="黑名单" value="blacklist" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%">
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
            <el-tag :type="row.status === 'normal' ? 'success' : 'danger'">
              {{ row.status === 'normal' ? '正常' : '禁用' }}
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
        <el-form-item label="车型">
          <el-select v-model="form.vehicleType" placeholder="请选择">
            <el-option label="轿车" value="轿车" />
            <el-option label="SUV" value="SUV" />
            <el-option label="MPV" value="MPV" />
            <el-option label="货车" value="货车" />
          </el-select>
        </el-form-item>
        <el-form-item label="车辆分类">
          <el-select v-model="form.vehicleTypeCat" placeholder="请选择">
            <el-option label="月卡车" value="monthly" />
            <el-option label="临时车" value="temp" />
            <el-option label="VIP" value="vip" />
            <el-option label="黑名单" value="blacklist" />
          </el-select>
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

const queryForm = reactive({
  plateNumber: '',
  vehicleTypeCat: '',
  status: ''
})

const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增车辆')
const form = reactive({
  id: null as number | null,
  plateNumber: '',
  plateColor: '',
  vehicleBrand: '',
  vehicleType: '',
  vehicleTypeCat: ''
})

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

const getCategoryName = (cat: string) => categoryMap[cat] || cat
const getCategoryType = (cat: string) => categoryTypeMap[cat] || 'info'

const loadData = async () => {
  try {
    const res = await request.get('/vehicle/v1/vehicles/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        plateNumber: queryForm.plateNumber || undefined,
        vehicleTypeCat: queryForm.vehicleTypeCat || undefined,
        status: queryForm.status || undefined
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
  queryForm.vehicleTypeCat = ''
  queryForm.status = ''
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '新增车辆'
  form.id = null
  form.plateNumber = ''
  form.plateColor = ''
  form.vehicleBrand = ''
  form.vehicleType = ''
  form.vehicleTypeCat = ''
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑车辆'
  form.id = row.id
  form.plateNumber = row.plateNumber
  form.plateColor = row.plateColor
  form.vehicleBrand = row.vehicleBrand
  form.vehicleType = row.vehicleType
  form.vehicleTypeCat = row.vehicleTypeCat
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/vehicle/v1/vehicles/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/vehicle/v1/vehicles/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/vehicle/v1/vehicles', form)
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
.vehicle-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
