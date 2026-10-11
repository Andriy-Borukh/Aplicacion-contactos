<template>
  <v-row class="mb-4" dense>
    <v-col cols="12" sm="4">
      <v-card variant="outlined" rounded="lg" class="pa-4 h-100">
        <div class="d-flex align-center justify-space-between">
          <div>
            <div class="text-body-2 text-medium-emphasis">
              Total de contactos
            </div>
            <div class="text-h4 font-weight-bold mt-2">
              {{ totalContactos }}
            </div>
          </div>

          <v-avatar color="primary" variant="tonal" size="48">
            <v-icon icon="mdi-account-multiple-outline" />
          </v-avatar>
        </div>
      </v-card>
    </v-col>

    <v-col cols="12" sm="4">
      <v-card variant="outlined" rounded="lg" class="pa-4 h-100">
        <div class="d-flex align-center justify-space-between">
          <div>
            <div class="text-body-2 text-medium-emphasis">
              Favoritos
            </div>
            <div class="text-h4 font-weight-bold mt-2">
              {{ totalFavoritos }}
            </div>
          </div>

          <v-avatar color="amber-darken-2" variant="tonal" size="48">
            <v-icon icon="mdi-star-outline" />
          </v-avatar>
        </div>
      </v-card>
    </v-col>

    <v-col cols="12" sm="4">
      <v-card variant="outlined" rounded="lg" class="pa-4 h-100">
        <div class="d-flex align-center justify-space-between">
          <div>
            <div class="text-body-2 text-medium-emphasis">
              Empresas
            </div>
            <div class="text-h4 font-weight-bold mt-2">
              {{ totalEmpresas }}
            </div>
          </div>

          <v-avatar color="teal" variant="tonal" size="48">
            <v-icon icon="mdi-domain" />
          </v-avatar>
        </div>
      </v-card>
    </v-col>
  </v-row>
</template>

    <script setup>
    import { computed } from 'vue'
    
    const props = defineProps({
      contactos: {
        type: Array,
        required: true,
      },
    })
    
    const totalContactos = computed(() => props.contactos.length)
    
    const totalFavoritos = computed(() =>
      props.contactos.filter((contacto) => contacto.favorito).length
    )
    
    const totalEmpresas = computed(() =>
      new Set(
        props.contactos
          .map((contacto) => contacto.empresa?.trim())
          .filter(Boolean)
          .map((empresa) => empresa.toLowerCase())
      ).size
    )
    </script>