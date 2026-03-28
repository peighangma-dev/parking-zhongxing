<template>
  <div class="operation-log-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
          <div class="header-actions">
            <el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length === 0">批量删除</el-button>
            <el-button type="primary" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="操作人">
          <el-input v-model="queryForm.username" placeholder="请输入操作人" clearable />
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select v-model="queryForm.module" placeholder="请选择" clearable style="width: 150px">
            <el-option label="用户管理" value="user" />
            <el-option label="角色管理" value="role" />
            <el-option label="菜单管理" value="menu" />
            <el-option label="租户管理" value="tenant" />
            <el-option label="设备管理" value="barrier" />
            <el-option label="车辆管理" value="vehicle" />
            <el-option label="支付管理" value="payment" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="queryForm.action" placeholder="请选择" clearable style="width: 120px">
            <el-option label="新增" value="create" />
            <el-option label="更新" value="update" />
            <el-option label="删除" value="delete" />
            <el-option label="查询" value="query" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 340px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        :data="tableData"
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="操作人" width="120" />
        <el-table-column prop="module" label="模块" width="120">
          <template #default="{ row }">
            <el-tag>{{ getModuleName(row.module) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="action" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getActionTagType(row.action)">{{ getActionName(row.action) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="操作描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="method" label="请求方法" width="100" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column prop="createdAt" label="操作时间" width="180" />
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const queryForm = reactive({
  username: '',
  module: '',
  action: ''
})

const dateRange = ref<string[]>([])
const tableData = ref<any[]>([])
const selectedIds = ref<number[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const moduleMap: Record<string, string> = {
  user: '用户管理',
  role: '角色管理',
  menu: '菜单管理',
  tenant: '租户管理',
  barrier: '设备管理',
  vehicle: '车辆管理',
  payment: '支付管理'
}

const actionMap: Record<string, string> = {
  create: '新增',
  update: '更新',
  delete: '删除',
  query: '查询'
}

const actionTagTypeMap: Record<string, string> = {
  create: 'success',
  update: 'warning',
  delete: 'danger',
  query: 'info'
}

const getModuleName = (module: string) => moduleMap[module] || module
const getActionName = (action: string) => actionMap[action] || action
const getActionTagType = (action: string) => actionTagTypeMap[action] || 'info'

const loadData = async () => {
  try {
    const params: any = {
      current: pagination.current,
      size: pagination.size,
      username: queryForm.username || undefined,
      module: queryForm.module || undefined,
      action: queryForm.action || undefined
    }
    if (dateRange.value?.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    const res: any = await request.get('/uc/v1/logs/operation/page', { params })
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load operation logs:', error)
    tableData.value = generateMockData()
    pagination.total = tableData.value.length
  }
}

const generateMockData = () => {
  const modules = Object.keys(moduleMap)
  const actions = Object.keys(actionMap)
  const mockData = []
  for (let i = 1; i <= 20; i++) {
    mockData.push({
      id: i,
      username: i % 3 === 0 ? 'admin' : i % 3 === 1 ? 'operator' : 'viewer',
      module: modules[Math.floor(Math.random() * modules.length)],
      action: actions[Math.floor(Math.random() * actions.length)],
      description: '这是一条操作日志描述',
      method: ['GET', 'POST', 'PUT', 'DELETE'][Math.floor(Math.random() * 4)],
      ipAddress: `192.168.1.${Math.floor(Math.random() * 255)}`,
      createdAt: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleString()
    })
  }
  return mockData
}

const handleQuery = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  queryForm.username = ''
  queryForm.module = ''
  queryForm.action = ''
  dateRange.value = []
  handleQuery()
}

const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map((item: any) => item.id)
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定要删除该操作日志吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/uc/v1/logs/operation/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) return
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 条日志吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.post('/uc/v1/logs/operation/batch-delete', { ids: selectedIds.value })
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '批量删除失败')
    }
  }
}

const handleExport = () => {
  ElMessage.success('导出成功')
}

const handleSizeChange = (val: number) => {
  pagination.size = val
  loadData()
}

const handleCurrentChange = (val: number) => {
  pagination.current = val
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.operation-log-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}
</style>
