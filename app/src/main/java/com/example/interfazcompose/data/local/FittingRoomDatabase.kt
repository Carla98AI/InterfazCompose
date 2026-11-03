package com.example.interfazcompose.data.local

// ROOM: herramientas para crear y administrar SQLite.
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// CLASE: FittingRoomDatabase.
// FUNCIÓN: configura la base de datos local y sus migraciones.
// CONEXIÓN: entrega RoomDao al Repository para acceder a SQLite.

// DATABASE: registra las dos tablas y la versión actual.
@Database(
    entities = [
        RoomEntity::class,
        RoomUsageEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class FittingRoomDatabase : RoomDatabase() {

    // ROOMDAO: proporciona acceso a las consultas de SQLite.
    // Room genera automáticamente su implementación.
    abstract fun roomDao(): RoomDao

    // COMPANION OBJECT: administra migraciones e instancia única.
    companion object {

        // MIGRATION 1→2: agrega la columna syncPending.
        // DEFAULT 0: los registros anteriores quedan sin sincronización pendiente.
        private val MIGRATION_1_2 = object : Migration(1, 2) {

            // MIGRATE: ejecuta los cambios del esquema SQLite.
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    "ALTER TABLE fitting_rooms " +
                            "ADD COLUMN syncPending INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        // MIGRATION 2→3: agrega rutas para fotografías.
        // TEXT: permite guardar las rutas como texto o null.
        private val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    "ALTER TABLE fitting_rooms " +
                            "ADD COLUMN occupancyPhotoPath TEXT"
                )

                db.execSQL(
                    "ALTER TABLE fitting_rooms " +
                            "ADD COLUMN cleaningPhotoPath TEXT"
                )
            }
        }

        // MIGRATION 3→4: crea la tabla del historial de atenciones.
        // CREATE TABLE: conserva los datos de los probadores existentes.
        private val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `fitting_room_usage` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `roomId` INTEGER NOT NULL,
                        `entryTime` INTEGER NOT NULL,
                        `exitTime` INTEGER,
                        `itemCount` INTEGER NOT NULL,
                        `category` TEXT NOT NULL,
                        `wasEdited` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        // VOLATILE: hace visible la instancia entre distintos hilos.
        // INSTANCE: guarda la base de datos para reutilizarla.
        @Volatile
        private var instance: FittingRoomDatabase? = null

        // GET: obtiene o crea la base de datos.
        // SINGLETON: mantiene una sola instancia en la aplicación.
        fun get(context: Context): FittingRoomDatabase =
            instance ?: synchronized(this) {

                // SYNCHRONIZED: evita crear varias instancias simultáneas.
                instance ?: Room.databaseBuilder(

                    // APPLICATIONCONTEXT: contexto de toda la aplicación.
                    context.applicationContext,

                    // Clase que define la base de datos.
                    FittingRoomDatabase::class.java,

                    // Nombre del archivo SQLite.
                    "rincon_sur.db"

                )
                    // ADDMIGRATIONS: registra los cambios entre versiones.
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4
                    )

                    // BUILD: construye la base de datos.
                    .build()

                    // ALSO: guarda la instancia recién creada.
                    .also {
                        instance = it
                    }
            }
    }
}


