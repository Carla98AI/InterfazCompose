package com.example.interfazcompose.platform

// ANDROID: permisos, canales y servicios de notificaciones.
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

// NOTIFICATIONCOMPAT: permite crear y mostrar notificaciones.
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import com.example.interfazcompose.R
import com.example.interfazcompose.domain.model.FittingRoom

// OBJETO: OccupancyNotifications.
// FUNCIÓN: configura y muestra alertas de ocupación prolongada.
// CONEXIÓN: OccupancyAlertWorker la utiliza para notificar.
object OccupancyNotifications {

    // CHANNEL_ID: identificador único del canal de notificaciones.
    private const val CHANNEL_ID = "ocupacion_probadores"

    // CREATECHANNEL: crea el canal de notificaciones en Android 8+.
    // CONEXIÓN: MainActivity lo llama al iniciar la aplicación.
    fun createChannel(context: Context) {

        // SDK_INT: comprueba la versión de Android.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // CHANNEL: configura nombre y prioridad del canal.
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ocupación de probadores",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {

                // DESCRIPTION: explica el propósito del canal.
                description = "Avisos de uso prolongado para el personal de tienda"
            }

            // NOTIFICATIONMANAGER: registra el canal en Android.
            context.getSystemService(
                NotificationManager::class.java
            ).createNotificationChannel(channel)
        }
    }

    // NOTIFYLONGOCCUPANCY: intenta mostrar una alerta del probador.
    // BOOLEAN: devuelve false si no puede notificar.
    fun notifyLongOccupancy(
        context: Context,
        room: FittingRoom
    ): Boolean {

        // PERMISO: en Android 13+ comprueba POST_NOTIFICATIONS.
        if (
            Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return false

        // VALIDACIÓN: comprueba que las notificaciones estén activadas.
        if (
            !NotificationManagerCompat.from(context)
                .areNotificationsEnabled()
        ) return false

        // BUILDER: construye el contenido de la notificación.
        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            // ICON: imagen pequeña de la notificación.
            .setSmallIcon(R.drawable.ic_launcher_foreground)

            // TITLE: muestra el nombre del probador.
            .setContentTitle("Uso prolongado · ${room.name}")

            // TEXT: indica al personal qué debe revisar.
            .setContentText(
                "Revisa el estado del probador desde el pasillo."
            )

            // PRIORITY: establece prioridad alta.
            .setPriority(NotificationCompat.PRIORITY_HIGH)

            // AUTOCANCEL: se cierra al tocar la notificación.
            .setAutoCancel(true)

            // BUILD: construye el objeto de notificación.
            .build()

        // NOTIFY: envía la notificación usando el ID del probador.
        NotificationManagerCompat.from(context)
            .notify(room.id, notification)

        // RETURN: indica que se solicitó mostrar la notificación.
        return true
    }
}

