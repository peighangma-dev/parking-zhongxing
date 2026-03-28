<template>
  <div class="user-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAdd">新增用户</el-button>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="用户名">
          <el-input v-model="queryForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'normal' ? 'success' : 'danger'">
              {{ row.status === 'normal' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link @click="handleAssignRoles(row)">分配角色</el-button>
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
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="!!form.id" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="roleDialogVisible" title="分配角色" width="500px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="用户名">
          <span>{{ currentUser?.username }}</span>
        </el-form-item>
        <el-form-item label="选择角色">
          <el-checkbox-group v-model="roleForm.roleIds">
            <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id">{{ role.roleName }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRoles">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const queryForm = reactive({
  username: '',
  nickname: ''
})

const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const roleDialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const currentUser = ref<any>(null)
const allRoles = ref<any[]>([])
const form = reactive({
  id: null as number | null,
  username: '',
  password: '',
  nickname: '',
  phone: '',
  email: ''
})
const roleForm = reactive({
  roleIds: [] as number[]
})

const loadData = async () => {
  try {
    const res = await request.get('/uc/v1/users/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        username: queryForm.username || undefined
      }
    })
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

const loadAllRoles = async () => {
  try {
    const res = await request.get('/uc/v1/roles/all')
    allRoles.value = res || []
  } catch (error) {
    console.error('Failed to load roles:', error)
  }
}

const handleQuery = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  queryForm.username = ''
  queryForm.nickname = ''
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  form.id = null
  form.username = ''
  form.password = ''
  form.nickname = ''
  form.phone = ''
  form.email = ''
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑用户'
  form.id = row.id
  form.username = row.username
  form.password = ''
  form.nickname = row.nickname
  form.phone = row.phone
  form.email = row.email
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/uc/v1/users/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
  }
}

const handleAssignRoles = async (row: any) => {
  currentUser.value = row
  roleDialogVisible.value = true
  try {
    const res = await request.get(`/uc/v1/users/${row.id}/roles`)
    roleForm.roleIds = res || []
  } catch (error) {
    roleForm.roleIds = []
  }
}

const handleSaveRoles = async () => {
  try {
    await request.put(`/uc/v1/users/${currentUser.value.id}/roles`, roleForm.roleIds)
    ElMessage.success('角色分配成功')
    roleDialogVisible.value = false
  } catch (error) {
    console.error('Failed to assign roles:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/uc/v1/users/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/uc/v1/users', form)
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
  loadAllRoles()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
