
import { createApp } from 'vue'
import { createVuetify } from 'vuetify'

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'
import './style.css'

import App from './App.vue'
import router from './router'

const temaClaro = {
  dark: false,
  colors: {
    background: '#F5F7FB',
    surface: '#FFFFFF',
    primary: '#3267E9',
    secondary: '#17243B',
    error: '#D64545',
    success: '#16865C',
    warning: '#D99A20',
    info: '#3978D4',
  },
}

const temaOscuro = {
  dark: true,
  colors: {
    background: '#101827',
    surface: '#192438',
    primary: '#7197FF',
    secondary: '#C5D2EA',
    error: '#FF7777',
    success: '#54D6A0',
    warning: '#F4C66B',
    info: '#82B5FF',
  },
}

const vuetify = createVuetify({
  theme: {
    defaultTheme: 'temaClaro',
    themes: {
      temaClaro,
      temaOscuro,
    },
  },
  icons: {
    defaultSet: 'mdi',
  },
})

createApp(App)
  .use(vuetify)
  .use(router)
  .mount('#app')