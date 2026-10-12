<template>
  <v-card class="etiquetas-card" rounded="lg">
    <v-card-title class="pa-5 d-flex align-center justify-space-between">
      <span>Etiquetas creadas</span>
      <v-chip>{{ etiquetas.length }}</v-chip>
    </v-card-title>

    <v-divider />

    <v-card-text v-if="cargando" class="text-center pa-8">
      <v-progress-circular indeterminate color="primary" />
      <p class="text-body-2 text-medium-emphasis mt-3">
        Cargando etiquetas...
      </p>
    </v-card-text>

    <v-card-text
      v-else-if="etiquetas.length === 0"
      class="text-center pa-8"
    >
      <v-icon size="48" color="medium-emphasis">
        mdi-tag-outline
      </v-icon>
      <p class="text-h6 mt-3">Todavía no tienes etiquetas</p>
      <p class="text-body-2 text-medium-emphasis">
        Crea una para empezar a organizar tus contactos.
      </p>
    </v-card-text>

    <div v-else class="etiquetas-lista-scroll">
      <v-list>
        <v-list-item
          v-for="etiqueta in etiquetas"
          :key="etiqueta.id"
          :title="etiqueta.nombre"
          prepend-icon="mdi-tag-outline"
        >
          <template #append>
            <v-btn
              icon="mdi-delete-outline"
              variant="text"
              color="error"
              :loading="eliminandoId === etiqueta.id"
              :disabled="eliminandoId !== null"
              aria-label="Eliminar etiqueta"
              @click="emit('eliminar', etiqueta)"
            />
          </template>
        </v-list-item>
      </v-list>
    </div>
  </v-card>
</template>

<script setup>
defineProps({
  etiquetas: {
    type: Array,
    default: () => [],
  },
  cargando: {
    type: Boolean,
    default: false,
  },
  eliminandoId: {
    type: [Number, String],
    default: null,
  },
})

const emit = defineEmits(['eliminar'])
</script>

<style scoped>

.etiquetas-card {
  display: flex;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
}

.etiquetas-lista-scroll {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

</style>