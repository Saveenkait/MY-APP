package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY id DESC")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isDownloaded = 1 ORDER BY id DESC")
    fun getDownloadedSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isFeatured = 1 ORDER BY id DESC")
    fun getFeaturedSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE genre = :genre ORDER BY id DESC")
    fun getSongsByGenre(genre: String): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchSongs(query: String): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE id = :id LIMIT 1")
    suspend fun getSongById(id: Long): Song?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<Song>)

    @Update
    suspend fun updateSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("UPDATE songs SET isDownloaded = :isDownloaded, downloadedFilePath = :path, downloadsCount = downloadsCount + 1 WHERE id = :id")
    suspend fun updateDownloadStatus(id: Long, isDownloaded: Boolean, path: String?)

    @Query("UPDATE songs SET playsCount = playsCount + 1 WHERE id = :id")
    suspend fun incrementPlayCount(id: Long)

    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getSongCount(): Int
}
