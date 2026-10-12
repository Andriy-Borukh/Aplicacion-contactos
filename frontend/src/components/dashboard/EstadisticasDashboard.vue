<template>
  <v-row class="mb-6">
    <v-col cols="12" sm="6" lg="3">
      <v-card rounded="lg" class="pa-4 h-100" elevation="1">
        <div class="d-flex align-center justify-space-between mb-4">
          <span class="text-body-2 text-medium-emphasis">
            Total de contactos
          </span>
          <v-avatar color="primary" variant="tonal" rounded="lg">
            <v-icon icon="mdi-account-multiple-outline" />
          </v-avatar>
        </div>

        <div class="text-h4 font-weight-bold">
          {{ totalContactos }}
        </div>
        <div class="text-caption text-medium-emphasis mt-2">
          Contactos guardados
        </div>
      </v-card>
    </v-col>

    <v-col cols="12" sm="6" lg="3">
      <v-card rounded="lg" class="pa-4 h-100" elevation="1">
        <div class="d-flex align-center justify-space-between mb-4">
          <span class="text-body-2 text-medium-emphasis">
            Favoritos
          </span>
          <v-avatar color="warning" variant="tonal" rounded="lg">
            <v-icon icon="mdi-star-outline" />
          </v-avatar>
        </div>

        <div class="text-h4 font-weight-bold">
          {{ totalFavoritos }}
        </div>
        <div class="text-caption text-medium-emphasis mt-2">
          Contactos destacados
        </div>
      </v-card>
    </v-col>

    <v-col cols="12" sm="6" lg="3">
      <v-card rounded="lg" class="pa-4 h-100" elevation="1">
        <div class="d-flex align-center justify-space-between mb-4">
          <span class="text-body-2 text-medium-emphasis">
            Etiquetas utilizadas
          </span>
          <v-avatar color="info" variant="tonal" rounded="lg">
            <v-icon icon="mdi-tag-outline" />
          </v-avatar>
        </div>

        <div class="text-h4 font-weight-bold">
          {{ totalEtiquetas }}
        </div>
        <div class="text-caption text-medium-emphasis mt-2">
          Etiquetas diferentes
        </div>
      </v-card>
    </v-col>

    <v-col cols="12" sm="6" lg="3">
      <v-card rounded="lg" class="pa-4 h-100" elevation="1">
        <div class="d-flex align-center justify-space-between mb-4">
          <span class="text-body-2 text-medium-emphasis">
            Cumpleaños próximos
          </span>
          <v-avatar color="success" variant="tonal" rounded="lg">
            <v-icon icon="mdi-cake-variant-outline" />
          </v-avatar>
        </div>

        <div class="text-h4 font-weight-bold">
          {{ totalCumpleanos }}
        </div>
        <div class="text-caption text-medium-emphasis mt-2">
          Durante los próximos 30 días
        </div>
      </v-card>
    </v-col>
  </v-row>
</template>

<script setup>
defineProps({
  totalContactos: {
    type: Number,
    default: 0,
  },
  totalFavoritos: {
    type: Number,
    default: 0,
  },
  totalEtiquetas: {
    type: Number,
    default: 0,
  },
  totalCumpleanos: {
    type: Number,
    default: 0,
  },
})
</script>
