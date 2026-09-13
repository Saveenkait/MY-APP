package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FullPlayerDialog
import com.example.ui.components.MiniPlayerBar
import com.example.ui.screens.DjStudioScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.MixingDataScreen
import com.example.ui.screens.RemixRequestScreen
import com.example.ui.screens.TracksScreen
import com.example.ui.screens.UploadSongDialog
import com.example.ui.theme.DjBackground
import com.example.ui.theme.DjPrimaryCyan
import com.example.ui.theme.DjSurface
import com.example.ui.theme.DjTextMuted
import com.example.ui.theme.DjTextPrimary
import com.example.ui.theme.DjTextSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.DjAppViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DjAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DjAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DjAppRoot(viewModel: DjAppViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val downloadedSongs by viewModel.downloadedSongs.collectAsStateWithLifecycle()
    val mixingPacks by viewModel.mixingPacks.collectAsStateWithLifecycle()
    val remixOrders by viewModel.remixOrders.collectAsStateWithLifecycle()

    val currentSong by viewModel.player.currentSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.player.isPlaying.collectAsStateWithLifecycle()
    val currentPosMs by viewModel.player.currentPositionMs.collectAsStateWithLifecycle()
    val durationMs by viewModel.player.durationMs.collectAsStateWithLifecycle()
    val isLooping by viewModel.player.isLooping.collectAsStateWithLifecycle()

    val downloadingIds by viewModel.downloader.downloadingIds.collectAsStateWithLifecycle()
    val downloadProgress by viewModel.downloader.downloadProgress.collectAsStateWithLifecycle()

    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isFullPlayerVisible by viewModel.isFullPlayerVisible.collectAsStateWithLifecycle()
    val isUploadDialogOpen by viewModel.isUploadDialogOpen.collectAsStateWithLifecycle()
    val djName by viewModel.djName.collectAsStateWithLifecycle()
    val djPhone by viewModel.djPhoneNumber.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = DjBackground,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                // Mini Audio Player Bar
                AnimatedVisibility(
                    visible = currentSong != null,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    currentSong?.let { song ->
                        MiniPlayerBar(
                            song = song,
                            isPlaying = isPlaying,
                            currentPosMs = currentPosMs,
                            durationMs = durationMs,
                            onPlayPauseClick = { viewModel.player.togglePlayPause() },
                            onNextClick = { viewModel.player.playNext() },
                            onBarClick = { viewModel.setFullPlayerVisible(true) }
                        )
                    }
                }

                // Material 3 Bottom Navigation Bar
                NavigationBar(
                    containerColor = DjSurface,
                    contentColor = DjTextPrimary,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("main_bottom_nav_bar")
                ) {
                    val tabs = listOf(
                        Triple(AppTab.TRACKS, Icons.Default.Headphones, "Remixes"),
                        Triple(AppTab.MIXING_DATA, Icons.Default.Dataset, "Mixing Data"),
                        Triple(AppTab.BOOK_REMIX, Icons.Default.MusicNote, "Book Remix"),
                        Triple(AppTab.DOWNLOADS, Icons.Default.DownloadDone, "Offline MP3"),
                        Triple(AppTab.STUDIO, Icons.Default.Tune, "DJ Studio")
                    )

                    tabs.forEach { (tab, icon, label) ->
                        val selected = currentTab == tab
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.setTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (selected) Color.Black else DjTextSecondary
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) DjPrimaryCyan else DjTextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = DjPrimaryCyan,
                                selectedIconColor = Color.Black,
                                unselectedIconColor = DjTextSecondary
                            ),
                            modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.TRACKS -> {
                    TracksScreen(
                        songs = songs,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        downloadingIds = downloadingIds,
                        downloadProgress = downloadProgress,
                        selectedGenre = selectedGenre,
                        searchQuery = searchQuery,
                        djName = djName,
                        onGenreSelect = { viewModel.setGenre(it) },
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onSongPlay = { viewModel.playSong(it) },
                        onSongDownload = { viewModel.downloadSong(it) },
                        onSongShare = { viewModel.shareSong(it) },
                        onUploadClick = { viewModel.setUploadDialogOpen(true) }
                    )
                }

                AppTab.MIXING_DATA -> {
                    MixingDataScreen(
                        packs = mixingPacks,
                        djName = djName,
                        onBuyPack = { viewModel.buyMixingPack(it) },
                        onContactCustomPack = { viewModel.openWhatsApp(it) }
                    )
                }

                AppTab.BOOK_REMIX -> {
                    RemixRequestScreen(
                        remixOrders = remixOrders,
                        djName = djName,
                        djPhone = djPhone,
                        onSubmitOrder = { name, phone, song, link, style, drop, urgency, notes ->
                            viewModel.submitRemixOrder(name, phone, song, link, style, drop, urgency, notes)
                        },
                        onCallDj = { viewModel.callDj() },
                        onDirectWhatsApp = { viewModel.openWhatsApp(it) }
                    )
                }

                AppTab.DOWNLOADS -> {
                    DownloadsScreen(
                        downloadedSongs = downloadedSongs,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onSongPlay = { viewModel.playSong(it) },
                        onSongShare = { viewModel.shareSong(it) },
                        onExploreClick = { viewModel.setTab(AppTab.TRACKS) }
                    )
                }

                AppTab.STUDIO -> {
                    DjStudioScreen(
                        songs = songs,
                        djName = djName,
                        djPhone = djPhone,
                        onUploadClick = { viewModel.setUploadDialogOpen(true) },
                        onDeleteSong = { viewModel.deleteSong(it) },
                        onPlaySong = { viewModel.playSong(it) }
                    )
                }
            }
        }
    }

    // Full Player Modal Dialog
    if (isFullPlayerVisible && currentSong != null) {
        FullPlayerDialog(
            song = currentSong!!,
            isPlaying = isPlaying,
            currentPosMs = currentPosMs,
            durationMs = durationMs,
            isLooping = isLooping,
            isDownloading = downloadingIds.contains(currentSong!!.id),
            onDismiss = { viewModel.setFullPlayerVisible(false) },
            onPlayPauseClick = { viewModel.player.togglePlayPause() },
            onSeekTo = { viewModel.player.seekTo(it) },
            onSkipNext = { viewModel.player.playNext() },
            onSkipPrevious = { viewModel.player.playPrevious() },
            onForward10 = { viewModel.player.skipForward10s() },
            onRewind10 = { viewModel.player.skipBackward10s() },
            onToggleLoop = { viewModel.player.toggleLoop() },
            onDownloadClick = { currentSong?.let { viewModel.downloadSong(it) } },
            onShareClick = { currentSong?.let { viewModel.shareSong(it) } },
            onOrderRemixClick = { viewModel.setTab(AppTab.BOOK_REMIX) }
        )
    }

    // Upload Song Modal Dialog
    if (isUploadDialogOpen) {
        UploadSongDialog(
            defaultArtist = djName,
            onDismiss = { viewModel.setUploadDialogOpen(false) },
            onUpload = { title, artist, genre, bpm, duration, fileUrl, tags, description ->
                viewModel.addNewSong(title, artist, genre, bpm, duration, fileUrl, tags, description)
            }
        )
    }
}
