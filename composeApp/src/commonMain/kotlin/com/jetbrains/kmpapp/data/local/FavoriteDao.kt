package com.jetbrains.kmpapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jetbrains.kmpapp.data.local.model.FavoriteMuseumObject
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteMuseumObject)

    @Delete
    suspend fun delete(favorite: FavoriteMuseumObject)

    @Query("DELETE FROM FavoriteMuseumObject WHERE objectID = :objectId")
    suspend fun deleteById(objectId: Int)

    @Query("SELECT * FROM FavoriteMuseumObject")
    fun getAllFavorites(): Flow<List<FavoriteMuseumObject>>

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteMuseumObject WHERE objectID = :objectId)")
    fun isFavorite(objectId: Int): Flow<Boolean>
}