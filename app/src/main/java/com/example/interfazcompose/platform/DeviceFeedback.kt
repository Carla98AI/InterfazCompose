
package com.example.interfazcompose.platform

// CONTEXT: permite acceder a servicios de Android.
import android.content.Context

// BUILD: identifica la versión de Android.
import android.os.Build

// VIBRACIÓN: clases que controlan el motor de vibración.
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

// GETSYSTEMSERVICE: obtiene servicios del dispositivo.
import androidx.core.content.getSystemService

// OBJETO: DeviceFeedback.
// FUNCIÓN: genera una vibración corta de confirmación.
// CONEXIÓN: HomeScreen lo llama después de ciertas acciones.
object DeviceFeedback {

    // CONFIRM: solicita una vibración de 45 milisegundos.
    fun confirm(context: Context) {

        // SDK_INT: comprueba la versión de Android.
        // ANDROID 12+: utiliza VibratorManager.
        // VERSIONES ANTERIORES: utiliza Vibrator.
        val vibrator = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) {
            context.getSystemService<VibratorManager>()
                ?.defaultVibrator
        } else {
            context.getSystemService<Vibrator>()
        }

        // ANDROID 8+: utiliza VibrationEffect.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // CREATEONESHOT: solicita una vibración de 45 ms.
            // DEFAULT_AMPLITUDE: utiliza intensidad predeterminada.
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    45,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )

        } else {

            // DEPRECATION: permite usar el método antiguo.
            @Suppress("DEPRECATION")

            // VIBRATE: vibración para versiones antiguas.
            vibrator?.vibrate(45)
        }
    }
}

