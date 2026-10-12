<template>
  <v-app>
    <v-navigation-drawer
      v-model="menuAbierto"
      :permanent="!esMovil"
      :temporary="esMovil"
      width="260"
      color="secondary"
    >
      <div class="pa-5 d-flex align-center ga-3">
        <v-avatar color="primary" rounded="lg" size="42">
          <v-icon icon="mdi-account-multiple" color="white" size="26" />
        </v-avatar>

        <div>
          <div class="text-h6 font-weight-bold text-white">
            Mis contactos
          </div>
          <div class="text-caption text-blue-grey-lighten-3">
            Gestión personal
          </div>
        </div>
      </div>

      <v-divider color="white" opacity="0.12" class="mb-4" />

      <v-list nav class="px-3" color="primary">
        <v-list-item
          prepend-icon="mdi-view-dashboard-outline"
          title="Dashboard"
          to="/dashboard"
          rounded="lg"
        />

        <v-list-item
          prepend-icon="mdi-account-multiple-outline"
          title="Contactos"
          to="/contactos"
          rounded="lg"
        />

        <v-list-item
          prepend-icon="mdi-tag-multiple-outline"
          title="Etiquetas"
          to="/etiquetas"
          rounded="lg"
        />
      </v-list>

      <template #append>
        <div class="pa-4">
          <v-divider color="white" opacity="0.12" class="mb-3" />

          <v-list nav class="pa-0">
            <v-list-item
              prepend-icon="mdi-theme-light-dark"
              title="Cambiar tema"
              rounded="lg"
              @click="cambiarTema"
            />
          </v-list>
        </div>
      </template>
    </v-navigation-drawer>

    <v-app-bar flat border="b" color="surface">
      <v-app-bar-nav-icon
        v-if="esMovil"
        aria-label="Abrir menú"
        @click="menuAbierto = !menuAbierto"
      />

      <v-app-bar-title class="font-weight-medium">
        {{ tituloPagina }}
      </v-app-bar-title>

      <template #append>
        <v-btn
          :icon="temaOscuro ? 'mdi-weather-sunny' : 'mdi-weather-night'"
          variant="text"
          :aria-label="temaOscuro ? 'Activar tema claro' : 'Activar tema oscuro'"
          @click="cambiarTema"
        />
      </template>
    </v-app-bar>

    <v-main class="contenido-main">
        <v-container fluid class="pa-4 pa-md-8 contenido-principal">
            <router-view />
        </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { useTheme } from 'vuetify'

const route = useRoute()
const theme = useTheme()

const menuAbierto = ref(true)
const esMovil = ref(false)

const temaOscuro = computed(() => theme.global.current.value.dark)

const tituloPagina = computed(() => {
  const titulos = {
    '/dashboard': 'Dashboard',
    '/contactos': 'Mis contactos',
    '/etiquetas': 'Mis etiquetas',
  }

  return titulos[route.path] || 'Mis contactos'
})

function cambiarTema() {
  theme.global.name.value = temaOscuro.value ? 'temaClaro' : 'temaOscuro'
}

function actualizarDispositivo() {
  esMovil.value = window.innerWidth < 960
  menuAbierto.value = !esMovil.value
}

onMounted(() => {
  actualizarDispositivo()
  window.addEventListener('resize', actualizarDispositivo)
})

onUnmounted(() => {
  window.removeEventListener('resize', actualizarDispositivo)
})
</script>

<style scoped>
.contenido-main {
  height: calc(100dvh - 64px);
  overflow: hidden;
}

.contenido-principal {
  height: 100%;
  max-width: 1800px;
  display: flex;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
}

:deep(.v-navigation-drawer) {
  border-right: none;
}

:deep(.v-list-item--active) {
  background: rgba(113, 151, 255, 0.18);
  color: #ffffff;
}

@media (max-width: 600px) {
  .contenido-principal {
    padding: 16px !important;
  }
}
</style>