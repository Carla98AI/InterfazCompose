
package com.example.interfazcompose.data.local

// ROOM: herramientas para almacenar datos en SQLite.
import androidx.room.Entity
import androidx.room.PrimaryKey

// MODEL: representa los probadores y sus estados.
import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.model.RoomStatus

// CLASE: RoomEntity.
// FUNCIÓN: representa un probador dentro de SQLite.
// CONEXIÓN: el DAO la guarda y el Repository la transforma.
@Entity(tableName = "fitting_rooms")
data class RoomEntity(

    // PRIMARYKEY: identificador único de cada probador.
    @PrimaryKey val id: Int,

    val name: String,               // Nombre del probador.
    val status: String,             // Estado guardado como texto.
    val startedAt: Long?,           // Hora de ingreso; admite null.
    val itemCount: Int,             // Cantidad de prendas.
    val category: String,           // Categoría de productos.
    val alertSent: Boolean,         // Indica si se envió una alerta.
    val syncPending: Boolean = false, // Cambio pendiente de sincronizar.
    val occupancyPhotoPath: String? = null, // Ruta de foto de ocupación.
    val cleaningPhotoPath: String? = null   // Ruta de foto de limpieza.
) {

    // TODOMAIN: convierte datos de SQLite al modelo FittingRoom.
    // CONEXIÓN: el Repository utiliza esta conversión.
    fun toDomain() = FittingRoom(
        id,
        name,
        RoomStatus.valueOf(status),
        startedAt,
        itemCount,
        category,
        alertSent,
        occupancyPhotoPath,
        cleaningPhotoPath
    )

    // COMPANION OBJECT: permite llamar from() sin crear un objeto.
    companion object {

        // FROM: convierte FittingRoom en RoomEntity.
        // CONEXIÓN: el Repository la utiliza para guardar datos.
        fun from(
            room: FittingRoom,
            syncPending: Boolean = false
        ) = RoomEntity(
            room.id,
            room.name,
            room.status.name,
            room.startedAt,
            room.itemCount,
            room.category,
            room.alertSent,
            syncPending,
            room.occupancyPhotoPath,
            room.cleaningPhotoPath
        )
    }
}

