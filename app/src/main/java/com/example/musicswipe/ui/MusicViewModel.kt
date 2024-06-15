package com.example.musicswipe.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicswipe.api.PlaylistService
import com.example.musicswipe.data.LoadingStatus
import com.example.musicswipe.data.MusicRepository
import com.example.musicswipe.data.PlaylistResults
import kotlinx.coroutines.launch

class MusicViewModel: ViewModel() {
    private val repository = MusicRepository(PlaylistService.create())

    private val _searchResults = MutableLiveData<PlaylistResults?>(null)
    val searchResults: LiveData<PlaylistResults?> = _searchResults

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadPlaylistResults() {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.fetchMusic()
            _searchResults.value = result.getOrNull()
            _errorMessage.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when(result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}