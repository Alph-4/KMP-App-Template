package com.jetbrains.kmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.model.ListUiState
import com.jetbrains.kmpapp.repository.MuseumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ListViewModel(
    private val museumRepository: MuseumRepository
) : ViewModel() {

    val objects: StateFlow<ListUiState> =
        museumRepository.getObjects()
            .map { list ->
                // On transforme la liste reçue en état "Succès"
                ListUiState.Success(list) as ListUiState
            }
            .onStart {
                // (Optionnel) Émet "Loading" au démarrage du flux si besoin
                emit(ListUiState.Loading)
            }
            .catch { error ->
                // On capture les erreurs pour éviter le crash
                emit(ListUiState.Error(error.message ?: "Erreur inconnue"))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                // L'état initial DOIT être Loading (et non emptyList)
                ListUiState.Loading
            )
}
