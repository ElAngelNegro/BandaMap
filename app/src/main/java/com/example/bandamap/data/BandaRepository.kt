package com.example.bandamap.data

import com.example.bandamap.model.Banda
import kotlinx.coroutines.flow.Flow

// REPOSITORY: intermediario entre los ViewModels y el DAO.
// La UI nunca toca el DAO directamente: siempre pasa por aquí.
class BandaRepository(private val bandaDao: BandaDao) {

    val bandas: Flow<List<Banda>> = bandaDao.obtenerTodas()

    fun obtenerPorId(id: Int): Flow<Banda?> = bandaDao.obtenerPorId(id)

    suspend fun insertar(banda: Banda) = bandaDao.insertar(banda)

    suspend fun actualizar(banda: Banda) = bandaDao.actualizar(banda)

    suspend fun eliminar(banda: Banda) = bandaDao.eliminar(banda)
}
