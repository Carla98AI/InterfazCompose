package com.example.interfazcompose.data.local

// ROOM: permite consultar, insertar y actualizar datos en SQLite.
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// FLOW: observa cambios y emite datos actualizados.
import kotlinx.coroutines.flow.Flow

// CLASE: RoomDao (Data Access Object).
// FUNCIÓN: administra los probadores y su historial en SQLite.
// CONEXIÓN: el Repository utiliza este DAO para acceder a Room.
@Dao //notacion que le permite a la interfaz comportarse como un DAO
interface RoomDao {

    // OBSERVEALL: observa todos los probadores en tiempo real.
    // FLOW: emite una nueva lista cuando cambian los datos.
    @Query("SELECT * FROM fitting_rooms ORDER BY id")
    fun observeAll(): Flow<List<RoomEntity>>

    // GETALL: obtiene todos los probadores una sola vez.
    // SUSPEND: permite ejecutar la consulta sin bloquear el hilo.
    @Query("SELECT * FROM fitting_rooms ORDER BY id")
    suspend fun getAll(): List<RoomEntity>

    // SAVEALL: guarda una lista de probadores.
    // REPLACE: reemplaza registros que tengan el mismo ID.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(rooms: List<RoomEntity>)

    // SAVE: guarda o reemplaza un probador individual.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(room: RoomEntity)

    // INSERTUSAGE: registra una nueva atención.
    // CREATE: devuelve el ID generado para esa atención.
    @Insert
    suspend fun insertUsage(
        usage: RoomUsageEntity
    ): Long

    // UPDATEUSAGE: modifica una atención existente.
    // UPDATE: permite editar prendas o registrar la salida.
    @Update
    suspend fun updateUsage(
        usage: RoomUsageEntity
    )

    // GETUSAGEBYID: busca una atención por su identificador.
    // NULL: devuelve null cuando no existe.
    @Query(
        "SELECT * FROM fitting_room_usage WHERE id = :usageId LIMIT 1"
    )
    suspend fun getUsageById(
        usageId: Long
    ): RoomUsageEntity?

    // GETACTIVEUSAGE: busca la última atención sin hora de salida.
    // IS NULL: indica que la atención sigue abierta.
    @Query(
        """
        SELECT * FROM fitting_room_usage
        WHERE roomId = :roomId
        AND exitTime IS NULL
        ORDER BY entryTime DESC, id DESC
        LIMIT 1
        """
    )
    suspend fun getActiveUsage(
        roomId: Int
    ): RoomUsageEntity?

    // OBSERVEDAILYUSAGE: observa el historial diario de un probador.
    // FLOW: actualiza la lista cuando cambian sus atenciones.
    @Query(
        """
        SELECT * FROM fitting_room_usage
        WHERE roomId = :roomId
        AND entryTime >= :startTime
        AND entryTime < :endTime
        ORDER BY entryTime DESC, id DESC
        """
    )
    fun observeDailyUsage(
        roomId: Int,
        startTime: Long,
        endTime: Long
    ): Flow<List<RoomUsageEntity>>

    // OBSERVEDAILYUSAGECOUNT: cuenta las atenciones del día.
    // COUNT: editar prendas no aumenta la cantidad de usos.
    @Query(
        """
        SELECT COUNT(*) FROM fitting_room_usage
        WHERE roomId = :roomId
        AND entryTime >= :startTime
        AND entryTime < :endTime
        """
    )
    fun observeDailyUsageCount(
        roomId: Int,
        startTime: Long,
        endTime: Long
    ): Flow<Int>

    // OBSERVEALLDAILYUSAGE: observa el historial diario general.
    // ORDER BY: muestra primero las atenciones más recientes.
    @Query(
        """
        SELECT * FROM fitting_room_usage
        WHERE entryTime >= :startTime
        AND entryTime < :endTime
        ORDER BY entryTime DESC, id DESC
        """
    )
    fun observeAllDailyUsage(
        startTime: Long,
        endTime: Long
    ): Flow<List<RoomUsageEntity>>
}


