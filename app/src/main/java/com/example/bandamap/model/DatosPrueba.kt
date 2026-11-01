package com.example.bandamap.model

import androidx.compose.runtime.mutableStateListOf

object DatosPrueba {

    val bandas = mutableStateListOf(
        Banda(1, "Sur de Acero", "Metal", "Santiago",
            "Cuarteto de metal de Santiago. Riffs intensos y letras sobre la vida en la ciudad.",
            spotify = "https://open.spotify.com",
            instagram = "https://www.instagram.com",
            youtube = "https://www.youtube.com"),
        Banda(2, "Puerto Ruido", "Rock", "Valparaíso",
            "Rock porteño con influencias de los 90.",
            spotify = "https://open.spotify.com",
            instagram = "https://www.instagram.com"),
        Banda(3, "Último Andén", "Punk", "Concepción",
            "Punk rápido y directo desde el sur.",
            instagram = "https://www.instagram.com",
            youtube = "https://www.youtube.com"),
        Banda(4, "Luz Austral", "Pop", "Valdivia",
            "Pop alternativo con sintetizadores y voces suaves.",
            spotify = "https://open.spotify.com")
    )

    val tocatas = listOf(
        Tocata(1, 1, "14 nov 2026 · 20:00", "Sala Horizonte · Santiago"),
        Tocata(2, 1, "28 nov 2026 · 19:00", "Sala del Sur · Concepción"),
        Tocata(3, 2, "21 nov 2026 · 19:30", "Espacio Puerto · Valparaíso"),
        Tocata(4, 3, "5 dic 2026 · 21:00", "Bar La Estación · Concepción")
    )

    val convocatorias = mutableStateListOf(
        Convocatoria(1, "Se buscan 2 bandas de metal", "Metal", "14 nov 2026", "Santiago", 2,
            "Tocata en Sala Horizonte. Cada banda toca 30 minutos."),
        Convocatoria(2, "Se busca 1 banda de rock", "Rock", "21 nov 2026", "Valparaíso", 1,
            "Fecha de rock porteño, con backline incluido."),
        Convocatoria(3, "Se buscan 3 bandas de punk", "Punk", "5 dic 2026", "Concepción", 3,
            "Festival punk autogestionado.")
    )

    fun buscarPorId(id: Int): Banda? = bandas.find { it.id == id }

    fun tocatasDeBanda(bandaId: Int): List<Tocata> =
        tocatas.filter { it.bandaId == bandaId }

    fun agregarBanda(
        nombre: String,
        genero: String,
        ciudad: String,
        descripcion: String,
        spotify: String,
        instagram: String
    ) {
        val nuevoId = (bandas.maxOfOrNull { it.id } ?: 0) + 1
        bandas.add(
            Banda(nuevoId, nombre, genero, ciudad, descripcion,
                spotify = spotify, instagram = instagram)
        )
    }

    fun agregarConvocatoria(
        titulo: String,
        genero: String,
        fecha: String,
        ciudad: String,
        cupos: Int,
        descripcion: String
    ) {
        val nuevoId = (convocatorias.maxOfOrNull { it.id } ?: 0) + 1
        convocatorias.add(
            Convocatoria(nuevoId, titulo, genero, fecha, ciudad, cupos, descripcion)
        )
    }

    // Devuelve true si la postulación se realizó, false si no había cupo o ya había postulado
    fun postular(convocatoriaId: Int, bandaId: Int): Boolean {
        val indice = convocatorias.indexOfFirst { it.id == convocatoriaId }
        if (indice == -1) return false

        val convocatoria = convocatorias[indice]
        val yaPostulo = bandaId in convocatoria.postulantes
        val sinCupos = convocatoria.postulantes.size >= convocatoria.cupos
        if (yaPostulo || sinCupos) return false

        convocatorias[indice] = convocatoria.copy(
            postulantes = convocatoria.postulantes + bandaId
        )
        return true
    }
}