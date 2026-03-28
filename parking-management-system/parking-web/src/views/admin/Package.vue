<template>
  <div class="package-container">
    <div class="page-header">
      <h2>套餐管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">新增套餐</el-button>
      </div>
    </div>

    <div class="table-wrapper">
      <el-table :data="packages" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="packageCode" label="套餐编码" width="120" />
        <el-table-column prop="packageName" label="套餐名称" min-width="120" />
        <el-table-column prop="packageType" label="类型" width="100" />
        <el-table-column label="价格" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="maxUsers" label="最大用户" width="100" />
        <el-table-column prop="maxSpaces" label="最大车位" width="100" />
        <el-table-column prop="maxDevices" label="最大设备" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'enabled' ? 'success' : 'danger'">
              {{ row.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="排序" width="80" prop="sortOrder" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'add' ? '新增套餐' : '编辑套餐'"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="套餐编码" prop="packageCode">
          <el-input v-model="form.packageCode" placeholder="请输入套餐编码" :disabled="dialogMode === 'edit'" />
        </el-form-item>
        <el-form-item label="套餐名称" prop="packageName">
          <el-input v-model="form.packageName" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-form-item label="套餐类型" prop="packageType">
          <el-select v-model="form.packageType" placeholder="请选择套餐类型" style="width: 100%">
            <el-option label="基础版" value="basic" />
            <el-option label="标准版" value="standard" />
            <el-option label="高级版" value="premium" />
            <el-option label="旗舰版" value="enterprise" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="最大用户数" prop="maxUsers">
          <el-input-number v-model="form.maxUsers" :min="1" />
        </el-form-item>
        <el-form-item label="最大车位数" prop="maxSpaces">
          <el-input-number v-model="form.maxSpaces" :min="1" />
        </el-form-item>
        <el-form-item label="最大设备数" prop="maxDevices">
          <el-input-number v-model="form.maxDevices" :min="1" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="enabled">启用</el-radio>
            <el-radio label="disabled">禁用</el-radio>
          </el-radio-group>
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

interface Package {
  id?: number
  packageCode: string
  packageName: string
  packageType: string
  description: string
  maxUsers: number
  maxSpaces: number
  maxDevices: number
  price: number
  sortOrder: number
  status: string
}

const packages = ref<Package[]>([])
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const formRef = ref()

const form = reactive<Package>({
  packageCode: '',
  packageName: '',
  packageType: 'basic',
  description: '',
  maxUsers: 10,
  maxSpaces: 100,
  maxDevices: 10,
  price: 0,
  sortOrder: 0,
  status: 'enabled'
})

const rules = {
  packageCode: [
    { required: true, message: '请输入套餐编码', trigger: 'blur' }
  ],
  packageName: [
    { required: true, message: '请输入套餐名称', trigger: 'blur' }
  ],
  packageType: [
    { required: true, message: '请选择套餐类型', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ]
}

const loadPackages = async () => {
  loading.value = true
  try {
    const res: any = await request.get('/package/v1/page', { current: 1, size: 100 })
    packages.value = res.records
  } catch (error: any) {
    ElMessage.error(error.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogMode.value = 'add'
  Object.assign(form, {
    packageCode: '',
    packageName: '',
    packageType: 'basic',
    description: '',
    maxUsers: 10,
    maxSpaces: 100,
    maxDevices: 10,
    price: 0,
    sortOrder: 0,
    status: 'enabled'
  })
  dialogVisible.value = true
}

const handleEdit = (row: Package) => {
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
        await request.post('/package/v1', form)
        ElMessage.success('添加成功')
      } else {
        await request.put(`/package/v1/${form.id}`, form)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      loadPackages()
    } catch (error: any) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDelete = async (row: Package) => {
  try {
    await ElMessageBox.confirm(`确定要删除套餐"${row.packageName}"吗？`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/package/v1/${row.id}`)
    ElMessage.success('删除成功')
    loadPackages()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

onMounted(() => {
  loadPackages()
})
</script>

<style scoped>
.package-container {
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
</style>
