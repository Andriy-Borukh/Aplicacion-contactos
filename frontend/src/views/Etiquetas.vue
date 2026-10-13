<template>
  <v-container class="etiquetas-page py-4">
    <CabeceraEtiquetas />

    <AlertaError v-if="error" :mensaje="error" />

    <AlertaExito v-model="mostrarAlertaExito" :mensaje="mensajeExito" />

    <FormularioEtiquetas ref="formularioEtiquetas"
     v-model:nombre="nuevoNombre"
     :guardando="guardando"
     @crear="crear"
     />

     <ListaEtiquetas
        :etiquetas="etiquetas"
        :cargando="cargando"
        :eliminando-id="eliminandoId"
        @eliminar="confirmarEliminacion"
    />
    
    <ConfirmarEliminacionEtiquetas
        v-model="dialogoEliminar"
        :etiqueta="etiquetaAEliminar"
        :contactos="contactosAsociados"
        :cargando-contactos="cargandoContactos"
        :eliminando="eliminandoId !== null"
        @cancelar="dialogoEliminar = false"
        @confirmar="eliminar"
    />
  </v-container>
</template>

<script setup>
import { onMounted, ref, nextTick } from 'vue'
import {
  obtenerEtiquetas,
  crearEtiqueta as crearEtiquetaAPI,
  eliminarEtiqueta as eliminarEtiquetaAPI,
  obtenerContactosAsociadosEtiqueta,
} from '@/services/api.js'
import CabeceraEtiquetas from '@/components/etiquetas/CabeceraEtiquetas.vue'
import AlertaError from '@/components/AlertaError.vue'
import AlertaExito from '@/components/AlertaExito.vue'
import FormularioEtiquetas from '@/components/etiquetas/FormularioEtiquetas.vue'
import ListaEtiquetas from '@/components/etiquetas/ListaEtiquetas.vue'
import ConfirmarEliminacionEtiquetas from '@/components/etiquetas/ConfirmarEliminacionEtiquetas.vue'

const etiquetas = ref([])
const nuevoNombre = ref('')
const cargando = ref(true)
const guardando = ref(false)
const eliminandoId = ref(null)
const error = ref('')
const mensajeExito = ref('')
const mostrarAlertaExito = ref(false)

const dialogoEliminar = ref(false)
const etiquetaAEliminar = ref(null)
const contactosAsociados = ref([])
const cargandoContactos = ref(false)

const formularioEtiquetas = ref(null)

async function cargarEtiquetas() {
  cargando.value = true
  error.value = ''

  try {
    etiquetas.value = await obtenerEtiquetas()
  } catch (e) {
    error.value = e.message || 'No se han podido cargar las etiquetas'
  } finally {
    cargando.value = false
  }
}

async function crear() {
  const nombre = nuevoNombre.value.trim()

  if (!nombre || guardando.value) return

  guardando.value = true
  error.value = ''
  mensajeExito.value = ''

  try {
    const etiqueta = await crearEtiquetaAPI({ nombre })
    etiquetas.value.push(etiqueta)
    etiquetas.value.sort((a, b) =>
      a.nombre.localeCompare(b.nombre, 'es', { sensitivity: 'base' })
    )
    nuevoNombre.value = ''
    await nextTick()
    await formularioEtiquetas.value?.limpiarValidacion()
    mensajeExito.value = 'Etiqueta creada correctamente'
    mostrarAlertaExito.value = true
  } catch (e) {
    error.value = e.message || 'No se ha podido crear la etiqueta'
  } finally {
    guardando.value = false
  }
}

async function confirmarEliminacion(etiqueta) {
  etiquetaAEliminar.value = etiqueta
  contactosAsociados.value = []
  error.value = ''
  dialogoEliminar.value = true
  cargandoContactos.value = true

  try {
    contactosAsociados.value =
      await obtenerContactosAsociadosEtiqueta(etiqueta.id)
  } catch (e) {
    error.value =
      e.message || 'No se han podido cargar los contactos asociados'
  } finally {
    cargandoContactos.value = false
  }
}

async function eliminar() {
  const etiqueta = etiquetaAEliminar.value

  if (!etiqueta || eliminandoId.value !== null) return

  eliminandoId.value = etiqueta.id
  error.value = ''
  mensajeExito.value = ''

  try {
    await eliminarEtiquetaAPI(etiqueta.id)
    etiquetas.value = etiquetas.value.filter(
      e => e.id !== etiqueta.id
    )
    dialogoEliminar.value = false
    etiquetaAEliminar.value = null
    mensajeExito.value = 'Etiqueta eliminada correctamente'
    mostrarAlertaExito.value = true
  } catch (e) {
    error.value = e.message || 'No se ha podido eliminar la etiqueta'
  } finally {
    eliminandoId.value = null
  }
}

onMounted(cargarEtiquetas)
</script>

<style scoped>

.etiquetas-page {
  min-height: 100%;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

</style>