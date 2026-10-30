package com.example.interfazcompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RoomEntity::class], version = 3, exportSchema = false)
abstract class FittingRoomDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Conserva los datos ya guardados al agregar el indicador de sincronización.
                db.execSQL("ALTER TABLE fitting_rooms ADD COLUMN syncPending INTEGER NOT NULL DEFAULT 0")
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Guarda rutas opcionales a fotos dentro del almacenamiento privado de la app.
                db.execSQL("ALTER TABLE fitting_rooms ADD COLUMN occupancyPhotoPath TEXT")
                db.execSQL("ALTER TABLE fitting_rooms ADD COLUMN cleaningPhotoPath TEXT")
            }
        }
        @Volatile private var instance: FittingRoomDatabase? = null
        fun get(context: Context): FittingRoomDatabase = instance ?: synchronized(this) {
            // Mantiene una sola instancia de la base para toda la app.
            instance ?: Room.databaseBuilder(
                context.applicationContext, FittingRoomDatabase::class.java, "rincon_sur.db"
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().also { instance = it }
        }
    }
}
