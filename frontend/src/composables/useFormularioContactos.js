import { ref } from 'vue'
import {
  crearContacto,
  editarContacto,
} from '@/services/api.js'

export function useFormularioContactos({
  contactoVacio,
  cargarContactos,
}) {
  const dialogo = ref(false)
  const guardando = ref(false)
  const errorFormulario = ref('')
  const erroresCampos = ref({})
  const contactoEditandoId = ref(null)
  const nuevoContacto = ref(contactoVacio())

  function abrirFormulario(contacto = null) {
    errorFormulario.value = ''
    erroresCampos.value = {}

    if (contacto) {
      contactoEditandoId.value = contacto.id
      nuevoContacto.value = {
        nombre: contacto.nombre || '',
        apellidos: contacto.apellidos || '',
        telefono: contacto.telefono || '',
        email: contacto.email || '',
        empresa: contacto.empresa || '',
        cargo: contacto.cargo || '',
        direccion: contacto.direccion || '',
        fechaCumpleanos: contacto.fechaCumpleanos || '',
        notas: contacto.notas || '',
        favorito: contacto.favorito ?? false,
        etiquetas: contacto.etiquetas || [],
      }
    } else {
      contactoEditandoId.value = null
      nuevoContacto.value = contactoVacio()
    }

    dialogo.value = true
  }

  async function guardarContacto(datosFormulario) {
    errorFormulario.value = ''
    erroresCampos.value = {}
    guardando.value = true

    try {
      const contacto = {
        ...datosFormulario,
        nombre: datosFormulario.nombre.trim(),
        apellidos: datosFormulario.apellidos.trim() || null,
        telefono: datosFormulario.telefono.trim() || null,
        email: datosFormulario.email.trim() || null,
        empresa: datosFormulario.empresa.trim() || null,
        cargo: datosFormulario.cargo.trim() || null,
        direccion: datosFormulario.direccion.trim() || null,
        fechaCumpleanos: datosFormulario.fechaCumpleanos || null,
        notas: datosFormulario.notas.trim() || null,
      }

      const editando = contactoEditandoId.value !== null

      if (editando) {
        await editarContacto(contactoEditandoId.value, contacto)
      } else {
        await crearContacto(contacto)
      }

      dialogo.value = false
      nuevoContacto.value = contactoVacio()
      contactoEditandoId.value = null

      await cargarContactos()

      return {
        correcto: true,
        mensaje: editando
          ? 'Contacto actualizado correctamente'
          : 'Contacto guardado correctamente',
      }
    } catch (e) {
      erroresCampos.value = e.erroresCampos || {}

      if (Object.keys(erroresCampos.value).length === 0) {
        errorFormulario.value =
          e.message || 'No se ha podido guardar el contacto'
      }

      return { correcto: false }
    } finally {
      guardando.value = false
    }
  }

  return {
    dialogo,
    guardando,
    errorFormulario,
    erroresCampos,
    contactoEditandoId,
    nuevoContacto,
    abrirFormulario,
    guardarContacto,
  }
}
