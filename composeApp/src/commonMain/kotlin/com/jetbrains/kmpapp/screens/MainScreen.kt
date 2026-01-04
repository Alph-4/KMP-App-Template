package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jetbrains.kmpapp.model.SortingType
import com.jetbrains.kmpapp.viewmodel.ListViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

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
    //currentTab - État pour savoir sur quel onglet on est
    var currentTab by remember { mutableStateOf(MainTab.Home) }
    val listViewModel = koinViewModel<ListViewModel>()

    // scope - Créer le Scope (pour lancer des actions asynchrones comme afficher la snackbar)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(currentTab.label) },
                actions = {
                    if (currentTab == MainTab.Home) {
                        IconButton(onClick = {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            listViewModel.toggleSorting()
                            scope.launch {
                                if (listViewModel.sortingType.value == SortingType.Ascending) {
                                    snackbarHostState.showSnackbar("Liste triée par ordre décroissant")

                                } else {
                                    snackbarHostState.showSnackbar("Liste triée par ordre croissant")
                                }
                            }

                        }) {
                            Icon(
                                contentDescription = "Sort icon",
                                imageVector = Icons.AutoMirrored.Default.Sort,
                            )
                        }
                    }
                }
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
