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

    <v-col cols="auto" class="d-flex ga-2">
      <v-btn
        icon="mdi-refresh"
        variant="outlined"
        aria-label="Actualizar contactos"
        :loading="cargando"
        @click="emit('actualizar')"
      />

      <v-btn
        color="primary"
        prepend-icon="mdi-account-plus"
        @click="emit('crear')"
      >
        Nuevo contacto
      </v-btn>
    </v-col>
  </v-row>
</template>

<script setup>
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
</script>