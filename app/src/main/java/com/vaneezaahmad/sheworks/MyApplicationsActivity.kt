package com.vaneezaahmad.sheworks

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject


class MyApplicationsActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    var jobList = ArrayList<Job>()
    var adapter = JobAdapter(jobList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_applications)

        val back_button = findViewById<ImageButton>(R.id.back_button)
        back_button.setOnClickListener {
            finish()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        val searchView = findViewById<android.widget.SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val sharedPreferences = getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
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
        val url = getString(R.string.IP) + "getAppliedJobs.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
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
                        Toast.makeText(this, "Jobs loaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to get jobs", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, "Volley error $error", Toast.LENGTH_SHORT).show()
            })
        {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["user_id"] = mAuth.currentUser?.uid.toString()
                return params
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)

    }

    override fun onResume() {
        super.onResume()
        jobList.clear()
        getJobs()
    }
}