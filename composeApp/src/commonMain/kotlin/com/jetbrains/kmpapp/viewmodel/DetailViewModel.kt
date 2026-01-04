package com.jetbrains.kmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.model.MuseumDtoObject
import com.jetbrains.kmpapp.repository.AiRepository
import com.jetbrains.kmpapp.repository.MuseumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val museumRepository: MuseumRepository,
    private val aiRepository: AiRepository,
    objectId: Int
) : ViewModel() {


    val obj: StateFlow<MuseumDtoObject?> =
        museumRepository.getObjectById(objectId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    private val _artWorkDesc: MutableStateFlow<String?> = MutableStateFlow(null)
    val artWorkDesc: StateFlow<String?> = _artWorkDesc


    fun toggleFavorites() {
        val newObj = obj.value?.copy(isFavorite = !obj.value!!.isFavorite)
        viewModelScope.launch {
            museumRepository.updateFavoriteStatus(newObj)

        }

    }

    fun getArtworkDescription(artworkName: String) {
        viewModelScope.launch {
            var response = aiRepository.getArtworkDescription(artworkName)

            println("DEBUG VM data received : $response")
            if (!response.isNullOrEmpty()) {
                _artWorkDesc.value = response
            }
        }
    }
}
