<template>
  <v-card rounded="lg" elevation="1" class="h-100">
    <v-card-title class="d-flex align-center justify-space-between pa-5">
      <div>
        <div class="text-h6 font-weight-bold">Contactos recientes</div>
        <div class="text-body-2 text-medium-emphasis mt-1">
          Tus últimos contactos añadidos
        </div>
      </div>

      <v-btn
        color="primary"
        variant="text"
        append-icon="mdi-arrow-right"
        :to="{ name: 'contactos' }"
      >
        Ver todos
      </v-btn>
    </v-card-title>

    <v-divider />

    <v-card-text v-if="contactos.length === 0" class="text-center py-10">
      <v-icon
        icon="mdi-account-multiple-outline"
        size="42"
        color="medium-emphasis"
        class="mb-3"
      />
      <div class="text-body-1 font-weight-medium">
        Todavía no tienes contactos
      </div>
      <div class="text-body-2 text-medium-emphasis mt-1">
        Cuando añadas contactos, aparecerán aquí.
      </div>

      <v-btn
        color="primary"
        class="mt-4"
        :to="{ name: 'contactos' }"
      >
        Ir a contactos
      </v-btn>
    </v-card-text>

    <v-list v-else class="py-0">
      <v-list-item
        v-for="contacto in contactos"
        :key="contacto.id"
        class="px-5 py-2"
      >
        <template #prepend>
          <v-avatar color="primary" variant="tonal" size="44">
            <span class="font-weight-bold">
              {{ iniciales(contacto) }}
            </span>
          </v-avatar>
        </template>

        <v-list-item-title class="font-weight-medium">
          {{ contacto.nombre }} {{ contacto.apellidos || '' }}
        </v-list-item-title>

        <v-list-item-subtitle class="mt-1">
          {{ contacto.empresa || contacto.email || 'Sin información adicional' }}
        </v-list-item-subtitle>

        <template #append>
          <v-icon
            v-if="contacto.favorito"
            icon="mdi-star"
            color="warning"
            size="small"
          />
        </template>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script setup>
defineProps({
  contactos: {
    type: Array,
    default: () => [],
  },
})

function iniciales(contacto) {
  const nombre = contacto.nombre?.trim() || ''
  const apellidos = contacto.apellidos?.trim() || ''

  return `${nombre.charAt(0)}${apellidos.charAt(0)}`.toUpperCase() || '?'
}
</script>