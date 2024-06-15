package com.example.musicswipe.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: TrackEntity)

    @Delete
    suspend fun delete(track: TrackEntity)

    @Query("SELECT * FROM TrackEntity")
    fun getAllTracks(): Flow<List<TrackEntity>>
}