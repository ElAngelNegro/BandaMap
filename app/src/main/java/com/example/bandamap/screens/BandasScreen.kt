package com.example.bandamap.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BandaCard
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.model.DatosPrueba

@Composable
fun BandasScreen(onBandaClick: (Int) -> Unit, onVolver: () -> Unit) {
    var busqueda by remember { mutableStateOf("") }

    val bandasFiltradas = DatosPrueba.bandas.filter {
        it.nombre.contains(busqueda, ignoreCase = true)
    }

    Scaffold(topBar = { BarraSuperior("Bandas", onVolver) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar bandas") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${bandasFiltradas.size} bandas para descubrir",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bandasFiltradas, key = { it.id }) { banda ->
                    BandaCard(banda = banda, onClick = { onBandaClick(banda.id) })
                }
            }
        }
    }
}