<template>
  <div class="login-container">
    <div class="bg-grid"></div>
    <div class="bg-glow bg-glow-1"></div>
    <div class="bg-glow bg-glow-2"></div>
    
    <div class="login-card">
      <div class="card-glow"></div>
      
      <div class="card-header">
        <div class="logo-wrapper">
          <div class="logo-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
            </svg>
          </div>
        </div>
        <h1 class="title">停车场管理系统</h1>
        <p class="subtitle">PARKING MANAGEMENT SYSTEM</p>
      </div>

      <el-form :model="form" :rules="rules" @submit.prevent="handleLogin" class="login-form" ref="formRef">
        <div class="form-title">
          <span class="line"></span>
          <span class="text">系统登录</span>
          <span class="line"></span>
        </div>
        
        <el-form-item prop="username">
          <div class="input-wrapper">
            <el-icon class="input-icon"><User /></el-icon>
            <el-input 
              v-model="form.username" 
              placeholder="请输入用户名"
              class="cyber-input"
            />
          </div>
        </el-form-item>
        
        <el-form-item prop="password">
          <div class="input-wrapper">
            <el-icon class="input-icon"><Lock /></el-icon>
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码"
              class="cyber-input"
              show-password
            />
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit" 
            :loading="loading" 
            class="login-btn"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>加 载 中...</span>
          </el-button>
        </el-form-item>

        <div class="register-link">
          <router-link to="/register">租户注册</router-link>
        </div>
      </el-form>

      <div class="card-footer">
        <div class="system-info">
          <span class="version">v2.0.26</span>
          <span class="separator">|</span>
          <span class="copyright">Powered by CyberTech</span>
        </div>
      </div>
    </div>

    <div class="scanline"></div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 32, message: '用户名长度为3-32个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 32, message: '密码长度为6-32个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      const res = await request.post('/uc/v1/login', form)
      localStorage.setItem('token', res.token)
      localStorage.setItem('userId', res.userId)
      localStorage.setItem('username', res.username)
      localStorage.setItem('nickname', res.nickname)
      localStorage.setItem('tenantId', res.tenantId || '')
      localStorage.setItem('isSuperAdmin', res.isSuperAdmin ? 'true' : 'false')
      router.push('/')
    } catch (error: any) {
      ElMessage.error(error.message || '登录失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: var(--cyber-bg-primary);
  position: relative;
  overflow: hidden;
}

.bg-grid {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(rgba(0, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
  pointer-events: none;
}

.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  pointer-events: none;
}

.bg-glow-1 {
  top: 10%;
  left: 20%;
  width: 400px;
  height: 400px;
  background: rgba(0, 255, 255, 0.15);
  animation: cyberPulse 4s ease-in-out infinite;
}

.bg-glow-2 {
  bottom: 10%;
  right: 20%;
  width: 300px;
  height: 300px;
  background: rgba(255, 0, 255, 0.1);
  animation: cyberPulse 4s ease-in-out infinite 2s;
}

.login-card {
  width: 420px;
  padding: 40px;
  background: var(--cyber-glass);
  border: 1px solid var(--cyber-border);
  border-radius: 20px;
  backdrop-filter: blur(30px);
  position: relative;
  z-index: 10;
  box-shadow: 
    0 0 50px rgba(0, 0, 0, 0.5),
    0 0 100px rgba(0, 255, 255, 0.1),
    inset 0 0 1px rgba(0, 255, 255, 0.3);
}

.card-glow {
  position: absolute;
  top: -1px;
  left: -1px;
  right: -1px;
  bottom: -1px;
  border-radius: 20px;
  background: linear-gradient(135deg, var(--cyber-cyan), transparent, var(--cyber-magenta));
  z-index: -1;
  opacity: 0.3;
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  mask-composite: xor;
  padding: 1px;
}

.card-header {
  text-align: center;
  margin-bottom: 35px;
}

.logo-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-icon {
  width: 60px;
  height: 60px;
  color: var(--cyber-cyan);
  filter: drop-shadow(0 0 20px var(--cyber-cyan));
  animation: cyberPulse 2s ease-in-out infinite;
}

.logo-icon svg {
  width: 100%;
  height: 100%;
}

.title {
  font-family: 'Orbitron', sans-serif;
  font-size: 24px;
  font-weight: 700;
  color: var(--cyber-cyan);
  letter-spacing: 4px;
  text-shadow: var(--cyber-glow-cyan);
  margin-bottom: 8px;
}

.subtitle {
  font-family: 'Orbitron', sans-serif;
  font-size: 10px;
  color: var(--cyber-text-dim);
  letter-spacing: 6px;
}

.login-form {
  margin-top: 30px;
}

.form-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  margin-bottom: 30px;
}

.form-title .line {
  width: 50px;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--cyber-cyan));
}

.form-title .text {
  font-family: 'Orbitron', sans-serif;
  font-size: 12px;
  color: var(--cyber-cyan);
  letter-spacing: 3px;
}

.input-wrapper {
  width: 100%;
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 15px;
  color: var(--cyber-cyan);
  font-size: 18px;
  z-index: 1;
  filter: drop-shadow(0 0 5px var(--cyber-cyan));
}

.input-wrapper :deep(.el-input__wrapper) {
  padding-left: 45px !important;
  height: 50px;
  background: rgba(0, 0, 0, 0.3) !important;
  border: 1px solid var(--cyber-border) !important;
  border-radius: 10px !important;
  box-shadow: inset 0 0 20px rgba(0, 0, 0, 0.5) !important;
}

.input-wrapper :deep(.el-input__inner) {
  color: var(--cyber-text) !important;
  font-size: 16px;
  letter-spacing: 1px;
}

.input-wrapper :deep(.el-input__inner)::placeholder {
  color: var(--cyber-text-dim) !important;
}

.login-btn {
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, var(--cyber-cyan), var(--cyber-blue)) !important;
  border: none !important;
  border-radius: 10px !important;
  font-family: 'Orbitron', sans-serif;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 4px;
  color: #000 !important;
  box-shadow: 
    0 0 30px rgba(0, 255, 255, 0.4),
    inset 0 0 1px rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.login-btn:hover {
  background: linear-gradient(135deg, var(--cyber-magenta), var(--cyber-cyan)) !important;
  box-shadow: 
    0 0 50px rgba(255, 0, 255, 0.5),
    inset 0 0 1px rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.card-footer {
  margin-top: 30px;
  text-align: center;
}

.register-link {
  text-align: center;
  margin-top: 16px;
}

.register-link a {
  color: var(--cyber-cyan);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
}

.register-link a:hover {
  color: var(--cyber-magenta);
  text-shadow: 0 0 10px var(--cyber-magenta);
}

.system-info {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  font-family: 'Orbitron', sans-serif;
  font-size: 10px;
  color: var(--cyber-text-dim);
  letter-spacing: 1px;
}

.version {
  color: var(--cyber-cyan);
}

.separator {
  color: var(--cyber-border);
}

.scanline {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, transparent, var(--cyber-cyan), transparent);
  opacity: 0.3;
  animation: scanline 8s linear infinite;
  pointer-events: none;
  z-index: 100;
}
</style>
