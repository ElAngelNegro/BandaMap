package com.example.bandamap.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BandaCard
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.components.EncabezadoSeccion
import com.example.bandamap.components.EstadoVacio
import com.example.bandamap.components.FondoFijo
import com.example.bandamap.model.DatosPrueba

@Composable
fun FavoritosScreen(
    onBandaClick: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val bandasFavoritas = DatosPrueba.bandas.filter { it.esFavorito }

    FondoFijo(imagen = com.example.bandamap.R.drawable.fondo_2) {
        Scaffold(
            topBar = { BarraSuperior("Favoritos", onVolver) },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                EncabezadoSeccion(
                    badge = "Mis Preferidas",
                    titulo = "Bandas Favoritas",
                    subtitulo = "Accede rápidamente a las bandas que has marcado como favoritas."
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (bandasFavoritas.isEmpty()) {
                    EstadoVacio(
                        icon = Icons.Default.FavoriteBorder,
                        titulo = "Sin bandas favoritas",
                        mensaje = "Aún no has guardado ninguna banda. Explora el catálogo y marca tus favoritas tocando el ícono de corazón en las tarjetas.",
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(bandasFavoritas, key = { it.id }) { banda ->
                            BandaCard(
                                banda = banda,
                                onClick = { onBandaClick(banda.id) },
                                onFavoriteClick = { DatosPrueba.toggleFavorito(banda.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
