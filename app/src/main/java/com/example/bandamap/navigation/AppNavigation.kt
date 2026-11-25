package com.example.bandamap.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bandamap.screens.BandasScreen
import com.example.bandamap.screens.ConvocatoriasScreen
import com.example.bandamap.screens.DetalleScreen
import com.example.bandamap.screens.FavoritosScreen
import com.example.bandamap.screens.InicioScreen
import com.example.bandamap.screens.LoginScreen
import com.example.bandamap.screens.NuevaConvocatoriaScreen
import com.example.bandamap.screens.RegistrarScreen
import com.example.bandamap.screens.RegistroScreen
import com.example.bandamap.viewmodel.AuthViewModel
import com.example.bandamap.viewmodel.BandaMapViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.bandamap.BandaMapApplication
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Factory con el Repository único de la app (creado en BandaMapApplication)
    val app = LocalContext.current.applicationContext as BandaMapApplication
    val factory = remember { BandaMapViewModelFactory(app.bandaRepository) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val mostrarMensaje: (String) -> Unit = { mensaje ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
        bottomBar = { com.example.bandamap.components.BarraNavegacionInferior(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            navController = navController,
            startDestination = if (authViewModel.haySesionActiva()) Rutas.INICIO else Rutas.LOGIN,
            enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(250))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(350))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(350, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(250))
        }
    ) {

        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO_USUARIO) }
            )
        }

        composable(Rutas.REGISTRO_USUARIO) {
            RegistroScreen(
                viewModel = authViewModel,
                onRegistroExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                onCerrarSesion = {
                    authViewModel.cerrarSesion()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.FAVORITOS) {
            FavoritosScreen(
                viewModel = viewModel(factory = factory),
                onBandaClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVolver = { navController.popBackStack() }
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
                onVolver = { navController.popBackStack() },
                onMostrarMensaje = mostrarMensaje
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
                onVolver = { navController.popBackStack() },
                onMostrarMensaje = mostrarMensaje
            )
        }

        composable(Rutas.CONVOCATORIAS) {
            ConvocatoriasScreen(
                viewModel = viewModel(factory = factory),
                onVolver = { navController.popBackStack() },
                onNuevaConvocatoria = { navController.navigate(Rutas.NUEVA_CONVOCATORIA) },
                onMostrarMensaje = mostrarMensaje
            )
        }

        composable(Rutas.NUEVA_CONVOCATORIA) {
            NuevaConvocatoriaScreen(
                onVolver = { navController.popBackStack() },
                onMostrarMensaje = mostrarMensaje
            )
        }
    }
    }
}