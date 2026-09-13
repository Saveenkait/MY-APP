package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RemixOrder
import com.example.ui.theme.DjAccentAmber
import com.example.ui.theme.DjBorder
import com.example.ui.theme.DjGreenNeon
import com.example.ui.theme.DjPrimaryCyan
import com.example.ui.theme.DjSecondaryPink
import com.example.ui.theme.DjSurfaceElevated
import com.example.ui.theme.DjSurfaceVariant
import com.example.ui.theme.DjTextMuted
import com.example.ui.theme.DjTextPrimary
import com.example.ui.theme.DjTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RemixRequestScreen(
    remixOrders: List<RemixOrder>,
    djName: String,
    djPhone: String,
    onSubmitOrder: (
        name: String,
        phone: String,
        song: String,
        link: String,
        style: String,
        dropName: String,
        urgency: String,
        notes: String
    ) -> Unit,
    onCallDj: () -> Unit,
    onDirectWhatsApp: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var songName by remember { mutableStateOf("") }
    var songLink by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Bollywood Club Dance") }
    var customDropName by remember { mutableStateOf("") }
    var selectedUrgency by remember { mutableStateOf("Standard (3-4 Days) - ₹999") }
    var notes by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val styles = listOf(
        "Bollywood Club Dance",
        "Punjabi Heavy Dholki",
        "Roadshow Vibration Bass",
        "EDM Festival Drop",
        "Wedding Special Mashup",
        "Lo-Fi Slowed & Reverb"
    )

    val urgencies = listOf(
        "Standard (3-4 Days) - ₹999",
        "Express (24 Hours) - ₹1,999"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("remix_request_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Hero Header
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
                                    Color(0xFF2A0822),
                                    Color(0xFF160E2A)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DjSecondaryPink.copy(alpha = 0.35f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = DjSecondaryPink,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CUSTOM REMIX BOOKING",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DjSecondaryPink,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Song Mujhse Remix Karwaye",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = DjTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Want your favourite track remixed exclusively by $djName for party, roadshow, DJ setup or wedding? Fill the form or connect directly on WhatsApp!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DjTextSecondary,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Call / Direct WhatsApp buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    onDirectWhatsApp("Hello $djName! I want to discuss a custom song remix project.")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DjGreenNeon),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("WhatsApp DJ", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = onCallDj,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DjPrimaryCyan),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, tint = DjPrimaryCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call Remixer", color = DjPrimaryCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // The Custom Remix Order Form Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                color = DjSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, DjBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "📝 Remix Booking Form",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DjTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Name
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Your Name *") },
                        modifier = Modifier.fillMaxWidth().testTag("remix_user_name_input"),
                        colors = customTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // WhatsApp Phone
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Your WhatsApp Number *") },
                        modifier = Modifier.fillMaxWidth().testTag("remix_user_phone_input"),
                        colors = customTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Song to remix
                    OutlinedTextField(
                        value = songName,
                        onValueChange = { songName = it },
                        label = { Text("Song Name to Remix * (e.g. 52 Gaj Ka Daman)") },
                        modifier = Modifier.fillMaxWidth().testTag("remix_song_name_input"),
                        colors = customTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // YouTube / Drive Link
                    OutlinedTextField(
                        value = songLink,
                        onValueChange = { songLink = it },
                        label = { Text("Song Link (YouTube, Drive, or Audio Link)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Desired Remix Style
                    Text(
                        text = "Choose Desired Remix Style:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = DjPrimaryCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        styles.forEach { style ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedStyle = style }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = selectedStyle == style,
                                    onClick = { selectedStyle = style },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = DjPrimaryCyan,
                                        unselectedColor = DjTextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = style,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (selectedStyle == style) DjTextPrimary else DjTextSecondary,
                                        fontWeight = if (selectedStyle == style) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Custom DJ voice drop
                    OutlinedTextField(
                        value = customDropName,
                        onValueChange = { customDropName = it },
                        label = { Text("Custom Voice Drop Name (e.g. 'Remix by DJ Rahul')") },
                        placeholder = { Text("Include your custom name drop in track", color = DjTextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Delivery Timeline
                    Text(
                        text = "Delivery Timeline & Pricing:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = DjAccentAmber,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        urgencies.forEach { urg ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedUrgency = urg }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = selectedUrgency == urg,
                                    onClick = { selectedUrgency = urg },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = DjAccentAmber,
                                        unselectedColor = DjTextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = urg,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (selectedUrgency == urg) DjTextPrimary else DjTextSecondary,
                                        fontWeight = if (selectedUrgency == urg) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Additional Notes
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Special Requests / Equalizer notes") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        colors = customTextFieldColors()
                    )

                    if (validationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = validationError ?: "",
                            color = Color(0xFFFF5252),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submit & WhatsApp Button
                    Button(
                        onClick = {
                            if (customerName.isBlank()) {
                                validationError = "Please enter your name"
                                return@Button
                            }
                            if (customerPhone.isBlank()) {
                                validationError = "Please enter your WhatsApp number"
                                return@Button
                            }
                            if (songName.isBlank()) {
                                validationError = "Please enter song name to remix"
                                return@Button
                            }
                            validationError = null
                            onSubmitOrder(
                                customerName,
                                customerPhone,
                                songName,
                                songLink,
                                selectedStyle,
                                customDropName,
                                selectedUrgency,
                                notes
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DjPrimaryCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_remix_request_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SUBMIT & CHAT ON WHATSAPP",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Recent Orders Section
        if (remixOrders.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DjGreenNeon, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Your Submitted Remix Requests",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DjTextPrimary
                        )
                    )
                }
            }

            items(remixOrders, key = { it.id }) { order ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = DjSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DjBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.songName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DjPrimaryCyan
                                )
                            )
                            Text(
                                text = order.status.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DjAccentAmber,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DjAccentAmber.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Style: ${order.genreStyle} • ${order.urgency}",
                            style = MaterialTheme.typography.bodySmall.copy(color = DjTextSecondary)
                        )
                        if (order.customDropName.isNotEmpty()) {
                            Text(
                                text = "Voice Drop: '${order.customDropName}'",
                                style = MaterialTheme.typography.labelSmall.copy(color = DjSecondaryPink)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.timestamp)),
                            style = MaterialTheme.typography.labelSmall.copy(color = DjTextMuted, fontSize = 10.sp)
                        )
                    }
                }
            }
        }
    }
}
