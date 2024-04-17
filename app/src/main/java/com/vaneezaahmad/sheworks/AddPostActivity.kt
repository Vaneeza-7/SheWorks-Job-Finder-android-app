package com.vaneezaahmad.sheworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AddPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }
    }
}