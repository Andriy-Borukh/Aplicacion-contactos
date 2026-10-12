<template>
  <div>
    <div class="mb-6">
      <h2 class="text-h6 font-weight-bold mb-1">
        {{ titulo}}
      </h2>

      <p class="text-body-2 text-medium-emphasis">
        {{
          descripcion
        }}
      </p>
    </div>

    <v-form
      validate-on="submit"
      @submit.prevent="enviarFormulario"
    >
      <v-text-field
        v-if="esRegistro"
        v-model="nombre"
        label="Nombre"
        placeholder="Tu nombre"
        prepend-inner-icon="mdi-account-outline"
        autocomplete="name"
        variant="outlined"
        density="comfortable"
        rounded="lg"
        :error-messages="erroresCampos.nombre"
        required
        class="mb-2"
      />

      <v-text-field
        v-model="email"
        label="Correo electrónico"
        placeholder="nombre@ejemplo.com"
        type="email"
        prepend-inner-icon="mdi-email-outline"
        :autocomplete="esRegistro ? 'email' : 'username'"
        variant="outlined"
        density="comfortable"
        rounded="lg"
        :error-messages="erroresCampos.email"
        required
        class="mb-2"
        :rules="[
          v => !!v || 'El correo electrónico es obligatorio',
        ]"
      />

      <v-text-field
        v-model="password"
        label="Contraseña"
        :type="mostrarPassword ? 'text' : 'password'"
        prepend-inner-icon="mdi-lock-outline"
        :append-inner-icon="
          mostrarPassword ? 'mdi-eye-off-outline' : 'mdi-eye-outline'
        "
        :autocomplete="esRegistro ? 'new-password' : 'current-password'"
        variant="outlined"
        density="comfortable"
        rounded="lg"
        :error-messages="erroresCampos.password"
        required
        @click:append-inner="mostrarPassword = !mostrarPassword"
        :rules="[
          v => !!v || 'La contraseña es obligatoria',
        ]"
      />

      <p
        v-if="esRegistro"
        class="text-caption text-medium-emphasis mb-4"
      >
        Usa al menos 6 caracteres, con mayúsculas, minúsculas y números.
      </p>

      <AlertaError v-if="error" :mensaje="error" />

      <v-btn
        type="submit"
        color="primary"
        block
        size="large"
        rounded="lg"
        :loading="loading"
        :disabled="loading"
        class="mt-3 text-none font-weight-bold"
      >
        {{ textoBoton }}
        <v-icon icon="mdi-arrow-right" end />
      </v-btn>
    </v-form>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import AlertaError from '@/components/AlertaError.vue'

const props = defineProps({
  modo: {
    type: String,
    required: true,
    validator: (valor) => ['login', 'registro'].includes(valor),
  },
  loading: {
    type: Boolean,
    default: false,
  },
  error: {
    type: String,
    default: '',
  },
  erroresCampos: {
    type: Object,
    default: () => ({}),
  },
})

const emit = defineEmits(['submit'])

const esRegistro = computed(() => props.modo === 'registro')

const titulo = computed(() =>
  esRegistro.value ? 'Crear una cuenta' : 'Bienvenido de nuevo',
)

const descripcion = computed(() =>
  esRegistro.value
    ? 'Regístrate para organizar tus contactos.'
    : 'Introduce tus credenciales para continuar.',
)

const textoBoton = computed(() =>
  esRegistro.value ? 'Crear cuenta' : 'Iniciar sesión',
)

const nombre = ref('')
const email = ref('')
const password = ref('')
const mostrarPassword = ref(false)

watch(
  () => props.modo,
  () => {
    nombre.value = ''
    email.value = ''
    password.value = ''
    mostrarPassword.value = false
  },
)

function enviarFormulario() {
  emit('submit', {
    nombre: nombre.value.trim(),
    email: email.value.trim(),
    password: password.value,
  })
}
</script>