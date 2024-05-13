package com.vaneezaahmad.sheworks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GuestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest)

        val jobList = listOf(
            Job(
                "Software Engineer",
                "Google",
                "$100k",
                "Full Time",
                "Mountain View, CA",
                "40 hrs/week",
                R.drawable.google_color_svgrepo_com
            ),
            Job(
                "Product Manager",
                "Facebook",
                "$120k",
                "Remote",
                "Menlo Park, CA",
                "17 hrs/week",
                R.drawable.facebook
            ),
            Job(
                "UX Designer",
                "Systems Private Limited",
                "$110k",
                "Part Time",
                "Cupertino, CA",
                "9-5 Mon-Fri",
                R.drawable.company_default_logo
            ),
            Job(
                "Data Scientist",
                "Amazon",
                "$130k",
                "Full Time",
                "Seattle, WA",
                "6 days/week",
                R.drawable.amazon
            ),
            Job(
                "Software Engineer",
                "Microsoft",
                "$110k",
                "Full Time",
                "Redmond, WA",
                "8 hrs/day",
                R.drawable.microsoft_logo
            ),
            Job(
                "Software Engineer",
                "Twitter",
                "$120k",
                "Full Time",
                "Los Gatos, CA",
                "9am-5am",
                R.drawable.twitter_logo
            )
            // Add other jobs here...
        )

        val adapter = guestdashboardadapter(jobList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
