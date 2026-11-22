package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bandamap.data.BandaRepository

// VIEWMODEL FACTORY: los ViewModels necesitan el Repository en su constructor,
// y Android no sabe crearlos solo. Esta clase le indica cómo construir cada uno.
class BandaMapViewModelFactory(
    private val repository: BandaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BandasViewModel::class.java) ->
                BandasViewModel(repository) as T
            modelClass.isAssignableFrom(DetalleViewModel::class.java) ->
                DetalleViewModel(repository) as T
            modelClass.isAssignableFrom(RegistrarViewModel::class.java) ->
                RegistrarViewModel(repository) as T
            modelClass.isAssignableFrom(ConvocatoriasViewModel::class.java) ->
                ConvocatoriasViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
