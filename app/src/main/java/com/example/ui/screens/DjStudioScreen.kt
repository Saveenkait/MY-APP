package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MicExternalOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Song
import com.example.ui.theme.DjAccentAmber
import com.example.ui.theme.DjAccentPurple
import com.example.ui.theme.DjBorder
import com.example.ui.theme.DjGreenNeon
import com.example.ui.theme.DjPrimaryCyan
import com.example.ui.theme.DjSecondaryPink
import com.example.ui.theme.DjSurfaceElevated
import com.example.ui.theme.DjSurfaceVariant
import com.example.ui.theme.DjTextMuted
import com.example.ui.theme.DjTextPrimary
import com.example.ui.theme.DjTextSecondary

@Composable
fun DjStudioScreen(
    songs: List<Song>,
    djName: String,
    djPhone: String,
    onUploadClick: () -> Unit,
    onDeleteSong: (Song) -> Unit,
    onPlaySong: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPlays = songs.sumOf { it.playsCount.toLong() }
    val totalDownloads = songs.sumOf { it.downloadsCount.toLong() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dj_studio_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // DJ Profile Header
        item {
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
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF1E1038),
                                    Color(0xFF0F0B1E)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.sweepGradient(
                                            listOf(DjPrimaryCyan, DjSecondaryPink, DjAccentPurple, DjPrimaryCyan)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MicExternalOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = djName,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Black,
                                            color = DjTextPrimary
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified DJ",
                                        tint = DjPrimaryCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Music Producer & DJ Remixer",
                                    style = MaterialTheme.typography.bodySmall.copy(color = DjTextSecondary)
                                )
                                Text(
                                    text = "WhatsApp Studio: $djPhone",
                                    style = MaterialTheme.typography.labelSmall.copy(color = DjGreenNeon)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Big Upload Button
                        Button(
                            onClick = onUploadClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("studio_upload_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DjPrimaryCyan),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "UPLOAD NEW REMIX SONG (MP3)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Studio Analytics Cards
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Studio Performance Overview",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DjTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatMetricCard(
                        title = "Uploaded Tracks",
                        value = "${songs.size}",
                        icon = Icons.Default.MusicNote,
                        color = DjPrimaryCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Total Plays",
                        value = "${totalPlays / 1000}k+",
                        icon = Icons.Default.Headphones,
                        color = DjSecondaryPink,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Free Downloads",
                        value = "${totalDownloads / 1000}k+",
                        icon = Icons.Default.Download,
                        color = DjGreenNeon,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Uploaded Song Management List
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Manage Uploaded Songs (${songs.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DjTextPrimary
                    )
                )
                Text(
                    text = "Live in App",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = DjGreenNeon,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        items(songs, key = { it.id }) { song ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                color = DjSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, DjBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onPlaySong(song) },
                        modifier = Modifier
                            .size(36.dp)
                            .background(DjPrimaryCyan.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = DjPrimaryCyan, modifier = Modifier.size(20.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DjTextPrimary
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${song.genre} • ${song.bpm} BPM • ${song.downloadsCount} downloads",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DjTextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = { onDeleteSong(song) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Track",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = DjSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DjBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = DjTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = DjTextMuted,
                    fontSize = 10.sp
                ),
                maxLines = 1
            )
        }
    }
}
