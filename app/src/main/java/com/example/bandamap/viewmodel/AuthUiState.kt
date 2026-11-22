package com.example.bandamap.viewmodel

// Estado de las pantallas de Login y Registro, en un solo objeto inmutable
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val cargando: Boolean = false,          // true mientras Firebase responde
    val error: String? = null,              // mensaje de error de Firebase, si lo hay
    val intentoEnviar: Boolean = false,     // se activa al presionar el botón
    val autenticado: Boolean = false        // true cuando el login o registro fue exitoso
) {
    // Validaciones (solo se muestran después de presionar el botón)
    val emailError: String?
        get() = when {
            !intentoEnviar -> null
            email.isBlank() -> "Ingresa tu correo"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Correo no válido"
            else -> null
        }

    val passwordError: String?
        get() = when {
            !intentoEnviar -> null
            password.isBlank() -> "Ingresa tu contraseña"
            password.length < 6 -> "Mínimo 6 caracteres"
            else -> null
        }

    val confirmarError: String?
        get() = when {
            !intentoEnviar -> null
            confirmarPassword != password -> "Las contraseñas no coinciden"
            else -> null
        }
}
