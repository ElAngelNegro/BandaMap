package com.example.bandamap.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.model.DatosPrueba

@Composable
fun DetalleScreen(bandaId: Int, onVolver: () -> Unit) {
    val banda = DatosPrueba.buscarPorId(bandaId)
    val uriHandler = LocalUriHandler.current

    Scaffold(topBar = { BarraSuperior(banda?.nombre ?: "Banda", onVolver) }) { padding ->
        if (banda == null) {
            Text("Banda no encontrada", modifier = Modifier.padding(padding).padding(16.dp))
        } else {
            val tocatas = DatosPrueba.tocatasDeBanda(banda.id)

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Recuadro que representa la foto de la banda
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Imagen de la banda")
                }

                Text(
                    "${banda.genero} · ${banda.ciudad}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(banda.descripcion)

                Text("Escúchalos en", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotonEnlace("Spotify", banda.spotify, Modifier.weight(1f)) {
                        uriHandler.openUri(it)
                    }
                    BotonEnlace("Instagram", banda.instagram, Modifier.weight(1f)) {
                        uriHandler.openUri(it)
                    }
                    BotonEnlace("YouTube", banda.youtube, Modifier.weight(1f)) {
                        uriHandler.openUri(it)
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Próximas tocatas", style = MaterialTheme.typography.titleMedium)
                        if (tocatas.isEmpty()) {
                            Text("Sin fechas programadas")
                        } else {
                            tocatas.forEach { tocata ->
                                Column {
                                    Text(tocata.fecha)
                                    Text(tocata.lugar, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BotonEnlace(
    texto: String,
    url: String,
    modifier: Modifier = Modifier,
    onAbrir: (String) -> Unit
) {
    OutlinedButton(
        onClick = { runCatching { onAbrir(url) } },
        enabled = url.isNotBlank(),
        modifier = modifier
    ) {
        Text(texto, maxLines = 1)
    }
}