
package com.example.interfazcompose.data.local

// Context permite acceder al contexto de la aplicación Android.
import android.content.Context

// Anotación que identifica la base de datos de Room.
import androidx.room.Database

// Clase utilizada para construir la base de datos.
import androidx.room.Room

// Clase base de las bases de datos Room.
import androidx.room.RoomDatabase

// Permite definir cambios entre versiones de SQLite.
import androidx.room.migration.Migration

// Permite ejecutar instrucciones SQL durante una migración.
import androidx.sqlite.db.SupportSQLiteDatabase

/*
 * CLASE: FittingRoomDatabase
 *
 * CAPA: DATA / PERSISTENCIA LOCAL
 *
 * Esta clase administra la base de datos SQLite
 * utilizando la biblioteca Room.
 *
 * Contiene dos entidades:
 *
 * RoomEntity:
 * Guarda el estado actual de los probadores.
 *
 * RoomUsageEntity:
 * Guarda el historial de las atenciones.
 *
 * Se utiliza la versión 4 porque agregamos
 * una nueva tabla al esquema de la base de datos.
 */

// @Database identifica las entidades y versión de Room.
// exportSchema = false evita exportar el esquema a archivos JSON.
@Database(
    entities = [
        RoomEntity::class,
        RoomUsageEntity::class
    ],
    version = 4,
    exportSchema = false
)

// abstract indica que esta clase no se instancia directamente.
// RoomDatabase proporciona la infraestructura de persistencia.
abstract class FittingRoomDatabase : RoomDatabase() {

    /*
     * Función abstracta que entrega el DAO.
     *
     * Room genera automáticamente su implementación.
     *
     * Retorna un objeto de tipo RoomDao.
     */
    abstract fun roomDao(): RoomDao

    /*
     * companion object:
     * Permite compartir propiedades y funciones
     * asociadas a la clase.
     *
     * Aquí administramos las migraciones y la
     * instancia única de la base de datos.
     */
    companion object {

        /*
         * MIGRACIÓN DE VERSIÓN 1 A 2
         *
         * Agrega la columna syncPending.
         *
         * INTEGER:
         * Tipo numérico entero de SQLite.
         *
         * NOT NULL:
         * Impide almacenar valores nulos.
         *
         * DEFAULT 0:
         * Asigna cero a los registros existentes.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {

            // override reemplaza la implementación
            // de la función definida en Migration.
            override fun migrate(db: SupportSQLiteDatabase) {

                // execSQL ejecuta una instrucción SQL.
                db.execSQL(
                    "ALTER TABLE fitting_rooms " +
                            "ADD COLUMN syncPending INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        /*
         * MIGRACIÓN DE VERSIÓN 2 A 3
         *
         * Agrega dos columnas opcionales para
         * almacenar rutas de fotografías.
         *
         * TEXT corresponde a cadenas de texto.
         *
         * Al no declarar NOT NULL,
         * estas columnas permiten valores nulos.
         */
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

        /*
         * MIGRACIÓN DE VERSIÓN 3 A 4
         *
         * NUEVA FUNCIONALIDAD:
         * Historial diario de uso de probadores.
         *
         * CREATE TABLE:
         * Crea una tabla en SQLite.
         *
         * IF NOT EXISTS:
         * Evita intentar crearla si ya existe.
         *
         * PRIMARY KEY AUTOINCREMENT:
         * Genera identificadores únicos.
         *
         * Esta migración NO elimina la tabla
         * fitting_rooms ni sus datos anteriores.
         */
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

        /*
         * @Volatile:
         * Hace visibles los cambios de esta variable
         * entre distintos hilos de ejecución.
         *
         * private:
         * Solo se puede acceder desde esta clase.
         *
         * var:
         * Variable mutable, puede cambiar.
         *
         * FittingRoomDatabase?:
         * Puede almacenar una instancia de la base
         * o null cuando todavía no se ha creado.
         */
        @Volatile
        private var instance: FittingRoomDatabase? = null

        /*
         * FUNCIÓN get()
         *
         * Recibe:
         * context: Context
         *
         * Retorna:
         * FittingRoomDatabase
         *
         * Implementa el patrón Singleton:
         * mantiene una única instancia de la base
         * de datos para toda la aplicación.
         */
        fun get(context: Context): FittingRoomDatabase =
            instance ?: synchronized(this) {

                // synchronized evita que varios hilos
                // creen la base simultáneamente.

                instance ?: Room.databaseBuilder(

                    // Contexto de la aplicación.
                    context.applicationContext,

                    // Clase que representa la base de datos.
                    FittingRoomDatabase::class.java,

                    // Nombre del archivo SQLite.
                    "rincon_sur.db"

                )
                    /*
                     * Registramos todas las migraciones.
                     *
                     * Room podrá actualizar bases
                     * existentes desde versiones anteriores.
                     */
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4
                    )

                    // build() construye la base de datos.
                    .build()

                    /*
                     * also permite ejecutar una acción
                     * sobre el objeto recién construido.
                     *
                     * it representa esa instancia.
                     *
                     * Guardamos la instancia para
                     * reutilizarla posteriormente.
                     */
                    .also {
                        instance = it
                    }
            }
    }
}

