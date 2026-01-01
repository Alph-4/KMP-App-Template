package com.jetbrains.kmpapp.model

sealed interface ListUiState {
    data object Loading : ListUiState
    data class Success(val objects: List<MuseumObject>) : ListUiState
    data class Error(val message: String) : ListUiState
}