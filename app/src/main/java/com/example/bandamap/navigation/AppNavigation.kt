package com.example.bandamap.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bandamap.screens.BandasScreen
import com.example.bandamap.screens.ConvocatoriasScreen
import com.example.bandamap.screens.DetalleScreen
import com.example.bandamap.screens.InicioScreen
import com.example.bandamap.screens.LoginScreen
import com.example.bandamap.screens.NuevaConvocatoriaScreen
import com.example.bandamap.screens.RegistrarScreen
import com.example.bandamap.screens.RegistroScreen
import com.example.bandamap.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Acceso protegido: si hay sesión guardada parte en Inicio, si no, en Login
    val inicio = if (authViewModel.haySesionActiva()) Rutas.INICIO else Rutas.LOGIN

    // Ir a Inicio borrando Login/Registro del historial (el botón atrás no vuelve al login)
    val irAInicio = {
        navController.navigate(Rutas.INICIO) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = inicio) {

        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginExitoso = irAInicio,
                onIrARegistro = {
                    authViewModel.limpiarFormulario()
                    navController.navigate(Rutas.REGISTRO)
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                viewModel = authViewModel,
                onRegistroExitoso = irAInicio,
                onVolver = {
                    authViewModel.limpiarFormulario()
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                onExplorarBandas = { navController.navigate(Rutas.BANDAS) },
                onConvocatorias = { navController.navigate(Rutas.CONVOCATORIAS) },
                onRegistrarBanda = { navController.navigate(Rutas.REGISTRAR) },
                correoUsuario = authViewModel.correoUsuario(),
                onCerrarSesion = {
                    authViewModel.cerrarSesion()
                    // Vuelve a Login y borra todo el historial
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.BANDAS) {
            BandasScreen(
                onBandaClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("bandaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bandaId = backStackEntry.arguments?.getInt("bandaId") ?: 0
            DetalleScreen(
                bandaId = bandaId,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.REGISTRAR) {
            RegistrarScreen(onVolver = { navController.popBackStack() })
        }

        composable(Rutas.CONVOCATORIAS) {
            ConvocatoriasScreen(
                onVolver = { navController.popBackStack() },
                onNuevaConvocatoria = { navController.navigate(Rutas.NUEVA_CONVOCATORIA) }
            )
        }

        composable(Rutas.NUEVA_CONVOCATORIA) {
            NuevaConvocatoriaScreen(onVolver = { navController.popBackStack() })
        }
    }
}