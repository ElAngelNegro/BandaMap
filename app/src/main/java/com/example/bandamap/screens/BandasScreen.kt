package com.example.bandamap.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BandaCard
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.components.EncabezadoSeccion
import com.example.bandamap.components.EstadoVacio
import com.example.bandamap.components.FondoFijo
import com.example.bandamap.model.DatosPrueba
import com.example.bandamap.viewmodel.BandasViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun BandasScreen(
    viewModel: BandasViewModel,
    onBandaClick: (Int) -> Unit,
    onAgregar: () -> Unit,
    onVolver: () -> Unit
) {
    val busqueda by viewModel.busqueda.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val bandasFiltradas = uiState.bandas

    FondoFijo(imagen = com.example.bandamap.R.drawable.fondo_3) {
        Scaffold(
            topBar = { BarraSuperior("Bandas", onVolver) },
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(onClick = onAgregar) {
                    Icon(Icons.Default.Add, contentDescription = "Registrar banda")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                EncabezadoSeccion(
                    badge = "Catálogo Nacional",
                    titulo = "Bandas y Artistas",
                    subtitulo = "Explora proyectos musicales de Chile por nombre, género o ciudad."
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = busqueda,
                    onValueChange = viewModel::onBusquedaChange,
                    label = { Text("Buscar bandas, géneros o ciudades...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "${bandasFiltradas.size} bandas encontradas",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                if (bandasFiltradas.isEmpty()) {
                    EstadoVacio(
                        icon = Icons.Default.Search,
                        titulo = "Sin resultados",
                        mensaje = "No se encontró ninguna banda que coincida con \"$busqueda\". Intenta con otro término."
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(bandasFiltradas, key = { it.id }) { banda ->
                            BandaCard(
                                banda = banda,
                                onClick = { onBandaClick(banda.id) },
                                onFavoriteClick = { viewModel.toggleFavorito(banda) }
                            )
                        }
                    }
                }
            }
        }
    }
}
