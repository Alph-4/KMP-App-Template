package com.jetbrains.kmpapp.screens.detail.composants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jetbrains.kmpapp.model.MuseumDtoObject
import com.jetbrains.kmpapp.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FavoriteBtn(
    obj: MuseumDtoObject,
    viewModel: DetailViewModel,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState
) {
    IconButton(onClick = {
        viewModel.toggleFavorites()

        scope.launch {
            val message =
                if (!obj.isFavorite) "Ajouté aux favoris ❤️" else "Retiré des favoris 💔"
            snackBarHostState.showSnackbar(
                message = message,
                withDismissAction = true
            )

        }

    }) {
        Icon(
            contentDescription = "Sort icon",
            imageVector = Icons.Default.Favorite,
            tint = if (obj.isFavorite) Color.Red else Color.Gray
        )
    }
}