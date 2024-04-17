package com.vaneezaahmad.sheworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AudioCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_call)

        val close = findViewById<ImageButton>(R.id.closeButton)
        close.setOnClickListener {
            finish()
        }
    }
}