package com.example.musicswipe.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.example.musicswipe.R
import com.example.musicswipe.data.LoadingStatus
import com.example.musicswipe.data.Track
import com.example.musicswipe.data.TrackEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track as TrackSDK;

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"

    private val clientId = "dcfdb30800d445d2888c475760f4d25f"
    private val redirectURI = "https://com.example.musicswipe/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val viewModel: MusicViewModel by viewModels()
    private val DbViewModel: LikedTracksViewModel by viewModels()

    private val playlistAdapter = PlaylistTrackListAdapter(::onThumbsDownClick, ::onThumbsUpClick, ::onPlayButtonClick)

    private lateinit var playlistResultsRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    private var scrollPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadingErrorTV = findViewById(R.id.tv_loading_error)
        loadingIndicator = findViewById(R.id.loading_indicator)
        playlistResultsRV = findViewById(R.id.rv_playlist_results)
        playlistResultsRV.layoutManager = LinearLayoutManager(this)
        playlistResultsRV.setHasFixedSize(true)
        playlistResultsRV.adapter = playlistAdapter

        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectURI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Android SDK Connected")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
            }
        })

        viewModel.searchResults.observe(this) { searchResults ->
            Log.d("updatePlaylist", searchResults.toString())
            playlistAdapter.updatePlaylist(searchResults)
        }

        viewModel.loadingStatus.observe(this) { loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    playlistResultsRV.visibility = View.INVISIBLE
                    loadingErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    playlistResultsRV.visibility = View.INVISIBLE
                    loadingErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    playlistResultsRV.visibility = View.VISIBLE
                    loadingErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Log.d(tag, "Error making API call: $errorMessage")
            }
        }

        viewModel.loadPlaylistResults()
    }

    private fun playSong(URI: String) {
        spotifyAppRemote?.playerApi?.play(URI)
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            val track: TrackSDK = it.track
            Log.d("MainActivity", track.name + " by " + track.artist.name)
        }
    }

    private fun stopSong() {
        spotifyAppRemote?.playerApi?.pause()
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            val track: TrackSDK = it.track
            Log.d("MainActivity", "paused " + track.name)
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_open_spotify -> {
                openSpotify()
                true
            }
            R.id.action_view_liked_tracks -> {
                val intent = Intent(this, LikedTracksActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun openSpotify() {
        val spotifyUri = Uri.parse(getString(R.string.spotify_uri))
        val intent = Intent(Intent.ACTION_VIEW, spotifyUri)
        try {
            startActivity(intent)
        } catch(e: ActivityNotFoundException) {
            Snackbar.make(
                findViewById(R.id.coordinator_layout),
                R.string.action_spotify_error,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
    
    private fun onThumbsDownClick(track: Track) {
        stopSong()
        scrollPosition++
        playlistResultsRV.smoothScrollToPosition(scrollPosition)
        val playButton: FloatingActionButton = findViewById(R.id.play_button)
        playButton.show()
    }

    private fun onThumbsUpClick(track: Track) {
        stopSong()
        DbViewModel.addLikedTrack(TrackEntity(uri=track.uri, song=track.songName, artist = track.mainArtist))
        scrollPosition++
        playlistResultsRV.smoothScrollToPosition(scrollPosition)
        val playButton: FloatingActionButton = findViewById(R.id.play_button)
        playButton.show()
    }

    private fun onPlayButtonClick(track: Track) {
        val playButton: FloatingActionButton = findViewById(R.id.play_button)
        playButton.hide()
        playSong(track.uri)
    }
}
