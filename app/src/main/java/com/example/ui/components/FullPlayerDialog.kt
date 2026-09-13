package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Song
import com.example.ui.theme.DjAccentAmber
import com.example.ui.theme.DjAccentPurple
import com.example.ui.theme.DjBackground
import com.example.ui.theme.DjBorder
import com.example.ui.theme.DjGreenNeon
import com.example.ui.theme.DjPrimaryCyan
import com.example.ui.theme.DjSecondaryPink
import com.example.ui.theme.DjSurfaceElevated
import com.example.ui.theme.DjTextMuted
import com.example.ui.theme.DjTextPrimary
import com.example.ui.theme.DjTextSecondary
import kotlin.math.sin

@Composable
fun FullPlayerDialog(
    song: Song,
    isPlaying: Boolean,
    currentPosMs: Long,
    durationMs: Long,
    isLooping: Boolean,
    isDownloading: Boolean,
    onDismiss: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onForward10: () -> Unit,
    onRewind10: () -> Unit,
    onToggleLoop: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onOrderRemixClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = DjBackground
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .background(DjSurfaceElevated, CircleShape)
                            .testTag("dismiss_player_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Collapse Player",
                            tint = DjTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "PLAYING FROM DJ REMIX HUB",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DjPrimaryCyan,
                                letterSpacing = 1.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = song.genre,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DjTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .size(44.dp)
                            .background(DjSurfaceElevated, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = DjTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Vinyl Turntable Display
                VinylTurntable(isPlaying = isPlaying)

                Spacer(modifier = Modifier.height(16.dp))

                // Sound Waveform Visualizer
                AudioWaveformCanvas(isPlaying = isPlaying)

                Spacer(modifier = Modifier.height(16.dp))

                // Song Title & Remixer
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DjTextPrimary
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Remix by ${song.artist}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = DjPrimaryCyan,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Badges: BPM, 320kbps, Free Download
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // BPM
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DjSurfaceElevated)
                            .border(1.dp, DjBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = DjAccentAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${song.bpm} BPM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DjAccentAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // 320 kbps MP3
                    Text(
                        text = "320 KBPS MP3",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DjGreenNeon,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DjGreenNeon.copy(alpha = 0.15f))
                            .border(1.dp, DjGreenNeon.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    // 100% Free badge
                    Text(
                        text = "100% FREE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DjSecondaryPink,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DjSecondaryPink.copy(alpha = 0.15f))
                            .border(1.dp, DjSecondaryPink.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrub / Seek Slider
                var sliderSeeking by remember { mutableFloatStateOf(-1f) }
                val currentFraction = if (durationMs > 0) {
                    (currentPosMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
                } else 0f

                Slider(
                    value = if (sliderSeeking >= 0f) sliderSeeking else currentFraction,
                    onValueChange = { sliderSeeking = it },
                    onValueChangeFinished = {
                        val targetMs = (sliderSeeking * durationMs).toLong()
                        onSeekTo(targetMs)
                        sliderSeeking = -1f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("player_seek_slider"),
                    colors = SliderDefaults.colors(
                        thumbColor = DjPrimaryCyan,
                        activeTrackColor = DjPrimaryCyan,
                        inactiveTrackColor = DjBorder
                    )
                )

                // Current time & Total duration labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val displayedPos = if (sliderSeeking >= 0f) {
                        (sliderSeeking * durationMs / 1000).toInt()
                    } else {
                        (currentPosMs / 1000).toInt()
                    }
                    Text(
                        text = formatDuration(displayedPos),
                        style = MaterialTheme.typography.labelSmall.copy(color = DjTextSecondary)
                    )
                    Text(
                        text = formatDuration((durationMs / 1000).toInt().coerceAtLeast(song.durationSec)),
                        style = MaterialTheme.typography.labelSmall.copy(color = DjTextSecondary)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Playback Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Loop button
                    IconButton(onClick = onToggleLoop) {
                        Icon(
                            imageVector = if (isLooping) Icons.Default.RepeatOne else Icons.Default.Repeat,
                            contentDescription = "Loop",
                            tint = if (isLooping) DjPrimaryCyan else DjTextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // -10s
                    IconButton(onClick = onRewind10) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "Rewind 10s",
                            tint = DjTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Skip Previous
                    IconButton(onClick = onSkipPrevious) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = DjTextPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Play/Pause Big Button
                    IconButton(
                        onClick = onPlayPauseClick,
                        modifier = Modifier
                            .size(68.dp)
                            .background(
                                Brush.linearGradient(listOf(DjPrimaryCyan, DjSecondaryPink)),
                                CircleShape
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                            .testTag("full_player_play_pause")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    // Skip Next
                    IconButton(onClick = onSkipNext) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = DjTextPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // +10s
                    IconButton(onClick = onForward10) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "Forward 10s",
                            tint = DjTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Big Free MP3 Download Action Button
                Button(
                    onClick = onDownloadClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("player_download_mp3_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = DjPrimaryCyan),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = if (song.isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
                        contentDescription = "Download MP3",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (song.isDownloaded) "DOWNLOADED (READY OFFLINE)" else "DOWNLOAD MP3 (FREE 320 KBPS)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Custom Remix Booking Button
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onOrderRemixClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DjSecondaryPink)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = DjSecondaryPink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Want Custom Remix of This Song? Book DJ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DjSecondaryPink,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun VinylTurntable(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vinyl_rot"
    )

    Box(
        modifier = Modifier
            .size(240.dp)
            .clip(CircleShape)
            .background(Color(0xFF0F0D18))
            .border(4.dp, DjBorder, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Vinyl grooves
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(if (isPlaying) rotation else 0f)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radii = listOf(110.dp, 95.dp, 80.dp, 65.dp, 50.dp)
            radii.forEach { r ->
                drawCircle(
                    color = Color.White.copy(alpha = 0.04f),
                    radius = r.toPx(),
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                )
            }
        }

        // Center DJ label
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(DjSecondaryPink, DjAccentPurple, DjPrimaryCyan, DjSecondaryPink)
                    )
                )
                .rotate(if (isPlaying) rotation else 0f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "DJ HUB",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                )
                Text(
                    text = "320 KBPS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = DjPrimaryCyan,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            // Spindle hole
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            )
        }
    }
}

@Composable
fun AudioWaveformCanvas(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
    ) {
        val barCount = 36
        val barWidth = size.width / (barCount * 1.5f)
        val spacing = barWidth * 0.5f

        for (i in 0 until barCount) {
            val factor = if (isPlaying) {
                val s = sin(i.toFloat() * 0.35f + phase)
                0.25f + 0.75f * ((s + 1f) / 2f)
            } else {
                0.2f
            }
            val barH = size.height * factor
            val x = i * (barWidth + spacing)
            val y = (size.height - barH) / 2

            drawRoundRect(
                color = if (i % 2 == 0) DjPrimaryCyan else DjSecondaryPink,
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
            )
        }
    }
}
