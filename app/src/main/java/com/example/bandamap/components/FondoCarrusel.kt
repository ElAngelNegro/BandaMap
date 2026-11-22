package com.example.bandamap.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.bandamap.R
import kotlinx.coroutines.delay

enum class TipoTransicionFondo {
    DESLIZAMIENTO,
    FUNDIDO
}

/**
 * Lista de imágenes por defecto tomadas de res/drawable.
 */
val FondosPorDefecto = listOf(
    R.drawable.fondo_1,
    R.drawable.fondo_2,
    R.drawable.fondo_3,
    R.drawable.fondo_4,
    R.drawable.fondo_5,
    R.drawable.fondo_6,
    R.drawable.fondo_7
)

/**
 * Componente que renderiza un carrusel de imágenes automático como fondo de pantalla,
 * con una capa oscura/gradiente para garantizar la legibilidad del contenido superpuesto.
 */
@Composable
fun FondoCarrusel(
    modifier: Modifier = Modifier,
    @DrawableRes imagenes: List<Int> = FondosPorDefecto,
    tipoTransicion: TipoTransicionFondo = TipoTransicionFondo.FUNDIDO,
    tiempoCambioMs: Long = 4000L,
    duracionAnimacionMs: Int = 1200,
    overlayGradiente: Brush = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.65f),
            Color.Black.copy(alpha = 0.45f),
            Color.Black.copy(alpha = 0.85f)
        )
    ),
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (imagenes.isNotEmpty()) {
            when (tipoTransicion) {
                TipoTransicionFondo.DESLIZAMIENTO -> {
                    CarruselDeslizamiento(
                        imagenes = imagenes,
                        tiempoCambioMs = tiempoCambioMs,
                        duracionAnimacionMs = duracionAnimacionMs
                    )
                }

                TipoTransicionFondo.FUNDIDO -> {
                    CarruselFundido(
                        imagenes = imagenes,
                        tiempoCambioMs = tiempoCambioMs,
                        duracionAnimacionMs = duracionAnimacionMs
                    )
                }
            }
        }

        // Capa de degradado / oscurecimiento para legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayGradiente)
        )

        // Contenido frontal superpuesto
        content()
    }
}

@Composable
private fun CarruselDeslizamiento(
    @DrawableRes imagenes: List<Int>,
    tiempoCambioMs: Long,
    duracionAnimacionMs: Int
) {
    val pagerState = rememberPagerState(pageCount = { imagenes.size })

    LaunchedEffect(pagerState, imagenes.size) {
        while (true) {
            delay(tiempoCambioMs)
            val siguiente = (pagerState.currentPage + 1) % imagenes.size
            pagerState.animateScrollToPage(
                page = siguiente,
                animationSpec = tween(
                    durationMillis = duracionAnimacionMs,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        Image(
            painter = painterResource(id = imagenes[page]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CarruselFundido(
    @DrawableRes imagenes: List<Int>,
    tiempoCambioMs: Long,
    duracionAnimacionMs: Int
) {
    var indiceActual by remember { mutableIntStateOf(0) }

    LaunchedEffect(imagenes.size) {
        while (true) {
            delay(tiempoCambioMs)
            indiceActual = (indiceActual + 1) % imagenes.size
        }
    }

    Crossfade(
        targetState = indiceActual,
        animationSpec = tween(durationMillis = duracionAnimacionMs),
        label = "TransicionFondoCarrusel"
    ) { indice ->
        Image(
            painter = painterResource(id = imagenes[indice]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun FondoCarruselPreview() {
    FondoCarrusel {
        androidx.compose.material3.Text(
            text = "BandaMap",
            color = Color.White,
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
