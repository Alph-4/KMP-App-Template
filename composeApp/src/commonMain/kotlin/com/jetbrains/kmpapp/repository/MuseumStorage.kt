package com.jetbrains.kmpapp.repository

import com.jetbrains.kmpapp.model.MuseumDtoObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface MuseumStorage {
    suspend fun saveObjects(newObjects: List<MuseumDtoObject>)

    fun getObjectById(objectId: Int): Flow<MuseumDtoObject?>

    fun getObjects(): Flow<List<MuseumDtoObject>>
}

class InMemoryMuseumStorage : MuseumStorage {
    private val storedObjects = MutableStateFlow(emptyList<MuseumDtoObject>())

    override suspend fun saveObjects(newObjects: List<MuseumDtoObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(objectId: Int): Flow<MuseumDtoObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getObjects(): Flow<List<MuseumDtoObject>> = storedObjects
}
