<template>
  <AuthCard>
    <AuthBrand 
      descripcion="Crea tu cuenta y empieza a organizar tus contactos."
    />

    <AuthFormulario
      v-if="!registroCorrecto"
      modo="registro"
      :loading="cargando"
      :error="error"
      :errores-campos="erroresCampos"
      @submit="enviarFormulario"
    />

    <div v-else class="text-center">
      <v-icon
        icon="mdi-check-circle-outline"
        color="success"
        size="64"
        class="mb-4"
      />

      <h2 class="text-h6 font-weight-bold mb-2">
        ¡Cuenta creada!
      </h2>

      <p class="text-body-2 text-medium-emphasis mb-6">
        Tu cuenta se ha registrado correctamente.
        Ya puedes iniciar sesión.
      </p>

      <v-btn
        color="primary"
        block
        size="large"
        rounded="lg"
        to="/login"
        class="boton-iniciar-sesion text-none font-weight-bold"
      >
        Ir al inicio de sesión
        <v-icon icon="mdi-arrow-right" end />
      </v-btn>
    </div>

    <v-divider class="my-6" />

    <p class="text-center text-body-2 text-medium-emphasis">
      ¿Ya tienes una cuenta?
      <router-link to="/login">Inicia sesión</router-link>
    </p>
  </AuthCard>
</template>

<script setup>
import { ref } from 'vue'
import { registrarUsuario } from '@/services/api.js'
import AuthCard from '@/components/auth/AuthCard.vue'
import AuthBrand from '@/components/auth/AuthBrand.vue'
import AuthFormulario from '@/components/auth/AuthFormulario.vue'

const cargando = ref(false)
const error = ref('')
const erroresCampos = ref({})
const registroCorrecto = ref(false)

async function enviarFormulario(datos) {
  error.value = ''
  erroresCampos.value = {}
  cargando.value = true

  try {
    await registrarUsuario(datos)

    registroCorrecto.value = true
  } catch (e) {
    error.value = e.message || 'No se ha podido crear la cuenta'

    if (e.erroresCampos) {
      erroresCampos.value = e.erroresCampos
    }
  } finally {
    cargando.value = false
  }
}
</script>


<style scoped>
.boton-iniciar-sesion {
  color: #ffffff !important;
}

.boton-iniciar-sesion :deep(.v-icon) {
  color: #ffffff !important;
}
</style>