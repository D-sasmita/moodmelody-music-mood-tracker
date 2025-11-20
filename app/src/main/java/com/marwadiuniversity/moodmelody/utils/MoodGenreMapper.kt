package com.marwadiuniversity.moodmelody.utils

/**
 * Maps moods to VALID Spotify genre seeds
 *
 * IMPORTANT: Spotify is very strict about genre names.
 * Only use genres from the official list:
 * https://developer.spotify.com/console/get-available-genre-seeds/
 */
object MoodGenreMapper {

    data class MoodParameters(
        val genres: String,        // Comma-separated valid Spotify genres
        val valence: Float?,       // 0.0-1.0 (happiness/positivity)
        val energy: Float?,        // 0.0-1.0 (intensity/activity)
        val acousticness: Float?,  // 0.0-1.0 (acoustic vs electric)
        val danceability: Float?,  // 0.0-1.0 (how suitable for dancing)
        val tempo: Float?          // BPM (beats per minute)
    )

    /**
     * Get Spotify API parameters for a given mood
     */
    fun getMoodParameters(mood: String): MoodParameters {
        return when (mood.lowercase().trim()) {
            "happy", "😊" -> MoodParameters(
                genres = "pop,happy,dance",  // ✅ Valid genres
                valence = 0.8f,              // High happiness
                energy = 0.7f,               // Moderate-high energy
                acousticness = 0.3f,         // Low acousticness
                danceability = 0.7f,         // High danceability
                tempo = null
            )

            "sad", "😢" -> MoodParameters(
                genres = "sad,acoustic,indie",  // ✅ Valid genres
                valence = 0.2f,                 // Low happiness
                energy = 0.3f,                  // Low energy
                acousticness = 0.7f,            // High acousticness
                danceability = 0.3f,            // Low danceability
                tempo = null
            )

            "calm", "relaxed", "😌" -> MoodParameters(
                genres = "ambient,chill,acoustic",  // ✅ Valid genres
                valence = 0.5f,                     // Neutral happiness
                energy = 0.2f,                      // Very low energy
                acousticness = 0.8f,                // Very high acousticness
                danceability = 0.3f,                // Low danceability
                tempo = 80f                         // Slow tempo
            )

            "energetic", "pumped", "⚡" -> MoodParameters(
                genres = "dance,edm,party",  // ✅ Valid genres
                valence = 0.7f,              // High happiness
                energy = 0.9f,               // Very high energy
                acousticness = 0.1f,         // Very low acousticness
                danceability = 0.9f,         // Very high danceability
                tempo = 140f                 // Fast tempo
            )

            "romantic", "love", "❤️" -> MoodParameters(
                genres = "romance,soul,r-n-b",  // ✅ Valid genres
                valence = 0.6f,                 // Moderate happiness
                energy = 0.4f,                  // Low-moderate energy
                acousticness = 0.5f,            // Balanced
                danceability = 0.5f,            // Balanced
                tempo = null
            )

            "angry", "rage", "😠" -> MoodParameters(
                genres = "metal,rock,punk",  // ✅ Valid genres
                valence = 0.3f,              // Low happiness
                energy = 0.9f,               // Very high energy
                acousticness = 0.1f,         // Very low acousticness
                danceability = 0.5f,         // Moderate danceability
                tempo = 160f                 // Very fast tempo
            )

            "focused", "study", "📚" -> MoodParameters(
                genres = "chill,study,ambient",  // ✅ Valid genres
                valence = 0.5f,                  // Neutral
                energy = 0.3f,                   // Low energy
                acousticness = 0.6f,             // High acousticness
                danceability = 0.2f,             // Low danceability
                tempo = 90f                      // Moderate tempo
            )

            "party", "club", "🎉" -> MoodParameters(
                genres = "party,dance,house",  // ✅ Valid genres
                valence = 0.8f,                // High happiness
                energy = 0.9f,                 // Very high energy
                acousticness = 0.1f,           // Very low acousticness
                danceability = 0.95f,          // Very high danceability
                tempo = 128f                   // Club tempo
            )

            "workout", "gym", "💪" -> MoodParameters(
                genres = "work-out,power-pop,edm",  // ✅ Valid genres (note hyphen!)
                valence = 0.7f,                     // High happiness
                energy = 0.95f,                     // Very high energy
                acousticness = 0.1f,                // Very low acousticness
                danceability = 0.8f,                // High danceability
                tempo = 150f                        // High tempo
            )

            "sleep", "bedtime", "😴" -> MoodParameters(
                genres = "sleep,ambient,minimal-techno",  // ✅ Valid genres
                valence = 0.4f,                           // Low-moderate happiness
                energy = 0.1f,                            // Very low energy
                acousticness = 0.9f,                      // Very high acousticness
                danceability = 0.1f,                      // Very low danceability
                tempo = 60f                               // Very slow tempo
            )

            else -> MoodParameters(
                genres = "pop",           // ✅ Safe default
                valence = 0.5f,
                energy = 0.5f,
                acousticness = null,
                danceability = null,
                tempo = null
            )
        }
    }

    /**
     * List of ALL valid Spotify genre seeds (as of 2024)
     * Use these to ensure you're using correct genre names
     */
    val VALID_SPOTIFY_GENRES = listOf(
        "acoustic", "afrobeat", "alt-rock", "alternative", "ambient", "anime",
        "black-metal", "bluegrass", "blues", "bossanova", "brazil", "breakbeat",
        "british", "cantopop", "chicago-house", "children", "chill", "classical",
        "club", "comedy", "country", "dance", "dancehall", "death-metal", "deep-house",
        "detroit-techno", "disco", "disney", "drum-and-bass", "dub", "dubstep",
        "edm", "electro", "electronic", "emo", "folk", "forro", "french", "funk",
        "garage", "german", "gospel", "goth", "grindcore", "groove", "grunge",
        "guitar", "happy", "hard-rock", "hardcore", "hardstyle", "heavy-metal",
        "hip-hop", "holidays", "honky-tonk", "house", "idm", "indian", "indie",
        "indie-pop", "industrial", "iranian", "j-dance", "j-idol", "j-pop", "j-rock",
        "jazz", "k-pop", "kids", "latin", "latino", "malay", "mandopop", "metal",
        "metal-misc", "metalcore", "minimal-techno", "movies", "mpb", "new-age",
        "new-release", "opera", "pagode", "party", "philippines-opm", "piano",
        "pop", "pop-film", "post-dubstep", "power-pop", "progressive-house", "psych-rock",
        "punk", "punk-rock", "r-n-b", "rainy-day", "reggae", "reggaeton", "road-trip",
        "rock", "rock-n-roll", "rockabilly", "romance", "sad", "salsa", "samba",
        "sertanejo", "show-tunes", "singer-songwriter", "ska", "sleep", "songwriter",
        "soul", "soundtracks", "spanish", "study", "summer", "swedish", "synth-pop",
        "tango", "techno", "trance", "trip-hop", "turkish", "work-out", "world-music"
    )

    /**
     * Validate if a genre is in Spotify's official list
     */
    fun isValidGenre(genre: String): Boolean {
        return VALID_SPOTIFY_GENRES.contains(genre.lowercase().trim())
    }

    /**
     * Get human-readable description of a mood
     */
    fun getMoodDescription(mood: String): String {
        return when (mood.lowercase().trim()) {
            "happy", "😊" -> "Upbeat and joyful tracks"
            "sad", "😢" -> "Melancholic and emotional songs"
            "calm", "😌" -> "Peaceful and relaxing music"
            "energetic", "⚡" -> "High-energy workout tracks"
            "romantic", "❤️" -> "Love songs and romantic melodies"
            "angry", "😠" -> "Intense and aggressive music"
            "focused", "📚" -> "Concentration and study music"
            "party", "🎉" -> "Dance and party anthems"
            "workout", "💪" -> "Motivating fitness tracks"
            "sleep", "😴" -> "Soothing sleep music"
            else -> "Music for your mood"
        }
    }
}