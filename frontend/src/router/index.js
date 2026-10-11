import { createRouter, createWebHistory } from 'vue-router'

import Login from '../views/Login.vue'
import Registro from '../views/Registro.vue'
import Contactos from '../views/Contactos.vue'
import { obtenerUsuarioActual } from '../services/api'

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
path: '/contactos',
name: 'contactos',
component: Contactos,
meta: { requiresAuth: true },
},
]

const router = createRouter({
history: createWebHistory(),
routes,
})

router.beforeEach(async (to) => {
if (!to.meta.requiresAuth) {
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
