package com.marwadiuniversity.moodmelody

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MoodSelectionActivity : AppCompatActivity() {

    private val moodList = mutableListOf<FrameLayout>()
    private var selectedMoodView: FrameLayout? = null
    private var selectedMood: String? = null
    private lateinit var loadingOverlay: FrameLayout
    private lateinit var tvMatchingVibe: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_selection)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Fix status bar overlap
        setupWindowInsets()

        initializeViews()
        setupMoodClickListeners()
    }

    private fun setupWindowInsets() {
        val rootLayout = findViewById<View>(android.R.id.content)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply top padding to toolbar to push it below status bar
            toolbar.setPadding(
                toolbar.paddingLeft,
                systemBars.top,
                toolbar.paddingRight,
                toolbar.paddingBottom
            )

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun initializeViews() {
        loadingOverlay = findViewById(R.id.loadingOverlay)
        tvMatchingVibe = findViewById(R.id.tvMatchingVibe)

        moodList.addAll(
            listOf(
                findViewById(R.id.moodHappy),
                findViewById(R.id.moodSad),
                findViewById(R.id.moodEnergetic),
                findViewById(R.id.moodCalm),
                findViewById(R.id.moodChill),
                findViewById(R.id.moodRomantic),
                findViewById(R.id.moodParty),
                findViewById(R.id.moodRelaxed)
            )
        )
    }

    private fun setupMoodClickListeners() {
        for (mood in moodList) {
            mood.setOnClickListener { view ->
                selectMood(view as FrameLayout)
            }
        }
    }

    private fun selectMood(clickedMood: FrameLayout) {
        // Deselect previous mood if any
        selectedMoodView?.let {
            if (it != clickedMood) {
                deselectMood(it)
            }
        }

        selectedMoodView = clickedMood
        selectedMood = clickedMood.tag.toString()

        // Get the emoji text
        val emojiView = clickedMood.getChildAt(0) as TextView
        val emojiText = emojiView.text.toString()

        // Add glow effect
        emojiView.setShadowLayer(35f, 0f, 0f, Color.parseColor("#FFD54F"))

        // Bounce and scale animation
        val scaleXUp = ObjectAnimator.ofFloat(clickedMood, View.SCALE_X, 1f, 1.3f)
        val scaleYUp = ObjectAnimator.ofFloat(clickedMood, View.SCALE_Y, 1f, 1.3f)
        val scaleXDown = ObjectAnimator.ofFloat(clickedMood, View.SCALE_X, 1.3f, 1.2f)
        val scaleYDown = ObjectAnimator.ofFloat(clickedMood, View.SCALE_Y, 1.3f, 1.2f)

        AnimatorSet().apply {
            play(scaleXUp).with(scaleYUp)
            play(scaleXDown).with(scaleYDown).after(scaleXUp)
            duration = 200
            interpolator = BounceInterpolator()
            start()
        }

        // Show matching vibe message and navigate
        showMatchingVibeAndNavigate(emojiText, selectedMood!!)
    }

    private fun showMatchingVibeAndNavigate(emoji: String, mood: String) {
        // Format mood name with capital first letter
        val moodFormatted = mood.replaceFirstChar { it.uppercase() }

        // Show loading overlay with message
        tvMatchingVibe.text = "Matching your vibe with\n$emoji $moodFormatted"
        loadingOverlay.visibility = View.VISIBLE

        // Fade in animation for overlay
        loadingOverlay.alpha = 0f
        loadingOverlay.animate()
            .alpha(1f)
            .setDuration(300)
            .start()

        // Also show toast
        Toast.makeText(this, "Matching your vibe with $emoji $moodFormatted", Toast.LENGTH_SHORT).show()

        // Navigate after 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToRecommendations()
        }, 1500)
    }

    private fun deselectMood(moodFrame: FrameLayout) {
        val emojiView = moodFrame.getChildAt(0) as TextView
        emojiView.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)

        val scaleX = ObjectAnimator.ofFloat(moodFrame, View.SCALE_X, moodFrame.scaleX, 1f)
        val scaleY = ObjectAnimator.ofFloat(moodFrame, View.SCALE_Y, moodFrame.scaleY, 1f)

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 250
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun navigateToRecommendations() {
        val intent = Intent(this, RecommendationsActivity::class.java)
        intent.putExtra("selected_mood", selectedMood)
        startActivity(intent)
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Reset UI for when user comes back
        Handler(Looper.getMainLooper()).postDelayed({
            loadingOverlay.visibility = View.GONE
            selectedMoodView?.let { deselectMood(it) }
            selectedMoodView = null
        }, 500)
    }

    // ---------------------------------------------------
    //              3 DOT MENU IMPLEMENTATION
    // ---------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)

        // Force show icons in overflow menu
        if (menu != null) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                try {
                    val method = menu.javaClass.getDeclaredMethod(
                        "setOptionalIconsVisible",
                        Boolean::class.javaPrimitiveType
                    )
                    method.isAccessible = true
                    method.invoke(menu, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            // ----- ABOUT US -----
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }

            // ----- SHARE APP -----
            R.id.menu_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Try the MoodMelody App! 🎶\nConnect your mood with music!\nDownload now: [Your Play Store Link]"
                )
                startActivity(Intent.createChooser(shareIntent, "Share MoodMelody via"))
                return true
            }

            // ----- WRITE US (EMAIL) -----
            R.id.menu_write_us -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("sasmita.das0302@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Feedback for MoodMelody")
                    putExtra(Intent.EXTRA_TEXT, "Hi MoodMelody Team,\n\n")
                }

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"))
                } catch (e: Exception) {
                    Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            // ----- RATE US -----
            R.id.menu_rate_us -> {
                val appPackage = packageName
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            android.net.Uri.parse("market://details?id=$appPackage")
                        )
                    )
                } catch (e: Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            android.net.Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
                        )
                    )
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}