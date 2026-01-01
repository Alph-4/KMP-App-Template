package com.jetbrains.kmpapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jetbrains.kmpapp.screens.DetailScreen
import com.jetbrains.kmpapp.screens.MainScreen

@Composable
fun App() {
    val systemIsDark = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemIsDark) }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Surface {
            val navController: NavHostController = rememberNavController()

            // startDestination pointe vers la liste, qui est maintenant contenue dans MainScreen
            NavHost(navController = navController, startDestination = AppDestination.List) {

                // 1. C'est ici le changement principal : on appelle MainScreen
                composable<AppDestination.List> {
                    MainScreen(
                        // Quand on clique sur un détail dans MainScreen, on navigue vers l'écran de détail global
                        navigateToDetails = { objectId ->
                            navController.navigate(AppDestination.Detail(objectId))
                        },
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it }
                    )
                }

                // 2. L'écran de détail reste ici (il couvre tout l'écran, pas de barre en bas)
                composable<AppDestination.Detail> { backStackEntry ->
                    DetailScreen(
                        objectId = backStackEntry.toRoute<AppDestination.Detail>().objectId,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
