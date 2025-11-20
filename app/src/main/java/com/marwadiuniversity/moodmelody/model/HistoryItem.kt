package com.marwadiuniversity.moodmelody.model  // ⚠️ Change to your package!

/**
 * Data class representing a history item (mood + song played)
 */
data class HistoryItem(
    val id: Int,
    val mood: String,
    val moodEmoji: String,
    val timestamp: String,
    val songTitle: String,
    val artistName: String
)