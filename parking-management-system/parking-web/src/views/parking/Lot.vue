<template>
  <div class="lot-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>停车场设置</span>
          <el-button type="primary" @click="handleAdd">添加停车场</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="lotName" label="停车场名称" />
        <el-table-column prop="lotCode" label="停车场编码" />
        <el-table-column prop="address" label="地址" show-overflow-tooltip />
        <el-table-column prop="totalSpaces" label="总车位" />
        <el-table-column prop="hourlyRate" label="每小时费率(元)">
          <template #default="{ row }">
            {{ row.hourlyRate || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'enabled' ? 'success' : 'info'">
              {{ row.status === 'enabled' ? '启用' : '停用' }}
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
        <el-form-item label="停车场名称">
          <el-input v-model="form.lotName" placeholder="请输入停车场名称" />
        </el-form-item>
        <el-form-item label="停车场编码">
          <el-input v-model="form.lotCode" placeholder="请输入停车场编码" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="总车位数">
          <el-input-number v-model="form.totalSpaces" :min="1" />
        </el-form-item>
        <el-form-item label="每小时费率">
          <el-input-number v-model="form.hourlyRate" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="开放时间">
          <el-input v-model="form.openTime" placeholder="如: 00:00-24:00" />
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
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref<any[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('添加停车场')
const form = reactive({
  id: null as number | null,
  lotName: '',
  lotCode: '',
  address: '',
  totalSpaces: 100,
  hourlyRate: 5,
  openTime: '00:00-24:00',
  status: 'enabled'
})

const loadData = async () => {
  try {
    const res = await request.get('/uc/v1/lots/page', {
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
  dialogTitle.value = '添加停车场'
  form.id = null
  form.lotName = ''
  form.lotCode = ''
  form.address = ''
  form.totalSpaces = 100
  form.hourlyRate = 5
  form.openTime = '00:00-24:00'
  form.status = 'enabled'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑停车场'
  form.id = row.id
  form.lotName = row.lotName
  form.lotCode = row.lotCode
  form.address = row.address || ''
  form.totalSpaces = row.totalSpaces || 100
  form.hourlyRate = row.hourlyRate || 5
  form.openTime = row.openTime || '00:00-24:00'
  form.status = row.status
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该停车场吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/uc/v1/lots/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/uc/v1/lots/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/uc/v1/lots', form)
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
.lot-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
