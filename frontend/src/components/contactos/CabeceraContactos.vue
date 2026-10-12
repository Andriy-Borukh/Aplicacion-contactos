<template>
  <v-row align="center" class="mb-6">
    <v-col>
      <h1 class="text-h4 font-weight-bold">
        Mis contactos
      </h1>

      <p class="text-body-2 text-medium-emphasis mt-1">
        Bienvenido, {{ usuario?.nombre || 'usuario' }}
      </p>
    </v-col>

    <v-col cols="auto" class="d-flex ga-2 flex-wrap justify-end">
      <v-btn
        icon="mdi-refresh"
        variant="outlined"
        aria-label="Actualizar contactos"
        :loading="cargando"
        @click="emit('actualizar')"
      />

      <v-btn
        prepend-icon="mdi-tag-multiple-outline"
        variant="outlined"
        to="/etiquetas"
      >
        Mis etiquetas
      </v-btn>

      <v-btn
        color="primary"
        prepend-icon="mdi-account-plus"
        @click="emit('crear')"
      >
        Nuevo contacto
      </v-btn>

      <v-btn
        color="error"
        variant="outlined"
        prepend-icon="mdi-logout"
        :loading="cerrandoSesion"
        :disabled="cerrandoSesion"
        @click="cerrarSesionUsuario"
      >
        Cerrar sesión
      </v-btn>
    </v-col>
  </v-row>

  <v-alert
    v-if="errorLogout"
    type="error"
    variant="tonal"
    closable
    class="mb-4"
    @click:close="errorLogout = ''"
  >
    {{ errorLogout }}
  </v-alert>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { cerrarSesion } from '@/services/api.js'

defineProps({
  usuario: {
    type: Object,
    default: null,
  },
  cargando: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['actualizar', 'crear'])

const router = useRouter()
const cerrandoSesion = ref(false)
const errorLogout = ref('')

async function cerrarSesionUsuario() {
  errorLogout.value = ''
  cerrandoSesion.value = true

  try {
    await cerrarSesion()
    await router.replace('/login')
  } catch (e) {
    errorLogout.value =
      e.message || 'No se ha podido cerrar la sesión. Inténtalo de nuevo.'
  } finally {
    cerrandoSesion.value = false
  }
}
</script>