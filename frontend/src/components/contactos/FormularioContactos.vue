<template>
  <v-dialog
    :model-value="modelValue"
    max-width="700"
    persistent
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card rounded="xl">
      <v-card-title class="text-h5 pa-5">
        {{ editando ? 'Editar contacto' : 'Nuevo contacto' }}
      </v-card-title>

      <v-divider />

      <v-card-text class="pa-5">
        <v-form ref="formulario" @submit.prevent="guardar">
          <v-row>
            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.nombre"
                label="Nombre"
                :rules="[reglas.requerido]"
                prepend-inner-icon="mdi-account-outline"
                required
                :error-messages="erroresCampos.nombre || []"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.apellidos"
                label="Apellidos"
                :rules="[reglas.apellidosRequeridos]"
                :error-messages="erroresCampos.apellidos || []"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.telefono"
                label="Teléfono"
                :rules="[reglas.telefonoRequerido]"
                placeholder="+34600111222"
                prepend-inner-icon="mdi-phone-outline"
                hint="Incluye el prefijo internacional, por ejemplo +34"
                persistent-hint
                :error-messages="erroresCampos.telefono || []"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.email"
                label="Correo electrónico"
                type="email"
                :rules="[reglas.emailRequerido]"
                prepend-inner-icon="mdi-email-outline"
                :error-messages="erroresCampos.email || []"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.empresa"
                label="Empresa"
                prepend-inner-icon="mdi-domain"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.cargo"
                label="Cargo"
                prepend-inner-icon="mdi-briefcase-outline"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.direccion"
                label="Dirección"
                prepend-inner-icon="mdi-map-marker-outline"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formularioContacto.fechaCumpleanos"
                label="Fecha de cumpleaños"
                type="date"
              />
            </v-col>

            <v-col cols="12">
              <v-textarea
                v-model="formularioContacto.notas"
                label="Notas"
                rows="3"
                auto-grow
              />
            </v-col>

            <v-col cols="12">
              <v-checkbox
                v-model="formularioContacto.favorito"
                label="Marcar como favorito"
                color="amber-darken-2"
                hide-details
              />
            </v-col>
          </v-row>

          <p class="text-caption text-medium-emphasis mb-3">
            * Campo obligatorio
          </p>

          <v-alert
            v-if="errorFormulario"
            type="error"
            variant="tonal"
            class="mb-4"
          >
            {{ errorFormulario }}
          </v-alert>

          <v-card-actions class="px-0 pb-0">
            <v-spacer />

            <v-btn
              variant="text"
              :disabled="guardando"
              @click="emit('cancelar')"
            >
              Cancelar
            </v-btn>

            <v-btn
              type="submit"
              color="primary"
              :loading="guardando"
            >
              {{ editando ? 'Guardar cambios' : 'Guardar contacto' }}
            </v-btn>
          </v-card-actions>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  contacto: {
    type: Object,
    required: true,
  },
  editando: {
    type: Boolean,
    default: false,
  },
  guardando: {
    type: Boolean,
    default: false,
  },
  erroresCampos: {
    type: Object,
    default: () => ({}),
  },
  errorFormulario: {
    type: String,
    default: '',
  },
})

const emit = defineEmits([
  'update:modelValue',
  'guardar',
  'cancelar',
])

const formulario = ref(null)

const formularioContacto = ref({ ...props.contacto })

watch(
  () => props.contacto,
  (contacto) => {
    formularioContacto.value = { ...contacto }
  },
  { deep: true },
)

const reglas = {
  requerido: (valor) =>
    !!valor?.trim() || 'El nombre es obligatorio',
  apellidosRequeridos: (valor) =>
    !!valor?.trim() || 'El apellido es obligatorio',
  telefonoRequerido: (valor) =>
    !!valor?.trim() || 'El telefono es obligatorio',
  emailRequerido: (valor) =>
    !!valor?.trim() || 'El email es obligatorio',
}

async function guardar() {
  const resultado = await formulario.value?.validate()

  if (!resultado?.valid) {
    return
  }

  emit('guardar', { ...formularioContacto.value })
}
</script>