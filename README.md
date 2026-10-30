# Rincón Sur: control de probadores

## ¿De qué trata?

Este proyecto es una app móvil de apoyo para el personal de vestuario de Tienda departamental Rincón Sur. Su objetivo es ayudar a saber qué espacios están disponibles, cuáles están siendo utilizados y cuáles necesitan limpieza. También permite advertir cuando una ocupación supera los 30 minutos.

El caso se trabaja con información ficticia. La app no identifica a clientes ni se conecta a sistemas reales de la tienda.

## ¿Qué se puede hacer?

- Consultar un resumen de disponibilidad, ocupación y alertas.
- Iniciar sesión como Administrador con usuario y contraseña, o entrar como invitado.
- Consultar los estados de los probadores como invitado, sin modificarlos.
- Revisar y configurar estados desde el modo Administrador.
- Asignar un espacio indicando la cantidad de prendas y su categoría.
- Finalizar una ocupación y dejar el espacio en estado de limpieza.
- Marcarlo disponible una vez que se haya limpiado.
- Guardar una foto opcional de la ocupación y otra del probador después de la limpieza.
- Recibir una notificación cuando una ocupación supera 30 minutos.

La asignación valida que se indique un espacio disponible, entre 1 y 12 prendas y una categoría. Los cambios se guardan primero en el dispositivo, para que la operación principal no dependa de que el servidor responda en ese instante.

## Tecnologías solicitadas y dónde se usan

El **frontend** es la parte que usa el personal en el teléfono. Está desarrollado en **Kotlin para Android**, con **Jetpack Compose y Material 3** para dibujar las pantallas. No es una página web ni está hecho con React: es una aplicación Android nativa. El **backend** es un servicio separado, hecho en **Java con Spring Boot**, que recibe las solicitudes de la app por una API REST.

| Requisito del proyecto | Tecnología | Cómo y dónde se utiliza |
|---|---|---|
| Interfaz móvil con diseño Material 3 | Kotlin, Jetpack Compose, Material 3 | `ui/screen/HomeScreen.kt` contiene el resumen, la lista, las alertas, las tarjetas y el formulario. `ui/theme/` define colores y tipografía. |
| Inicio de sesión y entrada de invitado | Kotlin y Compose Material 3 | `RoleEntryScreen` en `ui/screen/HomeScreen.kt` valida el usuario y la contraseña del Administrador; la opción Invitado abre directamente la vista de estados, que es de solo lectura. Al cerrar sesión se vuelve al acceso. |
| Navegación funcional | Compose Material 3 | La barra inferior en `HomeScreen.kt` cambia entre Inicio, Probadores y Alertas para Administrador. Usuario ve directamente la gestión de probadores. |
| Formularios de acceso y asignación | Compose y Kotlin | `RoleEntryScreen` muestra errores cuando las credenciales de administrador no coinciden; `domain/validation/AdminLoginValidator.kt` valida usuario y contraseña fuera de la interfaz. El formulario de asignación muestra sus errores en `HomeScreen.kt`; `domain/validation/AssignmentValidator.kt` aplica las reglas de 1 a 12 prendas, categoría obligatoria y selección de espacio. |
| Validación separada de la interfaz | Kotlin | `AssignmentValidator.kt` contiene las reglas; `ui/viewmodel/FittingRoomViewModel.kt` las ejecuta y entrega los errores a la pantalla. |
| Gestión del estado | MVVM con Android ViewModel y StateFlow | `FittingRoomViewModel.kt` mantiene la lista de espacios, la hora actual y los errores del formulario. Compose observa estos valores y redibuja las vistas cuando cambian. |
| Base de datos local SQLite | SQLite con Room | `data/local/RoomEntity.kt` define la tabla `fitting_rooms`; `RoomDao.kt` consulta y guarda filas; `FittingRoomDatabase.kt` crea `rincon_sur.db` y conserva los datos mediante una migración. |
| Acceso a API REST | Retrofit y Gson | `data/remote/FittingRoomApi.kt` define las solicitudes GET y PUT y convierte las respuestas del backend en objetos Kotlin. `data/repository/FittingRoomRepository.kt` guarda primero en Room y luego coordina el envío y la sincronización. |
| Backend solicitado | Java y Spring Boot | `backend/src/main/java/.../FittingRoomController.java` publica las rutas REST; `FittingRoomService.java` aplica las reglas y gestiona datos ficticios en memoria. |
| Recursos nativos del dispositivo | Cámara del sistema, Android Notification API y vibración | `HomeScreen.kt` abre la cámara del dispositivo para fotos opcionales; `platform/OccupancyNotifications.kt` muestra avisos y `platform/DeviceFeedback.kt` confirma acciones con vibración. Las fotos se guardan en el almacenamiento privado de la app y no se envían al backend. |
| Revisión periódica de ocupaciones | WorkManager | `platform/OccupancyAlertWorker.kt` consulta SQLite y genera un aviso si una ocupación excede 30 minutos. `MainActivity.kt` programa la tarea periódica. |
| Animaciones | Animaciones de Jetpack Compose | `HomeScreen.kt` anima la barra de ocupación, los cambios de color de estado y la aparición de información de uso. |
| Compilación del proyecto | Gradle | Los archivos `build.gradle.kts` y `gradle/libs.versions.toml` declaran módulos, plugins y dependencias. |

### Qué está escrito en Kotlin

Kotlin se usa para **todo el cliente Android**: la actividad que inicia la aplicación, las pantallas Compose, la navegación, los modelos de datos, la validación del formulario, el ViewModel, la conexión Retrofit, el repositorio, Room/SQLite y las funciones del dispositivo. Room crea código de soporte durante la compilación usando **KSP**, el procesador de código configurado en Gradle. El servidor, en cambio, está escrito en **Java**, como implementación del backend Spring Boot.

### Cómo se guardan y sincronizan los datos

Cuando el Administrador asigna, libera o limpia un espacio, el cambio se registra primero en la base SQLite del teléfono mediante Room. La interfaz observa esa base y muestra el resultado mientras la app intenta enviar la operación al backend con Retrofit. Las fotos opcionales de ocupación y limpieza se guardan como archivos privados del teléfono; Room conserva sus rutas y las imágenes no se envían a la API. La API de este prototipo mantiene su propia copia temporal en memoria, no una base de datos de producción.

## Organización del código

```text
app/src/main/java/com/example/interfazcompose/
  domain/model/       Datos y estados de un probador
  domain/validation/  Reglas del formulario de asignación
  data/local/         SQLite a través de Room: tabla, consultas y base
  data/remote/        Rutas de la API, datos enviados y recibidos
  data/repository/    Guarda localmente y coordina la sincronización
  platform/           Notificaciones, vibración y tarea programada
  ui/screen/          Pantallas y elementos visuales Compose
  ui/viewmodel/       Estado de pantalla y acciones del usuario
backend/              API REST Spring Boot y pruebas
```

## Cómo ejecutar

### Iniciar el backend

Se necesita JDK 17. Abre una terminal en la carpeta `backend` y ejecuta:

```powershell
cd backend
.\gradlew.bat bootRun
```

El servidor queda disponible en `http://localhost:8080`. Para ejecutar sus pruebas:

```powershell
.\gradlew.bat test
```

### Iniciar la app Android

1. Abre la carpeta del proyecto en Android Studio y espera a que termine la sincronización de Gradle.
2. Inicia un emulador Android desde Device Manager.
3. Si quieres probar la conexión al servidor, inicia antes el backend.
4. Ejecuta la configuración `app`.

En el emulador, la app usa `10.0.2.2:8080` para llegar al servidor del computador. Para usar un teléfono físico, cambia `BASE_URL` en `app/src/main/java/com/example/interfazcompose/data/remote/FittingRoomApi.kt` por la dirección local del computador y conecta ambos dispositivos a la misma red. Para ver notificaciones del sistema, acepta el permiso que solicita Android.

Al abrir la app aparece el formulario de acceso. Para ingresar al modo Administrador usa **usuario `admin`** y **contraseña `RinconSur2026!`**. Las credenciales se validan antes de habilitar la administración. También está disponible **Entrar como invitado**, que muestra solo los números y estados de los probadores. El Administrador consulta el resumen y las alertas, y asigna o cambia estados. Mientras un espacio está ocupado, puede tomar una foto opcional: la persona decide si aparece en ella o si se fotografían solo las prendas. Al terminar la limpieza, puede tomar otra foto para dejar constancia visual del estado del espacio. Las imágenes se conservan localmente y no se comparten con el servidor. Estas credenciales están incluidas en el cliente como acceso de demostración para la evaluación; para proteger una instalación real, la validación y los permisos de las operaciones tendrían que estar también en un servidor autenticado.

