import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/admin/Register.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: () => import('@/views/Layout.vue'),
      redirect: '/dashboard',
      meta: { requiresAuth: true },
      children: [
        {
          path: '/dashboard',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/parking/lot',
          name: 'ParkingLot',
          component: () => import('@/views/parking/Lot.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/parking/area',
          name: 'ParkingArea',
          component: () => import('@/views/space/Area.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/parking/space',
          name: 'ParkingSpace',
          component: () => import('@/views/space/Space.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/barrier/device',
          name: 'BarrierDevice',
          component: () => import('@/views/barrier/Device.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/barrier/lane',
          name: 'BarrierLane',
          component: () => import('@/views/barrier/Lane.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/vehicle/list',
          name: 'VehicleList',
          component: () => import('@/views/vehicle/List.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/vehicle/member',
          name: 'VehicleMember',
          component: () => import('@/views/vehicle/Member.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/vehicle/blacklist',
          name: 'VehicleBlacklist',
          component: () => import('@/views/vehicle/Blacklist.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/payment/channel',
          name: 'PaymentChannel',
          component: () => import('@/views/payment/Channel.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/payment/rate',
          name: 'PaymentRate',
          component: () => import('@/views/payment/Rate.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/payment/record',
          name: 'PaymentRecord',
          component: () => import('@/views/payment/Record.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/report/pass',
          name: 'ReportPass',
          component: () => import('@/views/report/PassRecord.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/report/daily',
          name: 'ReportDaily',
          component: () => import('@/views/report/DailyReport.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/uc/user',
          name: 'UserManage',
          component: () => import('@/views/uc/UserManage.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/uc/role',
          name: 'RoleManage',
          component: () => import('@/views/uc/Role.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/uc/menu',
          name: 'MenuManage',
          component: () => import('@/views/uc/MenuManage.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/uc/operation-log',
          name: 'OperationLog',
          component: () => import('@/views/uc/OperationLog.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/uc/login-log',
          name: 'LoginLog',
          component: () => import('@/views/uc/LoginLog.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/uc/system-config',
          name: 'SystemConfig',
          component: () => import('@/views/uc/SystemConfig.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/admin/tenant',
          name: 'TenantManage',
          component: () => import('@/views/admin/Tenant.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/admin/package',
          name: 'PackageManage',
          component: () => import('@/views/admin/Package.vue'),
          meta: { requiresAuth: true }
        }
      ]
    }
  ]
})

let routerInstance: any = null

export const setRouter = (router: any) => {
  routerInstance = router
}

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth !== false) {
    if (!token) {
      next('/login')
      return
    }
  }
  
  if ((to.path === '/login' || to.path === '/register') && token) {
    next('/dashboard')
    return
  }
  
  next()
})

export default router
