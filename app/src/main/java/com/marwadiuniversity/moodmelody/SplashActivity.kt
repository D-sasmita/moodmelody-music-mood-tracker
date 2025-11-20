package com.marwadiuniversity.moodmelody

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var rippleOuter: View
    private lateinit var rippleMiddle: View
    private lateinit var rippleInner: View
    private lateinit var horizontalLine: View
    private lateinit var bar1: View
    private lateinit var bar2: View
    private lateinit var bar3: View
    private lateinit var musicNote: View

    private val runningAnimators = mutableListOf<Animator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideSystemUI()
        initializeViews()
        startAnimations()

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 3000)
    }

    private fun initializeViews() {
        rippleOuter = findViewById(R.id.rippleOuter)
        rippleMiddle = findViewById(R.id.rippleMiddle)
        rippleInner = findViewById(R.id.rippleInner)
        horizontalLine = findViewById(R.id.horizontalLine)
        bar1 = findViewById(R.id.bar1)
        bar2 = findViewById(R.id.bar2)
        bar3 = findViewById(R.id.bar3)
        musicNote = findViewById(R.id.ivMusicNote)
    }

    private fun startAnimations() {
        animateMusicNote()
        animateRipples(150)
        animateHorizontalLine(250)
        animateBars(350)
    }

    private fun animateMusicNote() {
        val rotateAnimator = ObjectAnimator.ofFloat(musicNote, View.ROTATION, 0f, 360f).apply {
            duration = 3000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }

        val scaleXAnimator = ObjectAnimator.ofFloat(musicNote, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleYAnimator = ObjectAnimator.ofFloat(musicNote, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            playTogether(rotateAnimator, scaleXAnimator, scaleYAnimator)
            start()
            runningAnimators.add(this)
        }
    }

    private fun animateRipples(startDelay: Long) {
        val outerScaleX = ObjectAnimator.ofFloat(rippleOuter, View.SCALE_X, 0.8f, 1f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val outerScaleY = ObjectAnimator.ofFloat(rippleOuter, View.SCALE_Y, 0.8f, 1f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val outerAlpha = ObjectAnimator.ofFloat(rippleOuter, View.ALPHA, 0.3f, 0.6f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val middleScaleX = ObjectAnimator.ofFloat(rippleMiddle, View.SCALE_X, 0.9f, 1.1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val middleScaleY = ObjectAnimator.ofFloat(rippleMiddle, View.SCALE_Y, 0.9f, 1.1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val middleAlpha = ObjectAnimator.ofFloat(rippleMiddle, View.ALPHA, 0.4f, 0.7f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val innerScaleX = ObjectAnimator.ofFloat(rippleInner, View.SCALE_X, 1f, 1.05f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val innerScaleY = ObjectAnimator.ofFloat(rippleInner, View.SCALE_Y, 1f, 1.05f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            this.startDelay = startDelay
            playTogether(
                outerScaleX, outerScaleY, outerAlpha,
                middleScaleX, middleScaleY, middleAlpha,
                innerScaleX, innerScaleY
            )
            start()
            runningAnimators.add(this)
        }
    }

    private fun animateHorizontalLine(startDelay: Long) {
        val scaleXAnimator = ObjectAnimator.ofFloat(horizontalLine, View.SCALE_X, 1f, 1.2f, 1f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val alphaAnimator = ObjectAnimator.ofFloat(horizontalLine, View.ALPHA, 0.3f, 0.8f, 0.3f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            this.startDelay = startDelay
            playTogether(scaleXAnimator, alphaAnimator)
            start()
            runningAnimators.add(this)
        }
    }

    private fun animateBars(startDelay: Long) {
        val bar1Animator = ObjectAnimator.ofFloat(bar1, View.SCALE_Y, 1f, 1.5f, 0.8f, 1.2f, 1f).apply {
            duration = 1200
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val bar2Animator = ObjectAnimator.ofFloat(bar2, View.SCALE_Y, 1f, 0.7f, 1.4f, 0.9f, 1f).apply {
            duration = 1200
            repeatCount = ValueAnimator.INFINITE
            this.startDelay = 100
            interpolator = AccelerateDecelerateInterpolator()
        }

        val bar3Animator = ObjectAnimator.ofFloat(bar3, View.SCALE_Y, 1f, 1.6f, 0.7f, 1.3f, 1f).apply {
            duration = 1200
            repeatCount = ValueAnimator.INFINITE
            this.startDelay = 200
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            this.startDelay = startDelay
            playTogether(bar1Animator, bar2Animator, bar3Animator)
            start()
            runningAnimators.add(this)
        }
    }

    private fun navigateToNextScreen() {
        val intent = Intent(this, MoodSelectionActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API 30) and above - Modern approach
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For Android 10 (API 29) and below - Legacy approach
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runningAnimators.forEach { it.cancel() }
    }
}