package com.example.musicswipe.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

data class Track(
    val album: String,
    val imageURL: String,
    val imageHeight: Int,
    val imageWidth: Int,
    val artists: List<String>,
    val mainArtist: String,
    val songName: String,
    val song: String?,
    val uri: String
) : java.io.Serializable

@JsonClass(generateAdapter = true)
data class PlaylistItemsJson(
    val track: PlaylistTrackJson
)

@JsonClass(generateAdapter = true)
data class PlaylistTrackJson(
    val album: PlaylistAlbumJson,
    val artists: List<PlaylistArtistJson>,
    val name: String,
    val preview_url: String?,
    val uri: String,
)

@JsonClass(generateAdapter = true)
data class PlaylistAlbumJson(
    val images: List<PlaylistAlbumImageJson>,
    val name: String
)

@JsonClass(generateAdapter = true)
data class PlaylistAlbumImageJson(
    val height: Int,
    val url: String,
    val width: Int
)

@JsonClass(generateAdapter = true)
data class PlaylistArtistJson(
    val name: String
)

class PlaylistTrackAdapter{
    @FromJson
    fun trackFromJson(track: PlaylistItemsJson) = Track(
        album = track.track.album.name,
        imageURL = track.track.album.images[0].url,
        imageHeight = track.track.album.images[0].height,
        imageWidth = track.track.album.images[0].width,
        artists = listOf(track.track.artists.toString()),
        mainArtist = track.track.artists[0].name,
        songName = track.track.name,
        song = track.track.preview_url,
        uri = track.track.uri
    )

    @ToJson
    fun trackToJson(track: Track): String {
        throw UnsupportedOperationException("encoding Track to JSON is not supported")
    }
}