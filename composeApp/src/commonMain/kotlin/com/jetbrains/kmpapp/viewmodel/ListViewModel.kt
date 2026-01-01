package com.jetbrains.kmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.model.ListUiState
import com.jetbrains.kmpapp.model.SortingType
import com.jetbrains.kmpapp.repository.MuseumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ListViewModel(
    private val museumRepository: MuseumRepository
) : ViewModel() {

    val sortingType = MutableStateFlow(SortingType.Ascending)

    val objects: StateFlow<ListUiState> =
        combine(museumRepository.getObjects(), sortingType)
        { list, sortType ->
            // 1. STOCKEZ le résultat du tri dans une variable
            val sortedList = if (sortType == SortingType.Ascending) {
                list.sortedBy { it.title }
            } else {
                list.sortedByDescending { it.title }
            }

            // 2. Passez la liste TRIÉE (sortedList) à l'état Success
            ListUiState.Success(sortedList) as ListUiState

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
                SharingStarted.WhileSubscribed(1000),
                // L'état initial DOIT être Loading (et non emptyList)
                ListUiState.Loading
            )


    fun toggleSorting() {
        val current = sortingType.value
        sortingType.value = if (current == SortingType.Ascending) {
            SortingType.Descending
        } else {
            SortingType.Ascending
        }

    }
}
