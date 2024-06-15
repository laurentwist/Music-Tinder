package com.example.musicswipe.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicswipe.R
import com.example.musicswipe.data.TrackEntity
import com.google.android.material.snackbar.Snackbar

class LikedTracksActivity : AppCompatActivity() {
    private val trackListAdapter = LikedTracksAdapter(::onTrackClick)
    private lateinit var likedTracksRV: RecyclerView
    private val viewModel: LikedTracksViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liked_tracks)
        if (intent != null) {
            likedTracksRV = findViewById(R.id.rv_liked_tracks)
            likedTracksRV.layoutManager = LinearLayoutManager(this)
            likedTracksRV.setHasFixedSize(true)
            likedTracksRV.adapter = this.trackListAdapter

            viewModel.likedTracks.observe(this) { likedTracksRV ->
                trackListAdapter.updateLikedTracks(likedTracksRV)
            }
        }
    }

    private fun onTrackClick(track: TrackEntity) {
        val songUri = Uri.parse(track.uri)
        val intent = Intent(Intent.ACTION_VIEW, songUri)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(
                findViewById(R.id.coordinator_layout),
                R.string.action_spotify_error,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}