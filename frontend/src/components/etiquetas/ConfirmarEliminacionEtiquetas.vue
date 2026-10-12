
<template>
  <v-dialog
    :model-value="modelValue"
    max-width="500"
    persistent
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card rounded="lg">
      <v-card-title class="pa-5 d-flex align-center">
        <v-icon color="error" class="mr-3">
          mdi-alert-circle-outline
        </v-icon>
        Eliminar etiqueta
      </v-card-title>

      <v-card-text>
        <p class="mb-3">
          ¿Quieres eliminar la etiqueta
          <strong>{{ etiqueta?.nombre }}</strong>?
        </p>

        <v-alert
          type="warning"
          variant="tonal"
          density="compact"
          class="mb-4"
        >
          Los contactos no se eliminarán, pero perderán esta etiqueta.
        </v-alert>

        <div v-if="cargandoContactos" class="text-center pa-4">
          <v-progress-circular indeterminate color="primary" />
          <p class="text-body-2 mt-2">
            Consultando contactos asociados...
          </p>
        </div>

        <template v-else>
          <p class="text-body-2 mb-2">
            <strong>Contactos afectados: {{ contactos.length }}</strong>
          </p>

          <v-list
            v-if="contactos.length > 0"
            density="compact"
            max-height="200"
            class="overflow-y-auto"
          >
            <v-list-item
              v-for="contacto in contactos"
              :key="contacto.id"
              :title="`${contacto.nombre} ${contacto.apellidos}`"
              :subtitle="contacto.empresa || 'Sin empresa'"
              prepend-icon="mdi-account-outline"
            />
          </v-list>

          <p v-else class="text-body-2 text-medium-emphasis">
            Ningún contacto tiene asignada esta etiqueta.
          </p>
        </template>
      </v-card-text>

      <v-card-actions class="pa-5 pt-0">
        <v-spacer />

        <v-btn
          variant="text"
          :disabled="eliminando"
          @click="emit('cancelar')"
        >
          Cancelar
        </v-btn>

        <v-btn
          color="error"
          variant="flat"
          prepend-icon="mdi-delete-outline"
          :loading="eliminando"
          :disabled="cargandoContactos"
          @click="emit('confirmar')"
        >
          Eliminar etiqueta
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  etiqueta: {
    type: Object,
    default: null,
  },
  contactos: {
    type: Array,
    default: () => [],
  },
  cargandoContactos: {
    type: Boolean,
    default: false,
  },
  eliminando: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits([
  'update:modelValue',
  'confirmar',
  'cancelar',
])
</script>