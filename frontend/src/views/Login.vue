
<template>
  <AuthCard>
    <AuthBrand
      descripcion="Accede a tu cuenta y sigue organizando tus contactos." 
    />

    <AuthFormulario
      modo="login"
      :loading="cargando"
      :error="error"
      @submit="enviarFormulario"
    />

    <v-divider class="my-6" />

    <p class="text-center text-body-2 text-medium-emphasis">
      ¿Todavía no tienes cuenta?
      <router-link to="/registro">Regístrate</router-link>
    </p>
  </AuthCard>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { iniciarSesion } from '@/services/api.js'
import AuthCard from '@/components/auth/AuthCard.vue'
import AuthBrand from '@/components/auth/AuthBrand.vue'
import AuthFormulario from '@/components/auth/AuthFormulario.vue'

const router = useRouter()

const cargando = ref(false)
const error = ref('')

async function enviarFormulario(datos) {
  error.value = ''
  cargando.value = true

  try {
    await iniciarSesion(datos.email, datos.password)
    await router.push('/contactos')
  } catch (e) {
    error.value =
      e.message || 'No se ha podido iniciar sesión'
  } finally {
    cargando.value = false
  }
}
</script>