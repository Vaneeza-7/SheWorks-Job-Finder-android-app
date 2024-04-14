package com.vaneezaahmad.sheworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ApplyJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_job)

        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }

        val jobDetails = findViewById<Button>(R.id.viewJobDetailsButton)
        jobDetails.setOnClickListener {
                Intent(this, JobDetailsActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}