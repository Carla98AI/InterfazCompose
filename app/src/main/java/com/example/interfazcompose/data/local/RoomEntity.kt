package com.example.interfazcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.model.RoomStatus

@Entity(tableName = "fitting_rooms")
data class RoomEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val startedAt: Long?,
    val itemCount: Int,
    val category: String,
    val alertSent: Boolean,
    // Indica que el cambio local todavía debe enviarse al servidor.
    val syncPending: Boolean = false,
    val occupancyPhotoPath: String? = null,
    val cleaningPhotoPath: String? = null
) {
    // Convierte la fila de SQLite al modelo que usa la lógica de la app.
    fun toDomain() = FittingRoom(
        id, name, RoomStatus.valueOf(status), startedAt, itemCount, category, alertSent,
        occupancyPhotoPath, cleaningPhotoPath
    )

    companion object {
        // Convierte el modelo de la app en una fila que Room puede guardar.
        fun from(room: FittingRoom, syncPending: Boolean = false) = RoomEntity(
            room.id, room.name, room.status.name, room.startedAt, room.itemCount, room.category,
            room.alertSent, syncPending, room.occupancyPhotoPath, room.cleaningPhotoPath
        )
    }
}
