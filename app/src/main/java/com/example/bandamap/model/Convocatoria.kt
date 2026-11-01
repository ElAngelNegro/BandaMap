package com.example.bandamap.model

data class Convocatoria(
    val id: Int,
    val titulo: String,
    val genero: String,
    val fecha: String,
    val ciudad: String,
    val cupos: Int,
    val descripcion: String = "",
    val postulantes: List<Int> = emptyList()
)