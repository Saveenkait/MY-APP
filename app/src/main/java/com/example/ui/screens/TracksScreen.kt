package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Song
import com.example.ui.components.SongCard
import com.example.ui.theme.DjAccentAmber
import com.example.ui.theme.DjAccentPurple
import com.example.ui.theme.DjBorder
import com.example.ui.theme.DjGreenNeon
import com.example.ui.theme.DjPrimaryCyan
import com.example.ui.theme.DjSecondaryPink
import com.example.ui.theme.DjSurface
import com.example.ui.theme.DjSurfaceElevated
import com.example.ui.theme.DjSurfaceVariant
import com.example.ui.theme.DjTextMuted
import com.example.ui.theme.DjTextPrimary
import com.example.ui.theme.DjTextSecondary

@Composable
fun TracksScreen(
    songs: List<Song>,
    currentSong: Song?,
    isPlaying: Boolean,
    downloadingIds: Set<Long>,
    downloadProgress: Map<Long, Float>,
    selectedGenre: String,
    searchQuery: String,
    djName: String,
    onGenreSelect: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onSongPlay: (Song) -> Unit,
    onSongDownload: (Song) -> Unit,
    onSongShare: (Song) -> Unit,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val genres = listOf(
        "All",
        "Bollywood Remix",
        "Punjabi Dhol",
        "Club EDM",
        "Bhojpuri Mix",
        "Lo-Fi Mashup"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tracks_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Hero DJ Banner
        item {
            HeroBanner(
                djName = djName,
                totalTracks = songs.size,
                onUploadClick = onUploadClick
            )
        }

        // Search Bar
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_song_input"),
                    placeholder = { Text("Search songs, artists, #highbass...", color = DjTextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = DjPrimaryCyan)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = DjTextSecondary)
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = customTextFieldColors(),
                    singleLine = true
                )
            }
        }

        // Genre Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { genre ->
                    val isSelected = selectedGenre == genre
                    FilterChip(
                        selected = isSelected,
                        onClick = { onGenreSelect(genre) },
                        label = {
                            Text(
                                text = genre,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else DjTextSecondary,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DjPrimaryCyan,
                            containerColor = DjSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) DjPrimaryCyan else DjBorder,
                            enabled = true,
                            selected = isSelected
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = DjSecondaryPink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedGenre == "All") "All Remix Tracks" else "$selectedGenre Tracks",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DjTextPrimary
                        )
                    )
                }

                Text(
                    text = "${songs.size} Free MP3s",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DjGreenNeon,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // Empty state
        if (songs.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicOff,
                        contentDescription = null,
                        tint = DjTextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No remix tracks found",
                        style = MaterialTheme.typography.titleMedium.copy(color = DjTextPrimary)
                    )
                    Text(
                        text = "Try another search keyword or genre filter, or upload your new mix!",
                        style = MaterialTheme.typography.bodySmall.copy(color = DjTextSecondary),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            items(songs, key = { it.id }) { song ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SongCard(
                        song = song,
                        isPlaying = isPlaying,
                        isCurrentSong = currentSong?.id == song.id,
                        isDownloading = downloadingIds.contains(song.id),
                        downloadProgress = downloadProgress[song.id],
                        onPlayClick = { onSongPlay(song) },
                        onDownloadClick = { onSongDownload(song) },
                        onShareClick = { onSongShare(song) }
                    )
                }
            }
        }
    }
}

@Composable
fun HeroBanner(
    djName: String,
    totalTracks: Int,
    onUploadClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = DjSurfaceVariant,
        tonalElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            // Background DJ artwork
            Image(
                painter = painterResource(id = R.drawable.dj_banner),
                contentDescription = "DJ Console Hero",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x600C0A14),
                                Color(0xE00C0A14),
                                Color(0xFF0C0A14)
                            )
                        )
                    )
            )

            // Foreground Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Tag & Free MP3 Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DjSecondaryPink)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OFFICIAL DJ PORTAL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            )
                        )
                    }

                    // 100% Free MP3
                    Text(
                        text = "100% FREE MP3 DOWNLOADS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DjGreenNeon,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Middle: DJ Name & Motto
                Column {
                    Text(
                        text = "$djName REMIXES",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = DjTextPrimary,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Bollywood • Punjabi Dhol • Club EDM • Bhojpuri Tadka",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DjPrimaryCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                // Bottom: Upload New Song shortcut for DJ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎧 Studio 320kbps Master Quality",
                        style = MaterialTheme.typography.labelSmall.copy(color = DjTextSecondary)
                    )

                    Button(
                        onClick = onUploadClick,
                        colors = ButtonDefaults.buttonColors(containerColor = DjPrimaryCyan),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("hero_upload_track_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Upload Song", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
