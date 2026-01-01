package com.jetbrains.kmpapp


import kotlinx.serialization.Serializable

sealed interface AppDestination {
    @Serializable
    data object List : AppDestination

    @Serializable
    data class Detail(val objectId: Int) : AppDestination

    // Futures destinations
    @Serializable
    data class Profile(val userId: String) : AppDestination
}