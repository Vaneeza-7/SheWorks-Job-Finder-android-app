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
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(R.layout.fragment_home){
    val mAuth = FirebaseAuth.getInstance()
    var posts: ArrayList<Post> = ArrayList()
    var adapter = PostsAdapter(posts)
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

        getPosts();
        val recyclerView = view.findViewById<RecyclerView>(R.id.postsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter


        val reccomendations = listOf(
            Reccomendation(R.drawable.john, "Ali Ahmed", "Software Engineer"),
            Reccomendation(R.drawable.minha, "Minha Khan", "Product Manager"),
            Reccomendation(R.drawable.woman_profile, "Sara Ali", "Data Scientist")
        )

        val reccomendationsRecyclerView = view.findViewById<RecyclerView>(R.id.reccomendationsRecyclerView)
        reccomendationsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        reccomendationsRecyclerView.adapter = RecomendationAdapter(reccomendations)

    }
    fun getPosts() {
        val url = requireContext().getString(R.string.IP) + "getPosts.php"
        val request = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getInt("status")
                    if (status == 1) {
                        val postsJson = jsonResponse.getJSONArray("data")
                        posts.clear()
                        for (i in 0 until postsJson.length()) {
                            val postJson = postsJson.getJSONObject(i)
                            val timeAgo = getTimeAgo(postJson.getLong("timeAgo"))
                            val post = Post(
                                postJson.getString("profileImage"),
                                postJson.getString("username"),
                                timeAgo,
                                postJson.getString("postImage"),
                                postJson.getString("postContent"),
                                postJson.getInt("likesCount"),
                                postJson.getInt("commentsCount")
                            )
                            posts.add(post)
                            adapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: JSONException) {
                    Toast.makeText(
                        context,
                        "Invalid JSON response: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            { error ->
                Toast.makeText(context, error.message ?: "An error occurred", Toast.LENGTH_SHORT).show()
            })
        {}
        Volley.newRequestQueue(requireContext()).add(request)
    }

    fun getTimeAgo(time: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - time

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "just now"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} minutes ago"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} hours ago"
            else -> "${TimeUnit.MILLISECONDS.toDays(diff)} days ago"
        }
    }

    override fun onResume() {
        super.onResume()
        getPosts()
    }
}