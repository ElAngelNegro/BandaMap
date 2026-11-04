package com.example.bandamap.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.unit.dp
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.model.DatosPrueba

@Composable
fun RegistrarScreen(onVolver: () -> Unit) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var spotify by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }

    // Se activa al presionar Guardar; desde ahí se muestran los errores
    var intentoGuardar by remember { mutableStateOf(false) }

    val nombreError = intentoGuardar && nombre.isBlank()
    val generoError = intentoGuardar && genero.isBlank()
    val ciudadError = intentoGuardar && ciudad.isBlank()

    Scaffold(topBar = { BarraSuperior("Registrar banda", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Comparte tu música en BandaMap. Los campos con * son obligatorios.")

            CampoTexto("Nombre de la banda *", nombre, { nombre = it }, nombreError)

            CampoSeleccionGenero(
                generoSeleccionado = genero,
                onGeneroChange = { genero = it },
                error = generoError
            )

            CampoTexto("Ciudad *", ciudad, { ciudad = it }, ciudadError)
            CampoTexto("Descripción", descripcion, { descripcion = it }, false, unaLinea = false)
            CampoTexto("Enlace Spotify", spotify, { spotify = it }, false)
            CampoTexto("Enlace Instagram", instagram, { instagram = it }, false)

            Button(
                onClick = {
                    intentoGuardar = true
                    if (nombre.isNotBlank() && genero.isNotBlank() && ciudad.isNotBlank()) {
                        DatosPrueba.agregarBanda(
                            nombre.trim(), genero.trim(), ciudad.trim(),
                            descripcion.trim(), spotify.trim(), instagram.trim()
                        )
                        Toast.makeText(context, "Banda registrada", Toast.LENGTH_SHORT).show()
                        onVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoSeleccionGenero(
    generoSeleccionado: String,
    onGeneroChange: (String) -> Unit,
    error: Boolean
) {
    var expandido by remember { mutableStateOf(false) }
    val opcionesGenero = listOf(
        "Rock",
        "Metal",
        "Pop",
        "Indie",
        "Hip-Hop / Rap",
        "Folk / Acústico",
        "Punk",
        "Jazz / Blues",
        "Electrónica",
        "Otro"
    )

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = generoSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Género *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            isError = error,
            supportingText = if (error) {
                { Text("Este campo es obligatorio") }
            } else null,
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opcionesGenero.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onGeneroChange(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CampoTexto(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    error: Boolean,
    unaLinea: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        isError = error,
        supportingText = if (error) {
            { Text("Este campo es obligatorio") }
        } else null,
        singleLine = unaLinea,
        modifier = Modifier.fillMaxWidth()
    )
}