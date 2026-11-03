
package com.example.interfazcompose.data.local

// ROOM: permite definir tablas y claves primarias en SQLite.
import androidx.room.Entity
import androidx.room.PrimaryKey

// CLASE: RoomUsageEntity.
// FUNCIÓN: almacena cada atención realizada en un probador.
// CONEXIÓN: RoomDao la guarda y el Repository gestiona su historial.
@Entity(tableName = "fitting_room_usage")
data class RoomUsageEntity(

    // PRIMARYKEY: ID único generado automáticamente por Room.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // ROOMID: identifica el probador utilizado.
    val roomId: Int,

    // ENTRYTIME: fecha y hora de ingreso en milisegundos.
    val entryTime: Long,

    // EXITTIME: fecha y hora de salida; null si sigue ocupado.
    val exitTime: Long? = null,

    // ITEMCOUNT: cantidad de prendas de la atención.
    val itemCount: Int,

    // CATEGORY: categoría de las prendas ingresadas.
    val category: String,

    // WASEDITED: indica si se corrigió la cantidad de prendas.
    val wasEdited: Boolean = false
)

