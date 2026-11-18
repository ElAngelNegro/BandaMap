package com.example.bandamap.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.model.DatosPrueba

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetalleScreen(bandaId: Int, onVolver: () -> Unit) {
    // Obtenemos la banda y el handler de URI (para abrir links)
    val banda = DatosPrueba.buscarPorId(bandaId)
    val uriHandler = LocalUriHandler.current

    Scaffold(topBar = { BarraSuperior(banda?.nombre ?: "Banda", onVolver) }) { padding ->
        if (banda == null) {
            Text(
                "Banda no encontrada",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            val tocatas = DatosPrueba.tocatasDeBanda(banda.id)

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    // Habilitamos scroll vertical y animamos el tamaño por si cambia el contenido
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Imagen "Hero": si la banda tiene URL se carga con Coil;
                // si no, se muestra un recuadro de reemplazo
                if (banda.imagenUrl.isNotBlank()) {
                    AsyncImage(
                        model = banda.imagenUrl,
                        contentDescription = "Foto destacada de ${banda.nombre}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Sin imagen",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Información de cabecera
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = banda.nombre,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "${banda.genero} · ${banda.ciudad}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // Descripción
                Text(
                    text = banda.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Enlaces Sociales
                // Usamos FlowRow (layout adaptativo) por si los botones no caben en una línea
                // en pantallas pequeñas, saltarán automáticamente a la siguiente.
                // Solo mostramos la sección si la banda tiene al menos un enlace
                val tieneEnlaces = banda.spotify.isNotBlank() ||
                        banda.instagram.isNotBlank() || banda.youtube.isNotBlank()
                if (tieneEnlaces) {
                    Text("Escúchalos en", style = MaterialTheme.typography.titleMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BotonEnlace("Spotify", banda.spotify) { uriHandler.openUri(it) }
                        BotonEnlace("Instagram", banda.instagram) { uriHandler.openUri(it) }
                        BotonEnlace("YouTube", banda.youtube) { uriHandler.openUri(it) }
                    }
                }

                // Tarjeta de Tocatas
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Próximas tocatas", style = MaterialTheme.typography.titleMedium)

                        if (tocatas.isEmpty()) {
                            Text("Sin fechas programadas por el momento.")
                        } else {
                            tocatas.forEach { tocata ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = tocata.fecha,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = tocata.lugar,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
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
    // Usamos AnimatedVisibility para ocultar botones sin URL de forma fluida
    AnimatedVisibility(visible = url.isNotBlank(), modifier = modifier) {
        OutlinedButton(
            onClick = { runCatching { onAbrir(url) } }
        ) {
            Text(texto)
        }
    }
}
