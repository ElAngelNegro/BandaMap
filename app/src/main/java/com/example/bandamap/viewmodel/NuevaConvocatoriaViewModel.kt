package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bandamap.model.DatosPrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado inmutable para el formulario de Nueva Convocatoria
data class NuevaConvocatoriaFormState(
    val titulo: String = "",
    val genero: String = "",
    val fecha: String = "",
    val ciudad: String = "",
    val cupos: String = "",
    val descripcion: String = "",
    val intentoGuardar: Boolean = false
) {
    val cuposNumero get() = cupos.toIntOrNull()
    
    val tituloError get() = intentoGuardar && titulo.isBlank()
    val generoError get() = intentoGuardar && genero.isBlank()
    val fechaError get() = intentoGuardar && fecha.isBlank()
    val ciudadError get() = intentoGuardar && ciudad.isBlank()
    val cuposError get() = intentoGuardar && (cupos.isBlank() || cuposNumero == null || cuposNumero!! < 1)
    
    val esValido get() = titulo.isNotBlank() && genero.isNotBlank() && 
                         fecha.isNotBlank() && ciudad.isNotBlank() && 
                         cuposNumero != null && cuposNumero!! > 0
}

class NuevaConvocatoriaViewModel : ViewModel() {

    private val _formState = MutableStateFlow(NuevaConvocatoriaFormState())
    val formState: StateFlow<NuevaConvocatoriaFormState> = _formState.asStateFlow()

    fun onTituloChange(valor: String) = _formState.update { it.copy(titulo = valor) }
    fun onGeneroChange(valor: String) = _formState.update { it.copy(genero = valor) }
    fun onFechaChange(valor: String) = _formState.update { it.copy(fecha = valor) }
    fun onCiudadChange(valor: String) = _formState.update { it.copy(ciudad = valor) }
    fun onCuposChange(valor: String) = _formState.update { it.copy(cupos = valor) }
    fun onDescripcionChange(valor: String) = _formState.update { it.copy(descripcion = valor) }

    // Retorna true si se guardó con éxito
    fun guardarConvocatoria(): Boolean {
        _formState.update { it.copy(intentoGuardar = true) }
        
        val currentState = _formState.value
        
        if (currentState.esValido) {
            DatosPrueba.agregarConvocatoria(
                titulo = currentState.titulo.trim(),
                genero = currentState.genero.trim(),
                fecha = currentState.fecha.trim(),
                ciudad = currentState.ciudad.trim(),
                cupos = currentState.cuposNumero ?: 1,
                descripcion = currentState.descripcion.trim()
            )
            return true
        }
        return false
    }
}