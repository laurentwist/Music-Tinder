package com.example.musicswipe.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicswipe.R
import com.example.musicswipe.data.PlaylistResults
import com.example.musicswipe.data.Track
import com.example.musicswipe.data.TrackEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class PlaylistTrackListAdapter(
    private val onThumbsDownClick: (Track) -> Unit,
    private val onThumbsUpClick: (Track) -> Unit,
    private val onPlayButtonClick: (Track) -> Unit
) : RecyclerView.Adapter<PlaylistTrackListAdapter.PlaylistViewHolder>(){
    private var playlistList: List<Track> = listOf()

    override fun getItemCount() = this.playlistList.size


    fun updatePlaylist(newPlaylist: PlaylistResults?) {
        playlistList = newPlaylist?.tracks?.songs ?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_card, parent, false)
        return PlaylistViewHolder(itemView, onThumbsDownClick, onThumbsUpClick, onPlayButtonClick)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(this.playlistList[position])
    }


    class PlaylistViewHolder(
        itemView: View,
        onThumbsDownClick: (Track) -> Unit,
        onThumbsUpClick: (Track) -> Unit,
        onPlayButtonClick: (Track) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val songTV: TextView = itemView.findViewById(R.id.song_title)
        private val artistTV: TextView = itemView.findViewById(R.id.song_artist)
        private val albumCover: ImageView = itemView.findViewById(R.id.song_cover)
        private lateinit var currentTrack: Track
        private val thumbsDownBtn: ImageView = itemView.findViewById(R.id.thumbs_down_icon)
        private val thumbsUpBtn: ImageView = itemView.findViewById(R.id.thumbs_up_icon)
        private val playBtn: FloatingActionButton = itemView.findViewById(R.id.play_button)

        init {
            thumbsDownBtn.setOnClickListener {
                currentTrack.let(onThumbsDownClick)
            }
            thumbsUpBtn.setOnClickListener {
                currentTrack.let(onThumbsUpClick)
            }
            playBtn.setOnClickListener {
                currentTrack.let(onPlayButtonClick)
            }
        }

        fun bind(track: Track) {
            Log.d("bind", track.toString())
            currentTrack = track
            currentTrack = track
            songTV.text = track.songName
            artistTV.text = track.mainArtist
            Picasso.get().load(track.imageURL).into(albumCover)
        }
    }
}




