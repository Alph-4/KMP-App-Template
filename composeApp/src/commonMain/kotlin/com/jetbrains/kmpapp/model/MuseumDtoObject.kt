package com.jetbrains.kmpapp.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class MuseumDtoObject(
    val objectID: Int,
    val title: String,
    val artistDisplayName: String,
    val medium: String,
    val dimensions: String,
    val objectURL: String,
    val objectDate: String,
    val primaryImage: String,
    val primaryImageSmall: String,
    val repository: String,
    val department: String,
    val creditLine: String,
    val isFavorite: Boolean = false
)
