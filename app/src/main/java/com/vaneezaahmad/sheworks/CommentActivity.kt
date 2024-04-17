package com.vaneezaahmad.sheworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val back = findViewById<ImageView>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }

        val comments = listOf(
            Comment(R.drawable.john, "Ali Ahmed", "Congratulations! \uD83C\uDF89 \uD83C\uDF8A", "2 hours ago"),
            Comment(R.drawable.girl_job, "Amna Khan", "Nice! Wish you best of luck", "1 hour ago"),
            Comment(R.drawable.minha, "Yumna Ahmad", "Awesome! So proud of you", "30 minutes ago")
        )

        val recyclerview = findViewById<RecyclerView>(R.id.commentRecyclerView)
        recyclerview.adapter = CommentAdapter(comments)
        recyclerview.layoutManager = LinearLayoutManager(this)
    }
}