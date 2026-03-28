<template>
  <div class="menu-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleAdd(null)">新增菜单</el-button>
        </div>
      </template>
      <el-table
        :data="tableData"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        default-expand-all
        stripe
        style="width: 100%"
      >
        <el-table-column prop="menuName" label="菜单名称" min-width="150">
          <template #default="{ row }">
            <el-icon v-if="row.icon" style="margin-right: 8px">
              <component :is="row.icon" />
            </el-icon>
            {{ row.menuName }}
          </template>
        </el-table-column>
        <el-table-column prop="menuType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.menuType)">
              {{ getTypeName(row.menuType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" min-width="150" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="180" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'" size="small">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleAdd(row)" v-if="row.menuType !== 'button'">新增子菜单</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType" @change="handleTypeChange">
            <el-radio label="directory">目录</el-radio>
            <el-radio label="menu">菜单</el-radio>
            <el-radio label="button">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="treeSelectData"
            :props="{ label: 'menuName', value: 'id', children: 'children' }"
            check-strictly
            placeholder="选择上级菜单"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path" v-if="form.menuType !== 'button'">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="form.menuType === 'menu'">
          <el-input v-model="form.component" placeholder="请输入组件路径，如: views/xxx/Index.vue" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission" v-if="form.menuType === 'button'">
          <el-input v-model="form.permission" placeholder="请输入权限标识，如: system:user:delete" />
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="form.menuType !== 'button'">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="active">启用</el-radio>
            <el-radio label="inactive">禁用</el-radio>
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
import { reactive, ref, computed, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

interface MenuItem {
  id: number
  parentId: number | null
  menuName: string
  menuType: string
  path: string
  component: string
  icon: string
  sortOrder: number
  status: string
  permission: string
  children?: MenuItem[]
}

const tableData = ref<MenuItem[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const form = reactive<MenuItem>({
  id: 0,
  parentId: null,
  menuName: '',
  menuType: 'menu',
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  status: 'active',
  permission: ''
})

const rules: FormRules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }]
}

const treeSelectData = computed(() => {
  const result: MenuItem[] = [{ id: 0, parentId: null, menuName: '顶级菜单', menuType: 'directory', path: '', component: '', icon: '', sortOrder: 0, status: 'active', permission: '' }]
  const flatten = (items: MenuItem[], prefix: MenuItem[] = []): MenuItem[] => {
    let res: MenuItem[] = []
    for (const item of items) {
      const newItem = { ...item, menuName: [...prefix.map(() => '　'), item.menuName].join('') }
      res.push(newItem)
      if (item.children?.length) {
        res = res.concat(flatten(item.children, [...prefix, item]))
      }
    }
    return res
  }
  return result.concat(flatten(tableData.value))
})

const getTypeName = (type: string) => {
  const map: Record<string, string> = { directory: '目录', menu: '菜单', button: '按钮' }
  return map[type] || type
}

const getTypeTagType = (type: string) => {
  const map: Record<string, string> = { directory: 'warning', menu: '', button: 'info' }
  return map[type] || ''
}

const loadData = async () => {
  try {
    const res = await request.get('/uc/v1/menus/tree')
    tableData.value = res || []
  } catch (error) {
    console.error('Failed to load menu data:', error)
  }
}

const handleTypeChange = (type: string) => {
  if (type === 'directory') {
    form.component = ''
    form.permission = ''
  } else if (type === 'button') {
    form.path = ''
    form.component = ''
    form.icon = ''
  }
}

const handleAdd = (parent: MenuItem | null) => {
  dialogTitle.value = parent ? `新增子菜单 - ${parent.menuName}` : '新增菜单'
  form.id = 0
  form.parentId = parent?.id || null
  form.menuName = ''
  form.menuType = parent?.menuType === 'directory' ? 'menu' : (parent?.menuType || 'menu')
  form.path = ''
  form.component = ''
  form.icon = parent?.icon || ''
  form.sortOrder = 0
  form.status = 'active'
  form.permission = ''
  dialogVisible.value = true
}

const handleEdit = (row: MenuItem) => {
  dialogTitle.value = `编辑菜单 - ${row.menuName}`
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId,
    menuName: row.menuName,
    menuType: row.menuType,
    path: row.path || '',
    component: row.component || '',
    icon: row.icon || '',
    sortOrder: row.sortOrder || 0,
    status: row.status,
    permission: row.permission || ''
  })
  dialogVisible.value = true
}

const handleDelete = async (row: MenuItem) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除菜单"${row.menuName}"吗？${row.children?.length ? '该菜单包含子菜单，将一并删除。' : ''}`,
      '警告',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
    await request.delete(`/uc/v1/menus/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      const submitData = {
        ...form,
        parentId: form.parentId === 0 ? null : form.parentId
      }
      if (form.id) {
        await request.put(`/uc/v1/menus/${form.id}`, submitData)
        ElMessage.success('更新成功')
      } else {
        await request.post('/uc/v1/menus', submitData)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadData()
    } catch (error: any) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.menu-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
