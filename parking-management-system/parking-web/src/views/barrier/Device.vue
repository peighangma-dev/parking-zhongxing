<template>
  <div class="device-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>道闸设备列表</span>
          <el-button type="primary" @click="handleAdd">添加设备</el-button>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="设备编码">
          <el-input v-model="queryForm.code" placeholder="请输入设备编码" clearable />
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="queryForm.name" placeholder="请输入设备名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="在线" value="online" />
            <el-option label="离线" value="offline" />
            <el-option label="故障" value="fault" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="code" label="设备编码" />
        <el-table-column prop="name" label="设备名称" />
        <el-table-column prop="location" label="安装位置" />
        <el-table-column prop="laneCount" label="车道数" />
        <el-table-column prop="ipAddress" label="IP地址" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ statusMap[row.status] || row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button :type="row.status === 'online' ? 'warning' : 'success'" link @click="handleToggleStatus(row)">
              {{ row.status === 'online' ? '停用' : '启用' }}
            </el-button>
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
        <el-form-item label="设备编码">
          <el-input v-model="form.code" placeholder="请输入设备编码" />
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="安装位置">
          <el-input v-model="form.location" placeholder="请输入安装位置" />
        </el-form-item>
        <el-form-item label="IP地址">
          <el-input v-model="form.ipAddress" placeholder="请输入IP地址" />
        </el-form-item>
        <el-form-item label="车道数">
          <el-input-number v-model="form.laneCount" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="form.barrierType" placeholder="请选择">
            <el-option label="入口" value="in" />
            <el-option label="出口" value="out" />
            <el-option label="混合" value="mixed" />
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
import { ElMessage, ElMessageBox } from 'element-plus'

const statusMap: Record<string, string> = {
  online: '在线',
  offline: '离线',
  fault: '故障',
  normal: '正常',
  disabled: '停用'
}

const queryForm = reactive({
  code: '',
  name: '',
  status: ''
})

const tableData = ref<any[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('添加设备')
const form = reactive({
  id: null as number | null,
  code: '',
  name: '',
  location: '',
  ipAddress: '',
  laneCount: 2,
  barrierType: 'mixed',
  status: 'online'
})

const getStatusType = (status: string) => {
  const map: Record<string, string> = { online: 'success', offline: 'danger', fault: 'warning', normal: 'success', disabled: 'info' }
  return map[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await request.get('/barrier/v1/devices/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        code: queryForm.code || undefined,
        name: queryForm.name || undefined,
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
  queryForm.code = ''
  queryForm.name = ''
  queryForm.status = ''
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '添加设备'
  form.id = null
  form.code = ''
  form.name = ''
  form.location = ''
  form.ipAddress = ''
  form.laneCount = 2
  form.barrierType = 'mixed'
  form.status = 'online'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑设备'
  form.id = row.id
  form.code = row.code
  form.name = row.name
  form.location = row.location || ''
  form.ipAddress = row.ipAddress || ''
  form.laneCount = row.laneCount || 2
  form.barrierType = row.barrierType || 'mixed'
  form.status = row.status
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该设备吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/barrier/v1/devices/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete:', error)
    }
  }
}

const handleToggleStatus = async (row: any) => {
  const newStatus = row.status === 'online' ? 'disabled' : 'online'
  try {
    await ElMessageBox.confirm(`确定要${newStatus === 'online' ? '启用' : '停用'}该设备吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.post(`/barrier/v1/devices/${row.id}/status?status=${newStatus}`)
    ElMessage.success('操作成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to toggle status:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/barrier/v1/devices/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/barrier/v1/devices', form)
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
.device-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
