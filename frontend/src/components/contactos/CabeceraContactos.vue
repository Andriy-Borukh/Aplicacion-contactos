<template>
  <v-row align="center" class="mb-6">
    <v-col>
      <h1 class="text-h4 font-weight-bold">
        Mis contactos
      </h1>

      <p class="text-body-2 text-medium-emphasis mt-1">
        Bienvenido, {{ usuario?.nombre || 'usuario' }}
      </p>
    </v-col>

    <v-col cols="auto" class="d-flex ga-2 flex-wrap justify-end">
      <v-btn
        icon="mdi-refresh"
        variant="outlined"
        aria-label="Actualizar contactos"
        :loading="cargando"
        @click="emit('actualizar')"
      />

      <v-menu>
        <template #activator="{ props: menuProps }">
          <v-btn
            v-bind="menuProps"
            variant="outlined"
            prepend-icon="mdi-download"
            append-icon="mdi-chevron-down"
          >
            Exportar
          </v-btn>
        </template>

        <v-list>
          <v-list-item
            prepend-icon="mdi-file-delimited-outline"
            title="Exportar CSV"
            @click="exportarContactos('csv')"
          />
          <v-list-item
            prepend-icon="mdi-code-json"
            title="Exportar JSON"
            @click="exportarContactos('json')"
          />
        </v-list>
      </v-menu>

      <v-btn
        variant="outlined"
        prepend-icon="mdi-upload"
        :loading="importando"
        :disabled="importando"
        @click="abrirSelectorArchivo"
      >
        Importar
      </v-btn>

      <input
        ref="selectorArchivo"
        type="file"
        accept=".csv,.json,application/json,text/csv"
        hidden
        @change="importarContactos"
      />

      <v-btn
        prepend-icon="mdi-tag-multiple-outline"
        variant="outlined"
        to="/etiquetas"
      >
        Mis etiquetas
      </v-btn>

      <v-btn
        color="primary"
        prepend-icon="mdi-account-plus"
        @click="emit('crear')"
      >
        Nuevo contacto
      </v-btn>

      <v-btn
        color="error"
        variant="outlined"
        prepend-icon="mdi-logout"
        :loading="cerrandoSesion"
        :disabled="cerrandoSesion"
        @click="cerrarSesionUsuario"
      >
        Cerrar sesión
      </v-btn>
    </v-col>
  </v-row>

  <AlertaError v-if="errorLogout" :mensaje="errorLogout" />

  <v-row align="center" class="mb-6 cabecera-contactos">
    <v-alert
      v-if="mensajeImportacion"
      type="info"
      variant="tonal"
      class="resultado-importacion mt-4"
    >
      <div class="font-weight-bold mb-2">
        {{ mensajeImportacion }}
      </div>
    
      <div
        v-if="erroresImportacion.length"
        class="errores-importacion"
      >
        <div
          v-for="(detalle, indice) in erroresImportacion"
          :key="indice"
          class="error-importacion-item"
        >
          {{ detalle }}
        </div>
      </div>
    </v-alert>
  </v-row>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { cerrarSesion, peticion } from '@/services/api.js'
import AlertaError from '@/components/AlertaError.vue'

defineProps({
  usuario: {
    type: Object,
    default: null,
  },
  cargando: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['actualizar', 'crear'])
const router = useRouter()

const cerrandoSesion = ref(false)
const errorLogout = ref('')
const selectorArchivo = ref(null)
const importando = ref(false)
const mensajeImportacion = ref('')
const erroresImportacion = ref([])

function abrirSelectorArchivo() {
  if (!importando.value) {
    selectorArchivo.value?.click()
  }
}

function limpiarResultadoImportacion() {
  mensajeImportacion.value = ''
  erroresImportacion.value = []
}

let temporizadorImportacion

function ocultarResultadoImportacion() {
  clearTimeout(temporizadorImportacion)

  temporizadorImportacion = setTimeout(() => {
    limpiarResultadoImportacion()
  }, 3000)
}

function mostrarResultadoImportacion() {
  clearTimeout(temporizadorImportacion)
  ocultarResultadoImportacion()
}

async function importarContactos(evento) {
  const archivo = evento.target.files?.[0]

  // Permite volver a seleccionar el mismo archivo.
  evento.target.value = ''

  if (!archivo) return

  const nombre = archivo.name.toLowerCase()

  if (!nombre.endsWith('.csv') && !nombre.endsWith('.json')) {
    mensajeImportacion.value = 'Selecciona un archivo CSV o JSON.'
    erroresImportacion.value = []
    return
  }

  limpiarResultadoImportacion()
  importando.value = true

  try {
    const formulario = new FormData()
    formulario.append('archivo', archivo)

    const resultado = await peticion('/api/contactos/importar', {
      method: 'POST',
      body: formulario,
    })

    console.log('Resultado de la importación:', resultado)

    const importados = resultado?.importados ?? 0
    const errores = resultado?.errores ?? 0

    mensajeImportacion.value =
      `Importación finalizada: ${importados} contacto(s) importado(s) y ${errores} error(es).`

    erroresImportacion.value = resultado?.detalles ?? []

    mostrarResultadoImportacion()

    if (importados > 0) {
      emit('actualizar')
    }
  } catch (error) {
    mensajeImportacion.value =
      error.message || 'Ha ocurrido un error al importar los contactos.'
    erroresImportacion.value = []
    mostrarResultadoImportacion()
  } finally {
    importando.value = false
  }
}

function exportarContactos(formato) {
  const enlace = document.createElement('a')
  enlace.href = `/api/contactos/exportar/${formato}`
  enlace.download = `contactos.${formato}`
  document.body.appendChild(enlace)
  enlace.click()
  enlace.remove()
}


async function cerrarSesionUsuario() {
  errorLogout.value = ''
  cerrandoSesion.value = true

  try {
    await cerrarSesion()
    await router.replace('/login')
  } catch (e) {
    errorLogout.value =
      e.message || 'No se ha podido cerrar la sesión.'
  } finally {
    cerrandoSesion.value = false
  }
}
</script>

<style scoped>


.cabecera-contactos {
  position: relative;
  z-index: 1;
}

.resultado-importacion {
  position: relative;
  display: block;
  width: 100%;
  margin: 16px 0 24px;
  clear: both;
}

.errores-importacion {
  max-height: 300px;
  overflow-y: auto;
  padding-right: 8px;
}

.error-importacion-item {
  white-space: normal;
  overflow-wrap: anywhere;
  word-break: break-word;
  padding: 8px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.12);
}

</style>