package com.example.bandamap.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bandamap.ui.theme.BandaMapTheme

@Composable
fun EncabezadoSeccion(
    badge: String,
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
        ) {
            Text(
                text = badge.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.example.bandamap.R.font.pastor_of_muppets)),
            color = MaterialTheme.colorScheme.onBackground
        )

        if (!subtitulo.isNullOrBlank()) {
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EncabezadoSeccionPreview() {
    BandaMapTheme {
        EncabezadoSeccion(
            badge = "Catálogo Nacional",
            titulo = "Bandas y Artistas",
            subtitulo = "Explora proyectos musicales de todo Chile y conoce sus próximas fechas."
        )
    }
}
