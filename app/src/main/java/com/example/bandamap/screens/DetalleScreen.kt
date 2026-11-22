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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.viewmodel.DetalleViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetalleScreen(
    bandaId: Int,
    viewModel: DetalleViewModel,
    onEditar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val banda = uiState.banda
    val uriHandler = LocalUriHandler.current
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    // Carga la banda desde Room
    LaunchedEffect(bandaId) { viewModel.cargar(bandaId) }

    // Cuando Room terminó de borrar, volvemos a la lista
    LaunchedEffect(uiState.eliminada) {
        if (uiState.eliminada) onVolver()
    }

    // Diálogo para confirmar antes de eliminar
    if (mostrarConfirmacion && banda != null) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Eliminar banda") },
            text = { Text("¿Seguro que quieres eliminar \"${banda.nombre}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarConfirmacion = false
                    viewModel.eliminar()
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = banda?.nombre ?: "Banda",
                onVolver = onVolver,
                acciones = {
                    if (banda != null) {
                        IconButton(onClick = { onEditar(banda.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar banda")
                        }
                        IconButton(onClick = { mostrarConfirmacion = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar banda")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.cargando || uiState.eliminando) {
            // Mientras carga o se elimina no mostramos contenido
        } else if (banda == null) {
            Text(
                "Banda no encontrada",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            val tocatas = uiState.tocatas

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
