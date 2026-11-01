package com.example.bandamap.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.components.ConvocatoriaCard
import com.example.bandamap.model.Banda
import com.example.bandamap.model.Convocatoria
import com.example.bandamap.model.DatosPrueba

@Composable
fun ConvocatoriasScreen(onVolver: () -> Unit, onNuevaConvocatoria: () -> Unit) {
    val context = LocalContext.current

    // Guarda la convocatoria a la que se quiere postular. Si es null, el diálogo no se muestra.
    var convocatoriaSeleccionada by remember { mutableStateOf<Convocatoria?>(null) }

    Scaffold(
        topBar = { BarraSuperior("Convocatorias", onVolver) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevaConvocatoria) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Encuentra un escenario para tu banda.")
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                // Espacio al final para que el botón "+" no tape la última tarjeta
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(DatosPrueba.convocatorias, key = { it.id }) { convocatoria ->
                    ConvocatoriaCard(
                        convocatoria = convocatoria,
                        onPostular = { convocatoriaSeleccionada = convocatoria }
                    )
                }
            }
        }
    }

    convocatoriaSeleccionada?.let { convocatoria ->
        DialogoPostular(
            convocatoria = convocatoria,
            onElegirBanda = { banda ->
                val exito = DatosPrueba.postular(convocatoria.id, banda.id)
                val mensaje = if (exito) {
                    "${banda.nombre} postuló a la convocatoria"
                } else {
                    "${banda.nombre} ya había postulado"
                }
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                convocatoriaSeleccionada = null
            },
            onCancelar = { convocatoriaSeleccionada = null }
        )
    }
}

@Composable
private fun DialogoPostular(
    convocatoria: Convocatoria,
    onElegirBanda: (Banda) -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("¿Con qué banda postulas?") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(convocatoria.titulo, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                DatosPrueba.bandas.forEach { banda ->
                    TextButton(
                        onClick = { onElegirBanda(banda) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${banda.nombre} (${banda.genero})")
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