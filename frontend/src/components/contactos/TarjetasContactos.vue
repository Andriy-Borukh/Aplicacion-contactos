<template>
  <v-row>
    <v-col
      v-for="contacto in contactos"
      :key="contacto.id"
      cols="12"
      sm="6"
      lg="4"
    >
      <v-card height="100%">
        <v-card-item>
          <template #prepend>
            <v-avatar color="primary" size="48">
              {{ contacto.nombre?.charAt(0)?.toUpperCase() || '?' }}{{ contacto.apellidos?.charAt(0)?.toUpperCase() || '' }}
            </v-avatar>
          </template>

          <v-card-title>
            {{ contacto.nombre }} {{ contacto.apellidos || '' }}
          </v-card-title>

          <v-card-subtitle>
            {{ contacto.empresa || 'Sin empresa' }}
          </v-card-subtitle>

          <template #append>
            <v-btn
              :icon="contacto.favorito ? 'mdi-star' : 'mdi-star-outline'"
              :color="contacto.favorito ? 'amber-darken-2' : 'grey'"
              variant="text"
              size="small"
              :aria-label="
                contacto.favorito
                  ? 'Quitar de favoritos'
                  : 'Añadir a favoritos'
              "
              @click="emit('cambiar-favorito', contacto)"
            />
          </template>
        </v-card-item>

        <v-divider />

        <v-card-text>
          <div class="d-flex align-center mb-2">
            <v-icon icon="mdi-phone-outline" class="mr-2" />
            <span>{{ contacto.telefono || 'Sin teléfono' }}</span>
          </div>

          <div class="d-flex align-center">
            <v-icon icon="mdi-email-outline" class="mr-2" />
            <span>{{ contacto.email || 'Sin email' }}</span>
          </div>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn
            prepend-icon="mdi-pencil-outline"
            variant="text"
            color="primary"
            @click="emit('editar', contacto)"
          >
            Editar
          </v-btn>

          <v-btn
            icon="mdi-delete-outline"
            variant="text"
            color="error"
            aria-label="Eliminar contacto"
            @click="emit('eliminar', contacto)"
          />
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>
</template>

<script setup>
defineProps({
  contactos: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits([
  'cambiar-favorito',
  'editar',
  'eliminar',
])
</script>
