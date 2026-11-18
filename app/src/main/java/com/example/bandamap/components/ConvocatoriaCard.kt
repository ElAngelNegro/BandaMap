package com.example.bandamap.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bandamap.model.Convocatoria

@Composable
fun ConvocatoriaCard(
    convocatoria: Convocatoria,
    onPostular: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = convocatoria.titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${convocatoria.genero} · ${convocatoria.ciudad}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Fecha: ${convocatoria.fecha}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            // La descripción es opcional: solo se muestra si tiene texto
            if (convocatoria.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = convocatoria.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cálculo de cupos disponibles
                val cuposDisponibles = convocatoria.cupos - convocatoria.postulantes.size
                
                Text(
                    text = when (cuposDisponibles) {
                        0 -> "Cupos llenos"
                        1 -> "1 cupo disponible"
                        else -> "$cuposDisponibles cupos disponibles"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = if (cuposDisponibles > 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                
                Button(
                    onClick = onPostular,
                    enabled = cuposDisponibles > 0
                ) {
                    Text("Postular")
                }
            }
        }
    }
}