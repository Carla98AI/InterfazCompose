package com.example.interfazcompose.data.repository

import com.example.interfazcompose.data.local.RoomDao
import com.example.interfazcompose.data.local.RoomEntity
import com.example.interfazcompose.data.remote.FittingRoomApi
import com.example.interfazcompose.data.remote.OccupancyRequest
import com.example.interfazcompose.data.remote.RoomSyncRequest
import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.model.RoomStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.map

class FittingRoomRepository(private val dao: RoomDao, private val api: FittingRoomApi) {
    // Room es la fuente que observa la interfaz, también cuando no hay conexión.
    val rooms = dao.observeAll().map { list -> list.map(RoomEntity::toDomain) }

    suspend fun seedIfEmpty() {
        if (dao.getAll().isNotEmpty()) return
        val now = System.currentTimeMillis()
        dao.saveAll((1..8).map { id ->
            when (id) {
                1 -> FittingRoom(id, "Probador %02d".format(id), RoomStatus.OCCUPIED, now - 36 * 60_000L, 4, "Vestuario", false)
                2 -> FittingRoom(id, "Probador %02d".format(id), RoomStatus.OCCUPIED, now - 8 * 60_000L, 2, "Calzado", false)
                8 -> FittingRoom(id, "Probador %02d".format(id), RoomStatus.CLEANING)
                else -> FittingRoom(id, "Probador %02d".format(id), RoomStatus.AVAILABLE)
            }
        }.map(RoomEntity::from))
    }

    suspend fun refreshFromServer(): Boolean = runCatching {
        // Si existe un cambio local pendiente, se conserva y se envía antes de aplicar el remoto.
        val remoteRooms = api.getRooms()
        if (remoteRooms.isEmpty()) return@runCatching
        val localById = dao.getAll().associateBy { it.id }
        val merged = remoteRooms.map { remote ->
            val local = localById[remote.id]
            if (local?.syncPending == true) {
                runCatching {
                    api.syncRoom(
                        local.id,
                        RoomSyncRequest(local.status, local.startedAt, local.itemCount, local.category, local.alertSent)
                    )
                }.fold(
                    onSuccess = { RoomEntity.from(it.toDomain().copy(
                        occupancyPhotoPath = local.occupancyPhotoPath,
                        cleaningPhotoPath = local.cleaningPhotoPath
                    )) },
                    onFailure = { local }
                )
            } else RoomEntity.from(remote.toDomain().copy(
                occupancyPhotoPath = local?.occupancyPhotoPath,
                cleaningPhotoPath = local?.cleaningPhotoPath
            ))
        }
        dao.saveAll(merged)
    }.isSuccess

    suspend fun assign(room: FittingRoom, itemCount: Int, category: String, occupancyPhotoPath: String? = null) {
        val updated = room.copy(
            status = RoomStatus.OCCUPIED,
            startedAt = System.currentTimeMillis(),
            itemCount = itemCount,
            category = category,
            alertSent = false,
            occupancyPhotoPath = occupancyPhotoPath,
            cleaningPhotoPath = null
        )
        // Guardar primero en SQLite permite cerrar el formulario sin esperar a la red.
        val local = RoomEntity.from(updated, syncPending = true)
        dao.save(local)
        // Conserva la forma específica de asignación si el servidor rechaza la sincronización.
        runCatching {
            withContext(Dispatchers.IO) {
                api.occupy(room.id, OccupancyRequest(itemCount, category, updated.startedAt!!))
            }
        }.onSuccess { response ->
            dao.save(RoomEntity.from(response.toDomain().copy(occupancyPhotoPath = local.occupancyPhotoPath)))
        }
    }

    suspend fun syncPendingRoom(roomId: Int) {
        // Un fallo de red deja intacta la fila local para un intento posterior.
        val local = dao.getAll().firstOrNull { it.id == roomId } ?: return
        if (!local.syncPending) return
        syncCurrentRoom(roomId, local)
    }

    private suspend fun syncCurrentRoom(roomId: Int, fallback: RoomEntity) {
        runCatching {
            withContext(Dispatchers.IO) {
                api.syncRoom(
                    roomId,
                    RoomSyncRequest(
                        fallback.status, fallback.startedAt, fallback.itemCount,
                        fallback.category, fallback.alertSent
                    )
                )
            }
        }.onSuccess { response ->
            dao.save(RoomEntity.from(response.toDomain().copy(
                occupancyPhotoPath = fallback.occupancyPhotoPath,
                cleaningPhotoPath = fallback.cleaningPhotoPath
            )))
        }
    }

    suspend fun saveOccupancyPhoto(roomId: Int, path: String) {
        val room = dao.getAll().firstOrNull { it.id == roomId } ?: return
        dao.save(room.copy(occupancyPhotoPath = path))
    }

    suspend fun saveCleaningPhoto(roomId: Int, path: String) {
        val room = dao.getAll().firstOrNull { it.id == roomId } ?: return
        dao.save(room.copy(cleaningPhotoPath = path))
    }

    suspend fun release(room: FittingRoom) {
        val updated = room.copy(status = RoomStatus.CLEANING, startedAt = null, itemCount = 0, category = "", alertSent = false)
        // Al finalizar el uso, el espacio pasa a limpieza antes de volver a estar disponible.
        val local = RoomEntity.from(updated, syncPending = true)
        dao.save(local)
        syncCurrentRoom(room.id, local)
    }

    suspend fun finishCleaning(room: FittingRoom) {
        val updated = room.copy(status = RoomStatus.AVAILABLE)
        val local = RoomEntity.from(updated, syncPending = true)
        dao.save(local)
        syncCurrentRoom(room.id, local)
    }

    suspend fun markAlertSent(room: FittingRoom) {
        val updated = room.copy(alertSent = true)
        val local = RoomEntity.from(updated, syncPending = true)
        dao.save(local)
        syncCurrentRoom(room.id, local)
    }
}
