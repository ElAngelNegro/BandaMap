package com.example.bandamap.navigation

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO_USUARIO = "registro_usuario"
    const val INICIO = "inicio"
    const val BANDAS = "bandas"
    const val DETALLE = "detalle/{bandaId}"
    const val REGISTRAR = "registrar"
    const val EDITAR = "editar/{bandaId}"
    const val CONVOCATORIAS = "convocatorias"
    const val NUEVA_CONVOCATORIA = "nueva_convocatoria"
    const val FAVORITOS = "favoritos"

    fun detalle(bandaId: Int) = "detalle/$bandaId"
    fun editar(bandaId: Int) = "editar/$bandaId"
}