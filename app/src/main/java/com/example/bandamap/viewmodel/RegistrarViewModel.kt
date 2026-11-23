package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bandamap.data.BandaRepository
import com.example.bandamap.model.Banda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Representa todo el estado del formulario en un solo objeto inmutable
data class RegistrarFormState(
    val nombre: String = "",
    val genero: String = "",
    val ciudad: String = "",
    val descripcion: String = "",
    val spotify: String = "",
    val instagram: String = "",
    val intentoGuardar: Boolean = false,
    val esEdicion: Boolean = false,     // true si estamos editando una banda existente
    val guardado: Boolean = false       // true cuando Room terminó de guardar
) {
    val nombreError get() = intentoGuardar && nombre.isBlank()
    val generoError get() = intentoGuardar && genero.isBlank()
    val ciudadError get() = intentoGuardar && ciudad.isBlank()
    val esValido get() = nombre.isNotBlank() && genero.isNotBlank() && ciudad.isNotBlank()
}

// Sirve para CREAR (formulario vacío) y para EDITAR (formulario con datos de la banda)
class RegistrarViewModel(private val repository: BandaRepository) : ViewModel() {

    private val _formState = MutableStateFlow(RegistrarFormState())
    val formState: StateFlow<RegistrarFormState> = _formState.asStateFlow()

    // Banda original cuando se edita (para conservar id, imagen y YouTube)
    private var bandaEditando: Banda? = null

    // Funciones para actualizar cada campo
    fun onNombreChange(valor: String) = _formState.update { it.copy(nombre = valor) }
    fun onGeneroChange(valor: String) = _formState.update { it.copy(genero = valor) }
    fun onCiudadChange(valor: String) = _formState.update { it.copy(ciudad = valor) }
    fun onDescripcionChange(valor: String) = _formState.update { it.copy(descripcion = valor) }
    fun onSpotifyChange(valor: String) = _formState.update { it.copy(spotify = valor) }
    fun onInstagramChange(valor: String) = _formState.update { it.copy(instagram = valor) }

    // Modo edición: carga la banda desde Room y llena el formulario
    fun cargarBanda(id: Int) {
        if (bandaEditando?.id == id) return   // ya estaba cargada
        viewModelScope.launch {
            val banda = repository.obtenerPorId(id).first() ?: return@launch
            bandaEditando = banda
            _formState.value = RegistrarFormState(
                nombre = banda.nombre,
                genero = banda.genero,
                ciudad = banda.ciudad,
                descripcion = banda.descripcion,
                spotify = banda.spotify,
                instagram = banda.instagram,
                esEdicion = true
            )
        }
    }

    // Valida y guarda. Retorna false si faltan campos obligatorios.
    // Cuando Room termina, formState.guardado pasa a true y la pantalla vuelve atrás.
    fun guardarBanda(): Boolean {
        _formState.update { it.copy(intentoGuardar = true) }
        val estado = _formState.value
        if (!estado.esValido) return false

        viewModelScope.launch {
            val original = bandaEditando
            if (original != null) {
                // UPDATE
                repository.actualizar(
                    original.copy(
                        nombre = estado.nombre.trim(),
                        genero = estado.genero.trim(),
                        ciudad = estado.ciudad.trim(),
                        descripcion = estado.descripcion.trim(),
                        spotify = estado.spotify.trim(),
                        instagram = estado.instagram.trim()
                    )
                )
            } else {
                // CREATE
                repository.insertar(
                    Banda(
                        nombre = estado.nombre.trim(),
                        genero = estado.genero.trim(),
                        ciudad = estado.ciudad.trim(),
                        descripcion = estado.descripcion.trim(),
                        spotify = estado.spotify.trim(),
                        instagram = estado.instagram.trim()
                    )
                )
            }
            _formState.update { it.copy(guardado = true) }
        }
        return true
    }
}
