package com.example.bandamap.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.bandamap.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cuando Firebase confirma el inicio de sesión, pasamos a Inicio
    LaunchedEffect(uiState.autenticado) {
        if (uiState.autenticado) onLoginExitoso()
    }

    com.example.bandamap.components.FondoCarrusel {
        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { padding ->
            Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "BandaMap",
                style = MaterialTheme.typography.displayMedium,
                fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.example.bandamap.R.font.pastor_of_muppets)),
                color = androidx.compose.ui.graphics.Color.White
            )
            Text(
                "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge,
                color = androidx.compose.ui.graphics.Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = uiState.emailError != null,
                supportingText = uiState.emailError?.let { { Text(it) } },
                singleLine = true,
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.White,
                    focusedLabelColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedLabelColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f),
                    focusedLeadingIconColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedLeadingIconColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                isError = uiState.passwordError != null,
                supportingText = uiState.passwordError?.let { { Text(it) } },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.White,
                    focusedLabelColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedLabelColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f),
                    focusedLeadingIconColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedLeadingIconColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Error que devuelve Firebase (por ejemplo, contraseña incorrecta)
            uiState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::iniciarSesion,
                enabled = !uiState.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }

            TextButton(onClick = onIrARegistro) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}
}
