<template>
  <v-snackbar
    :model-value="modelValue"
    color="success"
    location="bottom end"
    :timeout="3000"
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-icon start icon="mdi-check-circle-outline" />
    {{ mensaje }}

    <template #actions>
      <v-btn
        variant="text"
        @click="emit('update:modelValue', false)"
      >
        Cerrar
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script setup>
defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  mensaje: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update:modelValue'])
</script>
