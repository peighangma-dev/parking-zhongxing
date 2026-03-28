<template>
  <el-select
    v-model="selectedTenantId"
    placeholder="选择租户"
    clearable
    style="min-width: 150px; margin-right: 10px"
    @change="handleTenantChange"
    :disabled="disabled"
  >
    <el-option
      v-for="tenant in tenants"
      :key="tenant.id"
      :label="tenant.tenantName"
      :value="tenant.id"
    />
  </el-select>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import request from '@/utils/request'

interface Tenant {
  id: number
  tenantCode: string
  tenantName: string
  status: string
}

const props = defineProps<{
  modelValue?: number | null
  disabled?: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: number | null)
  (e: 'change', value: number | null)
}>()

const selectedTenantId = ref<number | null>(props.modelValue || null)
const tenants = ref<Tenant[]>([])

const loadTenants = async () => {
  try {
    const res: any = await request.get('/tenant/v1/list')
    tenants.value = res || []
  } catch (error) {
    console.error('Failed to load tenants:', error)
  }
}

const handleTenantChange = (val: number | null) => {
  localStorage.setItem('selectedTenantId', val ? String(val) : '')
  emit('update:modelValue', val)
  emit('change', val)
}

watch(() => props.modelValue, (val) => {
  selectedTenantId.value = val || null
})

onMounted(() => {
  loadTenants()
  // 恢复之前选择的租户
  const savedTenantId = localStorage.getItem('selectedTenantId')
  if (savedTenantId && !props.modelValue) {
    const tenantId = parseInt(savedTenantId, 10)
    selectedTenantId.value = tenantId
    emit('update:modelValue', tenantId)
    emit('change', tenantId)
  }
})
</script>
