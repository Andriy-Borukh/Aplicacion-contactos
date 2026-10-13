<template>
  <v-alert
      v-if="mensaje"
      type="error"
      variant="tonal"
      class="alerta-error mb-4"
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

<style scoped>
.alerta-error {
  width: 100%;
  min-width: 0;
  height: auto;
  white-space: normal;
  overflow-wrap: anywhere;
}
</style>
