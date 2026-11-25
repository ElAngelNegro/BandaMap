package com.example.bandamap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bandamap.model.Banda
import com.example.bandamap.model.DatosPrueba
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// DATABASE: punto de acceso a la base de datos SQLite "bandamap.db".
// Lista las Entities (tablas) y entrega los DAO.
@Database(entities = [Banda::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bandaDao(): BandaDao

    companion object {
        // Una sola instancia para toda la app (patrón Singleton)
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerBaseDeDatos(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bandamap.db"
                )
                    .addCallback(object : Callback() {
                        // La primera vez que se crea la base: cargamos bandas de ejemplo
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            cargarBandasDeEjemplo()
                        }

                        // Al subir de versión (1 a 2) las tablas quedan vacías: las cargamos de nuevo
                        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                            super.onDestructiveMigration(db)
                            cargarBandasDeEjemplo()
                        }
                    })
                    // Si cambia la versión, Room borra y vuelve a crear las tablas
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instancia
                instancia
            }
        }

        private fun cargarBandasDeEjemplo() {
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.bandaDao()?.insertarTodas(DatosPrueba.bandas)
            }
        }
    }
}
