
<template>
  <v-card rounded="lg" class="mb-6">
    <v-card-title class="pa-5">
      Crear etiqueta
    </v-card-title>

    <v-card-text>
      <v-form @submit.prevent="emit('crear')">
        <v-row align="start">
          <v-col cols="12" sm="8">
            <v-text-field
              :model-value="nombre"
              @update:model-value="emit('update:nombre', $event)"
              label="Nombre de la etiqueta"
              placeholder="Ej. Trabajo, Amigos, Familia..."
              variant="outlined"
              prepend-inner-icon="mdi-tag-outline"
              maxlength="50"
              counter="50"
              :disabled="guardando"
              :rules="[
                v => !!v?.trim() || 'El nombre es obligatorio'
              ]"
            />
          </v-col>

          <v-col cols="12" sm="4">
            <v-btn
              type="submit"
              color="primary"
              prepend-icon="mdi-plus"
              block
              size="large"
              :loading="guardando"
              :disabled="!nombre.trim() || guardando"
            >
              Crear etiqueta
            </v-btn>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script setup>
defineProps({
  nombre: {
    type: String,
    required: true,
  },
  guardando: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:nombre', 'crear'])
</script>