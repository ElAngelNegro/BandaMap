package com.example.bandamap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bandamap.BandaMapApplication
import com.example.bandamap.screens.BandasScreen
import com.example.bandamap.screens.ConvocatoriasScreen
import com.example.bandamap.screens.DetalleScreen
import com.example.bandamap.screens.InicioScreen
import com.example.bandamap.screens.LoginScreen
import com.example.bandamap.screens.NuevaConvocatoriaScreen
import com.example.bandamap.screens.RegistrarScreen
import com.example.bandamap.screens.RegistroScreen
import com.example.bandamap.viewmodel.AuthViewModel
import com.example.bandamap.viewmodel.BandaMapViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Factory con el Repository: así los ViewModels reciben acceso a Room
    val app = LocalContext.current.applicationContext as BandaMapApplication
    val factory = remember { BandaMapViewModelFactory(app.bandaRepository) }

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
                    // Vuelve a Login y limpia el historial de navegación
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.BANDAS) {
            BandasScreen(
                viewModel = viewModel(factory = factory),
                onBandaClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onAgregar = { navController.navigate(Rutas.REGISTRAR) },
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
                viewModel = viewModel(factory = factory),
                onEditar = { id -> navController.navigate(Rutas.editar(id)) },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.REGISTRAR) {
            RegistrarScreen(
                viewModel = viewModel(factory = factory),
                onVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.EDITAR,
            arguments = listOf(navArgument("bandaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bandaId = backStackEntry.arguments?.getInt("bandaId") ?: 0
            RegistrarScreen(
                viewModel = viewModel(factory = factory),
                bandaId = bandaId,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.CONVOCATORIAS) {
            ConvocatoriasScreen(
                viewModel = viewModel(factory = factory),
                onVolver = { navController.popBackStack() },
                onNuevaConvocatoria = { navController.navigate(Rutas.NUEVA_CONVOCATORIA) }
            )
        }

        composable(Rutas.NUEVA_CONVOCATORIA) {
            NuevaConvocatoriaScreen(onVolver = { navController.popBackStack() })
        }
    }
}