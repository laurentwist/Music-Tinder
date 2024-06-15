package com.example.musicswipe.data

import android.util.Log
import com.example.musicswipe.api.PlaylistService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicRepository (
    private val service: PlaylistService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fetchMusic(): Result<PlaylistResults?> =
        withContext(dispatcher) {
        try {
            val response = service.loadNewMusicPlaylist()
            Log.d("Repository", response.body().toString())
            if(response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Log.d("RespositoryError", e.toString())
            Result.failure(e)
        }
    }
}