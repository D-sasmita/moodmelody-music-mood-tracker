package com.marwadiuniversity.moodmelody.model  // ⚠️ Change to your package!

/**
 * Data class representing a music playlist
 */
data class Playlist(
    val id: Int,
    val title: String,
    val genre: String,
    val songCount: Int,
    val duration: String,
    val albumCoverUrl: String, // Will use drawable resource for now
    var isFavorite: Boolean = false
)