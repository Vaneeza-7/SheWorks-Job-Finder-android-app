package com.vaneezaahmad.sheworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NewChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val chats = listOf(
            User(
                "Sajida Naeem", R.drawable.baseline_person_24),
            User(
                "Aqsa Ali", R.drawable.twitter_logo),
            User(
                "Yumna Ahmad", R.drawable.minha),
            User(
                "Minha Rehman", R.drawable.baseline_person_24),
            User(
                "Areeba Adnan", R.drawable.baseline_local_florist_24),
            User(
                "Aimen Asad", R.drawable.baseline_person_24),
            User(
                "Bareera Fatima", R.drawable.baseline_person_24),
            User(
                "Hadia Faisal", R.drawable.woman_profile),
            User(
                "Laiba Khan", R.drawable.google_color_svgrepo_com),
            User(
                "Binte Ahmed", R.drawable.john),
            User(
                "Arsh Malik", R.drawable.briefcase_svgrepo_com),
            User(
                "Faziha Jabir", R.drawable.baseline_person_24)
        )

        val recylerView = findViewById<RecyclerView>(R.id.recyclerView)
        recylerView.adapter = ChatAdapter(chats)
        recylerView.layoutManager = LinearLayoutManager(this)

    }
}