package com.vaneezaahmad.sheworks

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //get device token

        val firebaseService = FirebaseService()
        //firebaseService.sendPushNotification("f0M9NLYAQKmKZ33MyAs3Q1:APA91bFJaX9gy6ejbl1T-Bxc1-vOjtyrf-rYnbvtjVP8br5Va65ZVQTSR3kS-lEJ3lR-fWaF53pm5bSM-AzcFK0qfzdKHeHM-hy0FKnzcgq283BOvpI0SVorrUruoAFCqT5vOJHnv5Tp", "Welcome", "Welcome back", "We've missed you.", mapOf("key" to "value"));

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "FCM token: $token")
            //Toast.makeText(baseContext, "FCM Token: $token", Toast.LENGTH_SHORT).show()
            val dbRef = Firebase.database.getReference("tokens/${FirebaseAuth.getInstance().currentUser?.uid}")
            dbRef.setValue(token)
            firebaseService.sendPushNotification(
                token,
                "Welcome Back!",
                "World",
                "We've missed you.",
                mapOf("key" to "value")
            )
        }

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