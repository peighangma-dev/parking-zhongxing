<template>
  <div class="tenant-container">
    <div class="page-header">
      <h2>租户管理</h2>
      <button class="cyber-btn" @click="refresh">刷新</button>
    </div>

    <div class="table-wrapper">
      <table class="cyber-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>租户编码</th>
            <th>租户名称</th>
            <th>联系人</th>
            <th>联系电话</th>
            <th>套餐</th>
            <th>状态</th>
            <th>到期时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tenant in tenants" :key="tenant.id">
            <td>{{ tenant.id }}</td>
            <td>{{ tenant.tenantCode }}</td>
            <td>{{ tenant.tenantName }}</td>
            <td>{{ tenant.contactName }}</td>
            <td>{{ tenant.contactPhone }}</td>
            <td>{{ getPackageName(tenant.packageId) }}</td>
            <td>
              <span :class="['status-badge', tenant.status]">{{ tenant.status }}</span>
            </td>
            <td>{{ formatDate(tenant.expireTime) }}</td>
            <td>
              <button 
                class="action-btn" 
                @click="toggleStatus(tenant)"
                :class="tenant.status === 'active' ? 'danger' : 'success'"
              >
                {{ tenant.status === 'active' ? '禁用' : '启用' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Tenant {
  id: number
  tenantCode: string
  tenantName: string
  contactName: string
  contactPhone: string
  contactEmail: string
  packageId: number
  status: string
  expireTime: string
  maxUsers: number
  maxSpaces: number
}

interface Package {
  id: number
  packageCode: string
  packageName: string
}

const tenants = ref<Tenant[]>([])
const packages = ref<Package[]>([])

const loadTenants = async () => {
  try {
    const res = await fetch('/api/tenant/list')
    tenants.value = await res.json()
  } catch (error) {
    console.error('Failed to load tenants:', error)
  }
}

const loadPackages = async () => {
  try {
    const res = await fetch('/api/package/list')
    packages.value = await res.json()
  } catch (error) {
    console.error('Failed to load packages:', error)
  }
}

const getPackageName = (packageId: number) => {
  const pkg = packages.value.find(p => p.id === packageId)
  return pkg ? pkg.packageName : '-'
}

const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString()
}

const toggleStatus = async (tenant: Tenant) => {
  const newStatus = tenant.status === 'active' ? 'disabled' : 'active'
  try {
    await fetch(`/api/tenant/status/${tenant.id}?status=${newStatus}`, {
      method: 'PUT'
    })
    await loadTenants()
  } catch (error) {
    console.error('Failed to update status:', error)
  }
}

const refresh = () => {
  loadTenants()
  loadPackages()
}

onMounted(() => {
  loadTenants()
  loadPackages()
})
</script>

<style scoped>
.tenant-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  color: #00fff2;
  text-shadow: 0 0 10px rgba(0, 255, 242, 0.5);
}

.cyber-btn {
  background: linear-gradient(135deg, #0a3d62, #1e5f74);
  border: 1px solid #00fff2;
  color: #00fff2;
  padding: 8px 20px;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.3s;
}

.cyber-btn:hover {
  background: rgba(0, 255, 242, 0.1);
  box-shadow: 0 0 15px rgba(0, 255, 242, 0.3);
}

.table-wrapper {
  overflow-x: auto;
}

.cyber-table {
  width: 100%;
  border-collapse: collapse;
  background: rgba(10, 20, 40, 0.8);
  border: 1px solid rgba(0, 255, 242, 0.3);
}

.cyber-table th,
.cyber-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid rgba(0, 255, 242, 0.2);
  color: #e0e0e0;
}

.cyber-table th {
  background: rgba(0, 255, 242, 0.1);
  color: #00fff2;
  font-weight: 500;
}

.cyber-table tr:hover {
  background: rgba(0, 255, 242, 0.05);
}

.status-badge {
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  text-transform: uppercase;
}

.status-badge.active {
  background: rgba(0, 255, 0, 0.2);
  color: #00ff00;
  border: 1px solid #00ff00;
}

.status-badge.disabled {
  background: rgba(255, 0, 0, 0.2);
  color: #ff4444;
  border: 1px solid #ff4444;
}

.action-btn {
  padding: 5px 12px;
  border: 1px solid;
  background: transparent;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.3s;
}

.action-btn.danger {
  color: #ff4444;
  border-color: #ff4444;
}

.action-btn.danger:hover {
  background: rgba(255, 68, 68, 0.1);
}

.action-btn.success {
  color: #00ff00;
  border-color: #00ff00;
}

.action-btn.success:hover {
  background: rgba(0, 255, 0, 0.1);
}
</style>
