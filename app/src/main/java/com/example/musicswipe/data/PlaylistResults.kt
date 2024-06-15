package com.example.musicswipe.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistResults(
    val tracks: Tracks
) : java.io.Serializable
