<template>
  <div class="kiosk-container">
    <el-card class="header-card">
      <div class="header-actions">
        <div class="stats">
          <div class="stat-item">
            <span class="stat-label">在线岗亭</span>
            <span class="stat-value online">{{ onlineCount }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">离线岗亭</span>
            <span class="stat-value offline">{{ offlineCount }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">总计</span>
            <span class="stat-value">{{ totalCount }}</span>
          </div>
        </div>
        <el-button type="primary" @click="openDialog('create')">
          <el-icon><Plus /></el-icon>
          添加岗亭
        </el-button>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table :data="kioskList" stripe style="width: 100%">
        <el-table-column prop="kioskCode" label="岗亭编码" width="150" />
        <el-table-column prop="kioskName" label="岗亭名称" width="180" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'online' ? 'success' : 'danger'" size="small">
              {{ row.status === 'online' ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="150" />
        <el-table-column prop="lastHeartbeat" label="最后心跳" width="180">
          <template #default="{ row }">
            {{ formatTime(row.lastHeartbeat) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openDialog('edit', row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="岗亭编码" prop="kioskCode">
          <el-input v-model="form.kioskCode" placeholder="请输入岗亭编码" />
        </el-form-item>
        <el-form-item label="岗亭名称" prop="kioskName">
          <el-input v-model="form.kioskName" placeholder="请输入岗亭名称" />
        </el-form-item>
        <el-form-item label="IP地址" prop="ipAddress">
          <el-input v-model="form.ipAddress" placeholder="岗亭端IP地址" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const kioskList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)

const form = reactive({
  id: null,
  kioskCode: '',
  kioskName: '',
  ipAddress: '',
  remark: ''
})

const rules = {
  kioskCode: [{ required: true, message: '请输入岗亭编码', trigger: 'blur' }],
  kioskName: [{ required: true, message: '请输入岗亭名称', trigger: 'blur' }]
}

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const onlineCount = computed(() => kioskList.value.filter(k => k.status === 'online').length)
const offlineCount = computed(() => kioskList.value.filter(k => k.status === 'offline').length)
const totalCount = computed(() => kioskList.value.length)

const dialogTitle = computed(() => dialogType.value === 'create' ? '添加岗亭' : '编辑岗亭')

const loadData = async () => {
  try {
    const res = await request.get('/monitor/v1/kiosk/page', {
      params: {
        current: pagination.current,
        size: pagination.size
      }
    })
    kioskList.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load kiosk data:', error)
  }
}

const openDialog = (type, row = null) => {
  dialogType.value = type
  if (type === 'edit' && row) {
    Object.assign(form, {
      id: row.id,
      kioskCode: row.kioskCode,
      kioskName: row.kioskName,
      ipAddress: row.ipAddress || '',
      remark: row.remark || ''
    })
  } else {
    Object.assign(form, {
      id: null,
      kioskCode: '',
      kioskName: '',
      ipAddress: '',
      remark: ''
    })
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    try {
      if (dialogType.value === 'create') {
        await request.post('/monitor/v1/kiosk', form)
        ElMessage.success('添加成功')
      } else {
        await request.put(`/monitor/v1/kiosk/${form.id}`, form)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该岗亭吗？', '提示', {
      type: 'warning'
    })
    await request.delete(`/monitor/v1/kiosk/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const formatTime = (time) => {
  if (!time) return '--'
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.kiosk-container {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 12px;
  color: var(--cyber-text-dim);
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
}

.stat-value.online {
  color: var(--cyber-green);
}

.stat-value.offline {
  color: var(--cyber-red);
}

.table-card {
  background: var(--cyber-glass);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
