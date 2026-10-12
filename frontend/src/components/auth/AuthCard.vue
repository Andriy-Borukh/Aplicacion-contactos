<template>
  <div class="auth-page">
    <v-card
      class="auth-card pa-6 pa-sm-8"
      rounded="xl"
      elevation="0"
    >
      <slot />
    </v-card>

    <AuthFooter />
  </div>
</template>

<script setup>
import AuthFooter from '@/components/auth/AuthFooter.vue'
</script>

<style scoped>
.auth-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 32px 20px 64px;
}

.auth-footer {
  position: absolute;
  bottom: 16px;
  left: 0;
  width: 100%;
  text-align: center;
  padding: 0 16px;
}

@media (max-width: 600px) {
  .auth-page {
    padding: 20px 14px;
  }

  .auth-footer {
    position: static;
    margin-top: 24px;
  }
}
</style>