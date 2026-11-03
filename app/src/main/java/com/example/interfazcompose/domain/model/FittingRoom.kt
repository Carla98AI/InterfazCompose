
package com.example.interfazcompose.domain.model

// ENUM: define los tres estados posibles de un probador.
enum class RoomStatus {
    AVAILABLE, // Disponible para una nueva atención.
    OCCUPIED,  // Ocupado por un cliente.
    CLEANING   // En proceso de limpieza.
}

// MODEL: representa un probador y sus características.
// DATA CLASS: permite almacenar datos y usar copy().
data class FittingRoom(
    val id: Int,                      // Identificador único.
    val name: String,                 // Nombre del probador.
    val status: RoomStatus,           // Estado actual.
    val startedAt: Long? = null,      // Hora de ingreso; puede ser null.
    val itemCount: Int = 0,           // Cantidad de prendas.
    val category: String = "",        // Categoría de productos.
    val alertSent: Boolean = false,   // Indica si se envió una alerta.
    val occupancyPhotoPath: String? = null, // Ruta de foto de ocupación.
    val cleaningPhotoPath: String? = null   // Ruta de foto de limpieza.
) {

    // VALIDACIÓN: comprueba si supera el tiempo permitido.
    // Devuelve true si lleva 30 minutos o más ocupado.
    fun hasExceededLimit(
        now: Long,
        limitMinutes: Int = 30
    ): Boolean =
        status == RoomStatus.OCCUPIED &&
                startedAt != null &&
                now - startedAt >= limitMinutes * 60_000L
}

