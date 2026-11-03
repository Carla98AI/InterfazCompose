package com.example.interfazcompose.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * ROOM USAGE ENTITY
 *
 * Esta clase representa una atención realizada en un probador.
 *
 * Cada vez que un cliente ingresa al probador, se creará
 * un registro nuevo en la base de datos local.
 *
 * Esto permitirá consultar:
 * - Cuántas veces se utilizó un probador durante el día.
 * - La fecha y hora de ingreso.
 * - La fecha y hora de salida.
 * - La cantidad de prendas.
 * - La categoría de las prendas.
 *
 * Los datos permanecerán almacenados mediante Room/SQLite.
 */

// @Entity indica que esta clase representa una tabla SQLite.
@Entity(tableName = "fitting_room_usage")
data class RoomUsageEntity(

    // Identificador único de cada atención.
    // Long: número entero de 64 bits.
    // autoGenerate: Room genera automáticamente el ID.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Identificador del probador utilizado.
    // Int: número entero.
    val roomId: Int,

    // Fecha y hora de ingreso.
    // Long: milisegundos desde el 1 de enero de 1970.
    val entryTime: Long,

    // Fecha y hora de salida.
    // Long?: permite null mientras el probador está ocupado.
    val exitTime: Long? = null,

    // Cantidad de prendas que ingresaron al probador.
    // Int: número entero.
    val itemCount: Int,

    // Categoría de las prendas.
    // String: cadena de texto.
    val category: String,

    // Indica si se corrigió la cantidad de prendas.
    // Boolean: true o false.
    val wasEdited: Boolean = false
)
