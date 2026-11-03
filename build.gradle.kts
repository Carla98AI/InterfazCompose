
/*
 * ARCHIVO: build.gradle.kts (Project)
 *
 * FUNCIÓN:
 * Configura los plugins generales del proyecto Android.
 *
 * CONEXIÓN:
 * Los módulos, como app, pueden utilizar estos plugins.
 *
 * IMPORTANTE:
 * apply false declara el plugin sin aplicarlo
 * directamente al proyecto raíz.
 */

// PLUGINS: herramientas utilizadas para construir el proyecto.
plugins {

    // ANDROID APPLICATION:
    // Permite configurar y compilar aplicaciones Android.
    alias(libs.plugins.android.application) apply false

    // KOTLIN ANDROID:
    // Permite utilizar Kotlin en el proyecto Android.
    alias(libs.plugins.kotlin.android) apply false

    // KOTLIN COMPOSE:
    // Habilita el plugin del compilador de Jetpack Compose.
    alias(libs.plugins.kotlin.compose) apply false

    // KSP:
    // Procesa anotaciones para generar código automáticamente.
    // Puede ser utilizado por librerías como Room.
    alias(libs.plugins.kotlin.ksp) apply false
}

