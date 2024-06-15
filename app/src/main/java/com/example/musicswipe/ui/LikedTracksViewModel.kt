package com.example.musicswipe.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicswipe.data.AppDatabase
import com.example.musicswipe.data.LikedTracksRepository
import com.example.musicswipe.data.TrackEntity
import kotlinx.coroutines.launch

class LikedTracksViewModel(application: Application): AndroidViewModel(application) {
    private val repository = LikedTracksRepository(
        AppDatabase.getInstance(application).getTracksDao()
    )

    val likedTracks = repository.getAllLikedTracks().asLiveData()

    fun getLikedTracksAmount() : Int? {
        return likedTracks.value?.size
    }

    fun addLikedTrack(track: TrackEntity) {
        viewModelScope.launch {
            repository.insertLikedTrack(track)
        }
    }

    fun removeLikedTrack(track: TrackEntity) {
        viewModelScope.launch {
            repository.deleteLikedTrack(track)
        }
    }
}