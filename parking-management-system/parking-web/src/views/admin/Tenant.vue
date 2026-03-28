<template>
  <div class="tenant-container">
    <div class="page-header">
      <h2>租户管理</h2>
      <div class="header-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索租户名称"
          style="width: 200px; margin-right: 10px"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="searchStatus"
          placeholder="状态"
          style="width: 120px; margin-right: 10px"
          @change="handleSearch"
        >
          <el-option label="全部" value="" />
          <el-option label="启用" value="active" />
          <el-option label="禁用" value="disabled" />
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button type="primary" @click="handleAdd">新增租户</el-button>
      </div>
    </div>

    <div class="table-wrapper">
      <el-table :data="tenants" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="tenantCode" label="租户编码" width="150" />
        <el-table-column prop="tenantName" label="租户名称" min-width="150" />
        <el-table-column prop="contactName" label="联系人" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column label="套餐" width="120">
          <template #default="{ row }">
            {{ getPackageName(row.packageId) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="到期时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.expireTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link :type="row.status === 'active' ? 'danger' : 'success'" @click="handleToggleStatus(row)">
              {{ row.status === 'active' ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'add' ? '新增租户' : '编辑租户'"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="租户编码" prop="tenantCode">
          <el-input v-model="form.tenantCode" placeholder="请输入租户编码" :disabled="dialogMode === 'edit'" />
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户名称" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="form.contactName" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="contactEmail">
          <el-input v-model="form.contactEmail" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="套餐" prop="packageId">
          <el-select v-model="form.packageId" placeholder="请选择套餐" style="width: 100%">
            <el-option
              v-for="pkg in packages"
              :key="pkg.id"
              :label="pkg.packageName"
              :value="pkg.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="到期时间" prop="expireTime">
          <el-date-picker
            v-model="form.expireTime"
            type="date"
            placeholder="选择到期时间"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="最大用户数" prop="maxUsers">
          <el-input-number v-model="form.maxUsers" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="最大车位数" prop="maxSpaces">
          <el-input-number v-model="form.maxSpaces" :min="1" :max="10000" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

interface Tenant {
  id?: number
  tenantCode: string
  tenantName: string
  contactName: string
  contactPhone: string
  contactEmail: string
  packageId: number
  status: string
  expireTime: string
  maxUsers: number
  maxSpaces: number
}

interface Package {
  id: number
  packageCode: string
  packageName: string
}

const tenants = ref<Tenant[]>([])
const packages = ref<Package[]>([])
const loading = ref(false)
const submitLoading = ref(false)
const searchKeyword = ref('')
const searchStatus = ref('')
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const formRef = ref()

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive<Tenant>({
  tenantCode: '',
  tenantName: '',
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  packageId: undefined as any,
  status: 'active',
  expireTime: '',
  maxUsers: 10,
  maxSpaces: 100
})

const rules = {
  tenantCode: [
    { required: true, message: '请输入租户编码', trigger: 'blur' }
  ],
  tenantName: [
    { required: true, message: '请输入租户名称', trigger: 'blur' }
  ],
  contactPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  contactEmail: [
    { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }
  ]
}

const loadTenants = async () => {
  loading.value = true
  try {
    const res: any = await request.get('/tenant/v1/list')
    let data = res || []
    // 前端搜索过滤
    if (searchKeyword.value) {
      data = data.filter((t: Tenant) => t.tenantName?.includes(searchKeyword.value))
    }
    if (searchStatus.value) {
      data = data.filter((t: Tenant) => t.status === searchStatus.value)
    }
    // 前端分页
    pagination.total = data.length
    const start = (pagination.current - 1) * pagination.size
    const end = start + pagination.size
    tenants.value = data.slice(start, end)
  } catch (error: any) {
    ElMessage.error(error.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const loadPackages = async () => {
  try {
    const res: any = await request.get('/tenant/v1/packages')
    packages.value = res
  } catch (error) {
    console.error('Failed to load packages:', error)
  }
}

const getPackageName = (packageId: number) => {
  const pkg = packages.value.find(p => p.id === packageId)
  return pkg ? pkg.packageName : '-'
}

const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString()
}

const handleSearch = () => {
  pagination.current = 1
  loadTenants()
}

const handleSizeChange = (val: number) => {
  pagination.size = val
  loadTenants()
}

const handleCurrentChange = (val: number) => {
  pagination.current = val
  loadTenants()
}

const handleAdd = () => {
  dialogMode.value = 'add'
  Object.assign(form, {
    tenantCode: '',
    tenantName: '',
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    packageId: undefined,
    status: 'active',
    expireTime: '',
    maxUsers: 10,
    maxSpaces: 100
  })
  dialogVisible.value = true
}

const handleEdit = (row: Tenant) => {
  dialogMode.value = 'edit'
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      if (dialogMode.value === 'add') {
        await request.post('/tenant/v1', form)
        ElMessage.success('添加成功')
      } else {
        await request.put(`/tenant/v1/${form.id}`, form)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      loadTenants()
    } catch (error: any) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

const handleToggleStatus = async (row: Tenant) => {
  const newStatus = row.status === 'active' ? 'disabled' : 'active'
  const action = row.status === 'active' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(`确定要${action}租户"${row.tenantName}"吗？`, '提示')
    await request.put(`/tenant/v1/status/${row.id}`, { status: newStatus })
    ElMessage.success(`${action}成功`)
    loadTenants()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const handleDelete = async (row: Tenant) => {
  try {
    await ElMessageBox.confirm(`确定要删除租户"${row.tenantName}"吗？`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/tenant/v1/${row.id}`)
    ElMessage.success('删除成功')
    loadTenants()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

onMounted(() => {
  loadTenants()
  loadPackages()
})
</script>

<style scoped>
.tenant-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  color: var(--el-text-color-primary);
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
}

.table-wrapper {
  background: var(--el-bg-color);
  border-radius: 8px;
  padding: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
