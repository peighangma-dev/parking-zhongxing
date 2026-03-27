<template>
  <div class="parking-lot-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>停车场管理</span>
          <el-button type="primary" @click="handleAdd">新增停车场</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="lotName" label="停车场名称" />
        <el-table-column prop="lotCode" label="编码" />
        <el-table-column prop="address" label="地址" show-overflow-tooltip />
        <el-table-column prop="totalSpaces" label="总车位" width="100" />
        <el-table-column prop="occupiedSpaces" label="已占用" width="100" />
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="停车场名称">
          <el-input v-model="form.lotName" placeholder="请输入停车场名称" />
        </el-form-item>
        <el-form-item label="停车场编码">
          <el-input v-model="form.lotCode" placeholder="请输入编码" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="总车位数">
          <el-input-number v-model="form.totalSpaces" :min="0" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="active">启用</el-radio>
            <el-radio label="inactive">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
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
const dialogTitle = ref('新增停车场')
const form = reactive({
  id: null as number | null,
  lotName: '',
  lotCode: '',
  address: '',
  totalSpaces: 0,
  occupiedSpaces: 0,
  contactPerson: '',
  contactPhone: '',
  status: 'active',
  description: ''
})

const loadData = async () => {
  try {
    const res = await request.get('/uc/v1/lots/page', {
      params: {
        current: pagination.current,
        size: pagination.size
      }
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增停车场'
  form.id = null
  form.lotName = ''
  form.lotCode = ''
  form.address = ''
  form.totalSpaces = 0
  form.occupiedSpaces = 0
  form.contactPerson = ''
  form.contactPhone = ''
  form.status = 'active'
  form.description = ''
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑停车场'
  form.id = row.id
  form.lotName = row.lotName
  form.lotCode = row.lotCode
  form.address = row.address
  form.totalSpaces = row.totalSpaces
  form.occupiedSpaces = row.occupiedSpaces
  form.contactPerson = row.contactPerson
  form.contactPhone = row.contactPhone
  form.status = row.status
  form.description = row.description
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/uc/v1/lots/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
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
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
