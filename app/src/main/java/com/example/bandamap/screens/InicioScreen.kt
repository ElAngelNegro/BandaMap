package com.example.bandamap.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BarraSuperior

@Composable
fun InicioScreen(
    onExplorarBandas: () -> Unit,
    onConvocatorias: () -> Unit,
    onRegistrarBanda: () -> Unit
) {
    Scaffold(topBar = { BarraSuperior("BandaMap") }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Descubre bandas locales y nacionales",
                style = MaterialTheme.typography.headlineSmall
            )
            Text("Conoce la música que se hace en Chile")

            Text("Próximas tocatas", style = MaterialTheme.typography.titleMedium)
            TocataCard("Sur de Acero", "14 nov 2026 · 20:00", "Sala Horizonte · Santiago")
            TocataCard("Puerto Ruido", "21 nov 2026 · 19:30", "Espacio Puerto · Valparaíso")

            Button(onClick = onExplorarBandas, modifier = Modifier.fillMaxWidth()) {
                Text("Explorar bandas")
            }
            OutlinedButton(onClick = onConvocatorias, modifier = Modifier.fillMaxWidth()) {
                Text("Convocatorias")
            }
            OutlinedButton(onClick = onRegistrarBanda, modifier = Modifier.fillMaxWidth()) {
                Text("Registrar mi banda")
            }
        }
    }
}

@Composable
private fun TocataCard(banda: String, fecha: String, lugar: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(banda, style = MaterialTheme.typography.titleMedium)
            Text(fecha)
            Text(lugar, style = MaterialTheme.typography.bodySmall)
        }
    }
}