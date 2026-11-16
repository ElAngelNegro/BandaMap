package com.example.bandamap.model

data class Banda(
    val id: Int,
    val nombre: String,
    val genero: String,
    val ciudad: String,
    val descripcion: String,
    val imagenUrl: String = "",
    val spotify: String = "",
    val instagram: String = "",
    val youtube: String = ""
)