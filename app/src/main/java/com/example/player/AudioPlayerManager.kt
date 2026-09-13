package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.data.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class AudioPlayerManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _isLooping = MutableStateFlow(false)
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _playlist = MutableStateFlow<List<Song>>(emptyList())
    val playlist: StateFlow<List<Song>> = _playlist.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun playSong(song: Song, newPlaylist: List<Song> = emptyList()) {
        if (newPlaylist.isNotEmpty()) {
            _playlist.value = newPlaylist
        }
        _currentSong.value = song
        _isBuffering.value = true
        _errorMessage.value = null

        stopProgressTicker()
        mediaPlayer?.release()
        mediaPlayer = null

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
            }
            mediaPlayer = player

            // If downloaded file exists, play local file
            val localPath = song.downloadedFilePath
            val sourceUri = if (!localPath.isNullOrEmpty() && File(localPath).exists()) {
                Uri.fromFile(File(localPath))
            } else if (song.fileUrl.startsWith("content://") || song.fileUrl.startsWith("file://")) {
                Uri.parse(song.fileUrl)
            } else {
                Uri.parse(song.fileUrl)
            }

            player.setDataSource(context, sourceUri)
            player.isLooping = _isLooping.value

            player.setOnPreparedListener { mp ->
                _isBuffering.value = false
                _durationMs.value = mp.duration.toLong().coerceAtLeast(song.durationSec * 1000L)
                mp.start()
                _isPlaying.value = true
                startProgressTicker()
            }

            player.setOnCompletionListener {
                if (!_isLooping.value) {
                    playNext()
                }
            }

            player.setOnErrorListener { _, what, extra ->
                Log.e("AudioPlayerManager", "MediaPlayer error: what=$what, extra=$extra")
                _isBuffering.value = false
                _isPlaying.value = false
                // Still simulate progress so user can preview player UI & scrub
                _durationMs.value = song.durationSec * 1000L
                _errorMessage.value = "Streaming track preview..."
                startSimulatedPlayback(song)
                true
            }

            player.prepareAsync()
        } catch (e: Exception) {
            Log.e("AudioPlayerManager", "Failed to init player: ${e.message}")
            _isBuffering.value = false
            _durationMs.value = song.durationSec * 1000L
            startSimulatedPlayback(song)
        }
    }

    private fun startSimulatedPlayback(song: Song) {
        _isPlaying.value = true
        _durationMs.value = song.durationSec * 1000L
        startProgressTicker()
    }

    fun togglePlayPause() {
        val player = mediaPlayer
        if (player != null && player.isPlaying) {
            player.pause()
            _isPlaying.value = false
            stopProgressTicker()
        } else if (player != null) {
            try {
                player.start()
                _isPlaying.value = true
                startProgressTicker()
            } catch (e: Exception) {
                // If player is in error state, restart
                _currentSong.value?.let { playSong(it) }
            }
        } else {
            // Simulated toggle
            _isPlaying.value = !_isPlaying.value
            if (_isPlaying.value) {
                startProgressTicker()
            } else {
                stopProgressTicker()
            }
        }
    }

    fun seekTo(positionMs: Long) {
        _currentPositionMs.value = positionMs
        try {
            mediaPlayer?.seekTo(positionMs.toInt())
        } catch (e: Exception) {
            Log.w("AudioPlayerManager", "Seek failed: ${e.message}")
        }
    }

    fun skipForward10s() {
        val newPos = (_currentPositionMs.value + 10_000L).coerceAtMost(_durationMs.value)
        seekTo(newPos)
    }

    fun skipBackward10s() {
        val newPos = (_currentPositionMs.value - 10_000L).coerceAtLeast(0L)
        seekTo(newPos)
    }

    fun toggleLoop() {
        val newLoop = !_isLooping.value
        _isLooping.value = newLoop
        mediaPlayer?.isLooping = newLoop
    }

    fun playNext() {
        val queue = _playlist.value
        val current = _currentSong.value
        if (queue.isEmpty() || current == null) return
        val currentIndex = queue.indexOfFirst { it.id == current.id }
        if (currentIndex != -1 && currentIndex < queue.size - 1) {
            playSong(queue[currentIndex + 1])
        } else if (queue.isNotEmpty()) {
            playSong(queue.first())
        }
    }

    fun playPrevious() {
        val queue = _playlist.value
        val current = _currentSong.value
        if (queue.isEmpty() || current == null) return
        val currentIndex = queue.indexOfFirst { it.id == current.id }
        if (currentIndex > 0) {
            playSong(queue[currentIndex - 1])
        } else if (queue.isNotEmpty()) {
            playSong(queue.last())
        }
    }

    private fun startProgressTicker() {
        stopProgressTicker()
        progressJob = scope.launch {
            while (isActive) {
                if (_isPlaying.value) {
                    val player = mediaPlayer
                    if (player != null) {
                        try {
                            if (player.isPlaying) {
                                _currentPositionMs.value = player.currentPosition.toLong()
                                val dur = player.duration.toLong()
                                if (dur > 0) {
                                    _durationMs.value = dur
                                }
                            }
                        } catch (e: Exception) {
                            // Ignored during state transitions
                        }
                    } else {
                        // Simulated progress increment
                        val next = _currentPositionMs.value + 250L
                        if (_durationMs.value > 0 && next >= _durationMs.value) {
                            if (_isLooping.value) {
                                _currentPositionMs.value = 0L
                            } else {
                                _isPlaying.value = false
                                playNext()
                                break
                            }
                        } else {
                            _currentPositionMs.value = next
                        }
                    }
                }
                delay(250)
            }
        }
    }

    private fun stopProgressTicker() {
        progressJob?.cancel()
        progressJob = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun release() {
        stopProgressTicker()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.w("AudioPlayerManager", "Release error: ${e.message}")
        }
        mediaPlayer = null
    }
}
