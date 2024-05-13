package com.vaneezaahmad.sheworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField = findViewById<EditText>(R.id.useremail)
        val passwordField = findViewById<EditText>(R.id.password)

        val btn = findViewById<Button>(R.id.login)
        btn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "User logged in successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, NavigationActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, "Couldn't login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        val forgotpwd = findViewById<TextView>(R.id.forgotpwd)
        forgotpwd.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        val register = findViewById<TextView>(R.id.register)
        register.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        val guest = findViewById<TextView>(R.id.guest)
        guest.setOnClickListener {
            val intent = Intent(this, GuestActivity::class.java)
            startActivity(intent)
        }

    }
}