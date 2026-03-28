<template>
  <div class="member-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>月卡管理</span>
          <el-button type="primary" @click="handleAdd">开通月卡</el-button>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="会员类型">
          <el-select v-model="queryForm.memberType" placeholder="请选择" clearable>
            <el-option label="月卡" value="monthly" />
            <el-option label="季卡" value="seasonal" />
            <el-option label="年卡" value="annual" />
            <el-option label="VIP" value="vip" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" />
        <el-table-column prop="memberType" label="套餐类型">
          <template #default="{ row }">
            <el-tag>{{ getMemberTypeName(row.memberType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="生效日期" />
        <el-table-column prop="endDate" label="到期日期" />
        <el-table-column prop="balance" label="账户余额">
          <template #default="{ row }">
            ¥ {{ (row.balance || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleRenew(row)">续期</el-button>
            <el-button type="warning" link @click="handleEdit(row)">编辑</el-button>
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
        <el-form-item label="会员类型">
          <el-select v-model="form.memberType" placeholder="请选择">
            <el-option label="月卡" value="monthly" />
            <el-option label="季卡" value="seasonal" />
            <el-option label="年卡" value="annual" />
            <el-option label="VIP" value="vip" />
          </el-select>
        </el-form-item>
        <el-form-item label="生效日期">
          <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="到期日期">
          <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="账户余额">
          <el-input-number v-model="form.balance" :min="0" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="renewDialogVisible" title="续期" width="400px">
      <el-form :model="renewForm" label-width="80px">
        <el-form-item label="新到期日期">
          <el-date-picker v-model="renewForm.newEndDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="renewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRenewSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const queryForm = reactive({
  memberType: '',
  status: ''
})

const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const renewDialogVisible = ref(false)
const dialogTitle = ref('开通月卡')
const renewForm = reactive({
  id: null as number | null,
  newEndDate: ''
})

const form = reactive({
  id: null as number | null,
  plateNumber: '',
  memberType: '',
  startDate: '',
  endDate: '',
  balance: 0
})

const memberTypeMap: Record<string, string> = {
  monthly: '月卡',
  seasonal: '季卡',
  annual: '年卡',
  vip: 'VIP'
}

const statusMap: Record<string, string> = {
  active: '有效',
  expired: '已过期',
  cancelled: '已取消'
}

const statusTypeMap: Record<string, string> = {
  active: 'success',
  expired: 'danger',
  cancelled: 'info'
}

const getMemberTypeName = (type: string) => memberTypeMap[type] || type
const getStatusName = (status: string) => statusMap[status] || status
const getStatusType = (status: string) => statusTypeMap[status] || 'info'

const loadData = async () => {
  try {
    const res = await request.get('/vehicle/v1/members/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        memberType: queryForm.memberType || undefined,
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
  queryForm.memberType = ''
  queryForm.status = ''
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '开通月卡'
  form.id = null
  form.plateNumber = ''
  form.memberType = ''
  form.startDate = ''
  form.endDate = ''
  form.balance = 0
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑月卡'
  form.id = row.id
  form.plateNumber = row.plateNumber
  form.memberType = row.memberType
  form.startDate = row.startDate
  form.endDate = row.endDate
  form.balance = row.balance
  dialogVisible.value = true
}

const handleRenew = (row: any) => {
  renewForm.id = row.id
  renewForm.newEndDate = ''
  renewDialogVisible.value = true
}

const handleRenewSubmit = async () => {
  try {
    await request.put(`/vehicle/v1/members/${renewForm.id}/renew?newEndDate=${renewForm.newEndDate}`)
    ElMessage.success('续期成功')
    renewDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to renew:', error)
  }
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/vehicle/v1/members/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/vehicle/v1/members/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/vehicle/v1/members', form)
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
.member-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
