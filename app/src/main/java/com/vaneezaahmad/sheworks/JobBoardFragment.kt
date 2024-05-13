package com.vaneezaahmad.sheworks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton

class JobBoardFragment : Fragment(R.layout.fragment_job_board) {
 val mAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawerLayout)
        val menu = view.findViewById<ImageButton>(R.id.menu_button)
        menu.setOnClickListener {
            if (drawerLayout.isOpen()) {
                drawerLayout.close()
            } else {
                drawerLayout.open()
            }
        }

        val navigationView: NavigationView = view.findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.notification_item -> {
                    // Start the activity or fragment for Notifications
                    val intent = Intent(requireContext(), NotificationActivity::class.java)
                    startActivity(intent)
                }
                R.id.saved_item -> {
                    // Start the activity or fragment for My Saves
                    val intent = Intent(requireContext(), EditProfile::class.java)
                    startActivity(intent)
                }
                R.id.about -> {
                    // Start the activity or fragment for Settings
                    val intent = Intent(requireContext(), AboutActivity::class.java)
                    startActivity(intent)
                }
                R.id.logout_item -> {
                    // Start the activity or fragment for Logout
                    mAuth.signOut()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    //call finish() on the activity
                    activity?.finish()
                }
                R.id.feedback -> {
                    // Start the activity or fragment for Logout
                    val intent = Intent(requireContext(), FeedbackActivity::class.java)
                    startActivity(intent)
                }


            }
            // Close the navigation drawer when an item is tapped.
            drawerLayout.closeDrawers()
            true
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.newPost)

        fab.setOnClickListener {
            val intent = Intent(context, addjobpost::class.java)
            startActivity(intent);
        }

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
        )

        val adapter = JobAdapter(jobList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

    }

}