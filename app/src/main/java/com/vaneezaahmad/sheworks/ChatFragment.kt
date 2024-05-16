package com.vaneezaahmad.sheworks

import android.content.Context
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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

class ChatFragment : Fragment(R.layout.fragment_chat) {
    val mAuth = FirebaseAuth.getInstance()
    var userList = ArrayList<User>()
    var adapter = ChatAdapter(userList)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
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

        val fab = view.findViewById<FloatingActionButton>(R.id.newChat)
        fab.setOnClickListener {
            // Open new chat activity
            val intent = Intent(context, NewChatActivity::class.java)
            startActivity(intent);
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
                R.id.applications_item -> {
                    // Start the activity or fragment for My Applications
                    val intent = Intent(requireContext(), MyApplicationsActivity::class.java)
                    startActivity(intent)
                }


            }
            // Close the navigation drawer when an item is tapped.
            drawerLayout.closeDrawers()
            true
        }

        getUsers();
        val recyclerView = view.findViewById<RecyclerView>(R.id.chatsRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val searchView = view.findViewById<android.widget.SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val sharedPreferences = requireContext().getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val searches = sharedPreferences.getStringSet("searches", mutableSetOf()) ?: mutableSetOf()
                searches.add(query)
                editor.putStringSet("searches", searches)
                editor.apply()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = userList.filter { it.username.contains(newText ?: "", ignoreCase = true) }
                adapter.filterList(filteredList)
                return false
            }
        })


    }

    fun getUsers()
    {
        val url = requireContext().getString(R.string.IP) + "getUsers.php"
        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("status") == 1) {
                        val jsonArray = obj.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val user = jsonArray.getJSONObject(i)
                            val userObject = User(
                                uid = user.getString("id"),
                                username = user.getString("username"),
                                email = user.getString("email"),
                                contactNumber = user.getString("contact_number"),
                                country = user.getString("country"),
                                city = user.getString("city"),
                                useCase = user.getString("use_case"),
                                password = user.getString("password"),
                                profileImage = user.getString("profileImage"),
                            )
                            if (userObject.uid != mAuth.currentUser?.uid) {
                                userList.add(userObject)
                            }
                        }

                        adapter.notifyDataSetChanged()
                        if (isAdded) {
                            Toast.makeText(context, "Users loaded successfully", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        if (isAdded) {
                            Toast.makeText(context, "No users found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    if (isAdded) {
                        Toast.makeText(context, "Error: " + e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            { error ->
                if (isAdded) {
                    Toast.makeText(context, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
        {}
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }
}