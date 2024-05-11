package com.vaneezaahmad.sheworks

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signUp = findViewById<Button>(R.id.signUp)
        signUp.setOnClickListener {
            val intent = Intent(this, VerifyNumberActivity::class.java)
            startActivity(intent)
        }

    }
}