package com.marwadiuniversity.moodmelody

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marwadiuniversity.moodmelody.adapter.VideoAdapter
import com.marwadiuniversity.moodmelody.api.VideoItem
import com.marwadiuniversity.moodmelody.utils.PreferencesHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var rvFavorites: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var videoAdapter: VideoAdapter
    private var favoriteVideos = mutableListOf<VideoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        initializeViews()
        setupRecyclerView()
        setupBottomNavigation()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        rvFavorites = findViewById(R.id.rvFavorites)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        favoriteVideos = PreferencesHelper.getFavorites(this).toMutableList()

        if (favoriteVideos.isEmpty()) {
            rvFavorites.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            rvFavorites.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
        }

        videoAdapter = VideoAdapter(
            favoriteVideos,
            { video ->
                // No null check needed here, data is now safe
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${video.id.videoId}"))
                if (intent.resolveActivity(packageManager) == null) {
                    intent.data = Uri.parse("https://www.youtube.com/watch?v=${video.id.videoId}")
                }
                startActivity(intent)
            },
            { video, position ->
                PreferencesHelper.removeFavorite(this, video)
                favoriteVideos.removeAt(position)
                videoAdapter.notifyItemRemoved(position)
                videoAdapter.notifyItemRangeChanged(position, favoriteVideos.size)
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                if (favoriteVideos.isEmpty()) {
                    rvFavorites.visibility = View.GONE
                    emptyStateLayout.visibility = View.VISIBLE
                }
            }
        )

        rvFavorites.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = videoAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_favorites

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MoodSelectionActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_favorites -> {
                    true
                }
                R.id.nav_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}