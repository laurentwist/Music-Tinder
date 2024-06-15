package com.example.musicswipe.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tracks(
        @Json(name = "items") val songs: List<Track>
) : java.io.Serializable