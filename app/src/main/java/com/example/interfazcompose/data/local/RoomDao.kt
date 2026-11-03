
package com.example.interfazcompose.data.local

// Importaciones de Room para trabajar con SQLite.
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// Flow permite observar cambios en los datos.
import kotlinx.coroutines.flow.Flow

/*
 * ROOM DAO
 *
 * DAO significa Data Access Object.
 *
 * Esta interfaz contiene las operaciones que permiten
 * consultar, insertar y actualizar información en SQLite.
 *
 * Trabajamos con dos tablas:
 *
 * 1. fitting_rooms:
 *    Guarda el estado actual de cada probador.
 *
 * 2. fitting_room_usage:
 *    Guarda el historial de las atenciones realizadas.
 *
 * @Dao indica que Room debe generar automáticamente
 * la implementación de esta interfaz.
 */

@Dao
interface RoomDao {

    // =====================================================
    // 1. OPERACIONES EXISTENTES DE LOS PROBADORES
    // =====================================================

    /*
     * Devuelve todos los probadores ordenados por ID.
     *
     * Flow<List<RoomEntity>>:
     * Es un flujo de listas de probadores.
     *
     * Cuando los datos cambian, Flow emite una nueva lista.
     */
    @Query("SELECT * FROM fitting_rooms ORDER BY id")
    fun observeAll(): Flow<List<RoomEntity>>

    /*
     * Obtiene todos los probadores una sola vez.
     *
     * suspend:
     * Indica que esta función puede suspenderse
     * sin bloquear el hilo de ejecución.
     */
    @Query("SELECT * FROM fitting_rooms ORDER BY id")
    suspend fun getAll(): List<RoomEntity>

    /*
     * Guarda una lista de probadores.
     *
     * REPLACE:
     * Si ya existe un registro con el mismo ID,
     * se reemplaza por el nuevo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(rooms: List<RoomEntity>)

    /*
     * Guarda o reemplaza un probador individual.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(room: RoomEntity)


    // =====================================================
    // 2. OPERACIONES NUEVAS DEL HISTORIAL
    // =====================================================

    /*
     * CREATE: INSERTAR UNA NUEVA ATENCIÓN
     *
     * Se ejecutará cuando un cliente ingrese
     * a un probador disponible.
     *
     * Devuelve Long:
     * El ID generado para la nueva atención.
     */
    @Insert
    suspend fun insertUsage(
        usage: RoomUsageEntity
    ): Long

    /*
     * UPDATE: ACTUALIZAR UNA ATENCIÓN
     *
     * Permite modificar:
     * - La cantidad de prendas.
     * - La hora de salida.
     * - El indicador de edición.
     *
     * Room identifica el registro mediante
     * su clave primaria: id.
     */
    @Update
    suspend fun updateUsage(
        usage: RoomUsageEntity
    )

    /*
     * READ: CONSULTAR UNA ATENCIÓN POR SU ID
     *
     * RoomUsageEntity?:
     * El signo ? indica que puede devolver null
     * si no existe el registro.
     */
    @Query(
        "SELECT * FROM fitting_room_usage WHERE id = :usageId LIMIT 1"
    )
    suspend fun getUsageById(
        usageId: Long
    ): RoomUsageEntity?

    /*
     * CONSULTAR LA ATENCIÓN ACTIVA DE UN PROBADOR
     *
     * Busca una atención que todavía no tenga
     * hora de salida.
     *
     * exitTime IS NULL significa que sigue abierta.
     */
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

    /*
     * HISTORIAL DIARIO DE UN PROBADOR
     *
     * startTime: inicio del día.
     * endTime: inicio del día siguiente.
     *
     * Se utiliza >= y < para evitar
     * contar una atención en dos días.
     */
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

    /*
     * CONTAR LOS USOS DIARIOS DE UN PROBADOR
     *
     * COUNT(*) cuenta las atenciones registradas.
     *
     * Una edición de prendas NO aumenta este contador,
     * porque actualiza un registro existente.
     */
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

    /*
     * HISTORIAL DIARIO DE TODOS LOS PROBADORES
     *
     * Devuelve las atenciones realizadas durante
     * el día, ordenadas desde la más reciente.
     */
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

