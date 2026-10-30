package com.example.interfazcompose.domain.model

enum class RoomStatus { AVAILABLE, OCCUPIED, CLEANING }

data class FittingRoom(
    val id: Int,
    val name: String,
    val status: RoomStatus,
    val startedAt: Long? = null,
    val itemCount: Int = 0,
    val category: String = "",
    val alertSent: Boolean = false,
    val occupancyPhotoPath: String? = null,
    val cleaningPhotoPath: String? = null
) {
    fun hasExceededLimit(now: Long, limitMinutes: Int = 30): Boolean =
        status == RoomStatus.OCCUPIED && startedAt != null &&
            now - startedAt >= limitMinutes * 60_000L
}
