package com.jetbrains.kmpapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.jetbrains.kmpapp.model.MuseumDtoObject
import com.jetbrains.kmpapp.viewmodel.ListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ListScreen(
    navigateToDetails: (objectId: Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: ListViewModel = koinViewModel()
) {
    val listState by viewModel.objects.collectAsStateWithLifecycle()
    val sortType by viewModel.sortingType.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    LaunchedEffect(sortType) {
        gridState.scrollToItem(0)
    }

    ListScreenContent(
        uiState = listState,
        navigateToDetails = navigateToDetails,
        listState = gridState,
        contentPadding = contentPadding
    )
}

@Composable
fun ListScreenContent(
    uiState: ListUiState,
    navigateToDetails: (Int) -> Unit,
    listState: LazyGridState,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Scaffold interne supprimé car déjà géré par MainScreen.
    // On utilise directement une Box ou Column avec le padding passé en paramètre.
    Box(modifier = Modifier.padding(contentPadding)) {
        when (uiState) {
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
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun ObjectGrid(
    objects: List<MuseumDtoObject>,
    onObjectClick: (Int) -> Unit,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        // On n'ajoute pas de padding système ici car il est déjà géré par le Scaffold du MainScreen via contentPadding
        contentPadding = PaddingValues(vertical = 8.dp),
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
    obj: MuseumDtoObject,
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
                            modifier = Modifier.size(24.dp),
                            tint = Color.Red
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

@Preview
@Composable
fun ListScreenPreview() {
    val fakeObjects = listOf(
        MuseumDtoObject(
            objectID = 1,
            title = "The Night Watch",
            artistDisplayName = "Rembrandt",
            medium = "Oil on canvas",
            dimensions = "363 cm × 437 cm",
            objectURL = "",
            objectDate = "1642",
            primaryImage = "",
            primaryImageSmall = "",
            repository = "Rijksmuseum",
            department = "Paintings",
            creditLine = "On loan"
        ),
        MuseumDtoObject(
            objectID = 2,
            title = "The Starry Night",
            artistDisplayName = "Vincent van Gogh",
            medium = "Oil on canvas",
            dimensions = "73.7 cm × 92.1 cm",
            objectURL = "",
            objectDate = "1889",
            primaryImage = "",
            primaryImageSmall = "",
            repository = "MoMA",
            department = "Paintings",
            creditLine = "Acquired through the Lillie P. Bliss Bequest"
        ),
        MuseumDtoObject(
            objectID = 3,
            title = "Girl with a Pearl Earring",
            artistDisplayName = "Johannes Vermeer",
            medium = "Oil on canvas",
            dimensions = "44.5 cm × 39 cm",
            objectURL = "",
            objectDate = "1665",
            primaryImage = "",
            primaryImageSmall = "",
            repository = "Mauritshuis",
            department = "Paintings",
            creditLine = "Bequest of Arnoldus Andries des Tombe"
        )
    )

    MaterialTheme {
        ListScreenContent(
            uiState = ListUiState.Success(fakeObjects),
            listState = rememberLazyGridState(),
            navigateToDetails = {}
        )
    }
}
