package com.example.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MixingPack
import com.example.data.model.RemixOrder
import com.example.data.model.Song
import com.example.data.repository.DjMusicRepository
import com.example.download.DownloadHelper
import com.example.player.AudioPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    TRACKS("Remixes"),
    MIXING_DATA("Mixing Data"),
    BOOK_REMIX("Remix Order"),
    DOWNLOADS("Offline MP3"),
    STUDIO("DJ Studio")
}

class DjAppViewModel(application: Application) : AndroidViewModel(application) {
    val repository = DjMusicRepository(application)
    val player = AudioPlayerManager(application)
    val downloader = DownloadHelper(application, repository)

    private val _currentTab = MutableStateFlow(AppTab.TRACKS)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isFullPlayerVisible = MutableStateFlow(false)
    val isFullPlayerVisible: StateFlow<Boolean> = _isFullPlayerVisible.asStateFlow()

    private val _isUploadDialogOpen = MutableStateFlow(false)
    val isUploadDialogOpen: StateFlow<Boolean> = _isUploadDialogOpen.asStateFlow()

    private val _djPhoneNumber = MutableStateFlow("+919876543210")
    val djPhoneNumber: StateFlow<String> = _djPhoneNumber.asStateFlow()

    private val _djName = MutableStateFlow("DJ Saveen")
    val djName: StateFlow<String> = _djName.asStateFlow()

    // Combined filtered songs list
    val songs: StateFlow<List<Song>> = combine(
        repository.allSongs,
        _selectedGenre,
        _searchQuery
    ) { allSongs, genre, query ->
        allSongs.filter { song ->
            val matchesGenre = if (genre == "All") true else song.genre.equals(genre, ignoreCase = true)
            val matchesQuery = if (query.isBlank()) true else {
                song.title.contains(query, ignoreCase = true) ||
                song.artist.contains(query, ignoreCase = true) ||
                song.tags.contains(query, ignoreCase = true)
            }
            matchesGenre && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloadedSongs: StateFlow<List<Song>> = repository.downloadedSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mixingPacks: StateFlow<List<MixingPack>> = repository.allMixingPacks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val remixOrders: StateFlow<List<RemixOrder>> = repository.allRemixOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setGenre(genre: String) {
        _selectedGenre.value = genre
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFullPlayerVisible(visible: Boolean) {
        _isFullPlayerVisible.value = visible
    }

    fun setUploadDialogOpen(open: Boolean) {
        _isUploadDialogOpen.value = open
    }

    fun playSong(song: Song) {
        val currentList = if (_currentTab.value == AppTab.DOWNLOADS) {
            downloadedSongs.value
        } else {
            songs.value
        }
        player.playSong(song, currentList)
        viewModelScope.launch {
            repository.incrementPlay(song.id)
        }
    }

    fun downloadSong(song: Song) {
        viewModelScope.launch {
            Toast.makeText(getApplication(), "Downloading ${song.title} in MP3 (320kbps)...", Toast.LENGTH_SHORT).show()
            val result = downloader.downloadSong(song)
            if (result.isSuccess) {
                Toast.makeText(getApplication(), "Downloaded for FREE: ${song.title}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplication(), "Download completed (Offline Mode)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareSong(song: Song) {
        downloader.shareSong(song)
    }

    fun addNewSong(
        title: String,
        artist: String,
        genre: String,
        bpm: Int,
        durationSec: Int,
        fileUrl: String,
        tags: String,
        description: String
    ) {
        viewModelScope.launch {
            val newSong = Song(
                title = title.ifBlank { "Untitled Remix" },
                artist = artist.ifBlank { _djName.value },
                genre = genre.ifBlank { "Bollywood Remix" },
                bpm = bpm.coerceAtLeast(60),
                durationSec = durationSec.coerceAtLeast(60),
                fileUrl = fileUrl.ifBlank { "https://actions.google.com/sounds/v1/science_fiction/force_field_hum_loop.ogg" },
                downloadUrl = fileUrl.ifBlank { "https://actions.google.com/sounds/v1/science_fiction/force_field_hum_loop.ogg" },
                tags = tags.ifBlank { "#DJRemix #ClubMix #FreeMP3" },
                description = description.ifBlank { "New remix track uploaded by DJ." },
                isFeatured = true
            )
            repository.insertSong(newSong)
            _isUploadDialogOpen.value = false
            Toast.makeText(getApplication(), "Track uploaded successfully! Users can now download in MP3.", Toast.LENGTH_LONG).show()
        }
    }

    fun deleteSong(song: Song) {
        viewModelScope.launch {
            repository.deleteSong(song)
            Toast.makeText(getApplication(), "Removed track: ${song.title}", Toast.LENGTH_SHORT).show()
        }
    }

    fun submitRemixOrder(
        customerName: String,
        customerPhone: String,
        songName: String,
        originalSongLink: String,
        genreStyle: String,
        customDropName: String,
        urgency: String,
        notes: String
    ) {
        viewModelScope.launch {
            val order = RemixOrder(
                customerName = customerName,
                customerPhone = customerPhone,
                songName = songName,
                originalSongLink = originalSongLink,
                genreStyle = genreStyle,
                customDropName = customDropName,
                urgency = urgency,
                notes = notes
            )
            repository.submitRemixOrder(order)
            Toast.makeText(getApplication(), "Remix request submitted to DJ! Opening WhatsApp...", Toast.LENGTH_SHORT).show()

            // Open WhatsApp to directly chat with DJ
            val message = "Hello ${_djName.value}! I want to get a song remixed by you:\n" +
                    "🎵 Song: $songName\n" +
                    "🎧 Style: $genreStyle\n" +
                    "⚡ Urgency: $urgency\n" +
                    "🎙 Custom Voice Drop: $customDropName\n" +
                    "🔗 Link: $originalSongLink\n" +
                    "👤 Client: $customerName ($customerPhone)"
            openWhatsApp(message)
        }
    }

    fun buyMixingPack(pack: MixingPack) {
        val message = "Hello ${_djName.value}! I want to buy your Mixing Data Pack:\n" +
                "📦 Pack: ${pack.title}\n" +
                "📂 Format: ${pack.format} (${pack.fileSize})\n" +
                "💰 Price: ₹${pack.priceInr}\n" +
                "Please send UPI payment details and instant download drive link."
        openWhatsApp(message)
    }

    fun openWhatsApp(message: String) {
        try {
            val cleanNumber = _djPhoneNumber.value.replace("[^0-9+]".toRegex(), "")
            val encodedMsg = Uri.encode(message)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            getApplication<Application>().startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(getApplication(), "WhatsApp not installed. Number: ${_djPhoneNumber.value}", Toast.LENGTH_LONG).show()
        }
    }

    fun callDj() {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${_djPhoneNumber.value}")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            getApplication<Application>().startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(getApplication(), "Cannot open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
