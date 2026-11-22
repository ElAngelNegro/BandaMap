package com.example.bandamap.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// ENTITY: cada objeto Banda es una fila de la tabla "bandas" en la base de datos Room
@Entity(tableName = "bandas")
data class Banda(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                 // Room genera el id automáticamente al insertar
    val nombre: String,
    val genero: String,
    val ciudad: String,
    val descripcion: String,
    val imagenUrl: String = "",
    val spotify: String = "",
    val instagram: String = "",
    val youtube: String = ""
)
