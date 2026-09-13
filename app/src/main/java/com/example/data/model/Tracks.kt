package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

/**
 * Firestore data model representing an audio track and its comprehensive metadata.
 * Can be serialized to/from Firestore collections (e.g. "tracks").
 */
@IgnoreExtraProperties
data class Tracks(
    @DocumentId
    val id: String = "",

    @get:PropertyName("title")
    @set:PropertyName("title")
    var title: String = "",

    @get:PropertyName("artist")
    @set:PropertyName("artist")
    var artist: String = "",

    @get:PropertyName("remixer")
    @set:PropertyName("remixer")
    var remixer: String = "",

    @get:PropertyName("album")
    @set:PropertyName("album")
    var album: String = "",

    @get:PropertyName("genre")
    @set:PropertyName("genre")
    var genre: String = "",

    @get:PropertyName("bpm")
    @set:PropertyName("bpm")
    var bpm: Int = 128,

    @get:PropertyName("musicalKey")
    @set:PropertyName("musicalKey")
    var musicalKey: String = "",

    @get:PropertyName("durationSeconds")
    @set:PropertyName("durationSeconds")
    var durationSeconds: Int = 0,

    @get:PropertyName("audioStreamUrl")
    @set:PropertyName("audioStreamUrl")
    var audioStreamUrl: String = "",

    @get:PropertyName("downloadUrl")
    @set:PropertyName("downloadUrl")
    var downloadUrl: String = "",

    @get:PropertyName("fileFormat")
    @set:PropertyName("fileFormat")
    var fileFormat: String = "MP3",

    @get:PropertyName("bitrateKbps")
    @set:PropertyName("bitrateKbps")
    var bitrateKbps: Int = 320,

    @get:PropertyName("sampleRateHz")
    @set:PropertyName("sampleRateHz")
    var sampleRateHz: Int = 44100,

    @get:PropertyName("fileSizeBytes")
    @set:PropertyName("fileSizeBytes")
    var fileSizeBytes: Long = 0L,

    @get:PropertyName("isFreeDownload")
    @set:PropertyName("isFreeDownload")
    var isFreeDownload: Boolean = true,

    @get:PropertyName("coverImageUrl")
    @set:PropertyName("coverImageUrl")
    var coverImageUrl: String = "",

    @get:PropertyName("tags")
    @set:PropertyName("tags")
    var tags: List<String> = emptyList(),

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("playsCount")
    @set:PropertyName("playsCount")
    var playsCount: Long = 0L,

    @get:PropertyName("downloadsCount")
    @set:PropertyName("downloadsCount")
    var downloadsCount: Long = 0L,

    @get:PropertyName("createdAt")
    @set:PropertyName("createdAt")
    var createdAt: Long = System.currentTimeMillis(),

    @get:PropertyName("updatedAt")
    @set:PropertyName("updatedAt")
    var updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Converts model to Map representation for Firestore document writes.
     */
    fun toMap(): Map<String, Any?> = mapOf(
        "title" to title,
        "artist" to artist,
        "remixer" to remixer,
        "album" to album,
        "genre" to genre,
        "bpm" to bpm,
        "musicalKey" to musicalKey,
        "durationSeconds" to durationSeconds,
        "audioStreamUrl" to audioStreamUrl,
        "downloadUrl" to downloadUrl,
        "fileFormat" to fileFormat,
        "bitrateKbps" to bitrateKbps,
        "sampleRateHz" to sampleRateHz,
        "fileSizeBytes" to fileSizeBytes,
        "isFreeDownload" to isFreeDownload,
        "coverImageUrl" to coverImageUrl,
        "tags" to tags,
        "description" to description,
        "playsCount" to playsCount,
        "downloadsCount" to downloadsCount,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt
    )

    companion object {
        /**
         * Creates a Tracks model from a local Room Song entity.
         */
        fun fromSong(song: Song): Tracks {
            return Tracks(
                id = song.id.toString(),
                title = song.title,
                artist = song.artist,
                remixer = song.artist,
                genre = song.genre,
                bpm = song.bpm,
                durationSeconds = song.durationSec,
                audioStreamUrl = song.fileUrl,
                downloadUrl = song.fileUrl,
                fileFormat = "MP3",
                bitrateKbps = 320,
                isFreeDownload = true,
                tags = song.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                description = song.description,
                playsCount = song.playsCount.toLong(),
                downloadsCount = song.downloadsCount.toLong(),
                createdAt = song.releaseDate
            )
        }
    }
}

/**
 * Typealias for developers or code referring to singular Track.
 */
typealias Track = Tracks
