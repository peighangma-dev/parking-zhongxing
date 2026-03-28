<template>
  <div class="role-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">添加角色</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="roleCode" label="角色编码" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'normal' ? 'success' : 'info'">
              {{ row.status === 'normal' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link @click="handlePermission(row)">权限配置</el-button>
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
        <el-form-item label="角色编码">
          <el-input v-model="form.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
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

    <el-dialog v-model="permissionDialogVisible" :title="`${currentRole?.roleName} - 权限配置`" width="600px">
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        :props="{ label: 'menuName', children: 'children' }"
        node-key="id"
        :default-checked-keys="selectedMenuIds"
        show-checkbox
        check-strictly
        style="max-height: 400px; overflow-y: auto"
      />
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePermission">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref<any[]>([])
const menuTree = ref<any[]>([])
const selectedMenuIds = ref<any[]>([])
const menuTreeRef = ref()
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const permissionDialogVisible = ref(false)
const dialogTitle = ref('添加角色')
const currentRole = ref<any>(null)
const form = reactive({
  id: null as number | null,
  roleCode: '',
  roleName: '',
  description: '',
  status: 'normal'
})

const loadData = async () => {
  try {
    const res = await request.get('/uc/v1/roles/page', {
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

const loadMenuTree = async () => {
  try {
    const res = await request.get('/uc/v1/menus/tree')
    menuTree.value = res || []
  } catch (error) {
    console.error('Failed to load menu tree:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '添加角色'
  form.id = null
  form.roleCode = ''
  form.roleName = ''
  form.description = ''
  form.status = 'normal'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑角色'
  form.id = row.id
  form.roleCode = row.roleCode
  form.roleName = row.roleName
  form.description = row.description || ''
  form.status = row.status
  dialogVisible.value = true
}

const handlePermission = async (row: any) => {
  currentRole.value = row
  permissionDialogVisible.value = true
  try {
    const res = await request.get(`/uc/v1/menus/by-role/${row.id}`)
    selectedMenuIds.value = res || []
  } catch (error) {
    selectedMenuIds.value = []
  }
}

const handleSavePermission = async () => {
  const checkedKeys = menuTreeRef.value?.getCheckedKeys() || []
  try {
    await request.put(`/uc/v1/menus/role/${currentRole.value.id}/menus`, checkedKeys)
    ElMessage.success('权限保存成功')
    permissionDialogVisible.value = false
  } catch (error) {
    console.error('Failed to save permission:', error)
  }
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/uc/v1/roles/${row.id}`)
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
      await request.put(`/uc/v1/roles/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/uc/v1/roles', form)
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
  loadMenuTree()
})
</script>

<style scoped>
.role-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
