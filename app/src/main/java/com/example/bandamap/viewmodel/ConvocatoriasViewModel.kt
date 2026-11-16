package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bandamap.model.Banda
import com.example.bandamap.model.Convocatoria
import com.example.bandamap.model.DatosPrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConvocatoriasViewModel : ViewModel() {

    // Lista de convocatorias
    private val _convocatorias = MutableStateFlow(DatosPrueba.convocatorias.toList())
    val convocatorias: StateFlow<List<Convocatoria>> = _convocatorias.asStateFlow()

    // Estado para el diálogo (la convocatoria que se seleccionó para postular)
    private val _convocatoriaSeleccionada = MutableStateFlow<Convocatoria?>(null)
    val convocatoriaSeleccionada: StateFlow<Convocatoria?> = _convocatoriaSeleccionada.asStateFlow()

    // Lista de bandas del usuario (para elegir con cuál postular)
    val misBandas = DatosPrueba.bandas

    fun seleccionarConvocatoria(convocatoria: Convocatoria?) {
        _convocatoriaSeleccionada.value = convocatoria
    }

    // Retorna el mensaje de éxito o error
    fun postular(banda: Banda): String {
        val convocatoriaActual = _convocatoriaSeleccionada.value ?: return "Error interno"
        
        val exito = DatosPrueba.postular(convocatoriaActual.id, banda.id)
        
        // Actualizamos la lista local para reflejar los cupos tomados si hubo éxito
        if (exito) {
            _convocatorias.value = DatosPrueba.convocatorias.toList()
        }
        
        // Cerramos el diálogo
        _convocatoriaSeleccionada.value = null

        return if (exito) {
            "${banda.nombre} postuló con éxito"
        } else {
            "${banda.nombre} ya había postulado o no hay cupos"
        }
    }
}