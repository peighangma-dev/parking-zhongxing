import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/',
      component: () => import('@/views/Layout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: '/dashboard',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue')
        },
        {
          path: '/parking/lot',
          name: 'ParkingLot',
          component: () => import('@/views/parking/Lot.vue')
        },
        {
          path: '/parking/area',
          name: 'ParkingArea',
          component: () => import('@/views/space/Area.vue')
        },
        {
          path: '/parking/space',
          name: 'ParkingSpace',
          component: () => import('@/views/space/Space.vue')
        },
        {
          path: '/barrier/device',
          name: 'BarrierDevice',
          component: () => import('@/views/barrier/Device.vue')
        },
        {
          path: '/barrier/lane',
          name: 'BarrierLane',
          component: () => import('@/views/barrier/Lane.vue')
        },
        {
          path: '/vehicle/list',
          name: 'VehicleList',
          component: () => import('@/views/vehicle/List.vue')
        },
        {
          path: '/vehicle/member',
          name: 'VehicleMember',
          component: () => import('@/views/vehicle/Member.vue')
        },
        {
          path: '/vehicle/blacklist',
          name: 'VehicleBlacklist',
          component: () => import('@/views/vehicle/Blacklist.vue')
        },
        {
          path: '/payment/channel',
          name: 'PaymentChannel',
          component: () => import('@/views/payment/Channel.vue')
        },
        {
          path: '/payment/rate',
          name: 'PaymentRate',
          component: () => import('@/views/payment/Rate.vue')
        },
        {
          path: '/payment/record',
          name: 'PaymentRecord',
          component: () => import('@/views/payment/Record.vue')
        },
        {
          path: '/report/pass',
          name: 'ReportPass',
          component: () => import('@/views/report/PassRecord.vue')
        },
        {
          path: '/report/daily',
          name: 'ReportDaily',
          component: () => import('@/views/report/DailyReport.vue')
        },
        {
          path: '/uc/user',
          name: 'UserManage',
          component: () => import('@/views/uc/UserManage.vue')
        },
        {
          path: '/uc/role',
          name: 'RoleManage',
          component: () => import('@/views/uc/Role.vue')
        },
        {
          path: '/admin/tenant',
          name: 'TenantManage',
          component: () => import('@/views/admin/Tenant.vue')
        },
        {
          path: '/admin/package',
          name: 'PackageManage',
          component: () => import('@/views/admin/Package.vue')
        },
        {
          path: '/register',
          name: 'Register',
          component: () => import('@/views/admin/Register.vue')
        }
      ]
    }
  ]
})

export default router
