package com.jetbrains.kmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.model.MuseumObject
import com.jetbrains.kmpapp.repository.MuseumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DetailViewModel(
    private val museumRepository: MuseumRepository,
    objectId: Int
) : ViewModel() {
    
    val obj: StateFlow<MuseumObject?> = 
        museumRepository.getObjectById(objectId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
}
