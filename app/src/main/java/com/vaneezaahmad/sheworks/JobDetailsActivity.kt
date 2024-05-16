package com.vaneezaahmad.sheworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject

class JobDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }

        val apply = findViewById<Button>(R.id.apply_button)
        apply.setOnClickListener {
            Intent(this, ApplyJobActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }

        val jobId = intent.getIntExtra("jobId", 0).toString()
        getJobDetails(jobId)
    }

    fun getJobDetails(jobId: String) {
        val url = getString(R.string.IP) + "getJobDetails.php"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.POST, url,
            { response ->
                Log.d("JobDetailsActivity", "Response: $response")
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        val jobDetails = obj.getJSONObject("job_details")
                        val job = Job(
                            jobDetails.getInt("id"),
                            jobDetails.getString("job_title"),
                            jobDetails.getString("company_name"),
                            jobDetails.getString("job_description"),
                            jobDetails.getString("qualifications"),
                            jobDetails.getString("specifications"),
                            jobDetails.getString("skills"),
                            jobDetails.getString("responsibilities"),
                            jobDetails.getString("salary_range"),
                            jobDetails.getString("benefits"),
                            jobDetails.getString("job_location"),
                            jobDetails.getString("job_type_timings"),
                            jobDetails.getString("contact_information"),
                            jobDetails.getString("image_url"),
                            jobDetails.getString("job_type_timings"),
                            jobDetails.getString("created_at")
                        )
                        updateUI(job)
                    } else {
                        val message = obj.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        Log.e("JobDetailsActivity", "Error: $message")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("JobDetailsActivity", "JSON parsing error: ${e.message}")
                }
            },
            { error ->
                Toast.makeText(this, "Volley error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("JobDetailsActivity", "Volley error: ${error.message}")
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["job_id"] = jobId
                Log.d("JobDetailsActivity", "Params: $params")
                return params
            }
        }
        queue.add(stringRequest)
    }

    fun updateUI(job: Job) {
        Log.d("JobDetailsActivity", "Updating UI with job details")
        findViewById<TextView>(R.id.job_title).text = job.title
        findViewById<TextView>(R.id.company_name).text = job.company
        findViewById<TextView>(R.id.salary_range_text).text = job.salary
        findViewById<TextView>(R.id.job_location_text).text = job.location
        findViewById<TextView>(R.id.job_type_timings_text).text = job.jobType
        findViewById<TextView>(R.id.posted_time).text = "\uD83D\uDD52 Posted on " + job.createdAt
        findViewById<TextView>(R.id.job_description_text).text = job.description
        findViewById<TextView>(R.id.qualification_text).text = job.qualifications
        findViewById<TextView>(R.id.specifications_text).text = job.specifications
        findViewById<TextView>(R.id.skills_text).text = job.skills
        findViewById<TextView>(R.id.responsibilities_text).text = job.responsibilities
        findViewById<TextView>(R.id.benefits_text).text = job.benefits
        findViewById<TextView>(R.id.contact_information_text).text = job.contactInfo
        Glide.with(this).load(job.logo).into(findViewById<ImageView>(R.id.company_logo))
    }

    override fun onResume() {
        super.onResume()
        val jobId = intent.getIntExtra("jobId", 0).toString()
        getJobDetails(jobId)
    }
}
