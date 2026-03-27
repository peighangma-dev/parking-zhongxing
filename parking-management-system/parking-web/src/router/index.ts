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
          path: '/payment/channel',
          name: 'PaymentChannel',
          component: () => import('@/views/payment/Channel.vue')
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
