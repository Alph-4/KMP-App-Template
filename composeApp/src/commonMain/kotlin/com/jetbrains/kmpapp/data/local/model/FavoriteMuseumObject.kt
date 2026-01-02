package com.jetbrains.kmpapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMuseumObject(
     @PrimaryKey(autoGenerate = true) val objectID: Int,
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
)