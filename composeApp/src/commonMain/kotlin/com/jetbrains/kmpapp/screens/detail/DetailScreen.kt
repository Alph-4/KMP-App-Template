package com.jetbrains.kmpapp.screens.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign.Companion.Justify
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jetbrains.kmpapp.model.MuseumDtoObject
import com.jetbrains.kmpapp.screens.EmptyScreenContent
import com.jetbrains.kmpapp.screens.detail.composants.FavoriteBtn
import com.jetbrains.kmpapp.screens.detail.composants.LabeledInfo
import com.jetbrains.kmpapp.viewmodel.DetailViewModel
import kmp_app_template.composeapp.generated.resources.Res
import kmp_app_template.composeapp.generated.resources.back
import kmp_app_template.composeapp.generated.resources.label_artist
import kmp_app_template.composeapp.generated.resources.label_credits
import kmp_app_template.composeapp.generated.resources.label_date
import kmp_app_template.composeapp.generated.resources.label_department
import kmp_app_template.composeapp.generated.resources.label_dimensions
import kmp_app_template.composeapp.generated.resources.label_medium
import kmp_app_template.composeapp.generated.resources.label_repository
import kmp_app_template.composeapp.generated.resources.label_title
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun DetailScreen(
    objectId: Int,
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<DetailViewModel> { parametersOf(objectId) }
    val obj by viewModel.obj.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    // scope - Créer le Scope (pour lancer des actions asynchrones comme afficher la snackbar)
    rememberCoroutineScope()


    AnimatedContent(obj != null) { objectAvailable ->
        if (objectAvailable) {
            ObjectDetails(obj!!, snackBarHostState, viewModel, onBackClick = navigateBack)
        } else {
            EmptyScreenContent(Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun ObjectDetails(
    obj: MuseumDtoObject,
    snackBarHostState: SnackbarHostState,
    viewModel: DetailViewModel,
    scope: CoroutineScope = rememberCoroutineScope(),
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val artWorkDesc by viewModel.artWorkDesc.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                actions = {
                    FavoriteBtn(obj, viewModel, scope, snackBarHostState)
                }
            )
        },
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars),
    ) { paddingValues ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            AsyncImage(
                model = obj.primaryImageSmall,
                contentDescription = obj.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )

            SelectionContainer {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        obj.title,
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(6.dp))
                    LabeledInfo(stringResource(Res.string.label_title), obj.title, 1)
                    LabeledInfo(
                        stringResource(Res.string.label_artist),
                        obj.artistDisplayName,
                        null
                    )
                    LabeledInfo(stringResource(Res.string.label_date), obj.objectDate, null)
                    LabeledInfo(stringResource(Res.string.label_dimensions), obj.dimensions, null)
                    LabeledInfo(stringResource(Res.string.label_medium), obj.medium, null)
                    LabeledInfo(stringResource(Res.string.label_department), obj.department, null)
                    LabeledInfo(stringResource(Res.string.label_repository), obj.repository, null)
                    LabeledInfo(stringResource(Res.string.label_credits), obj.creditLine, null)
                }
            }
            askIABtn(viewModel, obj, artWorkDesc)
        }
    }
}

@Preview
@Composable
fun askIABtn(viewModel: DetailViewModel, obj: MuseumDtoObject, artWorkDesc: String?) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier.padding(16.dp),

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer, // Couleur un peu plus distincte
            ),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- HEADER ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome, // Icône "Magie/IA"
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "L'anecdote de l'IA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Text(
                    text = "Envie d'en savoir plus sur \"${obj.title}\" ? Demandez à l'intelligence artificielle de vous raconter son histoire.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // --- ACTION ---
                // On cache le bouton si on a déjà la réponse pour épurer l'UI
                androidx.compose.animation.AnimatedVisibility(visible = artWorkDesc == null) {
                    ElevatedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            viewModel.getArtworkDescription(obj.title)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology, // Ou Search, ou AutoAwesome
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Générer l'anecdote")
                    }
                }

                // --- RESULTAT ---
                androidx.compose.animation.AnimatedVisibility(
                    visible = artWorkDesc != null,
                    enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = artWorkDesc ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 1.5.em,
                                textAlign = Justify
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        // Petit footer optionnel
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Généré par Mistral AI",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
    MaterialTheme {
        ObjectDetails(
            obj = MuseumDtoObject(
                objectID = 1,
                title = "La Nuit étoilée",
                artistDisplayName = "Vincent van Gogh",
                objectDate = "1889",
                dimensions = "73.7 cm × 92.1 cm",
                medium = "Huile sur toile",
                repository = "MoMA",
                department = "Peinture",
                creditLine = "Donation",
                primaryImageSmall = "",
                objectURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPzqU9D_WSvRlDOJ7r8RLp_j1ynKqb1BmCIw&s",
                primaryImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPzqU9D_WSvRlDOJ7r8RLp_j1ynKqb1BmCIw&s" // Pas d'image en preview, c'est normal,
            ),
            snackBarHostState = remember { SnackbarHostState() },
            viewModel = koinViewModel(),
            onBackClick = {}
        )
    }
}
