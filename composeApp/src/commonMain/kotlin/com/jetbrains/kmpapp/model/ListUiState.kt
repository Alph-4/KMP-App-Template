package com.jetbrains.kmpapp.model


enum class SortingType{
    Ascending,
    Descending
}
sealed interface ListUiState {
    data object Loading : ListUiState
    data class Success(val objects: List<MuseumDtoObject>,
                       val sortingType: SortingType = SortingType.Ascending ) : ListUiState
    data class Error(val message: String) : ListUiState

}