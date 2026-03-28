<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">停车场管理</div>
      <el-menu
        :default-active="route.path"
        router
        class="sidebar-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <el-sub-menu index="/parking">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>停车场管理</span>
          </template>
          <el-menu-item index="/parking/lot">停车场设置</el-menu-item>
          <el-menu-item index="/parking/area">区域管理</el-menu-item>
          <el-menu-item index="/parking/space">车位管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/barrier">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>设备管理</span>
          </template>
          <el-menu-item index="/barrier/device">道闸设备</el-menu-item>
          <el-menu-item index="/barrier/lane">车道管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/vehicle">
          <template #title>
            <el-icon><Van /></el-icon>
            <span>车辆管理</span>
          </template>
          <el-menu-item index="/vehicle/list">车辆列表</el-menu-item>
          <el-menu-item index="/vehicle/member">月卡管理</el-menu-item>
          <el-menu-item index="/vehicle/blacklist">黑名单</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/payment">
          <template #title>
            <el-icon><Money /></el-icon>
            <span>支付管理</span>
          </template>
          <el-menu-item index="/payment/channel">支付渠道</el-menu-item>
          <el-menu-item index="/payment/rate">费率规则</el-menu-item>
          <el-menu-item index="/payment/record">交易记录</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/report">
          <template #title>
            <el-icon><DataAnalysis /></el-icon>
            <span>报表统计</span>
          </template>
          <el-menu-item index="/report/pass">通行记录</el-menu-item>
          <el-menu-item index="/report/daily">收费报表</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/uc">
          <template #title>
            <el-icon><User /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/uc/user">用户管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="page-title">{{ pageTitle }}</span>
        <span class="username">{{ username }}</span>
        <el-button @click="handleLogout">退出</el-button>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const username = computed(() => localStorage.getItem('username') || 'Admin')

const pageTitleMap: Record<string, string> = {
  '/dashboard': '首页',
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
}

.sidebar {
  background: #304156;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}

.sidebar-menu {
  border: none;
  background: transparent;
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border-bottom: 1px solid #e6e6e6;
}

.page-title {
  margin-right: auto;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.username {
  margin-right: 15px;
  color: #666;
}

.main-content {
  background: #f5f7fa;
  padding: 20px;
}
</style>
