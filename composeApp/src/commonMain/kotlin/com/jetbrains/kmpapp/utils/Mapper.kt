package com.jetbrains.kmpapp.utils

import com.jetbrains.kmpapp.data.local.model.MuseumObject
import com.jetbrains.kmpapp.model.MuseumDtoObject

object Mapper {

    // Convertit le DTO (Réseau) vers l'Entity (Base de données)
    fun MuseumDtoObject.toEntity(): MuseumObject {
        return MuseumObject(
            objectID = this.objectID,
            title = this.title,
            artistDisplayName = this.artistDisplayName,
            objectDate = this.objectDate,
            dimensions = this.dimensions,
            medium = this.medium,
            repository = this.repository,
            department = this.department,
            creditLine = this.creditLine,
            primaryImageSmall = this.primaryImageSmall,
            objectURL = this.objectURL,
            isFavorite = this.isFavorite ?: false,// Par défaut, false quand ça vient de l'API
            primaryImage = this.primaryImage
        )
    }

    // Si besoin, convertit l'Entity vers le DTO (souvent inutile si on utilise l'Entity dans l'UI)
    fun MuseumObject.toDto(): MuseumDtoObject {
        return MuseumDtoObject(
            objectID = this.objectID,
            title = this.title,
            artistDisplayName = this.artistDisplayName,
            objectDate = this.objectDate,
            dimensions = this.dimensions,
            medium = this.medium,
            repository = this.repository,
            department = this.department,
            creditLine = this.creditLine,
            primaryImageSmall = this.primaryImageSmall,
            objectURL = this.objectURL,
            isFavorite = this.isFavorite,
            primaryImage = this.primaryImage
        )
    }
}