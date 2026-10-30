package com.example.interfazcompose.platform

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.interfazcompose.data.local.FittingRoomDatabase
import com.example.interfazcompose.domain.model.RoomStatus

class OccupancyAlertWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Usa Room para revisar el estado persistido aunque el proceso se haya reiniciado.
        val dao = FittingRoomDatabase.get(applicationContext).roomDao()
        val now = System.currentTimeMillis()
        dao.getAll().forEach { entity ->
            val room = entity.toDomain()
            if (!room.alertSent && room.hasExceededLimit(now) && OccupancyNotifications.notifyLongOccupancy(applicationContext, room)) {
                dao.save(entity.copy(alertSent = true))
            }
        }
        return Result.success()
    }
}
