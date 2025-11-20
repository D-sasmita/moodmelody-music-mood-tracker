package com.marwadiuniversity.moodmelody

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marwadiuniversity.moodmelody.adapter.VideoAdapter
import com.marwadiuniversity.moodmelody.api.RetrofitClient
import com.marwadiuniversity.moodmelody.utils.PreferencesHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class RecommendationsActivity : AppCompatActivity() {

    private lateinit var tvMoodSelected: TextView
    private lateinit var rvVideos: RecyclerView // Changed from rvPlaylists
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var btnBack: ImageButton
    private lateinit var videoAdapter: VideoAdapter

    private var selectedMood: String = "happy"
    private var selectedMoodEmoji: String = "🙂"
    private lateinit var youtubeApiKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        // Retrieve API Key safely from strings.xml
        youtubeApiKey = getString(R.string.youtube_api_key)

        if (youtubeApiKey.isEmpty() || youtubeApiKey == "PASTE_YOUR_API_KEY_HERE") {
            Toast.makeText(this, "ERROR: YouTube API key not found!", Toast.LENGTH_LONG).show()
            finish() // This is OK to keep. The activity cannot function without a key.
            return
        }

        initializeViews()
        getMoodFromIntent()
        setupRecyclerView()
        fetchVideos() // This function now triggers the API call
        setupBottomNavigation()
    }

    private fun initializeViews() {
        tvMoodSelected = findViewById(R.id.tvMoodSelected)
        rvVideos = findViewById(R.id.rvVideos) // Make sure ID in XML is rvVideos
        bottomNavigation = findViewById(R.id.bottomNavigation)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getMoodFromIntent() {
        val moodFromIntent = intent.getStringExtra("selected_mood")

        if (moodFromIntent != null) {
            selectedMood = moodFromIntent
            selectedMoodEmoji = when (selectedMood.lowercase()) {
                "happy" -> getString(R.string.mood_happy_emoji)
                "sad" -> getString(R.string.mood_sad_emoji)
                "energetic" -> getString(R.string.mood_energetic_emoji)
                "calm" -> getString(R.string.mood_calm_emoji)
                "chill" -> getString(R.string.mood_chill_emoji)
                "romantic" -> getString(R.string.mood_romantic_emoji)
                "party" -> getString(R.string.mood_party_emoji)
                "relaxed" -> getString(R.string.mood_relaxed_emoji)
                else -> getString(R.string.mood_happy_emoji)
            }
        } else {
            // Fallback to last used mood if no intent extra is passed
            selectedMood = PreferencesHelper.getLastMood(this)
            selectedMoodEmoji = PreferencesHelper.getLastMoodEmoji(this)
        }

        // Save the mood to history
        PreferencesHelper.addMoodToHistory(this, selectedMood)

        tvMoodSelected.text = getString(R.string.you_chose_mood, selectedMoodEmoji, selectedMood.replaceFirstChar { it.uppercase() })
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter(
            emptyList(),
            { video ->
                // No null check needed here, data is now safe
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${video.id.videoId}"))
                if (intent.resolveActivity(packageManager) == null) {
                    intent.data = Uri.parse("https://www.youtube.com/watch?v=${video.id.videoId}")
                }
                startActivity(intent)
            },
            { video, position ->
                if (PreferencesHelper.isFavorite(this, video)) {
                    PreferencesHelper.removeFavorite(this, video)
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    PreferencesHelper.addFavorite(this, video)
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
                videoAdapter.notifyItemChanged(position)
            }
        )

        rvVideos.apply {
            layoutManager = LinearLayoutManager(this@RecommendationsActivity)
            adapter = videoAdapter
            setHasFixedSize(true)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun fetchVideos() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_LONG).show()
            return
        }

        // Construct the search query based on the mood
        val searchQuery = "$selectedMood songs playlist"

        // Launch a coroutine to make the network request
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.youtubeApiService.searchVideos(
                    query = searchQuery,
                    apiKey = youtubeApiKey
                )

                if (response.isSuccessful && response.body() != null) {
                    val videos = response.body()!!.items
                    if (videos.isNotEmpty()) {
                        videoAdapter.updateData(videos)
                    } else {
                        Toast.makeText(this@RecommendationsActivity, "No videos found for this mood.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown API error"
                    Log.e("YoutubeAPI", "API Error: ${response.code()} - $errorBody")
                    Toast.makeText(this@RecommendationsActivity, "API Error: Could not load videos.", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Log.e("YoutubeAPI", "Network Exception: ${e.message}", e)
                Toast.makeText(this@RecommendationsActivity, "Network error. Please check your connection.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MoodSelectionActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
