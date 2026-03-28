<template>
  <div class="space-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>车位管理</span>
          <el-button type="primary" @click="handleAdd">新增车位</el-button>
        </div>
      </template>
      <el-form inline :model="queryForm">
        <el-form-item label="车位类型">
          <el-select v-model="queryForm.spaceType" placeholder="请选择" clearable>
            <el-option label="小型车" value="small" />
            <el-option label="大型车" value="large" />
            <el-option label="新能源车" value="new_energy" />
            <el-option label="残疾人车位" value="accessible" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="空闲" value="empty" />
            <el-option label="占用" value="occupied" />
            <el-option label="预约" value="reserved" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="spaceCode" label="车位编号" />
        <el-table-column prop="areaName" label="所属区域" />
        <el-table-column prop="spaceType" label="车位类型">
          <template #default="{ row }">
            <el-tag>{{ getSpaceTypeName(row.spaceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="plateNumber" label="车牌号" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button v-if="row.status === 'empty'" type="success" link @click="handleOccupy(row)">占用</el-button>
            <el-button v-if="row.status === 'occupied'" type="warning" link @click="handleRelease(row)">释放</el-button>
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
        <el-form-item label="车位编号">
          <el-input v-model="form.spaceCode" placeholder="请输入车位编号" />
        </el-form-item>
        <el-form-item label="所属区域">
          <el-select v-model="form.areaId" placeholder="请选择">
            <el-option v-for="a in areas" :key="a.id" :label="a.areaName" :value="a.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="车位类型">
          <el-select v-model="form.spaceType" placeholder="请选择">
            <el-option label="小型车" value="small" />
            <el-option label="大型车" value="large" />
            <el-option label="新能源车" value="new_energy" />
            <el-option label="残疾人车位" value="accessible" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option label="空闲" value="empty" />
            <el-option label="占用" value="occupied" />
            <el-option label="预约" value="reserved" />
            <el-option label="禁用" value="disabled" />
          </el-select>
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
  spaceType: '',
  status: ''
})

const tableData = ref([])
const areas = ref<any[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增车位')
const form = reactive({
  id: null as number | null,
  spaceCode: '',
  areaId: null as number | null,
  spaceType: 'small',
  status: 'empty',
  plateNumber: ''
})

const spaceTypeMap: Record<string, string> = {
  small: '小型车',
  large: '大型车',
  new_energy: '新能源车',
  accessible: '残疾人车位'
}

const statusMap: Record<string, string> = {
  empty: '空闲',
  occupied: '占用',
  reserved: '预约',
  disabled: '禁用'
}

const getSpaceTypeName = (type: string) => spaceTypeMap[type] || type
const getStatusName = (status: string) => statusMap[status] || status
const getStatusType = (status: string) => {
  const map: Record<string, string> = { empty: 'success', occupied: 'danger', reserved: 'warning', disabled: 'info' }
  return map[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await request.get('/space/v1/spaces/page', {
      params: {
        current: pagination.current,
        size: pagination.size,
        spaceType: queryForm.spaceType || undefined,
        status: queryForm.status || undefined
      }
    })
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

const loadAreas = async () => {
  try {
    const res = await request.get('/space/v1/areas/page', { params: { current: 1, size: 100 } })
    areas.value = res.records || []
  } catch (error) {
    console.error('Failed to load areas:', error)
  }
}

const handleQuery = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  queryForm.spaceType = ''
  queryForm.status = ''
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '新增车位'
  form.id = null
  form.spaceCode = ''
  form.areaId = null
  form.spaceType = 'small'
  form.status = 'empty'
  form.plateNumber = ''
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑车位'
  form.id = row.id
  form.spaceCode = row.spaceCode
  form.areaId = row.areaId
  form.spaceType = row.spaceType
  form.status = row.status
  form.plateNumber = row.plateNumber
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  try {
    await request.delete(`/space/v1/spaces/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('Failed to delete:', error)
  }
}

const handleOccupy = async (row: any) => {
  const plateNumber = prompt('请输入车牌号:')
  if (plateNumber) {
    try {
      await request.put(`/space/v1/spaces/${row.id}/occupy?plateNumber=${plateNumber}`)
      ElMessage.success('操作成功')
      loadData()
    } catch (error) {
      console.error('Failed to occupy:', error)
    }
  }
}

const handleRelease = async (row: any) => {
  try {
    await request.put(`/space/v1/spaces/${row.id}/release`)
    ElMessage.success('释放成功')
    loadData()
  } catch (error) {
    console.error('Failed to release:', error)
  }
}

const handleSubmit = async () => {
  try {
    if (form.id) {
      await request.put(`/space/v1/spaces/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/space/v1/spaces', form)
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
  loadAreas()
})
</script>

<style scoped>
.space-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
