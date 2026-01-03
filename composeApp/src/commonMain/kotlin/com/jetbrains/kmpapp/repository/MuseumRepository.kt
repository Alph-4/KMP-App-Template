package com.jetbrains.kmpapp.repository

import com.jetbrains.kmpapp.data.remote.MuseumApi
import com.jetbrains.kmpapp.model.MuseumDtoObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MuseumRepository(
    private val museumApi: MuseumApi,
    private val museumStorage: MuseumStorage,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        museumStorage.saveObjects(museumApi.getData())
    }

    fun getObjects(): Flow<List<MuseumDtoObject>> = museumStorage.getObjects()

    fun getObjectById(objectId: Int): Flow<MuseumDtoObject?> = museumStorage.getObjectById(objectId)
}
