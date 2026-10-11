<template>
  <v-dialog
    :model-value="modelValue"
    max-width="450"
    :persistent="eliminando"
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card rounded="xl">
      <v-card-title class="text-h6 pa-5">
        Eliminar contacto
      </v-card-title>

      <v-card-text class="px-5">
        ¿Seguro que quieres eliminar a
        <strong>
          {{ contacto?.nombre }}
          {{ contacto?.apellidos || '' }}
        </strong>?

        <p class="text-body-2 text-medium-emphasis mt-3">
          Esta acción no se puede deshacer.
        </p>
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
          :loading="eliminando"
          @click="emit('confirmar')"
        >
          Eliminar
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  contacto: {
    type: Object,
    default: null,
  },
  eliminando: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits([
  'update:modelValue',
  'cancelar',
  'confirmar',
])
</script>