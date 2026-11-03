
/*
 * ARCHIVO: Theme.kt
 *
 * CAPA: VIEW (interfaz visual).
 *
 * FUNCIÓN:
 * Configura el tema visual de Rincón Sur.
 * Define los colores para modo claro y oscuro.
 *
 * CONEXIÓN:
 * Color.kt -> colores personalizados.
 * Type.kt -> estilos de tipografía.
 * MainActivity.kt -> aplica el tema a la interfaz.
 *
 * IMPORTANTE:
 * MaterialTheme proporciona colores y tipografías
 * a los componentes de Jetpack Compose.
 */

package com.example.interfazcompose.ui.theme

// BUILD: permite consultar la versión de Android.
import android.os.Build

// Detecta si el sistema utiliza modo oscuro.
import androidx.compose.foundation.isSystemInDarkTheme

// MATERIAL 3: administra el tema visual.
import androidx.compose.material3.MaterialTheme

// Funciones para crear esquemas de colores.
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme

// COMPOSABLE: permite crear funciones de interfaz.
import androidx.compose.runtime.Composable

// COLOR: representa colores en Compose.
import androidx.compose.ui.graphics.Color

// LOCAL CONTEXT: permite acceder al contexto de Android.
import androidx.compose.ui.platform.LocalContext

/*
 * DARK COLOR SCHEME
 *
 * FUNCIÓN:
 * Define los colores utilizados en modo oscuro.
 *
 * PRIVATE:
 * Solo puede utilizarse directamente en este archivo.
 *
 * CONEXIÓN:
 * Utiliza SurTealDark definido en Color.kt.
 */

// DARK COLOR SCHEME: paleta del modo oscuro.
private val DarkColorScheme = darkColorScheme(

    // PRIMARY: color principal.
    primary = SurTealDark,

    // ON PRIMARY: color del contenido sobre primary.
    onPrimary = Color(0xFF003731),

    // PRIMARY CONTAINER: fondo de elementos destacados.
    primaryContainer = Color(0xFF164A44),

    // ON PRIMARY CONTAINER: contenido sobre ese fondo.
    onPrimaryContainer = Color(0xFFB8EDE2),

    // SECONDARY: color secundario.
    secondary = Color(0xFFE6B86A),

    // TERTIARY: tercer color de apoyo.
    tertiary = Color(0xFFAAC9D5),

    // ERROR: color utilizado para indicar errores.
    error = Color(0xFFFFB4AB),

    // ERROR CONTAINER: fondo asociado a errores.
    errorContainer = Color(0xFF690005)
)

/*
 * LIGHT COLOR SCHEME
 *
 * FUNCIÓN:
 * Define los colores utilizados en modo claro.
 *
 * CONEXIÓN:
 * Utiliza SurTeal, SurMist y SurInk de Color.kt.
 */

// LIGHT COLOR SCHEME: paleta del modo claro.
private val LightColorScheme = lightColorScheme(

    // PRIMARY: verde petróleo principal.
    primary = SurTeal,

    // ON PRIMARY: texto blanco sobre el color principal.
    onPrimary = Color.White,

    // PRIMARY CONTAINER: fondo verde claro.
    primaryContainer = SurMist,

    // ON PRIMARY CONTAINER: contenido verde oscuro.
    onPrimaryContainer = SurInk,

    // SECONDARY: color secundario.
    secondary = Color(0xFF8C5B0B),

    // TERTIARY: color de apoyo.
    tertiary = Color(0xFF466675),

    // BACKGROUND: fondo general.
    background = Color(0xFFF6F8F6),

    // SURFACE: superficie de componentes.
    surface = Color.White,

    // SURFACE VARIANT: superficie alternativa.
    surfaceVariant = Color(0xFFEEF2EF),

    // ERROR: color para indicar errores.
    error = Color(0xFFB3261E),

    // ERROR CONTAINER: fondo asociado a errores.
    errorContainer = Color(0xFFF9DEDC)
)

/*
 * FUNCIÓN: InterfazComposeTheme
 *
 * TIPO: @Composable.
 *
 * FUNCIÓN:
 * Selecciona el esquema de colores correspondiente
 * y aplica el tema Material 3 a la interfaz.
 *
 * PARÁMETROS:
 * darkTheme: indica si se utiliza modo oscuro.
 * dynamicColor: permite colores dinámicos compatibles.
 * content: interfaz que recibirá el tema.
 *
 * CONEXIÓN:
 * MainActivity -> InterfazComposeTheme -> HomeScreen.
 */

// COMPOSABLE: permite utilizar esta función en Compose.
@Composable
fun InterfazComposeTheme(

    // BOOLEAN: detecta el modo oscuro del sistema.
    darkTheme: Boolean = isSystemInDarkTheme(),

    // BOOLEAN: colores dinámicos desactivados por defecto.
    dynamicColor: Boolean = false,

    // CONTENT: recibe el contenido visual de la aplicación.
    content: @Composable () -> Unit
) {

    // COLOR SCHEME: selecciona la paleta que se aplicará.
    val colorScheme = when {

        // Si se activan colores dinámicos y Android es 12+.
        dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

            // CONTEXT: obtiene el contexto actual de Android.
            val context = LocalContext.current

            // Selecciona colores dinámicos claros u oscuros.
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        // Si el sistema está en modo oscuro.
        darkTheme -> DarkColorScheme

        // En caso contrario, utiliza el modo claro.
        else -> LightColorScheme
    }

    // MATERIAL THEME: aplica colores y tipografía.
    MaterialTheme(

        // Esquema de colores seleccionado.
        colorScheme = colorScheme,

        // Tipografía definida en Type.kt.
        typography = Typography,

        // Contenido de la interfaz.
        content = content
    )
}
