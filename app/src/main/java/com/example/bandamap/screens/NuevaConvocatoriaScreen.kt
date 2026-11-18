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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bandamap.components.BarraSuperior
import com.example.bandamap.components.EncabezadoSeccion
import com.example.bandamap.viewmodel.NuevaConvocatoriaViewModel

@Composable
fun NuevaConvocatoriaScreen(
    onVolver: () -> Unit,
    viewModel: NuevaConvocatoriaViewModel = viewModel()
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsState()

    Scaffold(topBar = { BarraSuperior("Nueva convocatoria", onVolver) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EncabezadoSeccion(
                badge = "Publicar Aviso",
                titulo = "Nueva Convocatoria",
                subtitulo = "Arma tu tocata y encuentra bandas o músicos colaboradores. Los campos con * son obligatorios."
            )

            Spacer(modifier = Modifier.height(4.dp))

            CampoFormularioIcono(
                etiqueta = "Título * (ej: Se buscan 2 bandas de metal)",
                valor = formState.titulo,
                onCambio = viewModel::onTituloChange,
                icono = Icons.Default.Star,
                mensajeError = if (formState.tituloError) "Este campo es obligatorio" else null
            )

            CampoFormularioIcono(
                etiqueta = "Género buscado *",
                valor = formState.genero,
                onCambio = viewModel::onGeneroChange,
                icono = Icons.Default.Info,
                mensajeError = if (formState.generoError) "Este campo es obligatorio" else null
            )

            CampoFormularioIcono(
                etiqueta = "Fecha * (ej: 15 nov 2026)",
                valor = formState.fecha,
                onCambio = viewModel::onFechaChange,
                icono = Icons.Default.DateRange,
                mensajeError = if (formState.fechaError) "Este campo es obligatorio" else null
            )

            CampoFormularioIcono(
                etiqueta = "Ciudad *",
                valor = formState.ciudad,
                onCambio = viewModel::onCiudadChange,
                icono = Icons.Default.LocationOn,
                mensajeError = if (formState.ciudadError) "Este campo es obligatorio" else null
            )

            CampoFormularioIcono(
                etiqueta = "Cupos *",
                valor = formState.cupos,
                onCambio = viewModel::onCuposChange,
                icono = Icons.Default.Person,
                mensajeError = if (formState.cuposError) "Ingresa un número mayor a 0" else null,
                teclado = KeyboardType.Number
            )

            CampoFormularioIcono(
                etiqueta = "Descripción",
                valor = formState.descripcion,
                onCambio = viewModel::onDescripcionChange,
                icono = Icons.Default.Info,
                mensajeError = null,
                unaLinea = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (viewModel.guardarConvocatoria()) {
                        Toast.makeText(context, "¡Convocatoria publicada!", Toast.LENGTH_SHORT).show()
                        onVolver()
                    } else {
                        Toast.makeText(context, "Por favor, revisa los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Publicar Convocatoria", style = MaterialTheme.typography.titleMedium)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CampoFormularioIcono(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    icono: ImageVector,
    mensajeError: String?,
    unaLinea: Boolean = true,
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(imageVector = icono, contentDescription = null) },
        isError = mensajeError != null,
        supportingText = { 
            AnimatedVisibility(
                visible = mensajeError != null,
                enter = fadeIn() + expandVertically()
            ) {
                if (mensajeError != null) {
                    Text(mensajeError, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        singleLine = unaLinea,
        keyboardOptions = KeyboardOptions(
            keyboardType = teclado,
            capitalization = if (teclado == KeyboardType.Text) KeyboardCapitalization.Sentences else KeyboardCapitalization.None,
            imeAction = if (unaLinea) ImeAction.Next else ImeAction.Default
        ),
        modifier = Modifier.fillMaxWidth()
    )
}