package com.example.bandamap.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Maneja todo lo relacionado con Firebase Authentication:
// registro, inicio de sesión, sesión activa y cierre de sesión.
class AuthViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // ---- Sesión ----

    // Aquí se determina si el usuario está autenticado.
    // Firebase guarda la sesión en el celular, así que sigue activa al reabrir la app.
    fun haySesionActiva(): Boolean = auth.currentUser != null

    fun correoUsuario(): String = auth.currentUser?.email ?: ""

    fun cerrarSesion() {
        auth.signOut()
        _uiState.value = AuthUiState()   // limpia el formulario y el estado
    }

    // ---- Campos del formulario ----

    fun onEmailChange(valor: String) = _uiState.update { it.copy(email = valor, error = null) }
    fun onPasswordChange(valor: String) = _uiState.update { it.copy(password = valor, error = null) }
    fun onConfirmarChange(valor: String) = _uiState.update { it.copy(confirmarPassword = valor, error = null) }

    // Se llama al cambiar entre Login y Registro
    fun limpiarFormulario() {
        _uiState.value = AuthUiState(email = _uiState.value.email)
    }

    // ---- Acciones ----

    fun iniciarSesion() {
        _uiState.update { it.copy(intentoEnviar = true) }
        val estado = _uiState.value
        if (estado.emailError != null || estado.passwordError != null) return

        _uiState.update { it.copy(cargando = true, error = null) }
        auth.signInWithEmailAndPassword(estado.email.trim(), estado.password)
            .addOnSuccessListener {
                _uiState.update { it.copy(cargando = false, autenticado = true) }
            }
            .addOnFailureListener { e ->
                _uiState.update { it.copy(cargando = false, error = traducirError(e)) }
            }
    }

    fun registrar() {
        _uiState.update { it.copy(intentoEnviar = true) }
        val estado = _uiState.value
        if (estado.emailError != null || estado.passwordError != null || estado.confirmarError != null) return

        _uiState.update { it.copy(cargando = true, error = null) }
        auth.createUserWithEmailAndPassword(estado.email.trim(), estado.password)
            .addOnSuccessListener {
                // Firebase deja la sesión iniciada automáticamente después de registrar
                _uiState.update { it.copy(cargando = false, autenticado = true) }
            }
            .addOnFailureListener { e ->
                _uiState.update { it.copy(cargando = false, error = traducirError(e)) }
            }
    }

    // Convierte los errores de Firebase en mensajes claros en español
    private fun traducirError(e: Exception): String = when (e) {
        is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil (mínimo 6 caracteres)"
        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese correo"
        is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo"
        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"
        is FirebaseNetworkException -> "Sin conexión a internet"
        else -> "Ocurrió un error: ${e.localizedMessage ?: "inténtalo de nuevo"}"
    }
}
