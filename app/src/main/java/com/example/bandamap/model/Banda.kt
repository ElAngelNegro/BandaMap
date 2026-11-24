package com.example.bandamap.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bandas")
data class Banda(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val genero: String,
    val ciudad: String,
    val descripcion: String,
    val imagenUrl: String = "",
    val spotify: String = "",
    val instagram: String = "",
    val youtube: String = "",
    val esFavorito: Boolean = false
)