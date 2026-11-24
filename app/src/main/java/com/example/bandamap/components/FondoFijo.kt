package com.example.bandamap.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.bandamap.R

@Composable
fun FondoFijo(
    modifier: Modifier = Modifier,
    @DrawableRes imagen: Int = R.drawable.fondo_2,
    overlayGradiente: Brush = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.70f),
            Color.Black.copy(alpha = 0.50f),
            Color.Black.copy(alpha = 0.90f)
        )
    ),
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayGradiente)
        )

        content()
    }
}
