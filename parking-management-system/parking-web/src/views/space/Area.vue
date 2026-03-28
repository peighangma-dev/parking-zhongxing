<template>
  <div class="area-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>区域管理</span>
          <el-button type="primary" @click="handleAdd">新增区域</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="areaName" label="区域名称" />
        <el-table-column prop="floor" label="楼层" />
        <el-table-column prop="totalSpaces" label="总车位数" />
        <el-table-column prop="usedSpaces" label="已用车位">
          <template #default="{ row }">
            <el-text :type="row.usedSpaces > row.totalSpaces * 0.8 ? 'danger' : 'success'">
              {{ row.usedSpaces }}
            </el-text>
          </template>
        </el-table-column>
        <el-table-column prop="availableSpaces" label="可用车位">
          <template #default="{ row }">
            {{ row.totalSpaces - row.usedSpaces }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'normal' ? 'success' : 'danger'">
              {{ row.status === 'normal' ? '正常' : '停用' }}
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
        <el-form-item label="区域名称">
          <el-input v-model="form.areaName" placeholder="请输入区域名称" />
        </el-form-item>
        <el-form-item label="楼层">
          <el-input-number v-model="form.floor" :min="-1" :max="100" />
        </el-form-item>
        <el-form-item label="总车位数">
          <el-input-number v-model="form.totalSpaces" :min="1" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" active-value="normal" inactive-value="disabled" />
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
  areaName: '',
  floor: ''
})

const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增区域')
const form = reactive({
  id: null as number | null,
  areaName: '',
  floor: 1,
  totalSpaces: 50,
  usedSpaces: 0,
  status: 'normal'
})

const loadData = async () => {
  try {
    const res = await request.get('/space/v1/areas/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        areaName: queryForm.areaName || undefined,
        floor: queryForm.floor || undefined
      }
    })
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增区域'
  form.id = null
  form.areaName = ''
  form.floor = 1
  form.totalSpaces = 50
  form.usedSpaces = 0
  form.status = 'normal'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑区域'
  form.id = row.id
  form.areaName = row.areaName
  form.floor = row.floor
  form.totalSpaces = row.totalSpaces
  form.usedSpaces = row.usedSpaces
  form.status = row.status
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/space/v1/areas/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/space/v1/areas/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/space/v1/areas', form)
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
.area-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
