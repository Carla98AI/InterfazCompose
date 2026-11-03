package com.example.interfazcompose.data.repository

import com.example.interfazcompose.data.local.RoomDao
import com.example.interfazcompose.data.local.RoomEntity
import com.example.interfazcompose.data.local.RoomUsageEntity
import com.example.interfazcompose.data.remote.FittingRoomApi
import com.example.interfazcompose.data.remote.RoomSyncRequest
import com.example.interfazcompose.domain.model.FittingRoom
import com.example.interfazcompose.domain.model.RoomStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar

// REPOSITORY: conecta el ViewModel con Room (DAO) y la API.
class FittingRoomRepository(
    private val dao: RoomDao, // DAO de Room/SQLite.
    private val api: FittingRoomApi // API existente del proyecto.
) {

    // FLOW: observa probadores de SQLite y los convierte al modelo.
    val rooms: Flow<List<FittingRoom>> =
        dao.observeAll().map { list ->
            list.map(RoomEntity::toDomain)
        }

    // INICIALIZAR: crea 8 probadores solo si la base está vacía.
    // SUSPEND: puede suspenderse; no crea una corrutina por sí sola.
    suspend fun seedIfEmpty() {

        // EVITAR DUPLICADOS: si ya hay probadores, termina.
        if (dao.getAll().isNotEmpty()) return

        val now: Long = System.currentTimeMillis()

        // MAP: crea una entidad para cada probador del 1 al 8.
        val initialRooms: List<RoomEntity> = (1..8).map { id ->

            // WHEN: define el estado inicial según el número.
            val room: FittingRoom = when (id) {

                1 -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.OCCUPIED,
                    now - 36 * 60_000L,
                    4,
                    "Vestuario",
                    false
                )

                2 -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.OCCUPIED,
                    now - 8 * 60_000L,
                    2,
                    "Calzado",
                    false
                )

                8 -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.CLEANING
                )

                else -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.AVAILABLE
                )
            }

            RoomEntity.from(room)
        }

        // DAO: guarda los probadores iniciales en Room/SQLite.
        dao.saveAll(initialRooms)
    }

    // SINCRONIZAR: consulta la API e intenta combinar datos remotos y locales.
    // RUNCATCHING: captura fallos y devuelve éxito o fracaso.
    suspend fun refreshFromServer(): Boolean = runCatching {

        // API: solicita la lista de probadores al servidor.
        val remoteRooms = api.getRooms()

        if (remoteRooms.isEmpty()) return@runCatching

        // ASSOCIATEBY: permite buscar probadores locales por ID.
        val localById = dao.getAll().associateBy { it.id }

        val merged = remoteRooms.map { remote ->

            val local = localById[remote.id]

            // SYNC PENDING: detecta cambios locales aún no sincronizados.
            if (local?.syncPending == true) {

                runCatching {

                    // API: envía el estado del probador al servidor.
                    api.syncRoom(
                        local.id,
                        RoomSyncRequest(
                            local.status,
                            local.startedAt,
                            local.itemCount,
                            local.category,
                            local.alertSent
                        )
                    )

                }.fold(
                    // ÉXITO: usa la respuesta remota y conserva las fotos locales.
                    onSuccess = {

                        RoomEntity.from(
                            it.toDomain().copy(
                                occupancyPhotoPath = local.occupancyPhotoPath,
                                cleaningPhotoPath = local.cleaningPhotoPath
                            )
                        )
                    },

                    // FALLO: conserva el registro local.
                    onFailure = { local }
                )

            } else {

                RoomEntity.from(
                    remote.toDomain().copy(
                        occupancyPhotoPath = local?.occupancyPhotoPath,
                        cleaningPhotoPath = local?.cleaningPhotoPath
                    )
                )
            }
        }

        // DAO: guarda la lista combinada en SQLite.
        dao.saveAll(merged)

    }.isSuccess // Boolean: indica si runCatching terminó sin excepción.

    // ASIGNAR: valida, ocupa el probador y crea una atención histórica.
    suspend fun assign(
        room: FittingRoom, // Modelo del probador.
        itemCount: Int, // Cantidad de prendas, número entero.
        category: String, // Categoría, texto.
        occupancyPhotoPath: String? = null // Ruta opcional; ? permite null.
    ) {

        // REQUIRE: rechaza cantidades de prendas menores o iguales a cero.
        require(itemCount > 0) {
            "La cantidad debe ser mayor que cero"
        }

        // REQUIRE: obliga a ingresar una categoría.
        require(category.isNotBlank()) {
            "La categoría es obligatoria"
        }

        val current: RoomEntity = dao.getAll()
            .firstOrNull { it.id == room.id }
            ?: error("Probador no encontrado")

        // CHECK: solo permite asignar un probador disponible.
        check(current.status == RoomStatus.AVAILABLE.name) {
            "El probador no está disponible"
        }

        val now: Long = System.currentTimeMillis()

        // COPY: genera el nuevo estado sin alterar el objeto original.
        val updated: FittingRoom = room.copy(
            status = RoomStatus.OCCUPIED, // Enum: ocupado.
            startedAt = now, // Hora de ingreso.
            itemCount = itemCount, // Prendas asignadas.
            category = category, // Categoría registrada.
            alertSent = false, // Boolean: alerta no enviada.
            occupancyPhotoPath = occupancyPhotoPath, // Foto opcional.
            cleaningPhotoPath = null // Limpia la foto anterior.
        )

        dao.save(RoomEntity.from(updated, syncPending = true))

        // HISTORIAL: registra una nueva atención en SQLite.
        dao.insertUsage(
            RoomUsageEntity(
                roomId = room.id, // Int: identificador del probador.
                entryTime = now, // Long: hora de ingreso.
                itemCount = itemCount, // Int: prendas.
                category = category // String: categoría.
            )
        )
    }

    // EDITAR: corrige prendas en el uso actual sin crear otra atención.
    suspend fun editItemCount(
        roomId: Int, // Identificador del probador.
        newItemCount: Int // Nueva cantidad de prendas.
    ) {

        // VALIDACIÓN: solo acepta una cantidad positiva.
        require(newItemCount > 0) {
            "La cantidad debe ser mayor que cero"
        }

        val room: RoomEntity = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: error("Probador no encontrado")

        // CHECK: solo permite editar un probador ocupado.
        check(room.status == RoomStatus.OCCUPIED.name) {
            "Solo se pueden editar probadores en uso"
        }

        // ATENCIÓN ACTIVA: busca el uso que aún no tiene salida.
        val usage: RoomUsageEntity = dao.getActiveUsage(roomId)
            ?: error("No existe una atención activa")

        dao.save(
            room.copy(
                itemCount = newItemCount,
                syncPending = true
            )
        )

        // HISTORIAL: actualiza la atención existente.
        dao.updateUsage(
            usage.copy(
                itemCount = newItemCount,
                wasEdited = true
            )
        )
    }

    // HISTORIAL: observa las atenciones de un probador en un día.
    fun observeDailyHistory(
        roomId: Int, // Identificador del probador.
        dayMillis: Long // Fecha seleccionada en milisegundos.
    ): Flow<List<RoomUsageEntity>> {

        val (start, end) = dayRange(dayMillis)

        return dao.observeDailyUsage(roomId, start, end)
    }

    // CONTADOR: observa cuántas atenciones tuvo un probador.
    fun observeDailyCount(
        roomId: Int,
        dayMillis: Long
    ): Flow<Int> {

        val (start, end) = dayRange(dayMillis)

        return dao.observeDailyUsageCount(roomId, start, end)
    }

    // HISTORIAL GENERAL: observa todas las atenciones de un día.
    fun observeAllDailyHistory(
        dayMillis: Long
    ): Flow<List<RoomUsageEntity>> {

        val (start, end) = dayRange(dayMillis)

        return dao.observeAllDailyUsage(start, end)
    }

    // FECHAS: calcula inicio del día y comienzo del siguiente.
    private fun dayRange(dayMillis: Long): Pair<Long, Long> {

        val calendar: Calendar = Calendar.getInstance()

        calendar.timeInMillis = dayMillis

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val start: Long = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val end: Long = calendar.timeInMillis

        return Pair(start, end)
    }

    // SINCRONIZACIÓN: envía cambios solo si están pendientes.
    suspend fun syncPendingRoom(roomId: Int) {

        val local = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: return

        if (!local.syncPending) return

        syncCurrentRoom(roomId, local)
    }

    // API: intenta sincronizar un probador sin perder sus fotos locales.
    private suspend fun syncCurrentRoom(
        roomId: Int,
        fallback: RoomEntity
    ) {

        runCatching {

            // WITHCONTEXT(IO): ejecuta esta operación de red en el dispatcher de E/S.
            withContext(Dispatchers.IO) {

                // API: envía el estado del probador al servidor.
                api.syncRoom(
                    roomId,
                    RoomSyncRequest(
                        fallback.status,
                        fallback.startedAt,
                        fallback.itemCount,
                        fallback.category,
                        fallback.alertSent
                    )
                )
            }

        }.onSuccess { response ->

            dao.save(
                RoomEntity.from(
                    response.toDomain().copy(
                        occupancyPhotoPath = fallback.occupancyPhotoPath,
                        cleaningPhotoPath = fallback.cleaningPhotoPath
                    )
                )
            )
        }
    }

    // FOTO OCUPACIÓN: guarda la ruta de la imagen en SQLite.
    suspend fun saveOccupancyPhoto(
        roomId: Int, // Identificador del probador.
        path: String // Ruta de la imagen.
    ) {

        val room = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: return

        dao.save(room.copy(occupancyPhotoPath = path))
    }

    // FOTO LIMPIEZA: guarda la ruta de la imagen en SQLite.
    suspend fun saveCleaningPhoto(
        roomId: Int,
        path: String
    ) {

        val room = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: return

        dao.save(room.copy(cleaningPhotoPath = path))
    }

    // LIBERAR: registra la salida y pasa el probador a limpieza.
    suspend fun release(room: FittingRoom) {

        val current: RoomEntity = dao.getAll()
            .firstOrNull { it.id == room.id }
            ?: error("Probador no encontrado")

        check(current.status == RoomStatus.OCCUPIED.name) {
            "El probador no está ocupado"
        }

        // HISTORIAL: busca la atención que sigue abierta.
        val usage: RoomUsageEntity? = dao.getActiveUsage(room.id)

        if (usage != null) {

            // HISTORIAL: actualiza la atención existente.
            dao.updateUsage(
                usage.copy(
                    exitTime = System.currentTimeMillis()
                )
            )
        }

        // COPY: genera el nuevo estado sin alterar el objeto original.
        val updated: FittingRoom = room.copy(
            status = RoomStatus.CLEANING,
            startedAt = null,
            itemCount = 0,
            category = "",
            alertSent = false
        )

        dao.save(RoomEntity.from(updated, syncPending = true))
    }

    // FIN LIMPIEZA: deja el probador disponible.
    suspend fun finishCleaning(room: FittingRoom) {

        // COPY: genera el nuevo estado sin alterar el objeto original.
        val updated: FittingRoom = room.copy(
            status = RoomStatus.AVAILABLE
        )

        dao.save(RoomEntity.from(updated, syncPending = true))
    }

    // ALERTA: marca que ya se notificó una ocupación prolongada.
    suspend fun markAlertSent(room: FittingRoom) {

        // COPY: genera el nuevo estado sin alterar el objeto original.
        val updated: FittingRoom = room.copy(
            alertSent = true
        )

        dao.save(RoomEntity.from(updated, syncPending = true))
    }
}
