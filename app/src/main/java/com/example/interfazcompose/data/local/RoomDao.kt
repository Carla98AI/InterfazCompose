package com.example.interfazcompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    // Flow avisa a la interfaz cada vez que cambia la tabla.
    @Query("SELECT * FROM fitting_rooms ORDER BY id")
    fun observeAll(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM fitting_rooms ORDER BY id")
    suspend fun getAll(): List<RoomEntity>

    // Reemplazar por id permite guardar estados actualizados sin duplicar filas.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(rooms: List<RoomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(room: RoomEntity)
}
