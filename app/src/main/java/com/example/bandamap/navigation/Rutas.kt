package com.example.bandamap.navigation

object Rutas {
    const val INICIO = "inicio"
    const val BANDAS = "bandas"
    const val DETALLE = "detalle/{bandaId}"
    const val REGISTRAR = "registrar"
    const val CONVOCATORIAS = "convocatorias"
    const val NUEVA_CONVOCATORIA = "nueva_convocatoria"

    fun detalle(bandaId: Int) = "detalle/$bandaId"
}