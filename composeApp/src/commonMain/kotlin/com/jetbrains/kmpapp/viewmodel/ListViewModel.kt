package com.jetbrains.kmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.model.MuseumObject
import com.jetbrains.kmpapp.repository.MuseumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListViewModel(
    private val museumRepository: MuseumRepository
) : ViewModel() {
    
    val objects: StateFlow<List<MuseumObject>> = 
        museumRepository.getObjects()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}
