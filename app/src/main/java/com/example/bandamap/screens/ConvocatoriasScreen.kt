package com.example.bandamap.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.components.ConvocatoriaCard
import com.example.bandamap.components.EncabezadoSeccion
import com.example.bandamap.components.FondoFijo
import com.example.bandamap.model.Banda
import com.example.bandamap.model.Convocatoria
import com.example.bandamap.viewmodel.ConvocatoriasViewModel

@Composable
fun ConvocatoriasScreen(
    onVolver: () -> Unit,
    onNuevaConvocatoria: () -> Unit,
    onMostrarMensaje: (String) -> Unit,
    viewModel: ConvocatoriasViewModel = viewModel()
) {
    val convocatorias by viewModel.convocatorias.collectAsState()
    val misBandas by viewModel.misBandas.collectAsState()
    val convocatoriaSeleccionada by viewModel.convocatoriaSeleccionada.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.recargar()
    }

    FondoFijo(imagen = com.example.bandamap.R.drawable.fondo_4) {
        Scaffold(
            topBar = { BarraSuperior("Convocatorias", onVolver) },
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(onClick = onNuevaConvocatoria) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva Convocatoria")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                EncabezadoSeccion(
                    badge = "Comunidad Activa",
                    titulo = "Convocatorias",
                    subtitulo = "Encuentra fechas, festivales y escenarios disponibles para tocar."
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 340.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(convocatorias, key = { it.id }) { convocatoria ->
                        ConvocatoriaCard(
                            convocatoria = convocatoria,
                            onPostular = { viewModel.seleccionarConvocatoria(convocatoria) }
                        )
                    }
                }
            }
        }

        convocatoriaSeleccionada?.let { convocatoria ->
            DialogoPostular(
                convocatoria = convocatoria,
                misBandas = misBandas,
                onElegirBanda = { banda ->
                    val mensaje = viewModel.postular(banda)
                    onMostrarMensaje(mensaje)
                },
                onCancelar = { viewModel.seleccionarConvocatoria(null) }
            )
        }
    }
}

@Composable
private fun DialogoPostular(
    convocatoria: Convocatoria,
    misBandas: List<Banda>,
    onElegirBanda: (Banda) -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("¿Con qué banda postulas?") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Postulando a: ${convocatoria.titulo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (misBandas.isEmpty()) {
                    Text("No tienes bandas registradas aún.")
                } else {
                    misBandas.forEach { banda ->
                        TextButton(
                            onClick = { onElegirBanda(banda) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${banda.nombre} (${banda.genero})")
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
