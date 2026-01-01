package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

// 1. Définir les destinations possibles de la BottomBar
enum class MainTab(val label: String, val icon: ImageVector) {
    Home("Accueil", Icons.Default.Home),
    Profile("Profile", Icons.Default.Person)
}

@Composable
fun MainScreen(
    navigateToDetails: (Int) -> Unit, // Callback pour la navigation profonde vers les détails
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    // 2. État pour savoir sur quel onglet on est
    var currentTab by remember { mutableStateOf(MainTab.Home) }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(currentTab.label) }
            )
        },
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        selected = currentTab == tab,
                        onClick = { currentTab = tab }
                    )
                }
            }
        }
    ) { paddingValues ->
        // 3. Afficher le bon écran en fonction de l'onglet sélectionné
        when (currentTab) {
            MainTab.Home -> ListScreen(
                navigateToDetails = navigateToDetails,
                contentPadding = paddingValues
            )

            MainTab.Profile -> ProfileScreen(
                modifier = Modifier.padding(paddingValues),
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
    }
}
