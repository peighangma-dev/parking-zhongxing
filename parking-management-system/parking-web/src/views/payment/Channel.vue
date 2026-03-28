<template>
  <div class="channel-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>支付渠道列表</span>
          <el-button type="primary" @click="handleAdd">添加渠道</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="channelCode" label="渠道编码" />
        <el-table-column prop="channelName" label="渠道名称" />
        <el-table-column prop="feeRate" label="手续费率">
          <template #default="{ row }">
            {{ ((row.feeRate || 0) * 100).toFixed(2) }}%
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'enabled' ? 'success' : 'info'">
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
      <el-form :model="form" label-width="80px">
        <el-form-item label="渠道编码">
          <el-input v-model="form.channelCode" placeholder="请输入渠道编码，如 WECHAT" />
        </el-form-item>
        <el-form-item label="渠道名称">
          <el-input v-model="form.channelName" placeholder="请输入渠道名称" />
        </el-form-item>
        <el-form-item label="手续费率">
          <el-input-number v-model="form.feeRate" :min="0" :max="1" :precision="4" :step="0.0001" />
          <span style="margin-left: 10px">{{ (form.feeRate * 100).toFixed(2) }}%</span>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
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
const dialogTitle = ref('添加渠道')
const form = reactive({
  id: null as number | null,
  channelCode: '',
  channelName: '',
  feeRate: 0,
  sortOrder: 0,
  status: 'enabled'
})

const loadData = async () => {
  try {
    const res = await request.get('/payment/v1/channels/page', {
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
  dialogTitle.value = '添加渠道'
  form.id = null
  form.channelCode = ''
  form.channelName = ''
  form.feeRate = 0
  form.sortOrder = 0
  form.status = 'enabled'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑渠道'
  form.id = row.id
  form.channelCode = row.channelCode
  form.channelName = row.channelName
  form.feeRate = row.feeRate || 0
  form.sortOrder = row.sortOrder || 0
  form.status = row.status
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该支付渠道吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/payment/v1/channels/${row.id}`)
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
      await request.put(`/payment/v1/channels/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/payment/v1/channels', form)
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
.channel-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
