package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bandamap.model.DatosPrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Representa todo el estado del formulario en un solo objeto inmutable
data class RegistrarFormState(
    val nombre: String = "",
    val genero: String = "",
    val ciudad: String = "",
    val descripcion: String = "",
    val spotify: String = "",
    val instagram: String = "",
    val intentoGuardar: Boolean = false
) {
    val nombreError get() = intentoGuardar && nombre.isBlank()
    val generoError get() = intentoGuardar && genero.isBlank()
    val ciudadError get() = intentoGuardar && ciudad.isBlank()
    val esValido get() = nombre.isNotBlank() && genero.isNotBlank() && ciudad.isNotBlank()
}

class RegistrarViewModel : ViewModel() {

    private val _formState = MutableStateFlow(RegistrarFormState())
    val formState: StateFlow<RegistrarFormState> = _formState.asStateFlow()

    // Funciones para actualizar cada campo
    fun onNombreChange(valor: String) = _formState.update { it.copy(nombre = valor) }
    fun onGeneroChange(valor: String) = _formState.update { it.copy(genero = valor) }
    fun onCiudadChange(valor: String) = _formState.update { it.copy(ciudad = valor) }
    fun onDescripcionChange(valor: String) = _formState.update { it.copy(descripcion = valor) }
    fun onSpotifyChange(valor: String) = _formState.update { it.copy(spotify = valor) }
    fun onInstagramChange(valor: String) = _formState.update { it.copy(instagram = valor) }

    // Intenta guardar y retorna true si fue exitoso, false si faltan campos
    fun guardarBanda(): Boolean {
        _formState.update { it.copy(intentoGuardar = true) }
        
        val currentState = _formState.value
        
        if (currentState.esValido) {
            DatosPrueba.agregarBanda(
                nombre = currentState.nombre.trim(),
                genero = currentState.genero.trim(),
                ciudad = currentState.ciudad.trim(),
                descripcion = currentState.descripcion.trim(),
                spotify = currentState.spotify.trim(),
                instagram = currentState.instagram.trim()
            )
            return true
        }
        return false
    }
}