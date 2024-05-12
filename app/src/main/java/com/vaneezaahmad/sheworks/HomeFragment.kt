package com.vaneezaahmad.sheworks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import org.json.JSONObject
import java.lang.reflect.Method

class HomeFragment : Fragment(R.layout.fragment_home){
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
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
            val intent = Intent(context, AddPostActivity::class.java)
            startActivity(intent);
        }

        val comments = listOf(
            Comment(R.drawable.john, "Ali Ahmed", "Congratulations! \uD83C\uDF89 \uD83C\uDF8A", "2 hours ago"),
            Comment(R.drawable.girl_job, "Amna Khan", "Nice! Wish you best of luck", "1 hour ago"),
            Comment(R.drawable.minha, "Yumna Ahmad", "Awesome! So proud of you", "30 minutes ago")
        )

        val posts = listOf(
            Post(
                R.drawable.john,
                "John Doe",
                "2 hours ago",
                R.drawable.office_room,
                "Just got promoted to Senior Software Engineer at Google! \uD83D\uDE0E",
                123,
                12,
                listOf("Ali Ahmed", "Amna Khan", "Yumna Ahmad"),
                comments
            ),
            Post(
                R.drawable.woman_profile,
                "Jane Smith",
                "1 hour ago",
                R.drawable.work_anniversary,
                "Celebrating my 5th work anniversary at Facebook today! \uD83C\uDF89",
                456,
                23,
                listOf("Ali Ahmed", "Amna Khan", "Yumna Ahmad"),
                comments
            ),
            Post(
                R.drawable.minha,
                "Minha Khan",
                "30 minutes ago",
                R.drawable.amazon_office,
                "Just landed my dream job at Amazon! \uD83D\uDE0D",
                789,
                34,
                listOf("Ali Ahmed", "Amna Khan", "Yumna Ahmad"),
                comments
            )
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.postsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PostsAdapter(posts)


        val reccomendations = listOf(
            Reccomendation(R.drawable.john, "Ali Ahmed", "Software Engineer"),
            Reccomendation(R.drawable.minha, "Minha Khan", "Product Manager"),
            Reccomendation(R.drawable.woman_profile, "Sara Ali", "Data Scientist")
        )

        val reccomendationsRecyclerView = view.findViewById<RecyclerView>(R.id.reccomendationsRecyclerView)
        reccomendationsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        reccomendationsRecyclerView.adapter = RecomendationAdapter(reccomendations)

    }

}