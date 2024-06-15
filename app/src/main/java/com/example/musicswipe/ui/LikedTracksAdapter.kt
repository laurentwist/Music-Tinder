package com.example.musicswipe.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicswipe.R
import com.example.musicswipe.data.Track
import com.example.musicswipe.data.TrackEntity

class LikedTracksAdapter(
    private val onTrackClick: (TrackEntity) -> Unit
): RecyclerView.Adapter<LikedTracksAdapter.LikedTracksViewHolder>() {
    private var likedTracks: List<TrackEntity> = listOf()
    override fun getItemCount() = this.likedTracks.size

    fun updateLikedTracks(newTracks: List<TrackEntity>?) {
        likedTracks = newTracks ?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedTracksViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_entity_list_item, parent, false)
        return LikedTracksViewHolder(itemView, onTrackClick)
    }

    override fun onBindViewHolder(holder: LikedTracksViewHolder, position: Int) {
        holder.bind(this.likedTracks[position])
    }

    class LikedTracksViewHolder(
        itemView: View,
        private val onClick: (TrackEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val songTV: TextView = itemView.findViewById(R.id.tv_song_name)
        private val artistTV: TextView = itemView.findViewById(R.id.tv_artist_name)
        private val uri: TextView = itemView.findViewById(R.id.tv_uri)
        private lateinit var currentTrack: TrackEntity

        init {
            itemView.setOnClickListener{
                currentTrack?.let(onClick)
            }
        }

        fun bind(track: TrackEntity) {
            currentTrack = track
            songTV.text = track.song
            artistTV.text = track.artist
            uri.text = track.uri
        }
    }

}