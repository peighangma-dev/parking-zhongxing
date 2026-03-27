<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="sidebar">
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
        <el-sub-menu index="/barrier">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>道闸管理</span>
          </template>
          <el-menu-item index="/barrier/device">设备管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="/payment">
          <template #title>
            <el-icon><Money /></el-icon>
            <span>支付管理</span>
          </template>
          <el-menu-item index="/payment/channel">支付渠道</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="/vehicle">
          <template #title>
            <el-icon><Van /></el-icon>
            <span>车辆管理</span>
          </template>
          <el-menu-item index="/vehicle/list">车辆列表</el-menu-item>
          <el-menu-item index="/vehicle/member">月卡管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="/uc">
          <template #title>
            <el-icon><User /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/uc/user">用户管理</el-menu-item>
          <el-menu-item index="/uc/parking-lot">停车场管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
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

.username {
  margin-right: 15px;
  color: #666;
}

.main-content {
  background: #f5f7fa;
  padding: 20px;
}
</style>
