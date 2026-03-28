import { ref } from 'vue'

const featureCodes = ref<string[]>([])
const packageId = ref<number | null>(null)

export function useFeaturePermission() {
  const loadFeatures = async (tenantId: number) => {
    try {
      const res = await fetch(`/api/tenant/features/${tenantId}`)
      const data = await res.json()
      if (data.success) {
        featureCodes.value = data.features || []
        packageId.value = data.packageId
      }
    } catch (error) {
      console.error('Failed to load features:', error)
    }
  }

  const hasFeature = (featureCode: string): boolean => {
    if (featureCodes.value.length === 0) {
      return true
    }
    return featureCodes.value.includes(featureCode)
  }

  const isFeatureEnabled = (featureCode: string): boolean => {
    return hasFeature(featureCode)
  }

  return {
    featureCodes,
    packageId,
    loadFeatures,
    hasFeature,
    isFeatureEnabled
  }
}

export const FEATURE_CODES = {
  VEHICLE_MANAGEMENT: 'vehicle_management',
  MEMBER_MANAGEMENT: 'member_management',
  BLACKLIST: 'blacklist',
  PAYMENT_CHANNEL: 'payment_channel',
  RATE_RULE: 'rate_rule',
  ADVANCED_REPORTS: 'advanced_reports',
  BASIC_REPORTS: 'basic_reports',
  ELECTRONIC_INVOICE: 'electronic_invoice',
  MULTI_PARKING_LOT: 'multi_parking_lot',
  API_INTEGRATION: 'api_integration',
  CUSTOM_BRANDING: 'custom_branding'
}
