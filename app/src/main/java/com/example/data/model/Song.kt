package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artist: String = "DJ Remixer",
    val genre: String = "Bollywood Remix",
    val bpm: Int = 128,
    val durationSec: Int = 210,
    val fileUrl: String,
    val coverUrl: String = "",
    val downloadUrl: String = "",
    val isDownloaded: Boolean = false,
    val downloadedFilePath: String? = null,
    val playsCount: Int = 1250,
    val downloadsCount: Int = 450,
    val isFeatured: Boolean = false,
    val tags: String = "#Club #DholkiMix #320kbps",
    val releaseDate: Long = System.currentTimeMillis(),
    val description: String = "High quality 320kbps club DJ remix. Free download for personal and event use."
)
