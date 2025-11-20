package com.marwadiuniversity.moodmelody

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    private lateinit var developer1Card: LinearLayout
    private lateinit var developer2Card: LinearLayout
    private lateinit var developer3Card: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Handle back button manually
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupDeveloperCards()
    }

    private fun setupDeveloperCards() {
        developer1Card = findViewById(R.id.developer1Card)
        developer2Card = findViewById(R.id.developer2Card)
        developer3Card = findViewById(R.id.developer3Card)

        developer1Card.setOnClickListener {
            openEmailClient(getString(R.string.developer1_email), getString(R.string.developer1_name))
        }

        developer2Card.setOnClickListener {
            openEmailClient(getString(R.string.developer2_email), getString(R.string.developer2_name))
        }

        developer3Card.setOnClickListener {
            openEmailClient(getString(R.string.developer3_email), getString(R.string.developer3_name))
        }
    }

    private fun openEmailClient(email: String, name: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "Regarding ${getString(R.string.app_name)} - From About Page")
            putExtra(Intent.EXTRA_TEXT, "Hi $name,\n\n")
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email to $name"))
        } catch (e: Exception) {
            android.widget.Toast.makeText(
                this,
                "No email app found",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}
