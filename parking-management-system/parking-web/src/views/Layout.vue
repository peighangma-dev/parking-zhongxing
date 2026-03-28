<template>
  <el-container class="layout-container">
    <el-aside width="240px" class="sidebar">
      <div class="logo">
        <div class="logo-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
          </svg>
        </div>
        <div class="logo-text">
          <span class="main">PARKING</span>
          <span class="sub">SYSTEM</span>
        </div>
      </div>
      
      <div class="menu-container">
        <el-menu
          :default-active="route.path"
          router
          class="sidebar-menu"
          :collapse="false"
        >
          <el-menu-item index="/dashboard" class="menu-item-home">
            <el-icon><HomeFilled /></el-icon>
            <template #title>首页</template>
          </el-menu-item>
          
          <div class="menu-divider">
            <span class="divider-text">管理模块</span>
          </div>

          <el-sub-menu index="/parking">
            <template #title>
              <div class="menu-title-wrapper">
                <el-icon class="menu-icon"><OfficeBuilding /></el-icon>
                <span class="menu-title">停车场</span>
              </div>
            </template>
            <el-menu-item index="/parking/lot">停车场设置</el-menu-item>
            <el-menu-item index="/parking/area">区域管理</el-menu-item>
            <el-menu-item index="/parking/space">车位管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/barrier">
            <template #title>
              <div class="menu-title-wrapper">
                <el-icon class="menu-icon"><Monitor /></el-icon>
                <span class="menu-title">设备管理</span>
              </div>
            </template>
            <el-menu-item index="/barrier/device">道闸设备</el-menu-item>
            <el-menu-item index="/barrier/lane">车道管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/vehicle">
            <template #title>
              <div class="menu-title-wrapper">
                <el-icon class="menu-icon"><Van /></el-icon>
                <span class="menu-title">车辆管理</span>
              </div>
            </template>
            <el-menu-item index="/vehicle/list">车辆列表</el-menu-item>
            <el-menu-item index="/vehicle/member">月卡管理</el-menu-item>
            <el-menu-item index="/vehicle/blacklist">黑名单</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/payment">
            <template #title>
              <div class="menu-title-wrapper">
                <el-icon class="menu-icon"><Money /></el-icon>
                <span class="menu-title">支付管理</span>
              </div>
            </template>
            <el-menu-item index="/payment/channel">支付渠道</el-menu-item>
            <el-menu-item index="/payment/rate">费率规则</el-menu-item>
            <el-menu-item index="/payment/record">交易记录</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/report">
            <template #title>
              <div class="menu-title-wrapper">
                <el-icon class="menu-icon"><DataAnalysis /></el-icon>
                <span class="menu-title">报表统计</span>
              </div>
            </template>
            <el-menu-item index="/report/pass">通行记录</el-menu-item>
            <el-menu-item index="/report/daily">收费报表</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/uc">
            <template #title>
              <div class="menu-title-wrapper">
                <el-icon class="menu-icon"><User /></el-icon>
                <span class="menu-title">系统管理</span>
              </div>
            </template>
            <el-menu-item index="/uc/user">用户管理</el-menu-item>
            <el-menu-item index="/uc/role">角色管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/admin">
            <template #title>
              <div class="menu-title-wrapper">
                <el-icon class="menu-icon"><Key /></el-icon>
                <span class="menu-title">超级管理员</span>
              </div>
            </template>
            <el-menu-item index="/admin/tenant">租户管理</el-menu-item>
            <el-menu-item index="/admin/package">套餐管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>

      <div class="sidebar-footer">
        <div class="status-indicator">
          <span class="status-dot"></span>
          <span class="status-text">SYSTEM ONLINE</span>
        </div>
      </div>
    </el-aside>
    
    <el-container class="main-container">
      <el-header class="header">
        <div class="header-left">
          <div class="page-title-wrapper">
            <span class="page-title">{{ pageTitle }}</span>
            <div class="title-line"></div>
          </div>
        </div>
        <div class="header-right">
          <div v-if="isSuperAdmin" class="tenant-selector">
            <el-select
              v-model="selectedTenantId"
              placeholder="选择租户"
              size="default"
              style="width: 180px"
              @change="handleTenantChange"
              filterable
            >
              <el-option label="全部租户" :value="0" />
              <el-option
                v-for="tenant in tenants"
                :key="tenant.id"
                :label="tenant.tenantName"
                :value="tenant.id"
              />
            </el-select>
          </div>
          <div class="tenant-info">
            <span class="tenant-label">租户:</span>
            <span class="tenant-name">{{ displayTenantName }}</span>
          </div>
          <div class="user-info">
            <div class="user-avatar">
              <el-icon><UserFilled /></el-icon>
            </div>
            <div class="user-details">
              <span class="user-name">{{ username }}</span>
              <span class="user-role">Administrator</span>
            </div>
          </div>
          <el-button class="logout-btn" @click="handleLogout" type="danger" plain>
            <el-icon><SwitchButton /></el-icon>
            退出
          </el-button>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useFeaturePermission, FEATURE_CODES } from '@/utils/featurePermission'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const username = computed(() => localStorage.getItem('username') || 'Admin')
const tenantId = computed(() => localStorage.getItem('tenantId') || '1')
const isSuperAdmin = computed(() => localStorage.getItem('isSuperAdmin') === 'true')
const tenants = ref<any[]>([])
const selectedTenantId = ref<number>(0)

const displayTenantName = computed(() => {
  if (isSuperAdmin.value && selectedTenantId.value !== 0) {
    const tenant = tenants.value.find(t => t.id === selectedTenantId.value)
    return tenant ? tenant.tenantName : '全部租户'
  }
  return localStorage.getItem('tenantName') || '演示租户'
})

const { loadFeatures, hasFeature } = useFeaturePermission()

const loadTenants = async () => {
  if (!isSuperAdmin.value) return
  try {
    const res: any = await request.get('/tenant/v1/list')
    tenants.value = res || []
  } catch (error) {
    console.error('Failed to load tenants:', error)
  }
}

const handleTenantChange = (val: number) => {
  if (val === 0) {
    localStorage.removeItem('selectedTenantId')
  } else {
    localStorage.setItem('selectedTenantId', String(val))
  }
}

onMounted(async () => {
  const tid = Number(tenantId.value)
  if (tid) {
    await loadFeatures(tid)
  }
  await loadTenants()
  
  const savedTenantId = localStorage.getItem('selectedTenantId')
  if (savedTenantId) {
    selectedTenantId.value = Number(savedTenantId)
  }
})

const pageTitleMap: Record<string, string> = {
  '/dashboard': '数据概览',
  '/parking/lot': '停车场设置',
  '/parking/area': '区域管理',
  '/parking/space': '车位管理',
  '/barrier/device': '道闸设备',
  '/barrier/lane': '车道管理',
  '/vehicle/list': '车辆列表',
  '/vehicle/member': '月卡管理',
  '/vehicle/blacklist': '黑名单',
  '/payment/channel': '支付渠道',
  '/payment/rate': '费率规则',
  '/payment/record': '交易记录',
  '/report/pass': '通行记录',
  '/report/daily': '收费报表',
  '/report/monthly': '收费月报',
  '/uc/user': '用户管理',
  '/uc/role': '角色管理',
  '/admin/tenant': '租户管理',
  '/admin/package': '套餐管理',
}
const pageTitle = computed(() => pageTitleMap[route.path] || '')

const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  background: var(--cyber-bg-primary);
}

.sidebar {
  background: linear-gradient(180deg, var(--cyber-bg-secondary) 0%, var(--cyber-bg-primary) 100%) !important;
  border-right: 1px solid var(--cyber-border);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.sidebar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--cyber-cyan), transparent);
  box-shadow: var(--cyber-glow-cyan);
}

.logo {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 20px;
  border-bottom: 1px solid var(--cyber-border);
  position: relative;
}

.logo::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 80%;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--cyber-cyan), transparent);
}

.logo-icon {
  width: 40px;
  height: 40px;
  color: var(--cyber-cyan);
  filter: drop-shadow(0 0 10px var(--cyber-cyan));
  animation: cyberPulse 2s ease-in-out infinite;
}

.logo-icon svg {
  width: 100%;
  height: 100%;
}

.logo-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.logo-text .main {
  font-family: 'Orbitron', sans-serif;
  font-size: 18px;
  font-weight: 700;
  color: var(--cyber-cyan);
  letter-spacing: 3px;
  text-shadow: var(--cyber-glow-cyan);
}

.logo-text .sub {
  font-family: 'Orbitron', sans-serif;
  font-size: 10px;
  font-weight: 500;
  color: var(--cyber-text-dim);
  letter-spacing: 5px;
}

.menu-container {
  flex: 1;
  overflow-y: auto;
  padding: 15px 0;
}

.sidebar-menu {
  background: transparent !important;
  border: none;
}

.menu-divider {
  padding: 20px 20px 10px;
  position: relative;
}

.menu-divider::before {
  content: '';
  position: absolute;
  top: 30px;
  left: 20px;
  right: 20px;
  height: 1px;
  background: linear-gradient(90deg, var(--cyber-border), var(--cyber-cyan), var(--cyber-border));
}

.divider-text {
  font-family: 'Orbitron', sans-serif;
  font-size: 10px;
  color: var(--cyber-text-dim);
  letter-spacing: 2px;
  text-transform: uppercase;
  background: var(--cyber-bg-secondary);
  padding: 0 10px;
  position: relative;
}

.menu-title-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu-icon {
  font-size: 18px;
  color: var(--cyber-cyan);
  filter: drop-shadow(0 0 5px var(--cyber-cyan));
}

.menu-title {
  font-family: 'Rajdhani', sans-serif;
  font-weight: 600;
  letter-spacing: 1px;
}

.menu-item-home {
  margin-bottom: 10px;
}

.sidebar-footer {
  padding: 20px;
  border-top: 1px solid var(--cyber-border);
  position: relative;
}

.sidebar-footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--cyber-cyan), transparent);
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-dot {
  width: 8px;
  height: 8px;
  background: var(--cyber-green);
  border-radius: 50%;
  box-shadow: 0 0 10px var(--cyber-green);
  animation: cyberPulse 1.5s ease-in-out infinite;
}

.status-text {
  font-family: 'Orbitron', sans-serif;
  font-size: 9px;
  color: var(--cyber-green);
  letter-spacing: 2px;
}

.main-container {
  display: flex;
  flex-direction: column;
  background: var(--cyber-bg-primary);
}

.header {
  height: 70px !important;
  background: var(--cyber-bg-secondary) !important;
  border-bottom: 1px solid var(--cyber-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  position: relative;
}

.header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--cyber-cyan), transparent);
}

.header-left {
  display: flex;
  align-items: center;
}

.page-title-wrapper {
  display: flex;
  align-items: center;
  gap: 15px;
}

.page-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 20px;
  font-weight: 600;
  color: var(--cyber-cyan);
  letter-spacing: 3px;
  text-shadow: var(--cyber-glow-cyan);
}

.title-line {
  width: 40px;
  height: 2px;
  background: linear-gradient(90deg, var(--cyber-cyan), transparent);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 15px;
  background: rgba(0, 255, 255, 0.05);
  border: 1px solid var(--cyber-border);
  border-radius: 8px;
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-blue));
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000;
  font-size: 18px;
  box-shadow: var(--cyber-glow-cyan);
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-family: 'Rajdhani', sans-serif;
  font-size: 14px;
  font-weight: 600;
  color: var(--cyber-text);
}

.user-role {
  font-family: 'Orbitron', sans-serif;
  font-size: 9px;
  color: var(--cyber-cyan);
  letter-spacing: 1px;
}

.logout-btn {
  font-family: 'Rajdhani', sans-serif;
  font-weight: 600;
  letter-spacing: 1px;
  border-radius: 6px;
}

.tenant-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 15px;
  background: rgba(255, 0, 255, 0.1);
  border: 1px solid rgba(255, 0, 255, 0.3);
  border-radius: 8px;
}

.tenant-selector :deep(.el-input__wrapper) {
  background: rgba(0, 255, 255, 0.1) !important;
  border: 1px solid var(--cyber-cyan) !important;
  box-shadow: 0 0 10px rgba(0, 255, 255, 0.3);
}

.tenant-label {
  font-family: 'Rajdhani', sans-serif;
  font-size: 12px;
  color: var(--cyber-magenta);
}

.tenant-name {
  font-family: 'Rajdhani', sans-serif;
  font-size: 14px;
  font-weight: 600;
  color: var(--cyber-magenta);
  text-shadow: 0 0 10px rgba(255, 0, 255, 0.5);
}

.main-content {
  background: var(--cyber-bg-primary);
  padding: 25px;
  overflow-y: auto;
}
</style>
