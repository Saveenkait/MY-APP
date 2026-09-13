package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MixingPack
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
fun MixingPackCard(
    pack: MixingPack,
    onBuyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, DjBorder, RoundedCornerShape(18.dp))
            .testTag("mixing_pack_card_${pack.id}"),
        color = DjSurfaceVariant,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Category & Best seller / Format
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DjSurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderZip,
                        contentDescription = null,
                        tint = DjPrimaryCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pack.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DjPrimaryCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }

                if (pack.isBestSeller) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DjSecondaryPink.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = DjSecondaryPink,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "BESTSELLER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DjSecondaryPink,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = pack.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DjTextPrimary,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = pack.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = DjTextSecondary,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Included Items Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black.copy(alpha = 0.35f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "📦 What is Inside This Mixing Pack:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DjAccentAmber,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pack.includedItems,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DjTextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Format: ${pack.format}",
                            style = MaterialTheme.typography.labelSmall.copy(color = DjTextMuted, fontSize = 11.sp)
                        )
                        Text(
                            text = "Size: ${pack.fileSize}",
                            style = MaterialTheme.typography.labelSmall.copy(color = DjTextMuted, fontSize = 11.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pricing & Buy on WhatsApp button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "₹${pack.priceInr}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = DjGreenNeon,
                                fontWeight = FontWeight.Black
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "₹${pack.originalPriceInr}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DjTextMuted,
                                textDecoration = TextDecoration.LineThrough
                            ),
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                    Text(
                        text = "Instant Google Drive link",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DjTextSecondary,
                            fontSize = 10.sp
                        )
                    )
                }

                Button(
                    onClick = onBuyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DjGreenNeon
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("buy_pack_button_${pack.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Buy",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BUY ON WHATSAPP",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
