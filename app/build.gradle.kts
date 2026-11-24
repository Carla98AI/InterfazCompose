
/*
 * ARCHIVO: build.gradle.kts (Module :app)
 *
 * FUNCIÓN:
 * Configura la compilación de la aplicación Android.
 *
 * CONEXIÓN:
 * Jetpack Compose, Room, WorkManager,camara(106) Retrofit y Gson.
 *
 * IMPORTANTE:
 * Define las versiones de Android y las librerías
 * necesarias para ejecutar el proyecto Rincón Sur.
 */

// PLUGINS: habilitan herramientas para construir la app.
plugins {

    // ANDROID: permite compilar una aplicación Android.
    alias(libs.plugins.android.application)

    // KOTLIN: permite desarrollar la app con Kotlin.
    alias(libs.plugins.kotlin.android)

    // COMPOSE: habilita el compilador de Jetpack Compose.
    alias(libs.plugins.kotlin.compose)

    // KSP: genera código a partir de anotaciones.
    // CONEXIÓN: Room utiliza KSP para generar implementaciones.
    alias(libs.plugins.kotlin.ksp)
}

// ANDROID: configuración general de la aplicación.
android {

    // NAMESPACE: espacio de nombres del código generado.
    namespace = "com.example.interfazcompose"

    // COMPILE SDK: versión de Android usada para compilar.
    compileSdk = 36

    // DEFAULT CONFIG: configuración base de la aplicación.
    defaultConfig {

        // APPLICATION ID: identificador único de la app.
        applicationId = "com.example.interfazcompose"

        // MIN SDK: Android mínimo compatible (API 24).
        minSdk = 24

        // TARGET SDK: versión objetivo de Android (API 36).
        targetSdk = 36

        // VERSION CODE: número interno de versión.
        versionCode = 1

        // VERSION NAME: versión visible para el usuario.
        versionName = "1.0"

        // TEST RUNNER: ejecuta pruebas instrumentadas.
        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    // BUILD TYPES: configura las versiones de compilación.
    buildTypes {

        // RELEASE: configuración para distribuir la app.
        release {

            // MINIFY: no reduce ni ofusca el código.
            isMinifyEnabled = false

            // PROGUARD: archivos de reglas de optimización.
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    // COMPILE OPTIONS: compatibilidad con Java 11.
    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11

        targetCompatibility = JavaVersion.VERSION_11
    }

    // KOTLIN OPTIONS: genera bytecode compatible con Java 11.
    kotlinOptions {
        jvmTarget = "11"
    }

    // BUILD FEATURES: habilita interfaces con Compose.
    buildFeatures {
        compose = true
    }
}

// DEPENDENCIES: librerías utilizadas por la aplicación.
dependencies {

    // CORE KTX(camara): funciones de Android adaptadas a Kotlin.
    implementation(libs.androidx.core.ktx)

    // LIFECYCLE: componentes vinculados al ciclo de vida.
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ACTIVITY COMPOSE(camara): integra Compose con MainActivity.
    implementation(libs.androidx.activity.compose)

    // COMPOSE BOM: coordina versiones de librerías Compose.
    implementation(platform(libs.androidx.compose.bom))

    // ANIMATIONCOMPOSE UI: componentes base de la interfaz.
    implementation(libs.androidx.compose.ui)

    // ANIMATION GRAPHICS: herramientas gráficas para Compose.
    implementation(libs.androidx.compose.ui.graphics)

    // PREVIEW: permite previsualizar interfaces.
    implementation(libs.androidx.compose.ui.tooling.preview)

    // ANIMATION MATERIAL 3: componentes visuales de la aplicación.(importadas en home screen)
    implementation(libs.androidx.compose.material3)

    // ICONS: iconos para la interfaz de usuario.
    implementation(libs.androidx.compose.icons)

    // ROOM RUNTIME: motor de persistencia local.
    implementation(libs.androidx.room.runtime)

    // ROOM KTX: integración de Room con Kotlin y corrutinas.
    implementation(libs.androidx.room.ktx)

    // ROOM COMPILER: genera implementaciones de DAO y BD.
    ksp(libs.androidx.room.compiler)

    // WORKMANAGER: programa trabajos en segundo plano.
    // CONEXIÓN: OccupancyAlertWorker.kt.
    implementation(libs.androidx.work.runtime)

    // RETROFIT: permite realizar solicitudes HTTP al backend.
    // CONEXIÓN: FittingRoomApi.kt.
    implementation(libs.retrofit.core)

    // RETROFIT GSON: convierte JSON en objetos Kotlin.
    implementation(libs.retrofit.gson)

    // GSON: serializa y deserializa datos JSON.
    implementation(libs.gson)

    // JUNIT: pruebas unitarias.
    testImplementation(libs.junit)

    // ANDROIDX JUNIT: pruebas instrumentadas de Android.
    androidTestImplementation(libs.androidx.junit)

    // ESPRESSO: pruebas de interacción con la interfaz.
    androidTestImplementation(libs.androidx.espresso.core)

    // ANIMATION COMPOSE BOM: versiones coordinadas para pruebas.
    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    // COMPOSE TEST: pruebas de componentes Compose.
    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    // UI TOOLING: herramientas de diseño y depuración.
    debugImplementation(libs.androidx.compose.ui.tooling)

    // TEST MANIFEST: soporte para pruebas de Compose.
    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )
}

