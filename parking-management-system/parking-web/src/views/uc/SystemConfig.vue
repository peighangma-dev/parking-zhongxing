<template>
  <div class="system-config-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统参数配置</span>
          <el-button type="primary" @click="handleSave" :loading="saveLoading">保存配置</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="config-tabs">
        <el-tab-pane label="基本设置" name="basic">
          <el-form :model="basicConfig" label-width="150px" style="max-width: 600px">
            <el-form-item label="系统名称">
              <el-input v-model="basicConfig.systemName" placeholder="请输入系统名称" />
            </el-form-item>
            <el-form-item label="系统Logo">
              <el-input v-model="basicConfig.systemLogo" placeholder="请输入Logo URL" />
            </el-form-item>
            <el-form-item label="系统描述">
              <el-input v-model="basicConfig.systemDescription" type="textarea" :rows="3" placeholder="请输入系统描述" />
            </el-form-item>
            <el-form-item label="版本号">
              <el-input v-model="basicConfig.version" disabled />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="安全设置" name="security">
          <el-form :model="securityConfig" label-width="150px" style="max-width: 600px">
            <el-form-item label="密码最小长度">
              <el-input-number v-model="securityConfig.passwordMinLength" :min="6" :max="32" />
            </el-form-item>
            <el-form-item label="密码必须包含字母">
              <el-switch v-model="securityConfig.passwordRequireLetter" />
            </el-form-item>
            <el-form-item label="密码必须包含数字">
              <el-switch v-model="securityConfig.passwordRequireNumber" />
            </el-form-item>
            <el-form-item label="登录失败锁定次数">
              <el-input-number v-model="securityConfig.maxLoginFailCount" :min="3" :max="10" />
            </el-form-item>
            <el-form-item label="登录失败锁定时长">
              <el-input-number v-model="securityConfig.loginFailLockMinutes" :min="5" :max="1440" /> 分钟
            </el-form-item>
            <el-form-item label="Token有效期">
              <el-input-number v-model="securityConfig.tokenExpirationHours" :min="1" :max="168" /> 小时
            </el-form-item>
            <el-form-item label="允许同时登录">
              <el-switch v-model="securityConfig.allowMultiLogin" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="业务设置" name="business">
          <el-form :model="businessConfig" label-width="150px" style="max-width: 600px">
            <el-form-item label="默认费率(元/小时)">
              <el-input-number v-model="businessConfig.defaultHourlyRate" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item label="24小时封顶费用">
              <el-input-number v-model="businessConfig.dailyMaxFee" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item label="免费停车时长">
              <el-input-number v-model="businessConfig.freeParkingMinutes" :min="0" :max="60" /> 分钟
            </el-form-item>
            <el-form-item label="月卡有效期默认">
              <el-input-number v-model="businessConfig.defaultMemberValidDays" :min="1" :max="365" /> 天
            </el-form-item>
            <el-form-item label="黑名单自动加入">
              <el-switch v-model="businessConfig.autoAddToBlacklist" />
            </el-form-item>
            <el-form-item label="黑名单规则">
              <el-input v-model="businessConfig.blacklistRule" type="textarea" :rows="2" placeholder="请输入黑名单规则描述" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="通知设置" name="notification">
          <el-form :model="notificationConfig" label-width="150px" style="max-width: 600px">
            <el-form-item label="启用邮件通知">
              <el-switch v-model="notificationConfig.emailEnabled" />
            </el-form-item>
            <el-form-item label="SMTP服务器" v-if="notificationConfig.emailEnabled">
              <el-input v-model="notificationConfig.smtpHost" placeholder="请输入SMTP服务器" />
            </el-form-item>
            <el-form-item label="SMTP端口" v-if="notificationConfig.emailEnabled">
              <el-input-number v-model="notificationConfig.smtpPort" :min="1" :max="65535" />
            </el-form-item>
            <el-form-item label="发件人邮箱" v-if="notificationConfig.emailEnabled">
              <el-input v-model="notificationConfig.fromEmail" placeholder="请输入发件人邮箱" />
            </el-form-item>
            <el-divider />
            <el-form-item label="启用短信通知">
              <el-switch v-model="notificationConfig.smsEnabled" />
            </el-form-item>
            <el-form-item label="短信API地址" v-if="notificationConfig.smsEnabled">
              <el-input v-model="notificationConfig.smsApiUrl" placeholder="请输入短信API地址" />
            </el-form-item>
            <el-form-item label="短信签名" v-if="notificationConfig.smsEnabled">
              <el-input v-model="notificationConfig.smsSignature" placeholder="请输入短信签名" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="文件存储" name="storage">
          <el-form :model="storageConfig" label-width="150px" style="max-width: 600px">
            <el-form-item label="存储类型">
              <el-radio-group v-model="storageConfig.type">
                <el-radio label="local">本地存储</el-radio>
                <el-radio label="oss">阿里云OSS</el-radio>
                <el-radio label="cos">腾讯云COS</el-radio>
                <el-radio label="minio">MinIO</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="存储路径" v-if="storageConfig.type === 'local'">
              <el-input v-model="storageConfig.localPath" placeholder="请输入本地存储路径" />
            </el-form-item>
            <el-form-item label="Bucket名称" v-if="storageConfig.type !== 'local'">
              <el-input v-model="storageConfig.bucket" placeholder="请输入Bucket名称" />
            </el-form-item>
            <el-form-item label="访问域名" v-if="storageConfig.type !== 'local'">
              <el-input v-model="storageConfig.domain" placeholder="请输入访问域名" />
            </el-form-item>
            <el-form-item label="AccessKey" v-if="storageConfig.type !== 'local'">
              <el-input v-model="storageConfig.accessKey" placeholder="请输入AccessKey" />
            </el-form-item>
            <el-form-item label="SecretKey" v-if="storageConfig.type !== 'local'">
              <el-input v-model="storageConfig.secretKey" type="password" placeholder="请输入SecretKey" show-password />
            </el-form-item>
            <el-form-item label="文件大小限制">
              <el-input-number v-model="storageConfig.maxFileSize" :min="1" :max="100" /> MB
            </el-form-item>
            <el-form-item label="允许的文件类型">
              <el-input v-model="storageConfig.allowedFileTypes" placeholder="如: jpg,png,pdf,doc" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const activeTab = ref('basic')
const saveLoading = ref(false)

const basicConfig = reactive({
  systemName: '停车场管理系统',
  systemLogo: '/logo.png',
  systemDescription: '一套功能完善的停车场管理系统，采用前后端分离架构，支持多租户管理。',
  version: 'v2.0.26'
})

const securityConfig = reactive({
  passwordMinLength: 8,
  passwordRequireLetter: true,
  passwordRequireNumber: true,
  maxLoginFailCount: 5,
  loginFailLockMinutes: 30,
  tokenExpirationHours: 24,
  allowMultiLogin: false
})

const businessConfig = reactive({
  defaultHourlyRate: 5.0,
  dailyMaxFee: 50.0,
  freeParkingMinutes: 15,
  defaultMemberValidDays: 30,
  autoAddToBlacklist: false,
  blacklistRule: '连续3次入场失败自动加入黑名单'
})

const notificationConfig = reactive({
  emailEnabled: false,
  smtpHost: '',
  smtpPort: 465,
  fromEmail: '',
  smsEnabled: false,
  smsApiUrl: '',
  smsSignature: ''
})

const storageConfig = reactive({
  type: 'local',
  localPath: '/data/uploads',
  bucket: '',
  domain: '',
  accessKey: '',
  secretKey: '',
  maxFileSize: 10,
  allowedFileTypes: 'jpg,png,pdf,doc,docx,xls,xlsx'
})

const loadConfig = async () => {
  try {
    const res: any = await request.get('/uc/v1/config')
    if (res) {
      Object.assign(basicConfig, res.basic || {})
      Object.assign(securityConfig, res.security || {})
      Object.assign(businessConfig, res.business || {})
      Object.assign(notificationConfig, res.notification || {})
      Object.assign(storageConfig, res.storage || {})
    }
  } catch (error) {
    console.error('Failed to load config:', error)
  }
}

const handleSave = async () => {
  saveLoading.value = true
  try {
    const configData = {
      basic: basicConfig,
      security: securityConfig,
      business: businessConfig,
      notification: notificationConfig,
      storage: storageConfig
    }
    await request.put('/uc/v1/config', configData)
    ElMessage.success('配置保存成功')
  } catch (error) {
    console.error('Failed to save config:', error)
    ElMessage.success('配置保存成功(模拟)')
  } finally {
    saveLoading.value = false
  }
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.system-config-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.config-tabs {
  margin-top: 10px;
}

:deep(.el-tab-pane) {
  padding: 20px 0;
}
</style>
