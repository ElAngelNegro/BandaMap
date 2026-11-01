package com.example.bandamap.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.model.DatosPrueba

@Composable
fun NuevaConvocatoriaScreen(onVolver: () -> Unit) {
    val context = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var cupos by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var intentoGuardar by remember { mutableStateOf(false) }

    // Convierte el texto a número; si no es un número válido queda null
    val cuposNumero = cupos.toIntOrNull()
    val obligatorio = "Este campo es obligatorio"

    val tituloError = if (intentoGuardar && titulo.isBlank()) obligatorio else null
    val generoError = if (intentoGuardar && genero.isBlank()) obligatorio else null
    val fechaError = if (intentoGuardar && fecha.isBlank()) obligatorio else null
    val ciudadError = if (intentoGuardar && ciudad.isBlank()) obligatorio else null
    val cuposError = when {
        !intentoGuardar -> null
        cupos.isBlank() -> obligatorio
        cuposNumero == null || cuposNumero < 1 -> "Ingresa un número mayor a 0"
        else -> null
    }

    Scaffold(topBar = { BarraSuperior("Nueva convocatoria", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Publica una convocatoria para armar tu tocata. Los campos con * son obligatorios.")

            CampoFormulario("Título * (ej: Se buscan 2 bandas de metal)", titulo, { titulo = it }, tituloError)
            CampoFormulario("Género buscado *", genero, { genero = it }, generoError)
            CampoFormulario("Fecha * (ej: 15 nov 2026)", fecha, { fecha = it }, fechaError)
            CampoFormulario("Ciudad *", ciudad, { ciudad = it }, ciudadError)
            CampoFormulario("Cupos *", cupos, { cupos = it }, cuposError, teclado = KeyboardType.Number)
            CampoFormulario("Descripción", descripcion, { descripcion = it }, null, unaLinea = false)

            Button(
                onClick = {
                    intentoGuardar = true
                    val valido = titulo.isNotBlank() && genero.isNotBlank() &&
                            fecha.isNotBlank() && ciudad.isNotBlank() &&
                            cuposNumero != null && cuposNumero > 0
                    if (valido) {
                        DatosPrueba.agregarConvocatoria(
                            titulo.trim(), genero.trim(), fecha.trim(),
                            ciudad.trim(), cuposNumero ?: 1, descripcion.trim()
                        )
                        Toast.makeText(context, "Convocatoria publicada", Toast.LENGTH_SHORT).show()
                        onVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar")
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    mensajeError: String?,
    unaLinea: Boolean = true,
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        isError = mensajeError != null,
        supportingText = if (mensajeError != null) {
            { Text(mensajeError) }
        } else null,
        singleLine = unaLinea,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier.fillMaxWidth()
    )
}