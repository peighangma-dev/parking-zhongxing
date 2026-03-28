<template>
  <div class="lane-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>车道管理</span>
          <el-button type="primary" @click="handleAdd">新增车道</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="laneName" label="车道名称" />
        <el-table-column prop="laneType" label="车道类型">
          <template #default="{ row }">
            <el-tag :type="row.laneType === 'in' ? 'success' : 'warning'">
              {{ row.laneType === 'in' ? '入口' : '出口' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="barrierName" label="所属道闸" />
        <el-table-column prop="cameraId" label="摄像头ID" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'normal' ? 'success' : 'danger'">
              {{ row.status === 'normal' ? '正常' : '故障' }}
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
        <el-form-item label="车道名称">
          <el-input v-model="form.laneName" placeholder="请输入车道名称" />
        </el-form-item>
        <el-form-item label="车道类型">
          <el-select v-model="form.laneType" placeholder="请选择">
            <el-option label="入口" value="in" />
            <el-option label="出口" value="out" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属道闸">
          <el-select v-model="form.barrierId" placeholder="请选择">
            <el-option v-for="b in barriers" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="摄像头ID">
          <el-input v-model="form.cameraId" placeholder="请输入摄像头ID" />
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
const barriers = ref<any[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增车道')
const form = reactive({
  id: null as number | null,
  laneName: '',
  laneType: 'in',
  barrierId: null as number | null,
  cameraId: ''
})

const loadData = async () => {
  try {
    const res = await request.get('/barrier/v1/lanes/page', {
      params: {
        current: pagination.current,
        size: pagination.size
      }
    })
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load data:', error)
    tableData.value = []
    pagination.total = 0
  }
}

const loadBarriers = async () => {
  try {
    const res = await request.get('/barrier/v1/devices/page', {
      params: { current: 1, size: 100 }
    })
    barriers.value = res.records || []
  } catch (error) {
    console.error('Failed to load barriers:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增车道'
  form.id = null
  form.laneName = ''
  form.laneType = 'in'
  form.barrierId = null
  form.cameraId = ''
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑车道'
  form.id = row.id
  form.laneName = row.laneName
  form.laneType = row.laneType
  form.barrierId = row.barrierId
  form.cameraId = row.cameraId
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/barrier/v1/lanes/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/barrier/v1/lanes/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/barrier/v1/lanes', form)
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
  loadBarriers()
})
</script>

<style scoped>
.lane-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
