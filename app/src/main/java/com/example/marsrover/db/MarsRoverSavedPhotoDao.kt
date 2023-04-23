package com.example.marsrover.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MarsRoverSavedPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marsRoverSavedLocalModel: MarsRoverSavedLocalModel)

    @Delete
    suspend fun delete(marsRoverSavedLocalModel: MarsRoverSavedLocalModel)

    @Query("SELECT roverPhotoId FROM rover_photo WHERE sol = :sol AND roverName = :roverName")
    fun allSavedIds(sol: String, roverName: String): Flow<List<Int>>

    @Query("SELECT * FROM rover_photo ORDER BY earthDate DESC")
    fun getAllSaved(): Flow<List<MarsRoverSavedLocalModel>>
}