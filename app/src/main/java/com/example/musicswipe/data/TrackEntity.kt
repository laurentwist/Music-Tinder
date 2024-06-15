package com.example.musicswipe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackEntity(
    @PrimaryKey
    val uri: String,
    val song: String,
    val artist: String,
)
