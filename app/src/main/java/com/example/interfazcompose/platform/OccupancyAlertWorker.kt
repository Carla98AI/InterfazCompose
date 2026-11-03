package com.example.interfazcompose.platform

// CONTEXT: permite acceder a recursos de Android.
import android.content.Context

// WORKMANAGER: ejecuta tareas en segundo plano.
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

// ROOM: permite consultar los probadores almacenados.
import com.example.interfazcompose.data.local.FittingRoomDatabase

import com.example.interfazcompose.domain.model.RoomStatus

// CLASE: OccupancyAlertWorker.
// FUNCIÓN: revisa probadores y genera alertas por exceso de tiempo.
// CONEXIÓN: WorkManager la ejecuta; utiliza Room y OccupancyNotifications.
class OccupancyAlertWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    // DOWORK: ejecuta la revisión en segundo plano.
    // SUSPEND: permite realizar operaciones sin bloquear el hilo.
    override suspend fun doWork(): Result {

        // DAO: obtiene acceso a los datos guardados en SQLite.
        val dao = FittingRoomDatabase.get(applicationContext).roomDao()

        // NOW: obtiene la hora actual en milisegundos.
        val now = System.currentTimeMillis()

        // FOREACH: revisa todos los probadores almacenados.
        dao.getAll().forEach { entity ->

            // TODOMAIN: convierte la entidad al modelo FittingRoom.
            val room = entity.toDomain()

            // VALIDACIÓN: comprueba que no tenga alerta y supere el límite.
            // NOTIFY: envía la notificación si corresponde.
            if (
                !room.alertSent &&
                room.hasExceededLimit(now) &&
                OccupancyNotifications.notifyLongOccupancy(
                    applicationContext,
                    room
                )
            ) {

                // COPY: marca la alerta como enviada y la guarda en SQLite.
                dao.save(entity.copy(alertSent = true))
            }
        }

        // SUCCESS: informa a WorkManager que terminó correctamente.
        return Result.success()
    }
}

