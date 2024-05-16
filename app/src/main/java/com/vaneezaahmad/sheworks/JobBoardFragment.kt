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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

class JobBoardFragment : Fragment(R.layout.fragment_job_board) {
 val mAuth = FirebaseAuth.getInstance()
    var jobList = ArrayList<Job>()
    var adapter = JobAdapter(jobList)
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
                R.id.account_item -> {
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

        val fab = view.findViewById<FloatingActionButton>(R.id.newPost)

        fab.setOnClickListener {
            val intent = Intent(context, addjobpost::class.java)
            startActivity(intent);
        }

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chipTexts = checkedIds.map { id ->
                    val chip = group.findViewById<Chip>(id)
                    chip?.text.toString()
                }
                // Filter the job list based on all checked chip texts
                val filteredList = jobList.filter { job ->
                    chipTexts.any { chipText ->
                        job.location.contains(chipText, ignoreCase = true) || job.salary.contains(chipText, ignoreCase = true) || job.company.contains(chipText, ignoreCase = true)
                    }
                }
                adapter.filterList(filteredList)

            } else {
                adapter.filterList(jobList)
            }
        }


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
                val filteredList = jobList.filter { it.title.contains(newText ?: "", ignoreCase = true) }
                adapter.filterList(filteredList)
                return false
            }
        })

    }

    fun getJobs()
    {
        val url = requireContext().getString(R.string.IP) + "getJobs.php"
        val stringRequest = object : StringRequest(Method.GET, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("status") == 1) {
                        val jsonArray = obj.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val job = jsonArray.getJSONObject(i)
                            val jobObject = Job(
                                id = job.getInt("id"),
                                title = job.getString("job_title"),
                                company = job.getString("company_name"),
                                description = job.getString("job_description"),
                                qualifications = job.getString("qualifications"),
                                specifications = job.getString("specifications"),
                                skills = job.getString("skills"),
                                responsibilities = job.getString("responsibilities"),
                                salary = job.getString("salary_range"),
                                benefits = job.getString("benefits"),
                                location = job.getString("job_location"),
                                jobType = job.getString("job_type_timings"),
                                contactInfo = job.getString("contact_information"),
                                logo = job.getString("image_url"),
                                timings = job.getString("job_type_timings"),
                                createdAt = job.getString("created_at")
                            )
                            jobList.add(jobObject)
                        }

                        adapter.notifyDataSetChanged()
                        Toast.makeText(context, "Jobs loaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to get jobs", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(context, "Volley error $error", Toast.LENGTH_SHORT).show()
            })
        {}
        Volley.newRequestQueue(requireContext()).add(stringRequest)

    }

    override fun onResume() {
        super.onResume()
        jobList.clear()
        getJobs()
    }

}