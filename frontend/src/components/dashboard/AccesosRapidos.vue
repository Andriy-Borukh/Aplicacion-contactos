<template>
  <v-card rounded="xl" elevation="0" border>
    <v-card-title class="pa-5 text-h6 font-weight-bold">
      Accesos rápidos
    </v-card-title>

    <v-divider />

    <v-card-text class="pa-5 d-flex flex-column ga-3">
      <v-btn
        to="/contactos"
        color="primary"
        prepend-icon="mdi-account-plus-outline"
        block
        size="large"
        class="boton-gestionar-contactos text-none"
      >
        Gestionar contactos
      </v-btn>

      <v-btn
        to="/etiquetas"
        variant="outlined"
        prepend-icon="mdi-tag-plus-outline"
        block
        size="large"
        class="text-none"
      >
        Gestionar etiquetas
      </v-btn>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.boton-gestionar-contactos {
  color: #ffffff !important;
}

.boton-gestionar-contactos :deep(.v-btn__content),
.boton-gestionar-contactos :deep(.v-icon) {
  color: #ffffff !important;
}
</style>