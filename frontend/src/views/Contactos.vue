 <template>
  <v-container class="contactos-page py-4">

    <CabeceraContactos
  :usuario="usuario"
  :cargando="cargando"
  @actualizar="cargarContactos"
  @crear="abrirFormulario()"
/>

<AlertaError
  :mensaje="error"
  @cerrar="error = ''"
/>

<EstadisticasContactos :contactos="contactos" />

<v-card
  class="contactos-card"
  rounded="lg"
  elevation="2"
>
  <v-progress-linear
    v-if="cargando"
    indeterminate
    color="primary"
  />
  <v-card-text class="pb-0">

  <FiltrosContactos
    v-model:busqueda="busqueda"
    v-model:filtro-activo="filtroActivo"
    v-model:etiqueta-seleccionada="etiquetaSeleccionada"
    v-model:orden-seleccionado="ordenSeleccionado"
    v-model:orden-ascendente="ordenAscendente"
    v-model:vista-actual="vistaActual"
    :etiquetas-disponibles="etiquetasDisponibles"
  />
</v-card-text>

  <TablaContactos
  v-if="
    !cargando &&
    contactosFiltrados.length > 0 &&
    vistaActual === 'tabla'
  "
  class="contactos-tabla-scroll"
  :contactos="contactosFiltrados"
  @cambiar-favorito="cambiarFavorito"
  @editar="abrirFormulario"
  @eliminar="confirmarEliminacion"
/>

  <div
    v-if="vistaActual === 'tarjetas' && contactosFiltrados.length > 0"
    class="contactos-tarjetas-scroll"
  >
    <TarjetasContactos
      :contactos="contactosFiltrados"
      @cambiar-favorito="cambiarFavorito"
      @editar="abrirFormulario"
      @eliminar="confirmarEliminacion"
    />
  </div>

  <v-card-text
  v-else-if="!cargando && contactos.length === 0"
  class="text-center py-10"
>
  <v-icon
    icon="mdi-account-multiple-outline"
    size="56"
    color="grey"
    class="mb-3"
  />

  <h2 class="text-h6">Todavía no hay contactos</h2>

  <p class="text-body-2 text-medium-emphasis mt-2">
    Añade tu primer contacto para empezar.
  </p>

  <v-btn
    color="primary"
    class="mt-4"
    @click="abrirFormulario()"
  >
    Crear contacto
  </v-btn>
</v-card-text>
</v-card>

<FormularioContactos
  v-model="dialogo"
  :contacto="nuevoContacto"
  :editando="contactoEditandoId !== null"
  :guardando="guardando"
  :errores-campos="erroresCampos"
  :error-formulario="errorFormulario"
  @guardar="guardarContactoConMensaje"
  @cancelar="dialogo = false"
/>

  </v-container>
  <AlertaExito
  v-model="snackbar"
  :mensaje="mensajeExito"
/>
<ConfirmarEliminacion
  v-model="dialogoEliminar"
  :contacto="contactoAEliminar"
  :eliminando="eliminando"
  @cancelar="dialogoEliminar = false"
  @confirmar="eliminarContactoSeleccionado"
/>
</template>

<script setup>
import { onMounted, ref} from 'vue'
import EstadisticasContactos from '@/components/contactos/EstadisticasContactos.vue'
import FiltrosContactos from '@/components/contactos/FiltrosContactos.vue'
import TablaContactos from '@/components/contactos/TablaContactos.vue'
import TarjetasContactos from '@/components/contactos/TarjetasContactos.vue'
import FormularioContactos from '@/components/contactos/FormularioContactos.vue'
import ConfirmarEliminacion from '@/components/contactos/ConfirmarEliminacion.vue'
import CabeceraContactos from '@/components/contactos/CabeceraContactos.vue'
import AlertaError from '@/components/AlertaError.vue'
import AlertaExito from '@/components/AlertaExito.vue'
import { useContactos } from '@/composables/useContacto.js'
import { useDatosContactos } from '@/composables/useDatosContactos.js'
import { useFormularioContactos } from '@/composables/useFormularioContactos.js'
import { useAccionesContactos } from '@/composables/useAccionesContactos.js'

const {
  usuario,
  contactos,
  cargando,
  error,
  cargarContactos,
} = useDatosContactos()

const {
  dialogoEliminar,
  contactoAEliminar,
  eliminando,
  confirmarEliminacion,
  eliminarContactoSeleccionado,
  cambiarFavorito,
} = useAccionesContactos({
  cargarContactos,
  mostrarExito: (mensaje) => {
    mensajeExito.value = mensaje
    snackbar.value = true
  },
  mostrarError: (mensaje) => {
    error.value = mensaje
  },
})

const mensajeExito = ref('')
const snackbar = ref(false)
const busqueda = ref('')
const filtroActivo = ref('todos')
const etiquetaSeleccionada = ref(null)
const ordenSeleccionado = ref('nombre')
const ordenAscendente = ref(true)
const vistaActual = ref('tabla') 

const {
  contactosFiltrados,
  etiquetasDisponibles,
} = useContactos({
  contactos,
  busqueda,
  filtroActivo,
  etiquetaSeleccionada,
  ordenSeleccionado,
  ordenAscendente,
})

function contactoVacio() {
  return {
    nombre: '',
    apellidos: '',
    telefono: '',
    email: '',
    empresa: '',
    cargo: '',
    direccion: '',
    fechaCumpleanos: '',
    notas: '',
    favorito: false,
    etiquetas: [],
  }
}

const {
  dialogo,
  guardando,
  errorFormulario,
  erroresCampos,
  contactoEditandoId,
  nuevoContacto,
  abrirFormulario,
  guardarContacto,
} = useFormularioContactos({
  contactoVacio,
  cargarContactos,
})

onMounted(cargarContactos)

async function guardarContactoConMensaje(datosFormulario) {
  const resultado = await guardarContacto(datosFormulario)

  if (resultado.correcto) {
    mensajeExito.value = resultado.mensaje
    snackbar.value = true
  }
}
</script>

<style scoped>
.contactos-page {
  height: 100%;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 0;
}

.contactos-card {
  display: flex;
  flex-direction: column;
  min-height: 400px;
  overflow: hidden;
}

.contactos-card > .v-card-text {
  flex-shrink: 0;
}

.contactos-tabla-scroll {
  display: block;
  min-height: 250px;
  overflow-y: auto;
}

.contactos-tarjetas-scroll {
  overflow-y: auto;
  overflow-x: hidden;
  min-height: 0;
}
</style>
