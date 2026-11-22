package com.example.bandamap

import android.app.Application
import com.example.bandamap.data.AppDatabase
import com.example.bandamap.data.BandaRepository

// Se crea una sola vez al abrir la app. Guarda la base de datos y el repositorio
// para que todas las pantallas usen la misma instancia.
class BandaMapApplication : Application() {
    val database by lazy { AppDatabase.obtenerBaseDeDatos(this) }
    val bandaRepository by lazy { BandaRepository(database.bandaDao()) }
}
