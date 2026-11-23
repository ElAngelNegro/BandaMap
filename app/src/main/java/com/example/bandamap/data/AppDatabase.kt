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
@Database(entities = [Banda::class], version = 1, exportSchema = false)
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
                        // Solo la primera vez que se crea la base: cargamos bandas de ejemplo
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.bandaDao()?.insertarTodas(DatosPrueba.bandas)
                            }
                        }
                    })
                    .build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}
