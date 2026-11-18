package com.example.bandamap.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.viewmodel.RegistrarViewModel

@Composable
fun RegistrarScreen(
    onVolver: () -> Unit,
    viewModel: RegistrarViewModel = viewModel()
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsState()

    Scaffold(topBar = { BarraSuperior("Registrar banda", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Comparte tu música en BandaMap",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Los campos marcados con * son obligatorios.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoTextoIcono(
                etiqueta = "Nombre de la banda *",
                valor = formState.nombre,
                onCambio = viewModel::onNombreChange,
                icono = Icons.Default.Star,
                error = formState.nombreError
            )

            CampoSeleccionGenero(
                generoSeleccionado = formState.genero,
                onGeneroChange = viewModel::onGeneroChange,
                error = formState.generoError
            )

            CampoTextoIcono(
                etiqueta = "Ciudad *",
                valor = formState.ciudad,
                onCambio = viewModel::onCiudadChange,
                icono = Icons.Default.LocationOn,
                error = formState.ciudadError
            )

            CampoTextoIcono(
                etiqueta = "Descripción",
                valor = formState.descripcion,
                onCambio = viewModel::onDescripcionChange,
                icono = Icons.Default.Info,
                error = false,
                unaLinea = false
            )

            CampoTextoIcono(
                etiqueta = "Enlace Spotify",
                valor = formState.spotify,
                onCambio = viewModel::onSpotifyChange,
                icono = Icons.Default.PlayArrow,
                error = false,
                esEnlace = true
            )

            CampoTextoIcono(
                etiqueta = "Enlace Instagram",
                valor = formState.instagram,
                onCambio = viewModel::onInstagramChange,
                icono = Icons.Default.Person,
                error = false,
                esEnlace = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (viewModel.guardarBanda()) {
                        Toast.makeText(context, "¡Banda registrada exitosamente!", Toast.LENGTH_SHORT).show()
                        onVolver()
                    } else {
                        Toast.makeText(context, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Botón más grande y clickeable
            ) {
                Text("Guardar Banda", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(32.dp))
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
        "Rock", "Metal", "Pop", "Indie", "Hip-Hop / Rap",
        "Folk / Acústico", "Punk", "Jazz / Blues", "Electrónica", "Otro"
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
            supportingText = { MensajeErrorAnimado(error) },
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
private fun CampoTextoIcono(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    icono: ImageVector,
    error: Boolean,
    unaLinea: Boolean = true,
    esEnlace: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(imageVector = icono, contentDescription = null) },
        isError = error,
        supportingText = { MensajeErrorAnimado(error) },
        singleLine = unaLinea,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            // Los enlaces no llevan mayúscula inicial y usan el teclado para URL
            capitalization = if (esEnlace) KeyboardCapitalization.None else KeyboardCapitalization.Sentences,
            keyboardType = if (esEnlace) KeyboardType.Uri else KeyboardType.Text,
            imeAction = if (unaLinea) ImeAction.Next else ImeAction.Default
        )
    )
}

// Componente reutilizable para animar la aparición del mensaje de error
@Composable
private fun MensajeErrorAnimado(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically()
    ) {
        Text("Este campo es obligatorio", color = MaterialTheme.colorScheme.error)
    }
}