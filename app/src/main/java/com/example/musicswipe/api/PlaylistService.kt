package com.example.musicswipe.api

import com.example.musicswipe.data.PlaylistResults
import com.example.musicswipe.data.PlaylistTrackAdapter
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PlaylistService {
    @GET("37i9dQZF1DX4JAvHpjipBk")
    @Headers("Authorization: Bearer BQABUTcF-JPJavICx4n5OKcS6Xa6qse4TfIve-6VyKjDDRT92Cyzcr418TVx4oqxcyOYl7ITPBbscF-Ak0iFSgYeE1lk3m6Wqoln73AA34JEq3rs6E3S_rdaQpj0QV7XzGWBvhzRS8_rclb_mO8KiBlv3C-6WggMBUYF73iwSOqEpw-qUYE17gvZVGWstmOTG4ex")
    suspend fun loadNewMusicPlaylist(
        @Query("fields") fields: String = "tracks.items(track(album(images,name),preview_url,name,artists(name),uri))"
    ) : Response<PlaylistResults>

    companion object {
        private const val BASE_URL = "https://api.spotify.com/v1/playlists/"

        fun create() : PlaylistService {
            val moshi = Moshi.Builder()
                .add(PlaylistTrackAdapter())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(PlaylistService::class.java)
        }
    }
}