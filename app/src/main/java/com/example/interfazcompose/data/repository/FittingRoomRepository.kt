
package com.example.interfazcompose.data.repository

// Importa el DAO que permite consultar y guardar datos en SQLite.
import com.example.interfazcompose.data.local.RoomDao
// Importa la entidad que representa el estado actual de un probador.
import com.example.interfazcompose.data.local.RoomEntity
// Importa la entidad que representa cada atención del historial.
import com.example.interfazcompose.data.local.RoomUsageEntity
// Importa la interfaz de comunicación con el servidor existente.
import com.example.interfazcompose.data.remote.FittingRoomApi
// Importa el modelo utilizado para sincronizar estados.
import com.example.interfazcompose.data.remote.RoomSyncRequest
// Importa el modelo de negocio del probador.
import com.example.interfazcompose.domain.model.FittingRoom
// Importa el enum que define los estados posibles.
import com.example.interfazcompose.domain.model.RoomStatus
// Dispatcher utilizado para operaciones de entrada y salida.
import kotlinx.coroutines.Dispatchers
// Flow permite observar cambios en los datos.
import kotlinx.coroutines.flow.Flow
// map transforma los elementos de un flujo.
import kotlinx.coroutines.flow.map
// withContext cambia el contexto de una corrutina.
import kotlinx.coroutines.withContext
// Calendar permite trabajar con fechas y horas.
import java.util.Calendar

// class: Repository que conecta el ViewModel con la capa de datos.
class FittingRoomRepository(
    private val dao: RoomDao, // DAO de Room/SQLite.
    private val api: FittingRoomApi // API existente del proyecto.
) {

    // val: propiedad de solo lectura. // Flow<List<FittingRoom>>: flujo de listas de probadores.
    // map: transforma RoomEntity en FittingRoom.
    val rooms: Flow<List<FittingRoom>> =
        dao.observeAll().map { list ->
            list.map(RoomEntity::toDomain)
        }

    // suspend fun: función suspendible, no inicia una corrutina. // seedIfEmpty(): crea los probadores iniciales si no existen.
    suspend fun seedIfEmpty() {

        // isNotEmpty(): comprueba si SQLite ya contiene probadores. // return: termina la función para evitar duplicados.
        if (dao.getAll().isNotEmpty()) return

        // Long: entero de 64 bits con la hora actual en milisegundos.
        val now: Long = System.currentTimeMillis()

        // List<RoomEntity>: lista de entidades para guardar en SQLite. // (1..8): rango de ocho identificadores.
        // map: transforma cada ID en una entidad.
        val initialRooms: List<RoomEntity> = (1..8).map { id ->

            // when: selecciona el estado inicial según el ID. // FittingRoom: modelo de negocio del probador.
            val room: FittingRoom = when (id) {

                // Probador 1: ocupado inicialmente.
                1 -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.OCCUPIED,
                    now - 36 * 60_000L,
                    4,
                    "Vestuario",
                    false
                )

                // Probador 2: ocupado inicialmente.
                2 -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.OCCUPIED,
                    now - 8 * 60_000L,
                    2,
                    "Calzado",
                    false
                )

                // Probador 8: comienza en limpieza.
                8 -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.CLEANING
                )

                // else: los demás probadores quedan disponibles.
                else -> FittingRoom(
                    id,
                    "Probador %02d".format(id),
                    RoomStatus.AVAILABLE
                )
            }

            // from(): convierte el modelo en una entidad SQLite.
            RoomEntity.from(room)
        }

        // Función suspend del DAO: guarda todos los probadores.
        dao.saveAll(initialRooms)
    }

    // suspend fun: permite operaciones suspendibles. // Boolean: devuelve true si el bloque termina correctamente.
    // runCatching: captura errores como un resultado de éxito o fallo.
    suspend fun refreshFromServer(): Boolean = runCatching {

        // Obtiene los probadores desde la API existente.
        val remoteRooms = api.getRooms()

        // isEmpty(): comprueba si la lista está vacía.
        // return@runCatching: termina este bloque.
        if (remoteRooms.isEmpty()) return@runCatching

        // associateBy: crea un mapa de probadores según su ID.
        val localById = dao.getAll().associateBy { it.id }

        // map: recorre y transforma los probadores recibidos.
        val merged = remoteRooms.map { remote ->

            // Busca el probador local; puede ser null.
            val local = localById[remote.id]

            // ?. permite acceder si local no es null.
            // == true: comprueba si tiene sincronización pendiente.
            if (local?.syncPending == true) {

                // runCatching: captura posibles errores de sincronización.
                runCatching {

                    // Función suspend: envía el estado del probador.
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
                    // onSuccess: se ejecuta si la sincronización funciona.
                    onSuccess = {

                        // toDomain(): convierte a modelo.
                        // copy(): conserva las rutas de fotografías locales.
                        RoomEntity.from(
                            it.toDomain().copy(
                                occupancyPhotoPath = local.occupancyPhotoPath,
                                cleaningPhotoPath = local.cleaningPhotoPath
                            )
                        )
                    },

                    // onFailure: conserva la entidad local si falla.
                    onFailure = { local }
                )

            } else {

                // Si no hay cambios pendientes, utiliza datos remotos.
                RoomEntity.from(
                    remote.toDomain().copy(
                        occupancyPhotoPath = local?.occupancyPhotoPath,
                        cleaningPhotoPath = local?.cleaningPhotoPath
                    )
                )
            }
        }

        // Función suspend: guarda los probadores obtenidos.
        dao.saveAll(merged)

    }.isSuccess // Boolean: indica si runCatching terminó sin excepción.

    // suspend fun: función suspendible para asignar prendas. // CREATE: crea una atención y cambia el estado del probador.
    suspend fun assign(
        room: FittingRoom, // Modelo del probador.
        itemCount: Int, // Cantidad de prendas, número entero.
        category: String, // Categoría, texto.
        occupancyPhotoPath: String? = null // Ruta opcional; ? permite null.
    ) {

        // require(): valida un argumento; falla si no cumple la condición.
        require(itemCount > 0) {
            "La cantidad debe ser mayor que cero"
        }

        // isNotBlank(): verifica que el texto no esté vacío.
        require(category.isNotBlank()) {
            "La categoría es obligatoria"
        }

        // getAll(): consulta suspendible del DAO.
        // firstOrNull(): busca el primer registro que coincida.
        // ?: error(): lanza una excepción si no existe.
        val current: RoomEntity = dao.getAll()
            .firstOrNull { it.id == room.id }
            ?: error("Probador no encontrado")

        // check(): valida el estado actual del sistema.
        // .name: convierte el enum AVAILABLE a String.
        check(current.status == RoomStatus.AVAILABLE.name) {
            "El probador no está disponible"
        }

        // Long: almacena la fecha y hora de ingreso.
        val now: Long = System.currentTimeMillis()

        // copy(): crea un nuevo modelo con los valores modificados.
        val updated: FittingRoom = room.copy(
            status = RoomStatus.OCCUPIED, // Enum: ocupado.
            startedAt = now, // Hora de ingreso.
            itemCount = itemCount, // Prendas asignadas.
            category = category, // Categoría registrada.
            alertSent = false, // Boolean: alerta no enviada.
            occupancyPhotoPath = occupancyPhotoPath, // Foto opcional.
            cleaningPhotoPath = null // Limpia la foto anterior.
        )

        // Función suspend del DAO: actualiza el estado en SQLite.
        // syncPending=true: indica un cambio local pendiente.
        dao.save(RoomEntity.from(updated, syncPending = true))

        // Función suspend del DAO: inserta una atención histórica. // Cada inserción representa un nuevo uso del probador.
        dao.insertUsage(
            RoomUsageEntity(
                roomId = room.id, // Int: identificador del probador.
                entryTime = now, // Long: hora de ingreso.
                itemCount = itemCount, // Int: prendas.
                category = category // String: categoría.
            )
        )
    }

    // suspend fun: función suspendible de actualización. // UPDATE: corrige prendas sin generar otra atención.
    suspend fun editItemCount(
        roomId: Int, // Identificador del probador.
        newItemCount: Int // Nueva cantidad de prendas.
    ) {

        // require(): impide guardar cantidades inválidas.
        require(newItemCount > 0) {
            "La cantidad debe ser mayor que cero"
        }

        // Consulta el probador actual mediante el DAO.
        val room: RoomEntity = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: error("Probador no encontrado")

        // check(): permite editar solo cuando está ocupado. // .name: convierte OCCUPIED a String.
        check(room.status == RoomStatus.OCCUPIED.name) {
            "Solo se pueden editar probadores en uso"
        }

        // Consulta suspendible: obtiene la atención sin hora de salida.
        val usage: RoomUsageEntity = dao.getActiveUsage(roomId)
            ?: error("No existe una atención activa")

        // save(): actualiza la cantidad visible del probador.
        // copy(): mantiene los demás campos sin modificarlos.
        dao.save(
            room.copy(
                itemCount = newItemCount,
                syncPending = true
            )
        )

        // updateUsage(): modifica la atención existente. // wasEdited=true: indica que se corrigió el registro.
        dao.updateUsage(
            usage.copy(
                itemCount = newItemCount,
                wasEdited = true
            )
        )
    }

    // fun: función normal que devuelve un Flow. // READ: observa el historial diario de un probador.
    fun observeDailyHistory(
        roomId: Int, // Identificador del probador.
        dayMillis: Long // Fecha seleccionada en milisegundos.
    ): Flow<List<RoomUsageEntity>> {

        // Desestructuración: obtiene inicio y fin del día.
        val (start, end) = dayRange(dayMillis)

        // Flow: emite listas nuevas cuando cambia el historial.
        return dao.observeDailyUsage(roomId, start, end)
    }

    // fun: devuelve un Flow<Int> con el contador diario.
    fun observeDailyCount(
        roomId: Int,
        dayMillis: Long
    ): Flow<Int> {

        // Calcula el intervalo correspondiente al día.
        val (start, end) = dayRange(dayMillis)

        // COUNT(*) se ejecuta mediante la consulta del DAO.
        return dao.observeDailyUsageCount(roomId, start, end)
    }

    // fun: consulta las atenciones de todos los probadores.
    fun observeAllDailyHistory(
        dayMillis: Long
    ): Flow<List<RoomUsageEntity>> {

        // Obtiene los límites del día seleccionado.
        val (start, end) = dayRange(dayMillis)

        // Devuelve un flujo con las atenciones del día.
        return dao.observeAllDailyUsage(start, end)
    }

    // private fun: función auxiliar, accesible solo en esta clase.
    // Pair<Long, Long>: devuelve dos valores Long.
    private fun dayRange(dayMillis: Long): Pair<Long, Long> {

        // Calendar: objeto para manipular fechas locales.
        val calendar: Calendar = Calendar.getInstance()

        // Establece la fecha que queremos consultar.
        calendar.timeInMillis = dayMillis

        // set(): configura la hora a las 00:00:00.000.
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Long: inicio del día seleccionado.
        val start: Long = calendar.timeInMillis

        // add(): avanza un día en el calendario.
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        // Long: inicio del día siguiente.
        val end: Long = calendar.timeInMillis

        // Pair(): agrupa los dos límites en un objeto.
        return Pair(start, end)
    }

    // suspend fun: intenta sincronizar cambios pendientes.
    suspend fun syncPendingRoom(roomId: Int) {

        // Busca el probador local según su identificador.
        val local = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: return

        // Si no hay cambios pendientes, termina la función.
        if (!local.syncPending) return

        // Llama a otra función suspendible del repositorio.
        syncCurrentRoom(roomId, local)
    }

    // private: función accesible solo dentro del Repository.
    // suspend: puede suspenderse dentro de una corrutina.
    private suspend fun syncCurrentRoom(
        roomId: Int,
        fallback: RoomEntity
    ) {

        // runCatching: captura errores de la operación.
        runCatching {

            // CORRUTINA: withContext cambia el contexto a Dispatchers.IO.
            // Dispatchers.IO: operaciones de entrada y salida.
            withContext(Dispatchers.IO) {

                // Función suspend: sincroniza el estado remoto.
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

            // onSuccess: se ejecuta si la operación fue exitosa.
            // Conserva las fotografías locales.
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

    // suspend fun: guarda la ruta de una fotografía de ocupación.
    suspend fun saveOccupancyPhoto(
        roomId: Int, // Identificador del probador.
        path: String // Ruta de la imagen.
    ) {

        // Busca la entidad del probador; ?: return si no existe.
        val room = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: return

        // copy(): modifica solo la ruta de la fotografía.
        // save(): guarda la entidad actualizada en SQLite.
        dao.save(room.copy(occupancyPhotoPath = path))
    }

    // suspend fun: guarda la fotografía de limpieza.
    suspend fun saveCleaningPhoto(
        roomId: Int,
        path: String
    ) {

        // Busca el probador mediante una consulta suspendible.
        val room = dao.getAll()
            .firstOrNull { it.id == roomId }
            ?: return

        // Guarda la nueva ruta conservando los demás datos.
        dao.save(room.copy(cleaningPhotoPath = path))
    }

    // suspend fun: finaliza una atención del probador.
    // UPDATE: registra la salida y cambia el estado a limpieza.
    suspend fun release(room: FittingRoom) {

        // Obtiene el estado actual desde SQLite.
        val current: RoomEntity = dao.getAll()
            .firstOrNull { it.id == room.id }
            ?: error("Probador no encontrado")

        // check(): comprueba que el probador esté ocupado.
        // .name: convierte el enum OCCUPIED en String.
        check(current.status == RoomStatus.OCCUPIED.name) {
            "El probador no está ocupado"
        }

        // RoomUsageEntity?: puede ser una atención o null.
        // Busca el registro activo del probador.
        val usage: RoomUsageEntity? = dao.getActiveUsage(room.id)

        // != null: comprueba si existe una atención abierta.
        if (usage != null) {

            // updateUsage(): registra la hora de salida.
            // No elimina la atención del historial.
            dao.updateUsage(
                usage.copy(
                    exitTime = System.currentTimeMillis()
                )
            )
        }

        // copy(): cambia el estado del probador a limpieza.
        val updated: FittingRoom = room.copy(
            status = RoomStatus.CLEANING,
            startedAt = null,
            itemCount = 0,
            category = "",
            alertSent = false
        )

        // Función suspend: guarda el nuevo estado en SQLite.
        dao.save(RoomEntity.from(updated, syncPending = true))
    }

    // suspend fun: finaliza la limpieza del probador.
    suspend fun finishCleaning(room: FittingRoom) {

        // copy(): cambia el estado a AVAILABLE.
        val updated: FittingRoom = room.copy(
            status = RoomStatus.AVAILABLE
        )

        // Guarda el probador disponible en SQLite.
        dao.save(RoomEntity.from(updated, syncPending = true))
    }

    // suspend fun: marca que la alerta ya fue enviada.
    suspend fun markAlertSent(room: FittingRoom) {

        // Boolean true: indica que la alerta fue enviada.
        val updated: FittingRoom = room.copy(
            alertSent = true
        )

        // Guarda el cambio en la base de datos local.
        dao.save(RoomEntity.from(updated, syncPending = true))
    }
}

