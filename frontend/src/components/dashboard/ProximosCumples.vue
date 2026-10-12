<template>
  <v-card rounded="xl" elevation="0" border>
    <v-card-title class="pa-5 text-h6 font-weight-bold">
      Próximos cumpleaños
    </v-card-title>

    <v-divider />

    <v-list v-if="cumpleanos.length" lines="two">
      <v-list-item
        v-for="contacto in cumpleanos"
        :key="contacto.id"
        :title="`${contacto.nombre} ${contacto.apellidos || ''}`.trim()"
        :subtitle="formatearCumpleanos(contacto.fechaCumpleanos)"
      >
        <template #prepend>
          <v-avatar color="info" variant="tonal">
            <v-icon icon="mdi-cake-variant-outline" />
          </v-avatar>
        </template>
      </v-list-item>
    </v-list>

    <v-card-text v-else class="text-body-2 text-medium-emphasis">
      No hay cumpleaños próximos registrados.
    </v-card-text>
  </v-card>
</template>

<script setup>
defineProps({
  cumpleanos: {
    type: Array,
    default: () => [],
  },
})

function formatearCumpleanos(fecha) {
  if (!fecha) return ''

  const [, mes, dia] = fecha.split('-').map(Number)

  return new Date(2000, mes - 1, dia).toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'long',
  })
}
</script>