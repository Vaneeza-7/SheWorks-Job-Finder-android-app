package com.vaneezaahmad.sheworks

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONException
import org.json.JSONObject

class ApplyJobActivity : AppCompatActivity() {
    private val PICK_RESUME_REQUEST = 1
    val storage = FirebaseStorage.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    var resumeUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_job)

        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }

        //load page with job details
        val jobId = intent.getIntExtra("jobId", 0).toString()
        getJobDetails(jobId)

        val textViewProfileInfo = findViewById<TextView>(R.id.textViewProfileInfo)
        textViewProfileInfo.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            intent.putExtra("openProfileFragment", true)
            startActivity(intent)
            finish()
        }

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val storageRef = storage.reference
                val resumeRef = storageRef.child("resumes/${mAuth.currentUser?.uid}")
                resumeRef.putFile(it)
                    .addOnSuccessListener {
                        val imageView = findViewById<ImageView>(R.id.imageViewResume)
                        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pdf_icon))
                        //downlaod url
                        resumeRef.downloadUrl.addOnSuccessListener {
                            Toast.makeText(this, "Resume uploaded successfully", Toast.LENGTH_SHORT).show()
                            resumeUrl = it.toString()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to upload resume", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        val iconButton = findViewById<Button>(R.id.iconButton)
        iconButton.setOnClickListener {
            getContent.launch("application/pdf")
        }

        val editTextCoverLetter = findViewById<TextView>(R.id.editTextCoverLetter)
        val editTextAdditionalInfo = findViewById<TextView>(R.id.editTextAdditionalInfo)
        val checkBoxConfirmInfo = findViewById<CheckBox>(R.id.checkBoxConfirmInfo)
        val applyButton = findViewById<Button>(R.id.buttonSubmit)
        applyButton.setOnClickListener {
            if (resumeUrl.isEmpty()) {
                Toast.makeText(this, "Please upload your resume", Toast.LENGTH_SHORT).show()
            } else if (editTextCoverLetter.text.isEmpty()) {
                Toast.makeText(this, "Please enter your cover letter", Toast.LENGTH_SHORT).show()
            } else if (!checkBoxConfirmInfo.isChecked) {
                Toast.makeText(this, "Please confirm the information", Toast.LENGTH_SHORT).show()
            } else if(editTextAdditionalInfo.text.isEmpty()){
                Toast.makeText(this, "Please enter additional information", Toast.LENGTH_SHORT).show()
            } else {
                postJobApplication(resumeUrl, editTextCoverLetter.text.toString(), editTextAdditionalInfo.text.toString())
            }
        }

    }
    fun postJobApplication(resumeUrl : String, coverLetter : String, additionalInfo : String){
        val url = getString(R.string.IP) + "applyJob.php"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.POST, url,
            { response ->
                Log.d("ApplyJobActivity", "Response: $response")
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        Toast.makeText(this, "Job application submitted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val message = obj.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        Log.e("ApplyJobActivity", "Error: $message")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("ApplyJobActivity", "JSON parsing error: ${e.message}")
                }
            },
            { error ->
                Toast.makeText(this, "Volley error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("ApplyJobActivity", "Volley error: ${error.message}")
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["job_id"] = intent.getIntExtra("jobId", 0).toString()
                params["user_id"] = mAuth.currentUser?.uid.toString()
                params["resume_path"] = resumeUrl
                params["cover_letter"] = coverLetter
                params["additional_info"] = additionalInfo
                return params
            }
        }
        queue.add(stringRequest)
    }

    fun getJobDetails(id: String)
    {
        val url = getString(R.string.IP) + "getJobDetails.php"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.POST, url,
            { response ->
                Log.d("ApplyJobActivity", "Response: $response")
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        val jobDetails = obj.getJSONObject("job_details")
                        val id = jobDetails.getInt("id")
                        val job_title = jobDetails.getString("job_title")
                        val company_name = jobDetails.getString("company_name")
                        val company_image = jobDetails.getString("image_url")
                        updateUI(id, job_title, company_name, company_image);
                    } else {
                        val message = obj.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        Log.e("ApplyJobActivity", "Error: $message")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("ApplyJobActivity", "JSON parsing error: ${e.message}")
                }
            },
            { error ->
                Toast.makeText(this, "Volley error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("ApplyJobActivity", "Volley error: ${error.message}")
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["job_id"] = id
                Log.d("ApplyJobActivity", "Params: $params")
                return params
            }
        }
        queue.add(stringRequest)
    }

    fun updateUI (id: Int, title: String, name: String, company_image: String){
        val company_logo = findViewById<ImageView>(R.id.company_logo)
        val job_title = findViewById<TextView>(R.id.job_title)
        val company_name = findViewById<TextView>(R.id.company_name)

        job_title.text = title
        company_name.text = name
        Glide.with(company_logo.context).load(company_image).into(company_logo)
    }
}