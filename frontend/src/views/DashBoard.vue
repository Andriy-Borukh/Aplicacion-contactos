<template>
  <div>
    <div class="mb-8">
      <h1 class="text-h4 font-weight-bold mb-2">
        Dashboard
      </h1>

      <p class="text-body-1 text-medium-emphasis">
        Aquí tienes un resumen de tus contactos.
      </p>
    </div>

    <v-progress-linear
      v-if="cargando"
      indeterminate
      color="primary"
      rounded
      class="mb-6"
    />

    <EstadisticasDashboard
        :total-contactos="contactos.length"
        :total-favoritos="totalFavoritos"
        :total-etiquetas="totalEtiquetas"
        :total-cumpleanos="proximosCumpleanos.length"
    />

    <v-row class="mt-4">
      <v-col cols="12" md="8">
        <ContactosRecientes :contactos="contactosRecientes" />
      </v-col>

      <v-col cols="12" md="4"> 
        <AccesosRapidos 
        />
        <ProximosCumples 
            :cumpleanos="proximosCumpleanos"
            class="mt-4" 
        />
        </v-col>
    </v-row>

    <AlertaError v-if="error" :mensaje="error" />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useDatosContactos } from '@/composables/useDatosContactos.js'
import EstadisticasDashboard from '@/components/dashboard/EstadisticasDashboard.vue'
import ContactosRecientes from '@/components/dashboard/ContactosRecientes.vue'
import AccesosRapidos from '@/components/dashboard/AccesosRapidos.vue'
import ProximosCumples from '@/components/dashboard/ProximosCumples.vue'
import AlertaError from '@/components/AlertaError.vue'

const {
  contactos,
  cargando,
  error,
  cargarContactos,
} = useDatosContactos()

const totalFavoritos = computed(() =>
  contactos.value.filter((contacto) => contacto.favorito).length,
)

const totalEtiquetas = computed(() => {
  const etiquetas = new Set()

  contactos.value.forEach((contacto) => {
    contacto.etiquetas?.forEach((etiqueta) => {
      etiquetas.add(
        typeof etiqueta === 'string' ? etiqueta : etiqueta.nombre,
      )
    })
  })

  return etiquetas.size
})

const contactosRecientes = computed(() =>
  [...contactos.value]
    .sort((a, b) => Number(b.id) - Number(a.id))
    .slice(0, 5),
)

function diasHastaCumpleanos(fecha) {
  if (!fecha) return null

  const coincidencia = String(fecha).match(/^(\d{4})-(\d{2})-(\d{2})/)

  if (!coincidencia) return null

  const mes = Number(coincidencia[2])
  const dia = Number(coincidencia[3])

  const hoy = new Date()
  hoy.setHours(0, 0, 0, 0)

  let proximo = new Date(hoy.getFullYear(), mes - 1, dia)

  if (
    proximo.getMonth() !== mes - 1 ||
    proximo.getDate() !== dia
  ) {
    return null
  }

  if (proximo < hoy) {
    proximo = new Date(hoy.getFullYear() + 1, mes - 1, dia)
  }

  return Math.round(
    (proximo.getTime() - hoy.getTime()) / 86400000,
  )
}

const proximosCumpleanos = computed(() =>
  contactos.value
    .filter((contacto) => {
      const dias = diasHastaCumpleanos(contacto.fechaCumpleanos)
      return dias !== null && dias >= 0 && dias <= 30
    })
    .sort(
      (a, b) =>
        diasHastaCumpleanos(a.fechaCumpleanos) -
        diasHastaCumpleanos(b.fechaCumpleanos),
    ),
)

onMounted(() => {
  console.log(
      'Fechas de cumpleaños:',
      contactos.value.map(contacto => ({
        nombre: contacto.nombre,
        fechaCumpleanos: contacto.fechaCumpleanos,
      })),
  )
})

onMounted(cargarContactos)
</script>
