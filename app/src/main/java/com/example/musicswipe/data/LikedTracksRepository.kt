package com.example.musicswipe.data

class LikedTracksRepository(
    private val dao: TracksDao
) {
    suspend fun insertLikedTrack(track: TrackEntity) = dao.insert(track)
    suspend fun deleteLikedTrack(track: TrackEntity) = dao.delete(track)

    fun getAllLikedTracks() = dao.getAllTracks()

}