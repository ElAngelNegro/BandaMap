package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bandamap.data.BandaRepository
import com.example.bandamap.model.Banda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Estado de la pantalla de lista de bandas
data class BandasUiState(
    val bandas: List<Banda> = emptyList(),
    val cargando: Boolean = true
)

class BandasViewModel(repository: BandaRepository) : ViewModel() {

    // Texto del buscador (función propia: búsqueda por nombre, género o ciudad)
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // Combina la lista de Room con el texto de búsqueda.
    // Cada vez que cambia la tabla o el texto, se recalcula la lista filtrada.
    val uiState: StateFlow<BandasUiState> =
        combine(repository.bandas, _busqueda) { lista, texto ->
            val filtradas = if (texto.isBlank()) lista else lista.filter {
                it.nombre.contains(texto, ignoreCase = true) ||
                    it.genero.contains(texto, ignoreCase = true) ||
                    it.ciudad.contains(texto, ignoreCase = true)
            }
            BandasUiState(bandas = filtradas, cargando = false)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BandasUiState())

    fun onBusquedaChange(texto: String) {
        _busqueda.value = texto
    }
}
