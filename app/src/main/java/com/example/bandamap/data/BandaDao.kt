package com.example.bandamap.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bandamap.model.Banda
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object): define las consultas SQL a la tabla "bandas".
// Room genera el código real de estas funciones al compilar.
@Dao
interface BandaDao {

    // READ: todas las bandas ordenadas por nombre.
    // Devuelve un Flow, así la UI se actualiza sola cuando la tabla cambia.
    @Query("SELECT * FROM bandas ORDER BY nombre ASC")
    fun obtenerTodas(): Flow<List<Banda>>

    // READ: una banda por su id
    @Query("SELECT * FROM bandas WHERE id = :id")
    fun obtenerPorId(id: Int): Flow<Banda?>

    // CREATE
    @Insert
    suspend fun insertar(banda: Banda)

    // CREATE (varias a la vez, para los datos iniciales)
    // IGNORE: si una banda de ejemplo ya existe, no la duplica ni se cae
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(bandas: List<Banda>)

    // UPDATE
    @Update
    suspend fun actualizar(banda: Banda)

    // DELETE
    @Delete
    suspend fun eliminar(banda: Banda)
}
