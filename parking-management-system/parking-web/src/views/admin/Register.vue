<template>
  <div class="register-container">
    <div class="register-box">
      <h2>租户注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>租户编码</label>
          <input v-model="form.tenantCode" type="text" required placeholder="唯一标识" />
        </div>
        <div class="form-group">
          <label>租户名称</label>
          <input v-model="form.tenantName" type="text" required placeholder="公司/停车场名称" />
        </div>
        <div class="form-group">
          <label>联系人</label>
          <input v-model="form.contactName" type="text" required placeholder="姓名" />
        </div>
        <div class="form-group">
          <label>联系电话</label>
          <input v-model="form.contactPhone" type="tel" required placeholder="手机号" />
        </div>
        <div class="form-group">
          <label>邮箱</label>
          <input v-model="form.contactEmail" type="email" placeholder="邮箱" />
        </div>
        <div class="form-group">
          <label>管理员密码</label>
          <input v-model="form.password" type="password" required placeholder="设置管理员密码" />
        </div>
        <div class="form-group">
          <label>选择套餐</label>
          <select v-model="form.packageId" required>
            <option value="">请选择套餐</option>
            <option v-for="pkg in packages" :key="pkg.id" :value="pkg.id">
              {{ pkg.packageName }} - ¥{{ pkg.price }}/月
            </option>
          </select>
        </div>
        <button type="submit" class="cyber-btn">注册</button>
        <p v-if="message" :class="['message', success ? 'success' : 'error']">{{ message }}</p>
      </form>
      <div class="login-link">
        已有账号? <router-link to="/login">返回登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Package {
  id: number
  packageCode: string
  packageName: string
  price: number
}

const form = ref({
  tenantCode: '',
  tenantName: '',
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  password: '',
  packageId: '' as number | ''
})

const packages = ref<Package[]>([])
const message = ref('')
const success = ref(false)

const loadPackages = async () => {
  try {
    const res = await fetch('/api/package/enabled')
    packages.value = await res.json()
  } catch (error) {
    console.error('Failed to load packages:', error)
  }
}

const handleRegister = async () => {
  try {
    const res = await fetch('/api/tenant/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
    const result = await res.json()
    message.value = result.message
    success.value = result.success
    if (result.success) {
      setTimeout(() => {
        window.location.href = '/login'
      }, 1500)
    }
  } catch (error) {
    message.value = '注册失败，请稍后重试'
    success.value = false
  }
}

onMounted(() => {
  loadPackages()
})
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a3a 100%);
}

.register-box {
  background: rgba(10, 20, 40, 0.9);
  border: 1px solid rgba(0, 255, 242, 0.3);
  border-radius: 12px;
  padding: 40px;
  width: 100%;
  max-width: 450px;
}

.register-box h2 {
  color: #00fff2;
  text-align: center;
  margin-bottom: 30px;
  text-shadow: 0 0 10px rgba(0, 255, 242, 0.5);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  color: #aaa;
  margin-bottom: 8px;
  font-size: 14px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(0, 255, 242, 0.3);
  border-radius: 6px;
  color: #fff;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #00fff2;
  box-shadow: 0 0 10px rgba(0, 255, 242, 0.2);
}

.cyber-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #0a3d62, #1e5f74);
  border: 1px solid #00fff2;
  color: #00fff2;
  font-size: 16px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s;
}

.cyber-btn:hover {
  background: rgba(0, 255, 242, 0.1);
  box-shadow: 0 0 20px rgba(0, 255, 242, 0.3);
}

.message {
  text-align: center;
  margin-top: 16px;
  padding: 10px;
  border-radius: 4px;
}

.message.success {
  background: rgba(0, 255, 0, 0.1);
  color: #00ff00;
  border: 1px solid #00ff00;
}

.message.error {
  background: rgba(255, 0, 0, 0.1);
  color: #ff4444;
  border: 1px solid #ff4444;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #888;
}

.login-link a {
  color: #00fff2;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
