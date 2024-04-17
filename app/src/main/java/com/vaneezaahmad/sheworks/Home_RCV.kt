package com.vaneezaahmad.sheworks

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home_RCV : AppCompatActivity() {

    private lateinit var adapter: PostAdapter
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_rcv)

        val list = initializeModelList()
        adapter = PostAdapter(list, this)

        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }
    val imagePath = "file:///C:/Users/Minha%20Rehman/Downloads/maaz.jpeg"



    private fun initializeModelList(): ArrayList<Model> {
        val list = arrayListOf<Model>(
            Model(
                "Ali",
                Uri.parse("android.resource://com.firstclass.project/" + R.drawable.minha),
                Uri.parse(""),
                "At Risetech"
            ),
            Model(
                "Ahmed",
                Uri.parse("android.resource://com.firstclass.project/" + R.drawable.john),
                Uri.parse(""),
                "At Data Lab" +
                        "+"
            )
            // Add more data as needed
        )
        return list
    }
}
