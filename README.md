# Aplicación de Contactos

Aplicación web sencilla para gestionar contactos personales.

El proyecto permite a los usuarios registrarse, iniciar sesión y gestionar sus propios contactos mediante las operaciones de crear, consultar, editar y eliminar.

## Funcionalidades

* Registro de usuarios.
* Validación de los datos del registro.
* Inicio y cierre de sesión.
* Contraseñas almacenadas de forma segura mediante BCrypt.
* Gestión de contactos:

  * Crear contactos.
  * Consultar contactos.
  * Editar contactos.
  * Eliminar contactos.
* Cada usuario solo puede acceder a sus propios contactos.
* Validación de los datos de los contactos.
* Interfaz web sencilla utilizando Bootstrap.

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring MVC
* Spring Data JPA
* Spring Security
* Thymeleaf
* Hibernate
* H2 Database
* Bean Validation
* Lombok
* Bootstrap 5

## Estructura del proyecto

El proyecto sigue una estructura basada en capas:

```text
src/
└── main/
    ├── java/
    │   └── andriy/com/aplicacion_contactos/
    │       ├── controller/
    │       ├── dtos/
    │       ├── entities/
    │       ├── repositories/
    │       ├── security/
    │       └── service/
    │
    └── resources/
        ├── templates/
        └── application.properties
```

### Capas principales

**Controller**

Gestiona las peticiones HTTP y las vistas de la aplicación.

**Service**

Contiene la lógica de negocio.

**Repository**

Se encarga del acceso a la base de datos mediante Spring Data JPA.

**Entity**

Representa las entidades almacenadas en la base de datos.

**DTO**

Se utilizan para transportar los datos entre las diferentes capas de la aplicación.

**Security**

Contiene la configuración de Spring Security y la autenticación de los usuarios.

## Base de datos

El proyecto utiliza H2 como base de datos.

La configuración actual utiliza una base de datos en memoria:

```text
jdbc:h2:mem:aplicacion_contactos
```

Esto significa que los datos se eliminan al reiniciar la aplicación.

La consola de H2 está disponible en:

```text
http://localhost:9090/h2-console
```

## Ejecución

### Requisitos

* Java 21
* Gradle

### Pasos

1. Clonar o descargar el proyecto.
2. Abrirlo con IntelliJ IDEA, Eclipse u otro IDE compatible.
3. Ejecutar la aplicación Spring Boot.
4. Abrir el navegador y acceder a:

```text
http://localhost:9090/login
```

También se puede acceder al registro desde:

```text
http://localhost:9090/registro
```

## Autenticación

La aplicación utiliza Spring Security para gestionar la autenticación.

Los usuarios inician sesión utilizando su email y contraseña.

Las contraseñas no se almacenan directamente en la base de datos, sino que se almacenan utilizando BCrypt.

Además, las operaciones relacionadas con los contactos requieren que el usuario esté autenticado.

## Gestión de contactos

Cada contacto está asociado a un usuario.

Por este motivo, las operaciones de consulta, edición y eliminación comprueban que el contacto pertenece al usuario que ha iniciado sesión.

De esta forma, un usuario no puede acceder ni modificar los contactos de otro usuario.

## Objetivo del proyecto

El objetivo del proyecto es poner en práctica diferentes conceptos de desarrollo de aplicaciones web con Java y Spring Boot, especialmente:

* Arquitectura por capas.
* Spring MVC.
* Persistencia con JPA.
* Validación de datos.
* Autenticación y autorización.
* Uso de DTOs.
* Desarrollo de vistas con Thymeleaf.
* Gestión de bases de datos con H2.
