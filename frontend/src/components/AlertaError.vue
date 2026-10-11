<template>
  <v-alert
    v-if="mensaje"
    type="error"
    variant="tonal"
    class="mb-4"
    closable
    @click:close="emit('cerrar')"
  >
    {{ mensaje }}
  </v-alert>
</template>

<script setup>
defineProps({
  mensaje: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['cerrar'])
</script>
