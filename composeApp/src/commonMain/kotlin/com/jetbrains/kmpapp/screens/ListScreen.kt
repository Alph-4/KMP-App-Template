package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.jetbrains.kmpapp.model.ListUiState
import com.jetbrains.kmpapp.model.MuseumObject
import com.jetbrains.kmpapp.viewmodel.ListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ListScreen(
    navigateToDetails: (objectId: Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: ListViewModel = koinViewModel()
) {
    val state by viewModel.objects.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()

    LaunchedEffect(state) {
        println("ListScreen : State changed, checking scroll...")
        if (state is ListUiState.Success) {
            listState.scrollToItem(0)
        }
    }


    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val uiState = state) {
                is ListUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ListUiState.Success -> {
                    val objects = uiState.objects

                    if (objects.isNotEmpty()) {
                        ObjectGrid(
                            objects = objects,
                            onObjectClick = navigateToDetails,
                            listState = listState
                        )
                    }
                }

                is ListUiState.Error -> {
                    // Afficher le message d'erreur
                    EmptyScreenContent(Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
private fun ObjectGrid(
    objects: List<MuseumObject>,
    onObjectClick: (Int) -> Unit,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier.fillMaxSize().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        state = listState
    ) {
        itemsIndexed(
            items = objects,
            key = { _, obj -> obj.objectID }
        ) { index, obj ->
            ObjectFrame(
                obj = obj,
                index = index,
                onClick = { onObjectClick(obj.objectID) },
            )
        }
    }
}

@Composable
private fun ObjectFrame(
    obj: MuseumObject,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.height(300.dp)) {
        Column(
            modifier
                .padding(8.dp)
                .clickable { onClick() }
        ) {
            SubcomposeAsyncImage(
                model = obj.primaryImageSmall,
                contentDescription = obj.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(1f)
                    .background(Color.LightGray),
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Erreur",
                            // C'EST ICI QUE VOUS GÉREZ LA TAILLE
                            modifier = Modifier.size(24.dp),
                            tint = Color.Red // Optionnel : changer la couleur
                        )
                    }
                }
            )

            Spacer(Modifier.height(2.dp))

            Text(
                "#${index} - ${obj.title} - ${obj.objectID}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(obj.artistDisplayName, style = MaterialTheme.typography.bodyMedium)
            Text(obj.objectDate, style = MaterialTheme.typography.bodySmall)
        }
    }
}
