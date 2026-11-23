package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bandamap.data.BandaRepository
import com.example.bandamap.model.Banda
import com.example.bandamap.model.DatosPrueba
import com.example.bandamap.model.Tocata
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleUiState(
    val banda: Banda? = null,
    val tocatas: List<Tocata> = emptyList(),
    val cargando: Boolean = true,
    val eliminando: Boolean = false,
    val eliminada: Boolean = false     // true cuando Room terminó de borrar
)

class DetalleViewModel(private val repository: BandaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    private var trabajoCarga: Job? = null

    // Observa la banda en Room: si se edita, el detalle se actualiza solo
    fun cargar(bandaId: Int) {
        trabajoCarga?.cancel()
        trabajoCarga = viewModelScope.launch {
            repository.obtenerPorId(bandaId).collect { banda ->
                _uiState.update {
                    it.copy(
                        banda = banda,
                        tocatas = if (banda != null) DatosPrueba.tocatasDeBanda(banda.id) else emptyList(),
                        cargando = false
                    )
                }
            }
        }
    }

    // DELETE
    fun eliminar() {
        val banda = _uiState.value.banda ?: return
        _uiState.update { it.copy(eliminando = true) }
        viewModelScope.launch {
            repository.eliminar(banda)
            _uiState.update { it.copy(eliminada = true) }
        }
    }
}
