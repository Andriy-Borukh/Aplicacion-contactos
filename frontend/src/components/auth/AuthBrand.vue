
<template>
  <div class="text-center mb-8">
    <div class="auth-brand-icon mb-4">
      <v-icon
        icon="mdi-account-multiple"
        size="34"
        color="primary"
      />
    </div>

    <h1 class="text-h4 font-weight-bold mb-2">
      Mis contactos
    </h1>

    <p class="text-body-2 text-medium-emphasis auth-subtitle">
      {{ descripcion }}
    </p>
  </div>
</template>

<script setup>
defineProps({
  descripcion: {
    type: String,
    default: 'Gestiona tus contactos de forma sencilla y organizada.',
  },
})
</script>