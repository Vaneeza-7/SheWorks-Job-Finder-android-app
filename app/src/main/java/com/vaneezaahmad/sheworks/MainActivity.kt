package com.vaneezaahmad.sheworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tap = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        tap.setOnClickListener {
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent)
        }

        if(mAuth.currentUser != null){
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
    }
}