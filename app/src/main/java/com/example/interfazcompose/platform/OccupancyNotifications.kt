package com.example.interfazcompose.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.interfazcompose.R
import com.example.interfazcompose.domain.model.FittingRoom

object OccupancyNotifications {
    private const val CHANNEL_ID = "ocupacion_probadores"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Ocupación de probadores", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Avisos de uso prolongado para el personal de tienda" }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    fun notifyLongOccupancy(context: Context, room: FittingRoom): Boolean {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return false
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return false
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.example.interfazcompose.R.drawable.ic_launcher_foreground)
            .setContentTitle("Uso prolongado · ${room.name}")
            .setContentText("Revisa el estado del probador desde el pasillo.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(room.id, notification)
        return true
    }
}
