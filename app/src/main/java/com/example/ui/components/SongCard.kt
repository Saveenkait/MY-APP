package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun SongCard(
    song: Song,
    isPlaying: Boolean,
    isCurrentSong: Boolean,
    isDownloading: Boolean,
    downloadProgress: Float?,
    onPlayClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val borderColor = if (isCurrentSong) {
        DjPrimaryCyan
    } else {
        DjBorder.copy(alpha = 0.6f)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isCurrentSong) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onPlayClick() }
            .testTag("song_card_${song.id}"),
        color = if (isCurrentSong) DjSurfaceElevated else DjSurfaceVariant,
        tonalElevation = if (isCurrentSong) 6.dp else 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Vinyl / Artwork Icon with Equalizer if playing
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(DjAccentPurple, DjSecondaryPink, DjPrimaryCyan)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCurrentSong && isPlaying) {
                        AnimatedEqualizerBars()
                    } else {
                        IconButton(
                            onClick = onPlayClick,
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isCurrentSong && isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play Track",
                                tint = DjPrimaryCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Song Title & Artist info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isCurrentSong) DjPrimaryCyan else DjTextPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${song.artist} • ${song.genre}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DjTextSecondary,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // BPM badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.4f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = DjAccentAmber,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${song.bpm} BPM",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DjAccentAmber,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        // Duration badge
                        Text(
                            text = formatDuration(song.durationSec),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DjTextMuted,
                                fontSize = 11.sp
                            )
                        )

                        // 320 kbps MP3 badge
                        Text(
                            text = "320k MP3",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DjGreenNeon,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(DjGreenNeon.copy(alpha = 0.15f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }

                // Download MP3 Button & More Menu
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isDownloading) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { downloadProgress ?: 0.5f },
                                strokeWidth = 2.5.dp,
                                color = DjPrimaryCyan,
                                trackColor = DjBorder
                            )
                        }
                    } else if (song.isDownloaded) {
                        IconButton(
                            onClick = onShareClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Downloaded Offline",
                                tint = DjGreenNeon,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        // Free Download Button
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DjPrimaryCyan.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DjPrimaryCyan.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onDownloadClick() }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("free_download_btn_${song.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download MP3 Free",
                                    tint = DjPrimaryCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "FREE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DjPrimaryCyan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    // More Menu
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = DjTextSecondary
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(DjSurfaceElevated)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Download MP3 (Free)", color = DjTextPrimary) },
                                leadingIcon = { Icon(Icons.Default.Download, contentDescription = null, tint = DjPrimaryCyan) },
                                onClick = {
                                    menuExpanded = false
                                    onDownloadClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Share Track", color = DjTextPrimary) },
                                leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, tint = DjTextSecondary) },
                                onClick = {
                                    menuExpanded = false
                                    onShareClick()
                                }
                            )
                            if (onDeleteClick != null) {
                                DropdownMenuItem(
                                    text = { Text("Delete Track", color = Color(0xFFFF5252)) },
                                    onClick = {
                                        menuExpanded = false
                                        onDeleteClick()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedEqualizerBars(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(300, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )

    Row(
        modifier = modifier.height(24.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height((24 * bar1).dp)
                .clip(RoundedCornerShape(2.dp))
                .background(DjPrimaryCyan)
        )
        Box(
            modifier = Modifier
                .width(4.dp)
                .height((24 * bar2).dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .width(4.dp)
                .height((24 * bar3).dp)
                .clip(RoundedCornerShape(2.dp))
                .background(DjSecondaryPink)
        )
    }
}

fun formatDuration(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return String.format("%02d:%02d", m, s)
}
