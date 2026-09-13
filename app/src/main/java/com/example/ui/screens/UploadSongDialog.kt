package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.DjBorder
import com.example.ui.theme.DjGreenNeon
import com.example.ui.theme.DjPrimaryCyan
import com.example.ui.theme.DjSecondaryPink
import com.example.ui.theme.DjSurface
import com.example.ui.theme.DjSurfaceElevated
import com.example.ui.theme.DjTextPrimary
import com.example.ui.theme.DjTextSecondary

@Composable
fun UploadSongDialog(
    defaultArtist: String,
    onDismiss: () -> Unit,
    onUpload: (
        title: String,
        artist: String,
        genre: String,
        bpm: Int,
        durationSec: Int,
        fileUrl: String,
        tags: String,
        description: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf(defaultArtist) }
    var genre by remember { mutableStateOf("Bollywood Remix") }
    var bpmText by remember { mutableStateOf("128") }
    var durationText by remember { mutableStateOf("210") }
    var fileUrl by remember { mutableStateOf("https://actions.google.com/sounds/v1/science_fiction/force_field_hum_loop.ogg") }
    var tags by remember { mutableStateOf("#ClubMix #HighBass #320kbps") }
    var description by remember { mutableStateOf("New club remix track ready for free MP3 download.") }

    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = DjSurfaceElevated,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = DjPrimaryCyan,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Upload Song / Remix",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = DjTextPrimary
                            )
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DjTextSecondary)
                    }
                }

                Text(
                    text = "Add your remix track. Users will be able to stream and download it in MP3 for free.",
                    style = MaterialTheme.typography.bodySmall.copy(color = DjTextSecondary)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Track Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Song Title * (e.g. Lungi Dance Club Mix)") },
                    modifier = Modifier.fillMaxWidth().testTag("upload_title_input"),
                    colors = customTextFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Artist / Remixer Name
                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Remixer / Artist Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Genre
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre (Bollywood Remix, Punjabi Dhol, Club EDM, Bhojpuri)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // BPM & Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = bpmText,
                        onValueChange = { bpmText = it },
                        label = { Text("BPM (e.g. 130)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = customTextFieldColors()
                    )
                    OutlinedTextField(
                        value = durationText,
                        onValueChange = { durationText = it },
                        label = { Text("Duration Sec (e.g. 210)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = customTextFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Audio File / Stream Link
                OutlinedTextField(
                    value = fileUrl,
                    onValueChange = { fileUrl = it },
                    label = { Text("Audio Stream / MP3 URL") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tags
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (e.g. #ClubMix #Dholki #320kbps)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & DJ Credits") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = customTextFieldColors()
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg ?: "",
                        color = Color(0xFFFF5252),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = DjTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMsg = "Please enter Song Title"
                                return@Button
                            }
                            val bpm = bpmText.toIntOrNull() ?: 128
                            val duration = durationText.toIntOrNull() ?: 210
                            onUpload(title, artist, genre, bpm, duration, fileUrl, tags, description)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DjPrimaryCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("confirm_upload_button")
                    ) {
                        Text("Upload Track", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DjPrimaryCyan,
    unfocusedBorderColor = DjBorder,
    focusedLabelColor = DjPrimaryCyan,
    unfocusedLabelColor = DjTextSecondary,
    focusedTextColor = DjTextPrimary,
    unfocusedTextColor = DjTextPrimary,
    cursorColor = DjPrimaryCyan
)
