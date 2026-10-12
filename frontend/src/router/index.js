import { createRouter, createWebHistory } from 'vue-router'

import Login from '@/views/Login.vue'
import Registro from '@/views/Registro.vue'
import Contactos from '@/views/Contactos.vue'
import Etiquetas from '@/views/Etiquetas.vue'
import AppLayout from '@/layouts/AppLayout.vue'
import DashBoard from '@/views/DashBoard.vue'

import { obtenerUsuarioActual } from '@/services/api.js'

const routes = [
  {
    path: '/',
    redirect: '/contactos',
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
  },
  {
    path: '/registro',
    name: 'registro',
    component: Registro,
  },
  {
    path: '/',
    component: AppLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'contactos',
        name: 'contactos',
        component: Contactos,
      },
      {
        path: 'etiquetas',
        name: 'etiquetas',
        component: Etiquetas,
      },
      {
        path: 'dashboard',
        name: 'dashboard',
        component: DashBoard,
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to) => {
  if (!to.matched.some((record) => record.meta.requiresAuth)) {
    return true
  }

  try {
    await obtenerUsuarioActual()
    return true
  } catch {
    return {
      name: 'login',
      query: { redirect: to.fullPath },
    }
  }
})

export default router