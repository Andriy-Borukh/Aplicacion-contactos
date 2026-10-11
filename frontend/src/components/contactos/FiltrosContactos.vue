<template>
  <v-card-text>
    <v-row align="center" dense>
      <v-col cols="12" md="4">
        <v-text-field
          :model-value="busqueda"
          @update:model-value="emit('update:busqueda', $event)"
          label="Buscar contactos"
          placeholder="Nombre, teléfono, correo o empresa..."
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="comfortable"
          clearable
          hide-details
        />
      </v-col>

      <v-col cols="12" sm="6" md="2">
        <v-select
          :model-value="filtroActivo === 'etiqueta' ? 'todos' : filtroActivo"
          @update:model-value="actualizarFiltro"
          :items="[
            { title: 'Todos', value: 'todos' },
            { title: 'Favoritos', value: 'favoritos' },
          ]"
          label="Mostrar"
          variant="outlined"
          density="comfortable"
          hide-details
        />
      </v-col>

      <v-col
        v-if="etiquetasDisponibles.length > 0"
        cols="12"
        sm="6"
        md="2"
      >
        <v-select
          :model-value="etiquetaSeleccionada"
          @update:model-value="actualizarEtiqueta"
          :items="etiquetasDisponibles"
          label="Filtrar por etiqueta"
          variant="outlined"
          density="comfortable"
          hide-details
          clearable
        />
      </v-col>

      <v-col cols="12" sm="6" md="2">
        <v-select
          :model-value="ordenSeleccionado"
          @update:model-value="emit('update:ordenSeleccionado', $event)"
          :items="opcionesOrden"
          label="Ordenar por"
          variant="outlined"
          density="comfortable"
          hide-details
        />
      </v-col>

      <v-col cols="12" sm="6" md="2">
        <v-btn
          block
          variant="outlined"
          height="48"
          :prepend-icon="
            ordenAscendente
              ? 'mdi-sort-ascending'
              : 'mdi-sort-descending'
          "
          @click="emit('update:ordenAscendente', !ordenAscendente)"
        >
          {{ ordenAscendente ? 'Ascendente' : 'Descendente' }}
        </v-btn>
      </v-col>

      <v-col cols="12">
        <v-btn-toggle
          :model-value="vistaActual"
          @update:model-value="emit('update:vistaActual', $event)"
          mandatory
          divided
          color="primary"
          variant="outlined"
          class="w-100"
        >
          <v-btn value="tabla" prepend-icon="mdi-table">
            Tabla
          </v-btn>
          <v-btn value="tarjetas" prepend-icon="mdi-view-grid">
            Tarjetas
          </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>
  </v-card-text>
</template>

<script setup>
defineProps({
  busqueda: {
    type: String,
    required: true,
  },
  filtroActivo: {
    type: String,
    required: true,
  },
  etiquetaSeleccionada: {
    type: String,
    default: null,
  },
  etiquetasDisponibles: {
    type: Array,
    required: true,
  },
  ordenSeleccionado: {
    type: String,
    required: true,
  },
  ordenAscendente: {
    type: Boolean,
    required: true,
  },
  vistaActual: {
    type: String,
    required: true,
  },
})

const emit = defineEmits([
  'update:busqueda',
  'update:filtroActivo',
  'update:etiquetaSeleccionada',
  'update:ordenSeleccionado',
  'update:ordenAscendente',
  'update:vistaActual',
])

const opcionesOrden = [
  { title: 'Nombre', value: 'nombre' },
  { title: 'Apellidos', value: 'apellidos' },
  { title: 'Empresa', value: 'empresa' },
]

function actualizarEtiqueta(etiqueta) {
  emit('update:etiquetaSeleccionada', etiqueta ?? null)
  emit('update:filtroActivo', etiqueta ? 'etiqueta' : 'todos')
}

function actualizarFiltro(filtro) {
  emit('update:filtroActivo', filtro)
  emit('update:etiquetaSeleccionada', null)
}
</script>