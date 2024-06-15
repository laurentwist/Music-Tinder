package com.example.musicswipe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrackEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTracksDao(): TracksDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "likedTracks.db"
            ).build()

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) { //prevent multiple threads from using
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}