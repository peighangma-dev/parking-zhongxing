<template>
  <div class="package-container">
    <div class="page-header">
      <h2>套餐管理</h2>
    </div>

    <div class="packages-grid">
      <div v-for="pkg in packages" :key="pkg.id" class="package-card">
        <div class="package-header">
          <h3>{{ pkg.packageName }}</h3>
          <span class="package-type">{{ pkg.packageType }}</span>
        </div>
        <div class="package-price">
          <span class="currency">¥</span>
          <span class="amount">{{ pkg.price }}</span>
          <span class="period">/月</span>
        </div>
        <div class="package-desc">{{ pkg.description }}</div>
        <ul class="package-features">
          <li>最大用户数: {{ pkg.maxUsers }}</li>
          <li>最大车位数: {{ pkg.maxSpaces }}</li>
          <li>最大设备数: {{ pkg.maxDevices }}</li>
        </ul>
        <div class="package-status">
          <span :class="['status', pkg.status]">{{ pkg.status }}</span>
        </div>
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
  packageType: string
  description: string
  maxUsers: number
  maxSpaces: number
  maxDevices: number
  price: number
  sortOrder: number
  status: string
}

const packages = ref<Package[]>([])

const loadPackages = async () => {
  try {
    const res = await fetch('/api/package/list')
    packages.value = await res.json()
  } catch (error) {
    console.error('Failed to load packages:', error)
  }
}

onMounted(() => {
  loadPackages()
})
</script>

<style scoped>
.package-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h2 {
  color: #00fff2;
  text-shadow: 0 0 10px rgba(0, 255, 242, 0.5);
}

.packages-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.package-card {
  background: linear-gradient(135deg, rgba(10, 30, 60, 0.9), rgba(20, 50, 80, 0.9));
  border: 1px solid rgba(0, 255, 242, 0.3);
  border-radius: 8px;
  padding: 24px;
  transition: all 0.3s;
}

.package-card:hover {
  border-color: #00fff2;
  box-shadow: 0 0 20px rgba(0, 255, 242, 0.2);
}

.package-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.package-header h3 {
  color: #fff;
  margin: 0;
}

.package-type {
  background: rgba(0, 255, 242, 0.2);
  color: #00fff2;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  text-transform: uppercase;
}

.package-price {
  display: flex;
  align-items: baseline;
  margin-bottom: 16px;
}

.currency {
  color: #ff00ff;
  font-size: 20px;
}

.amount {
  color: #ff00ff;
  font-size: 36px;
  font-weight: bold;
  text-shadow: 0 0 10px rgba(255, 0, 255, 0.5);
}

.period {
  color: #888;
  margin-left: 4px;
}

.package-desc {
  color: #aaa;
  font-size: 14px;
  margin-bottom: 16px;
  line-height: 1.5;
}

.package-features {
  list-style: none;
  padding: 0;
  margin: 0 0 16px 0;
}

.package-features li {
  color: #e0e0e0;
  padding: 8px 0;
  border-bottom: 1px solid rgba(0, 255, 242, 0.1);
}

.package-features li:last-child {
  border-bottom: none;
}

.package-status {
  text-align: center;
}

.status {
  padding: 6px 16px;
  border-radius: 4px;
  font-size: 12px;
  text-transform: uppercase;
}

.status.enabled {
  background: rgba(0, 255, 0, 0.2);
  color: #00ff00;
  border: 1px solid #00ff00;
}

.status.disabled {
  background: rgba(255, 0, 0, 0.2);
  color: #ff4444;
  border: 1px solid #ff4444;
}
</style>
