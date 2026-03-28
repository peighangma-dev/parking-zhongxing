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
          path: '/space/area',
          name: 'SpaceArea',
          component: () => import('@/views/space/Area.vue')
        },
        {
          path: '/space/space',
          name: 'SpaceSpace',
          component: () => import('@/views/space/Space.vue')
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
          path: '/uc/parking-lot',
          name: 'ParkingLotManage',
          component: () => import('@/views/uc/ParkingLotManage.vue')
        }
      ]
    }
  ]
})

export default router
