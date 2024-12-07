# Aplicación de Gestión de Piezas en Android Studio

Este proyecto es una aplicación de escritorio desarrollada en Android Studio utilizando SQLite. Permite la gestión de piezas en una base de datos, con un sistema de roles que define los privilegios de cada usuario.

## Características

- **Sistema de Login**:
  - Los usuarios deben iniciar sesión para acceder a la aplicación.
  - Dependiendo del rol del usuario, tendrá diferentes permisos para realizar cambios en la base de datos.
  - Roles predeterminados: `Administrador`, `Usuario`, `Invitado`.
  - La base de datos incluye al menos un usuario para cada rol.

- **Pantallas Principales**:
  1. **Pantalla de Login**:
     - Permite a los usuarios autenticarse con su nombre de usuario y contraseña.
     - Redirige a la pantalla principal según el rol del usuario.

  2. **Pantalla de Gestión de Piezas**:
     - Muestra una lista inicial de **Tipos de Piezas**.
     - Funciones basadas en el rol del usuario:
       - **Invitado**: Solo puede ver los tipos de piezas, sin acceso a las piezas específicas.
       - **Usuario**: Puede ver detalles de las piezas seleccionando un tipo.
       - **Administrador**: Puede insertar, modificar o borrar piezas.
     - Botones principales:
       - **Salir**: Cierra la aplicación.
       - **Limpiar**: Vacía las cajas de texto y deselecciona cualquier elemento en la lista.

## Funcionalidades Detalladas

### Roles y Permisos
1. **Administrador**:
   - Puede **insertar**, **modificar** y **borrar** piezas.
   - Tiene acceso completo a todas las funcionalidades de la aplicación.

2. **Usuario**:
   - Puede seleccionar un tipo de pieza y ver las piezas asociadas.
   - No puede realizar modificaciones en la base de datos.

3. **Invitado**:
   - Solo puede ver la lista de tipos de piezas.
   - No puede ver las piezas ni realizar modificaciones.

### Interacciones
- **Insertar Piezas (Administrador)**:
  - Inserta una nueva pieza utilizando los datos introducidos en las cajas de texto.
- **Borrar Piezas (Administrador)**:
  - Elimina una pieza cuyo ID esté especificado en las cajas de texto.
- **Modificar Piezas (Administrador)**:
  - Actualiza los valores de una pieza en la base de datos si difieren de los actuales.
- **Ver Piezas (Usuario y Administrador)**:
  - Seleccionar un tipo de pieza carga todas las piezas relacionadas en un `GridView`.
  - Al seleccionar una pieza, sus detalles se cargan en las cajas de texto correspondientes.
- **Limpiar**:
  - Vacía las cajas de texto y deselecciona cualquier pieza o tipo en las listas.

## Requisitos

- **Android Studio** con soporte para Kotlin.
- Dispositivo o emulador con al menos Android 5.0 (API 21).
- Configuración inicial de SQLite con los siguientes datos precargados:
  - **Roles**: `Administrador`, `Usuario`, `Invitado`.
  - **Usuarios**: Al menos un usuario para cada rol.
  - **Piezas y Tipos**: Datos iniciales para probar la aplicación.

## Configuración de la Base de Datos

La base de datos SQLite incluye las siguientes tablas:
1. **Usuarios**:
   - `id` (PK)
   - `username`
   - `password`
   - `role_id` (FK hacia Roles)
2. **Roles**:
   - `id` (PK)
   - `name` (Ej: Administrador, Usuario, Invitado)
3. **Tipos de Piezas**:
   - `id` (PK)
   - `name`
4. **Piezas**:
   - `id` (PK)
   - `name`
   - `type_id` (FK hacia Tipos de Piezas)
   - `details`

## Instalación y Ejecución

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tuusuario/gestion-piezas-android.git
   cd gestion-piezas-android
