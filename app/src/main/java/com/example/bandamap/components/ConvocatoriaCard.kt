package com.example.bandamap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bandamap.model.Convocatoria

@Composable
fun ConvocatoriaCard(convocatoria: Convocatoria, onPostular: () -> Unit) {
    val disponibles = convocatoria.cupos - convocatoria.postulantes.size

    val textoCupos = when (disponibles) {
        0 -> "Sin cupos"
        1 -> "1 cupo disponible"
        else -> "$disponibles cupos disponibles"
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(convocatoria.titulo, style = MaterialTheme.typography.titleMedium)
            Text("${convocatoria.fecha} · ${convocatoria.ciudad}")
            Text("Género: ${convocatoria.genero}", style = MaterialTheme.typography.bodySmall)
            if (convocatoria.descripcion.isNotBlank()) {
                Text(convocatoria.descripcion, style = MaterialTheme.typography.bodySmall)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(textoCupos, modifier = Modifier.weight(1f))
                OutlinedButton(onClick = onPostular, enabled = disponibles > 0) {
                    Text("Postular")
                }
            }
        }
    }
}