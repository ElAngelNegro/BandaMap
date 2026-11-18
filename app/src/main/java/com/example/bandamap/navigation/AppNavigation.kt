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
import com.example.bandamap.screens.InicioScreen
import com.example.bandamap.screens.NuevaConvocatoriaScreen
import com.example.bandamap.screens.RegistrarScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.INICIO,
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

        composable(Rutas.INICIO) {
            InicioScreen(
                onExplorarBandas = { navController.navigate(Rutas.BANDAS) },
                onConvocatorias = { navController.navigate(Rutas.CONVOCATORIAS) },
                onRegistrarBanda = { navController.navigate(Rutas.REGISTRAR) }
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