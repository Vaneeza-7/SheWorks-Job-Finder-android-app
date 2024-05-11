package com.vaneezaahmad.sheworks

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_resetpassword)

            val back = findViewById<ImageButton>(R.id.back_button)
            back.setOnClickListener {
                finish()
            }

            val submit = findViewById<Button>(R.id.reset)
            submit.setOnClickListener {
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            }

            val login = findViewById<TextView>(R.id.Login)
            login.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
}