package com.example.bandamap.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bandamap.navigation.Rutas

sealed class DestinoNavegacion(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    object Inicio : DestinoNavegacion(Rutas.INICIO, "Inicio", Icons.Default.Home)
    object Bandas : DestinoNavegacion(Rutas.BANDAS, "Bandas", Icons.AutoMirrored.Filled.List)
    object Convocatorias : DestinoNavegacion(Rutas.CONVOCATORIAS, "Convoc.", Icons.Default.Notifications)
    object Favoritos : DestinoNavegacion(Rutas.FAVORITOS, "Favoritos", Icons.Default.Favorite)
}

val destinosPrincipales = listOf(
    DestinoNavegacion.Inicio,
    DestinoNavegacion.Bandas,
    DestinoNavegacion.Convocatorias,
    DestinoNavegacion.Favoritos
)

@Composable
fun BarraNavegacionInferior(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry.value?.destination?.route

    val mostrarBarra = destinosPrincipales.any { it.ruta == rutaActual } ||
            rutaActual == Rutas.REGISTRAR ||
            rutaActual == Rutas.NUEVA_CONVOCATORIA ||
            rutaActual?.startsWith("detalle/") == true

    if (mostrarBarra) {
        NavigationBar {
            destinosPrincipales.forEach { destino ->
                NavigationBarItem(
                    icon = { Icon(destino.icono, contentDescription = destino.titulo) },
                    label = { Text(destino.titulo) },
                    selected = rutaActual == destino.ruta,
                    onClick = {
                        navController.navigate(destino.ruta) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
