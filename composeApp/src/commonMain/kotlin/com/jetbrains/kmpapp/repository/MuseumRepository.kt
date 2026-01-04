package com.jetbrains.kmpapp.repository

import com.jetbrains.kmpapp.data.local.MuseumDao
import com.jetbrains.kmpapp.data.remote.MuseumApi
import com.jetbrains.kmpapp.model.MuseumDtoObject
import com.jetbrains.kmpapp.utils.Mapper.toDto
import com.jetbrains.kmpapp.utils.Mapper.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MuseumRepository(
    private val museumApi: MuseumApi,
    private val museumDao: MuseumDao,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        try {
            val apiDtos = museumApi.getData()
            // On mappe les DTOs vers les entités Room
            val roomEntities = apiDtos.map { dto ->
                dto.toEntity() // Voir fonction extension plus bas
            }
            // On sauvegarde. Room gère les conflits (REPLACE)
            // Note: Si vous voulez garder les favoris lors du refresh,
            // il faudrait idéalement vérifier l'existant avant d'insérer,
            // ou utiliser une stratégie d'update partielle.
            museumDao.insertAll(roomEntities)
        } catch (e: Exception) {
            // Gestion d'erreur (log, etc.)
            println("Erreur lors du refresh: ${e.message}")
        }
    }

    fun getObjects(): Flow<List<MuseumDtoObject>> = museumDao.getAll()
        .map { museumObjects -> museumObjects.map { museumObject -> museumObject.toDto() } }

    fun getObjectById(objectId: Int): Flow<MuseumDtoObject> =
        museumDao.getObjectById(objectId).map {
            it!!.toDto()
        }

    suspend fun update(museumObject: MuseumDtoObject?) {
        museumDao.update(museumObject!!.toEntity())
    }

    suspend fun updateFavoriteStatus(museumObject: MuseumDtoObject?) {
        museumDao.updateFavoriteStatus(museumObject!!.objectID, museumObject.isFavorite)
    }
}
