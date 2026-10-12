const API_URL = '/api'

function obtenerTokenCSRF() {
const cookie = document.cookie
.split('; ')
.find((fila) => fila.startsWith('XSRF-TOKEN='))

return cookie ? decodeURIComponent(cookie.split('=').slice(1).join('=')) : null
}

export async function peticion(url, opciones = {}) {
const metodo = (opciones.method || 'GET').toUpperCase()
const cabeceras = {
...opciones.headers,
}

if (opciones.body && !cabeceras['Content-Type']) {
cabeceras['Content-Type'] = 'application/json'
}

if (!['GET', 'HEAD', 'OPTIONS'].includes(metodo)) {
  // Solicitar primero un token CSRF al backend.
  const respuestaCSRF = await fetch('/api/csrf', {
    credentials: 'same-origin',
  })

  if (!respuestaCSRF.ok) {
    throw new Error('No se ha podido obtener el token CSRF')
  }

  const token = obtenerTokenCSRF()

  if (token) {
    cabeceras['X-XSRF-TOKEN'] = token
  } else {
    throw new Error('No se ha recibido la cookie CSRF')
  }
}

const respuesta = await fetch(url, {
...opciones,
method: metodo,
headers: cabeceras,
credentials: 'same-origin',
})

if (respuesta.status === 204) {
return null
}

const contenido = await respuesta.text()
let datos = null

if (contenido) {
try {
datos = JSON.parse(contenido)
} catch {
datos = contenido
}
}

if (!respuesta.ok) {
  const excepcion = new Error(
    datos?.mensaje ||
    datos?.error ||
    datos?.message ||
    `Error HTTP ${respuesta.status}`
  )

  excepcion.erroresCampos = datos?.campos || {}

  throw excepcion
}

return datos
}

export async function obtenerUsuarioActual() {
return peticion(`${API_URL}/usuarios/me`)
}

export async function obtenerContactos() {
return peticion(`${API_URL}/contactos`)
}

export async function iniciarSesion(email, password) {
// Obtener primero el token CSRF y la cookie correspondiente.
const respuestaCSRF = await fetch('/api/csrf', {
credentials: 'same-origin',
})

if (!respuestaCSRF.ok) {
throw new Error('No se ha podido obtener el token CSRF')
}

const token = obtenerTokenCSRF()

if (!token) {
throw new Error('No se ha recibido la cookie CSRF')
}

// Spring Security espera los campos username y password.
const datos = new URLSearchParams()
datos.set('username', email)
datos.set('password', password)

const respuesta = await fetch('/backend-login', {
method: 'POST',
credentials: 'same-origin',
headers: {
'Content-Type': 'application/x-www-form-urlencoded',
'X-XSRF-TOKEN': token,
},
body: datos.toString(),
redirect: 'manual',
})

// Spring Security suele responder con una redirección al iniciar sesión.
if (respuesta.type === 'opaqueredirect' || respuesta.status === 0) {
return true
}

if (respuesta.redirected) {
return true
}

if (respuesta.status >= 200 && respuesta.status < 300) {
return true
}

if (respuesta.status === 401 || respuesta.status === 403) {
throw new Error('Email o contraseña incorrectos, o sesión rechazada')
}

throw new Error(`No se ha podido iniciar sesión (HTTP ${respuesta.status})`)
}

export async function crearContacto(contacto) {
  return peticion('/api/contactos', {
    method: 'POST',
    body: JSON.stringify(contacto),
  })
}


export async function editarContacto(id, contacto) {
  return peticion(`/api/contactos/${id}`, {
    method: 'PUT',
    body: JSON.stringify(contacto),
  })
}


export async function eliminarContacto(id) {
  return peticion(`/api/contactos/${id}`, {
    method: 'DELETE',
  })
}


export async function obtenerEtiquetas() {
  return peticion(`${API_URL}/etiquetas`)
}

export async function crearEtiqueta(etiqueta) {
  return peticion(`${API_URL}/etiquetas`, {
    method: 'POST',
    body: JSON.stringify(etiqueta),
  })
}

export async function eliminarEtiqueta(id) {
  return peticion(`${API_URL}/etiquetas/${id}`, {
    method: 'DELETE',
  })
}

export function obtenerContactosAsociadosEtiqueta(id) {
  return peticion(`/api/etiquetas/${id}/contactos`)
}


export async function cerrarSesion() {
  return peticion('/api/usuarios/logout', {
    method: 'POST',
  })
}


export async function registrarUsuario(usuario) {
  return peticion(`${API_URL}/usuarios`, {
    method: 'POST',
    body: JSON.stringify(usuario),
  })
}