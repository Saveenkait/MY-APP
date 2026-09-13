package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mixing_packs")
data class MixingPack(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // "FLP Project Files", "Multitrack Stems", "Dholki Loops", "DJ Sound FX & Drops"
    val description: String,
    val includedItems: String, // e.g. "Full FL Studio Project (.flp), Vocal Acapellas, Kick/Bass Stems, Master Chain"
    val format: String = "ZIP (24-Bit WAV + FLP)",
    val fileSize: String = "450 MB",
    val priceInr: Int = 399,
    val originalPriceInr: Int = 799,
    val previewAudioUrl: String? = null,
    val isBestSeller: Boolean = false,
    val tags: String = "#MixingData #Stems #FLP"
)
