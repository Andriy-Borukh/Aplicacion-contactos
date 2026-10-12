# 📇 Aplicación Contactos

Aplicación web para gestionar contactos personales y profesionales, desarrollada con **Spring Boot** en el backend y **Vue 3 + Vuetify** en el frontend.

El proyecto permite registrar usuarios, iniciar sesión y gestionar contactos de forma segura, con funcionalidades de búsqueda, organización mediante etiquetas, favoritos e importación y exportación de datos.

## ✨ Funcionalidades

### 👤 Usuarios y autenticación

* Registro de nuevos usuarios.
* Inicio y cierre de sesión.
* Autenticación mediante Spring Security.
* Contraseñas protegidas mediante BCrypt.
* Validación de datos en los formularios.
* Cada usuario gestiona sus propios contactos y etiquetas.

### 📇 Gestión de contactos

* Crear, consultar, editar y eliminar contactos.
* Guardar información personal y profesional:

  * Nombre y apellidos.
  * Teléfono y correo electrónico.
  * Empresa y cargo.
  * Dirección y notas.
  * Fecha de cumpleaños.
  * Favoritos.
  * Etiquetas personalizadas.
* Asociar varios contactos a diferentes etiquetas.
* Validar los datos antes de guardarlos.

### 🏷️ Gestión de etiquetas

* Crear y eliminar etiquetas.
* Asociar etiquetas a los contactos.
* Reutilizar etiquetas existentes.
* Crear automáticamente las etiquetas que no existan durante la importación.
* Filtrar y organizar los contactos mediante etiquetas.

### 📊 Panel de control

* Resumen de la información de los contactos.
* Accesos rápidos a las principales funcionalidades.
* Organización de la información para facilitar su consulta.

### 📁 Importación y exportación

* Exportar contactos en formato CSV.
* Exportar contactos en formato JSON.
* Importar contactos desde archivos CSV y JSON.
* Validar los registros importados.
* Mostrar un resumen de los contactos importados y los errores encontrados.

### 🎨 Interfaz

* Interfaz desarrollada con Vue 3 y Vuetify.
* Diseño adaptable a diferentes tamaños de pantalla.
* Componentes reutilizables.
* Formularios con validación y mensajes de error.
* Navegación mediante Vue Router.
* Comunicación con el backend mediante peticiones HTTP.

## 🛠️ Tecnologías utilizadas

| Tecnología         | Uso                                   |
| ------------------ | ------------------------------------- |
| Java 21            | Lenguaje del backend                  |
| Spring Boot        | Desarrollo de la aplicación backend   |
| Spring Web MVC     | Controladores y API REST              |
| Spring Data JPA    | Persistencia y acceso a datos         |
| Spring Security    | Autenticación y autorización          |
| Jakarta Validation | Validación de datos                   |
| Hibernate          | Mapeo objeto-relacional               |
| H2 Database        | Base de datos durante el desarrollo   |
| Gradle             | Gestión de dependencias y compilación |
| Vue 3              | Desarrollo del frontend               |
| Vuetify            | Componentes visuales                  |
| Vue Router         | Navegación entre vistas               |
| JavaScript         | Lógica del frontend                   |
| Git y GitHub       | Control de versiones                  |

## 📋 Requisitos previos

Antes de ejecutar el proyecto, necesitas:

* Java Development Kit (JDK) 21.
* Node.js y npm.
* Git, si deseas clonar el repositorio.
* Un IDE como IntelliJ IDEA o Visual Studio Code.

## 🚀 Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
cd "Aplicacion Contactos"
```

Sustituye `URL_DEL_REPOSITORIO` por la URL real de tu repositorio de GitHub.

### 2. Ejecutar el backend

Desde la carpeta raíz del backend, ejecuta:

**Windows:**

```powershell
.\gradlew.bat bootRun
```

**Linux o macOS:**

```bash
./gradlew bootRun
```

El backend utiliza el puerto `9090` en la configuración de desarrollo.

### 3. Ejecutar el frontend

Abre otra terminal y entra en la carpeta del frontend, donde se encuentra `package.json`.

Instala las dependencias:

```bash
npm install
```

Inicia el servidor de desarrollo:

```bash
npm run dev
```

Vite mostrará en la terminal la dirección local desde la que acceder a la aplicación, normalmente `http://localhost:5173`.

> **Nota:** la configuración del proxy de Vite debe dirigir las peticiones `/api` al backend de Spring Boot. Comprueba también que las rutas y los puertos coincidan con la configuración de tu proyecto.

## 🔌 API REST

Los principales endpoints del backend se organizan bajo `/api`.

| Método | Endpoint                       | Descripción                               |
| ------ | ------------------------------ | ----------------------------------------- |
| GET    | `/api/usuarios/me`             | Obtener los datos del usuario autenticado |
| GET    | `/api/contactos`               | Listar contactos del usuario              |
| GET    | `/api/contactos/{id}`          | Obtener un contacto                       |
| POST   | `/api/contactos`               | Crear un contacto                         |
| PUT    | `/api/contactos/{id}`          | Editar un contacto                        |
| DELETE | `/api/contactos/{id}`          | Eliminar un contacto                      |
| GET    | `/api/contactos/exportar/csv`  | Exportar contactos a CSV                  |
| GET    | `/api/contactos/exportar/json` | Exportar contactos a JSON                 |
| POST   | `/api/contactos/importar`      | Importar contactos desde un archivo       |
| GET    | `/api/etiquetas`               | Listar etiquetas                          |

Las operaciones protegidas requieren una sesión autenticada. Las peticiones que modifican datos también deben respetar la protección CSRF configurada en Spring Security.

## 🗂️ Estructura del proyecto

### Backend

El backend sigue una organización por responsabilidades:

```text
src/main/java/andriy/com/aplicacion_contactos/
├── controller/
├── dtos/
├── entities/
├── exceptions/
├── repositories/
└── service/
```

* **controller:** recibe las peticiones HTTP y devuelve las respuestas.
* **dtos:** define los objetos utilizados para recibir y devolver datos.
* **entities:** contiene las entidades persistentes.
* **exceptions:** gestiona las excepciones de la aplicación.
* **repositories:** proporciona el acceso a la base de datos.
* **service:** implementa la lógica de negocio.

### Frontend

```text
src/
├── components/
├── services/
├── views/
├── router/
├── App.vue
└── main.js
```

* **components:** componentes reutilizables de la interfaz.
* **services:** comunicación con la API REST.
* **views:** vistas principales de la aplicación.
* **router:** configuración de las rutas.
* **App.vue:** estructura principal de la interfaz.
* **main.js:** punto de entrada de Vue.

La estructura exacta puede variar según la organización final del código.

## 🔐 Seguridad

La aplicación incorpora varias medidas de seguridad:

* Autenticación y autorización mediante Spring Security.
* Almacenamiento de contraseñas mediante BCrypt.
* Validación de datos recibidos por el backend.
* Protección CSRF para las operaciones que modifican información.
* Restricción del acceso a los contactos y etiquetas de cada usuario.

## 🗄️ Base de datos

Durante el desarrollo se utiliza H2 Database.

La configuración de desarrollo puede utilizar una base de datos en memoria, por lo que los datos podrían perderse al reiniciar la aplicación. Si se utiliza una configuración persistente en archivo, deben revisarse las propiedades correspondientes.

No se deben incluir credenciales reales ni secretos en el repositorio.

## 👨‍💻 Autor

Proyecto personal desarrollado como parte del aprendizaje de desarrollo de aplicaciones web y multiplataforma.

**Tecnologías principales:** Java, Spring Boot, Spring Security, JPA, Vue 3 y Vuetify.
