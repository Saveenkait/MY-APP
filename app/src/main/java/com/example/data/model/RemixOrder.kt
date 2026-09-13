package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remix_orders")
data class RemixOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val customerPhone: String,
    val songName: String,
    val originalSongLink: String = "",
    val genreStyle: String = "Bollywood Club Mix",
    val customDropName: String = "",
    val urgency: String = "Standard (3-4 Days)",
    val budgetInr: Int = 999,
    val notes: String = "",
    val status: String = "Submitted", // "Submitted", "In Studio", "Completed"
    val timestamp: Long = System.currentTimeMillis()
)
