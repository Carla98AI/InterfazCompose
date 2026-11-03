
/*
 * ARCHIVO: Type.kt
 *
 * CAPA: VIEW (interfaz visual).
 *
 * FUNCIÓN:
 * Define los estilos de texto de la aplicación.
 *
 * CONEXIÓN:
 * Theme.kt utiliza Typography dentro de MaterialTheme.
 *
 * IMPORTANTE:
 * Permite mantener una tipografía consistente
 * en los componentes de Jetpack Compose.
 */

package com.example.interfazcompose.ui.theme

// TYPOGRAPHY: conjunto de estilos de texto de Material 3.
import androidx.compose.material3.Typography

// TEXTSTYLE: permite configurar la apariencia del texto.
import androidx.compose.ui.text.TextStyle

// FONTFAMILY: define la familia de fuente.
import androidx.compose.ui.text.font.FontFamily

// FONTWEIGHT: define el grosor de las letras.
import androidx.compose.ui.text.font.FontWeight

// SP: unidad de medida utilizada para texto.
import androidx.compose.ui.unit.sp

/*
 * VARIABLE: Typography
 *
 * TIPO: Typography (Material 3).
 *
 * FUNCIÓN:
 * Agrupa los estilos tipográficos de la aplicación.
 *
 * VAL:
 * Su referencia no puede reasignarse.
 *
 * CONEXIÓN:
 * Theme.kt -> MaterialTheme -> Typography.
 */

// TYPOGRAPHY: configura los estilos de texto.
val Typography = Typography(

    // BODY LARGE: estilo para texto de cuerpo grande.
    bodyLarge = TextStyle(

        // FONT FAMILY: fuente predeterminada del sistema.
        fontFamily = FontFamily.Default,

        // FONT WEIGHT: grosor normal.
        fontWeight = FontWeight.Normal,

        // FONT SIZE: tamaño de letra de 16 sp.
        fontSize = 16.sp,

        // LINE HEIGHT: altura de línea de 24 sp.
        lineHeight = 24.sp,

        // LETTER SPACING: separación entre letras.
        letterSpacing = 0.5.sp
    )

    /*
     * OTROS ESTILOS DISPONIBLES:
     *
     * Este bloque está comentado.
     * No se ejecuta ni modifica la tipografía actual.
     *
     * titleLarge: estilo para títulos grandes.
     * labelSmall: estilo para etiquetas pequeñas.
     *
     * Si se necesitaran, podrían activarse
     * y personalizarse posteriormente.
     */

    /*
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
