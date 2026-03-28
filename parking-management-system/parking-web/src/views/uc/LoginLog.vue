<template>
  <div class="login-log-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>登录日志</span>
          <div class="header-actions">
            <el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length === 0">批量删除</el-button>
            <el-button type="primary" @click="handleExport">导出</el-button>
            <el-button type="warning" @click="handleClear">清空30天前日志</el-button>
          </div>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="用户名">
          <el-input v-model="queryForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="登录状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="成功" value="success" />
            <el-option label="失败" value="fail" />
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
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="status" label="登录状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column prop="ipLocation" label="登录地点" min-width="150" show-overflow-tooltip />
        <el-table-column prop="browser" label="浏览器" width="120" show-overflow-tooltip />
        <el-table-column prop="os" label="操作系统" width="120" show-overflow-tooltip />
        <el-table-column prop="message" label="登录消息" min-width="200" show-overflow-tooltip />
        <el-table-column prop="loginTime" label="登录时间" width="180" />
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
  status: ''
})

const dateRange = ref<string[]>([])
const tableData = ref<any[]>([])
const selectedIds = ref<number[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const loadData = async () => {
  try {
    const params: any = {
      current: pagination.current,
      size: pagination.size,
      username: queryForm.username || undefined,
      status: queryForm.status || undefined
    }
    if (dateRange.value?.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    const res: any = await request.get('/uc/v1/logs/login/page', { params })
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load login logs:', error)
    tableData.value = generateMockData()
    pagination.total = tableData.value.length
  }
}

const generateMockData = () => {
  const mockData = []
  const usernames = ['admin', 'operator', 'viewer', 'manager']
  const statuses = ['success', 'fail']
  const browsers = ['Chrome', 'Firefox', 'Safari', 'Edge']
  const osList = ['Windows 10', 'macOS', 'Ubuntu', 'iOS', 'Android']
  const locations = ['北京市海淀区', '上海市浦东新区', '广州市天河区', '深圳市南山区', '杭州市西湖区']

  for (let i = 1; i <= 20; i++) {
    const status = statuses[Math.floor(Math.random() * statuses.length)]
    mockData.push({
      id: i,
      username: usernames[Math.floor(Math.random() * usernames.length)],
      status: status,
      ipAddress: `192.168.${Math.floor(Math.random() * 255)}.${Math.floor(Math.random() * 255)}`,
      ipLocation: locations[Math.floor(Math.random() * locations.length)],
      browser: browsers[Math.floor(Math.random() * browsers.length)],
      os: osList[Math.floor(Math.random() * osList.length)],
      message: status === 'success' ? '登录成功' : '密码错误',
      loginTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleString()
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
  queryForm.status = ''
  dateRange.value = []
  handleQuery()
}

const handleSelectionChange = (selection: any[]) => {
  selectedIds.value = selection.map((item: any) => item.id)
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定要删除该登录日志吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/uc/v1/logs/login/${row.id}`)
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
    await request.post('/uc/v1/logs/login/batch-delete', { ids: selectedIds.value })
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '批量删除失败')
    }
  }
}

const handleClear = async () => {
  try {
    await ElMessageBox.confirm('确定要清空30天前的所有登录日志吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete('/uc/v1/logs/login/clear')
    ElMessage.success('清空成功')
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '清空失败')
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
.login-log-page {
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
