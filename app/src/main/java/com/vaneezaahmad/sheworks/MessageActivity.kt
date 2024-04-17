package com.vaneezaahmad.sheworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val call = findViewById<ImageButton>(R.id.call)
        call.setOnClickListener {
            val intent = Intent(this, AudioCallActivity::class.java)
            startActivity(intent)
        }

        val videoCall = findViewById<ImageButton>(R.id.video)
        videoCall.setOnClickListener {
            val intent = Intent(this, VideoCallActivity::class.java)
            startActivity(intent)
        }
    }
}