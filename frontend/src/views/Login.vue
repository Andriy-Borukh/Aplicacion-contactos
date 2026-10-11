<template>
  <v-container class="fill-height" fluid>
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="5" lg="4">
        <v-card class="pa-6" rounded="xl" elevation="4">
          <div class="text-center mb-6">
            <v-icon icon="mdi-account-circle-outline" size="56" color="primary" />
            <h1 class="text-h4 font-weight-bold mt-3">Mis contactos</h1>
            <p class="text-body-2 text-medium-emphasis mt-2">
              Inicia sesión para continuar
            </p>
          </div>

      <v-form @submit.prevent="enviarFormulario">
        <v-text-field
          v-model="email"
          label="Correo electrónico"
          type="email"
          prepend-inner-icon="mdi-email-outline"
          autocomplete="username"
          required
        />

        <v-text-field
          v-model="password"
          label="Contraseña"
          :type="mostrarPassword ? 'text' : 'password'"
          prepend-inner-icon="mdi-lock-outline"
          :append-inner-icon="mostrarPassword ? 'mdi-eye-off' : 'mdi-eye'"
          autocomplete="current-password"
          required
          @click:append-inner="mostrarPassword = !mostrarPassword"
        />

        <v-alert
          v-if="error"
          type="error"
          variant="tonal"
          class="mb-4"
        >
          {{ error }}
        </v-alert>

        <v-btn
          type="submit"
          color="primary"
          block
          size="large"
          :loading="cargando"
          class="mt-2"
        >
          Iniciar sesión
        </v-btn>
      </v-form>

      <div class="text-center mt-5">
        ¿Todavía no tienes cuenta?
        <router-link to="/registro">Regístrate</router-link>
      </div>
    </v-card>
  </v-col>
</v-row>

  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { iniciarSesion } from '../services/api'

const router = useRouter()

const email = ref('')
const password = ref('')
const mostrarPassword = ref(false)
const cargando = ref(false)
const error = ref('')

async function enviarFormulario() {
  error.value = ''
  cargando.value = true

  try {
    await iniciarSesion(email.value, password.value)
    await router.push('/contactos')
  } catch (e) {
    error.value = e.message || 'No se ha podido iniciar sesión'
  } finally {
    cargando.value = false
  }
}
</script>
