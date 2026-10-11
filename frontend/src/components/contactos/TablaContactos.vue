<template>
  <v-table>
    <thead>
      <tr>
        <th>Nombre</th>
        <th>Teléfono</th>
        <th>Email</th>
        <th>Empresa</th>
        <th>Favorito</th>
        <th class="text-end">Acciones</th>
      </tr>
    </thead>

    <tbody>
      <tr
        v-for="contacto in contactos"
        :key="contacto.id"
      >
        <td class="font-weight-medium">
          {{ contacto.nombre }} {{ contacto.apellidos || '' }}
        </td>

        <td>{{ contacto.telefono || '—' }}</td>
        <td>{{ contacto.email || '—' }}</td>
        <td>{{ contacto.empresa || '—' }}</td>

        <td>
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
        </td>

        <td class="text-end">
          <v-btn
            icon="mdi-pencil-outline"
            variant="text"
            size="small"
            color="primary"
            aria-label="Editar contacto"
            @click="emit('editar', contacto)"
          />

          <v-btn
            icon="mdi-delete-outline"
            variant="text"
            size="small"
            color="error"
            aria-label="Eliminar contacto"
            @click="emit('eliminar', contacto)"
          />
        </td>
      </tr>
    </tbody>
  </v-table>
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