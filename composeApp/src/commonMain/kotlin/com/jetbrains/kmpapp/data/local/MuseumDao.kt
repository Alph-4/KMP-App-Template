package com.jetbrains.kmpapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jetbrains.kmpapp.data.local.model.MuseumObject
import kotlinx.coroutines.flow.Flow

@Dao
interface MuseumDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(museumObject: MuseumObject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(museumObject: MuseumObject)

    @Query("UPDATE museum_objects SET isFavorite = :isFavorite WHERE objectID = :objectId")
    suspend fun updateFavoriteStatus(objectId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(museumObjects: List<MuseumObject>)

    @Query("SELECT * FROM museum_objects")
    fun getAll(): Flow<List<MuseumObject>>

    @Query("SELECT * FROM museum_objects WHERE objectID = :objectId")
    fun getObjectById(objectId: Int): Flow<MuseumObject?>

    @Delete
    suspend fun delete(museumObject: MuseumObject)

    @Query("DELETE FROM museum_objects WHERE objectID = :objectId")
    suspend fun deleteById(objectId: Int)

    @Query("SELECT * FROM museum_objects")
    fun getAllFavorites(): Flow<List<MuseumObject>>

    @Query("SELECT EXISTS(SELECT 1 FROM museum_objects WHERE objectID = :objectId)")
    fun isFavorite(objectId: Int): Flow<Boolean>

}