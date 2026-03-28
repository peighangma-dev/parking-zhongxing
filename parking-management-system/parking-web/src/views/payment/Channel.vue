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
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
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
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link @click="handleConfig(row)">配置</el-button>
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
        <el-form-item label="渠道编码">
          <el-input v-model="form.channelCode" placeholder="请输入渠道编码，如 WECHAT" />
        </el-form-item>
        <el-form-item label="渠道名称">
          <el-input v-model="form.channelName" placeholder="请输入渠道名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="手续费率">
          <el-input-number v-model="form.feeRate" :min="0" :max="1" :precision="4" :step="0.0001" />
          <span style="margin-left: 10px">{{ ((form.feeRate || 0) * 100).toFixed(2) }}%</span>
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

    <el-dialog v-model="configDialogVisible" :title="`${currentChannel?.channelName} - 配置`" width="700px">
      <el-form :model="configForm" label-width="120px">
        <template v-if="currentChannel?.channelCode === 'WECHAT'">
          <el-form-item label="公众号AppID">
            <el-input v-model="configForm.appId" placeholder="微信公众平台AppID" />
          </el-form-item>
          <el-form-item label="商户号(MchID)">
            <el-input v-model="configForm.mchId" placeholder="微信支付商户号" />
          </el-form-item>
          <el-form-item label="API密钥">
            <el-input v-model="configForm.apiKey" type="password" placeholder="API密钥" show-password />
          </el-form-item>
          <el-form-item label="证书路径">
            <el-input v-model="configForm.certPath" placeholder="退款等操作需要的证书路径" />
          </el-form-item>
          <el-form-item label="证书密码">
            <el-input v-model="configForm.certPassword" type="password" placeholder="证书密码" show-password />
          </el-form-item>
        </template>
        <template v-else-if="currentChannel?.channelCode === 'ALIPAY'">
          <el-form-item label="应用AppID">
            <el-input v-model="configForm.appId" placeholder="支付宝应用AppID" />
          </el-form-item>
          <el-form-item label="商户号">
            <el-input v-model="configForm.mchId" placeholder="支付宝商户号" />
          </el-form-item>
          <el-form-item label="应用私钥">
            <el-input v-model="configForm.privateKey" type="textarea" :rows="3" placeholder="应用私钥(RSA2)" />
          </el-form-item>
          <el-form-item label="支付宝公钥">
            <el-input v-model="configForm.alipayPublicKey" type="textarea" :rows="3" placeholder="支付宝公钥" />
          </el-form-item>
        </template>
        <template v-else-if="currentChannel?.channelCode === 'CASH'">
          <el-form-item label="收款人">
            <el-input v-model="configForm.payee" placeholder="现金收款人" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="configForm.remark" type="textarea" placeholder="备注说明" />
          </el-form-item>
        </template>
        <template v-else-if="currentChannel?.channelCode === 'ETC'">
          <el-form-item label="ETC设备编号">
            <el-input v-model="configForm.etcDeviceId" placeholder="ETC设备编号" />
          </el-form-item>
          <el-form-item label="ETC密钥">
            <el-input v-model="configForm.etcKey" type="password" placeholder="ETC密钥" show-password />
          </el-form-item>
          <el-form-item label="OBU编号">
            <el-input v-model="configForm.obuNo" placeholder="OBU车载单元编号" />
          </el-form-item>
        </template>
        <template v-else>
          <el-form-item label="配置项">
            <el-input v-model="configForm.customConfig" type="textarea" :rows="4" placeholder="自定义配置(JSON格式)" />
          </el-form-item>
        </template>
        <el-form-item label="启用状态">
          <el-switch v-model="configForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="configDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveConfig">保存配置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

interface ChannelConfig {
  appId?: string
  mchId?: string
  apiKey?: string
  certPath?: string
  certPassword?: string
  privateKey?: string
  alipayPublicKey?: string
  payee?: string
  remark?: string
  etcDeviceId?: string
  etcKey?: string
  obuNo?: string
  customConfig?: string
  enabled?: boolean
}

const tableData = ref<any[]>([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const configDialogVisible = ref(false)
const dialogTitle = ref('添加渠道')
const currentChannel = ref<any>(null)
const form = reactive({
  id: null as number | null,
  channelCode: '',
  channelName: '',
  description: '',
  feeRate: 0,
  sortOrder: 0,
  status: 'enabled'
})
const configForm = reactive<ChannelConfig>({
  appId: '',
  mchId: '',
  apiKey: '',
  certPath: '',
  certPassword: '',
  privateKey: '',
  alipayPublicKey: '',
  payee: '',
  remark: '',
  etcDeviceId: '',
  etcKey: '',
  obuNo: '',
  customConfig: '',
  enabled: true
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
  form.description = ''
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
  form.description = row.description || ''
  form.feeRate = row.feeRate || 0
  form.sortOrder = row.sortOrder || 0
  form.status = row.status
  dialogVisible.value = true
}

const handleConfig = (row: any) => {
  currentChannel.value = row
  const config = row.config || {}
  configForm.appId = config.appId || ''
  configForm.mchId = config.mchId || ''
  configForm.apiKey = config.apiKey || ''
  configForm.certPath = config.certPath || ''
  configForm.certPassword = config.certPassword || ''
  configForm.privateKey = config.privateKey || ''
  configForm.alipayPublicKey = config.alipayPublicKey || ''
  configForm.payee = config.payee || ''
  configForm.remark = config.remark || ''
  configForm.etcDeviceId = config.etcDeviceId || ''
  configForm.etcKey = config.etcKey || ''
  configForm.obuNo = config.obuNo || ''
  configForm.customConfig = typeof config === 'string' ? config : ''
  configForm.enabled = config.enabled !== false
  configDialogVisible.value = true
}

const handleSaveConfig = async () => {
  try {
    let config: any = { ...configForm }
    delete (config as any).customConfig
    if (!currentChannel.value?.channelCode) {
      Object.keys(config).forEach(key => {
        if ((config as any)[key] === '' || (config as any)[key] === undefined) {
          delete (config as any)[key]
        }
      })
    }
    if (currentChannel.value?.channelCode === 'WECHAT' || 
        currentChannel.value?.channelCode === 'ALIPAY' ||
        currentChannel.value?.channelCode === 'CASH' ||
        currentChannel.value?.channelCode === 'ETC') {
      Object.keys(config).forEach(key => {
        if ((config as any)[key] === '' || (config as any)[key] === undefined) {
          delete (config as any)[key]
        }
      })
    } else if (configForm.customConfig) {
      try {
        config = JSON.parse(configForm.customConfig)
      } catch {
        ElMessage.error('自定义配置必须是有效的JSON格式')
        return
      }
    }
    await request.put(`/payment/v1/channels/${currentChannel.value.id}`, {
      config: config
    })
    ElMessage.success('配置保存成功')
    configDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to save config:', error)
  }
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
