package com.example.bandamap.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bandamap.components.FondoCarrusel
import com.example.bandamap.ui.theme.BandaMapTheme
import com.example.bandamap.ui.theme.RedLight
import com.example.bandamap.ui.theme.RedPrimary

@Composable
fun InicioScreen(
    onCerrarSesion: () -> Unit
) {
        Scaffold(
            containerColor = Color.Black
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeroTitulo()

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Próximas tocatas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                TocataCard("Sur de Acero", "14 nov 2026 · 20:00", "Sala Horizonte · Santiago")
                TocataCard("Puerto Ruido", "21 nov 2026 · 19:30", "Espacio Puerto · Valparaíso")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onCerrarSesion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(2.dp, Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Red.copy(alpha = 0.2f),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Cerrar sesión",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }
        }
}

@Composable
private fun HeroTitulo() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = RedPrimary.copy(alpha = 0.35f),
            border = BorderStroke(1.dp, RedLight.copy(alpha = 0.7f))
        ) {
            Text(
                text = "MÚSICA EN VIVO · CHILE",
                color = RedLight,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 44.sp,
                            letterSpacing = 2.sp
                        )
                    ) {
                        append("BANDA")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = RedLight,
                            fontWeight = FontWeight.Black,
                            fontSize = 44.sp,
                            letterSpacing = 2.sp
                        )
                    ) {
                        append("MAP")
                    }
                }
            )
        }

        Text(
            text = "Descubre bandas locales y conecta con la música de todo Chile",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f),
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun TocataCard(banda: String, fecha: String, lugar: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(banda, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(fecha, color = Color.DarkGray)
            Text(lugar, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InicioScreenPreview() {
    BandaMapTheme(darkTheme = true) {
        InicioScreen(
            onCerrarSesion = {}
        )
    }
}