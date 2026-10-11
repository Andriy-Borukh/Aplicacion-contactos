import { ref } from 'vue'
import {
  eliminarContacto,
  editarContacto,
} from '../services/api.js'

export function useAccionesContactos({
  cargarContactos,
  mostrarExito,
  mostrarError,
}) {
  const dialogoEliminar = ref(false)
  const contactoAEliminar = ref(null)
  const eliminando = ref(false)

  function confirmarEliminacion(contacto) {
    contactoAEliminar.value = contacto
    dialogoEliminar.value = true
  }

  async function eliminarContactoSeleccionado() {
    if (!contactoAEliminar.value) return

    eliminando.value = true

    try {
      const nombre = contactoAEliminar.value.nombre

      await eliminarContacto(contactoAEliminar.value.id)

      dialogoEliminar.value = false
      contactoAEliminar.value = null

      await cargarContactos()

      mostrarExito(`Contacto "${nombre}" eliminado correctamente`)
    } catch (e) {
      mostrarError(
        e.message || 'No se ha podido eliminar el contacto',
      )
      dialogoEliminar.value = false
    } finally {
      eliminando.value = false
    }
  }

  async function cambiarFavorito(contacto) {
    const favoritoAnterior = contacto.favorito

    try {
      await editarContacto(contacto.id, {
        nombre: contacto.nombre,
        apellidos: contacto.apellidos || null,
        telefono: contacto.telefono || null,
        email: contacto.email || null,
        empresa: contacto.empresa || null,
        cargo: contacto.cargo || null,
        direccion: contacto.direccion || null,
        fechaCumpleanos: contacto.fechaCumpleanos || null,
        notas: contacto.notas || null,
        favorito: !favoritoAnterior,
        etiquetas: contacto.etiquetas || [],
      })

      contacto.favorito = !favoritoAnterior

      mostrarExito(
        contacto.favorito
          ? 'Contacto añadido a favoritos'
          : 'Contacto eliminado de favoritos',
      )
    } catch (e) {
      mostrarError(
        e.message || 'No se ha podido actualizar el favorito',
      )
    }
  }

  return {
    dialogoEliminar,
    contactoAEliminar,
    eliminando,
    confirmarEliminacion,
    eliminarContactoSeleccionado,
    cambiarFavorito,
  }
}