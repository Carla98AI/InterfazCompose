
/*
 * ARCHIVO: Color.kt
 *
 * CAPA: VIEW (interfaz visual).
 *
 * FUNCIÓN:
 * Define los colores personalizados de Rincón Sur.
 *
 * CONEXIÓN:
 * Theme.kt y los componentes visuales que utilicen
 * estos colores.
 *
 * IMPORTANTE:
 * Color(0xFF...) representa un color hexadecimal.
 * FF indica opacidad completa.
 */

package com.example.interfazcompose.ui.theme

// COLOR: clase de Jetpack Compose para representar colores.
import androidx.compose.ui.graphics.Color

// SUR TEAL: verde petróleo principal de la aplicación.
val SurTeal = Color(0xFF245B57)

// SUR TEAL DARK: verde claro para contrastes.
val SurTealDark = Color(0xFF9BD0C5)

// SUR INK: verde muy oscuro para textos o elementos.
val SurInk = Color(0xFF172B2A)

// SUR MIST: verde muy claro para fondos suaves.
val SurMist = Color(0xFFE7F0EE)

// SUR AMBER: tono ámbar para destacar elementos.
val SurAmber = Color(0xFF9A5F00)
