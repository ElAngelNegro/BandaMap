package com.example.bandamap.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BandaCard
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.viewmodel.BandasViewModel

@Composable
fun BandasScreen(
    viewModel: BandasViewModel,
    onBandaClick: (Int) -> Unit,
    onAgregar: () -> Unit,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()

    Scaffold(
        topBar = { BarraSuperior("Bandas", onVolver) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Icon(Icons.Default.Add, contentDescription = "Registrar banda")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = viewModel::onBusquedaChange,
                label = { Text("Buscar por nombre, género o ciudad") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${uiState.bandas.size} bandas para descubrir",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.bandas, key = { it.id }) { banda ->
                    BandaCard(banda = banda, onClick = { onBandaClick(banda.id) })
                }
            }
        }
    }
}
