import { computed } from 'vue'

export function useContactos({
  contactos,
  busqueda,
  filtroActivo,
  etiquetaSeleccionada,
  ordenSeleccionado,
  ordenAscendente,
}) {
  const contactosFiltrados = computed(() => {
    let resultado = [...contactos.value]

    // Filtrar por favoritos.
    if (filtroActivo.value === 'favoritos') {
      resultado = resultado.filter(
        (contacto) => contacto.favorito,
      )
    }

    // Filtrar por etiqueta.
    if (
      filtroActivo.value === 'etiqueta' &&
      etiquetaSeleccionada.value
    ) {
      resultado = resultado.filter((contacto) =>
        (contacto.etiquetas || []).some(
          (etiqueta) => etiqueta === etiquetaSeleccionada.value,
        ),
      )
    }

    // Aplicar la búsqueda.
    const texto = busqueda.value.trim().toLowerCase()

    if (texto) {
      resultado = resultado.filter((contacto) => {
        const campos = [
          contacto.nombre,
          contacto.apellidos,
          contacto.telefono,
          contacto.email,
          contacto.empresa,
        ]

        return campos.some((campo) =>
          String(campo || '').toLowerCase().includes(texto),
        )
      })
    }

    // Ordenar los resultados.
    resultado.sort((a, b) => {
      const valorA = String(
        a[ordenSeleccionado.value] || '',
      )
      const valorB = String(
        b[ordenSeleccionado.value] || '',
      )

      const comparacion = valorA.localeCompare(valorB, 'es', {
        sensitivity: 'base',
      })

      return ordenAscendente.value
        ? comparacion
        : -comparacion
    })

    return resultado
  })

  const etiquetasDisponibles = computed(() => {
    const etiquetas = contactos.value.flatMap(
      (contacto) => contacto.etiquetas || [],
    )

    return [...new Set(etiquetas)].sort((a, b) =>
      a.localeCompare(b, 'es'),
    )
  })

  return {
    contactosFiltrados,
    etiquetasDisponibles,
  }
}
