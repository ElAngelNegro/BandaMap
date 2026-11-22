package com.example.bandamap.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.components.ConvocatoriaCard
import com.example.bandamap.model.Banda
import com.example.bandamap.model.Convocatoria
import com.example.bandamap.viewmodel.ConvocatoriasViewModel

@Composable
fun ConvocatoriasScreen(
    onVolver: () -> Unit,
    onNuevaConvocatoria: () -> Unit,
    viewModel: ConvocatoriasViewModel
) {
    val context = LocalContext.current

    // Observamos los estados desde el ViewModel
    val convocatorias by viewModel.convocatorias.collectAsState()
    val convocatoriaSeleccionada by viewModel.convocatoriaSeleccionada.collectAsState()
    val misBandas by viewModel.misBandas.collectAsState()

    // Cada vez que esta pantalla se muestra (también al volver), recargamos la lista
    LaunchedEffect(Unit) {
        viewModel.recargar()
    }

    Scaffold(
        topBar = { BarraSuperior("Convocatorias", onVolver) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevaConvocatoria) {
                // Usamos un ícono estándar de Material en lugar de texto "+"
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva Convocatoria")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Encuentra un escenario para tu banda.",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Reemplazamos LazyColumn por LazyVerticalGrid para soportar tablets
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 340.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                // Espacio al final para que el FAB no tape la última tarjeta
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

    // Diálogo de postulación
    convocatoriaSeleccionada?.let { convocatoria ->
        DialogoPostular(
            convocatoria = convocatoria,
            misBandas = misBandas,
            onElegirBanda = { banda ->
                // El ViewModel maneja la lógica y nos devuelve el mensaje a mostrar
                val mensaje = viewModel.postular(banda)
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            },
            onCancelar = { viewModel.seleccionarConvocatoria(null) }
        )
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