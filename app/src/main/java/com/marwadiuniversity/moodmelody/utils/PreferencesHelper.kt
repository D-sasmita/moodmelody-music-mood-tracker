package com.marwadiuniversity.moodmelody.utils

import android.content.Context
import android.content.SharedPreferences
import com.marwadiuniversity.moodmelody.api.VideoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Data class to hold a single mood entry with its timestamp.
data class MoodHistoryItem(
    val mood: String,
    val timestamp: Long
)

// Object to manage all SharedPreferences operations for the app.
object PreferencesHelper {

    private const val PREFS_NAME = "MoodMelodyPrefs"
    private const val KEY_LAST_MOOD = "last_selected_mood"
    private const val KEY_LAST_MOOD_EMOJI = "last_mood_emoji"
    private const val KEY_MOOD_HISTORY = "mood_history"
    private const val KEY_FAVORITES = "favorites"
    private val gson = Gson()

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // --- Mood Preferences ---

    fun saveLastMood(context: Context, mood: String, emoji: String) {
        getPreferences(context).edit().apply {
            putString(KEY_LAST_MOOD, mood)
            putString(KEY_LAST_MOOD_EMOJI, emoji)
            apply() // Use apply() for asynchronous saving.
        }
    }

    fun getLastMood(context: Context): String {
        return getPreferences(context).getString(KEY_LAST_MOOD, "happy") ?: "happy"
    }

    fun getLastMoodEmoji(context: Context): String {
        return getPreferences(context).getString(KEY_LAST_MOOD_EMOJI, "😊") ?: "😊"
    }

    // --- History Preferences (using a List of MoodHistoryItem) ---

    /**
     * Adds a new mood to the beginning of the history list.
     * The entire list is stored as a single JSON string in SharedPreferences.
     */
    fun addMoodToHistory(context: Context, mood: String) {
        val prefs = getPreferences(context)
        val historyList = getMoodHistory(context).toMutableList()

        // Create a new item and add it to the top of the list.
        val newItem = MoodHistoryItem(mood, System.currentTimeMillis())
        historyList.add(0, newItem)

        // Convert the updated list back to a JSON string and save it.
        val updatedJson = gson.toJson(historyList)
        prefs.edit().putString(KEY_MOOD_HISTORY, updatedJson).apply()
    }

    /**
     * Retrieves the list of mood history items from SharedPreferences.
     */
    /**
     * Retrieves the list of mood history items from SharedPreferences.
     * Handles migration from old HashSet format to new JSON format.
     */
    fun getMoodHistory(context: Context): List<MoodHistoryItem> {
        val prefs = getPreferences(context)

        // Try to get as String first (new format)
        try {
            val json = prefs.getString(KEY_MOOD_HISTORY, null)
            if (json.isNullOrEmpty()) {
                return emptyList()
            }
            val type = object : TypeToken<List<MoodHistoryItem>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            // If it fails, try to migrate from old HashSet format
            try {
                val oldSet = prefs.getStringSet(KEY_MOOD_HISTORY, null)
                if (oldSet != null) {
                    // Convert old HashSet to new format
                    val migratedList = oldSet.map { mood ->
                        MoodHistoryItem(mood, System.currentTimeMillis())
                    }
                    // Save in new format
                    val json = gson.toJson(migratedList)
                    prefs.edit().putString(KEY_MOOD_HISTORY, json).apply()
                    return migratedList
                }
            } catch (migrationError: Exception) {
                // If migration also fails, clear the corrupted data
                prefs.edit().remove(KEY_MOOD_HISTORY).apply()
            }
            return emptyList()
        }
    }

    // --- Favorites Preferences (using a List of VideoItem) ---

    fun addFavorite(context: Context, video: VideoItem) {
        val favorites = getFavorites(context).toMutableList()
        val newVideoId = video.id.videoId

        // Add the video only if it's not already in the favorites.
        if (favorites.none { it.id.videoId == newVideoId }) {
            favorites.add(video)
            saveFavorites(context, favorites)
        }
    }

    fun removeFavorite(context: Context, video: VideoItem) {
        val favorites = getFavorites(context).toMutableList()
        val videoIdToRemove = video.id.videoId
        // Remove all items matching the video ID.
        if (favorites.removeAll { it.id.videoId == videoIdToRemove }) {
            saveFavorites(context, favorites)
        }
    }

    fun isFavorite(context: Context, video: VideoItem): Boolean {
        val favorites = getFavorites(context)
        val videoIdToCheck = video.id.videoId
        return favorites.any { it.id.videoId == videoIdToCheck }
    }

    fun getFavorites(context: Context): List<VideoItem> {
        val json = getPreferences(context).getString(KEY_FAVORITES, null)
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        // Define the type for deserialization: a List of VideoItem.
        val type = object : TypeToken<List<VideoItem>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            // If parsing fails, return an empty list to prevent crashes.
            emptyList()
        }
    }

    private fun saveFavorites(context: Context, favorites: List<VideoItem>) {
        val json = gson.toJson(favorites)
        getPreferences(context).edit().putString(KEY_FAVORITES, json).apply()
    }
}
