package com.marwadiuniversity.moodmelody

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marwadiuniversity.moodmelody.adapter.HistoryAdapter
import com.marwadiuniversity.moodmelody.utils.PreferencesHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {

    private val TAG = "HistoryActivity"

    private lateinit var btnBack: ImageButton
    private lateinit var rvHistory: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var emptyStateLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        Log.d(TAG, "onCreate called")

        initializeViews()
        setupRecyclerView()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
        loadAndDisplayHistory()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        rvHistory = findViewById(R.id.rvHistory)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView called")
        // Only set the LayoutManager here. The adapter will be set in onResume.
        rvHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun loadAndDisplayHistory() {
        val historyItems = PreferencesHelper.getMoodHistory(this)
        if (historyItems.isEmpty()) {
            rvHistory.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            rvHistory.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
        }

        val historyAdapter = HistoryAdapter(historyItems) { item ->
            val intent = Intent(this, RecommendationsActivity::class.java)
            intent.putExtra("selected_mood", item.mood)
            startActivity(intent)
        }
        rvHistory.adapter = historyAdapter
    }


    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_history

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
                    val intent = Intent(this, FavoritesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    true
                }
                R.id.nav_history -> {
                    true
                }
                else -> false
            }
        }
    }
}
