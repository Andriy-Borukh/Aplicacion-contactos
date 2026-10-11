import { ref } from 'vue'
import {
  obtenerUsuarioActual,
  obtenerContactos,
} from '../services/api.js'

export function useDatosContactos() {
  const usuario = ref(null)
  const contactos = ref([])
  const cargando = ref(true)
  const error = ref('')

  async function cargarContactos() {
    cargando.value = true
    error.value = ''

    try {
      const [datosUsuario, datosContactos] = await Promise.all([
        obtenerUsuarioActual(),
        obtenerContactos(),
      ])

      usuario.value = datosUsuario
      contactos.value = Array.isArray(datosContactos)
        ? datosContactos
        : []
    } catch (e) {
      error.value =
        e.message || 'No se han podido cargar los contactos'
    } finally {
      cargando.value = false
    }
  }

  return {
    usuario,
    contactos,
    cargando,
    error,
    cargarContactos,
  }
}
