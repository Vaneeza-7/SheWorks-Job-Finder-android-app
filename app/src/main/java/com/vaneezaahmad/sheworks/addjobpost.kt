package com.vaneezaahmad.sheworks

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONException
import org.json.JSONObject

class addjobpost : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()
    var image_url: String = ""
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_addjobpost)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val back_button = findViewById<ImageButton>(R.id.back_button)
        back_button.setOnClickListener {
            finish()
        }

        val jobTitle = findViewById<EditText>(R.id.job_title)
        val companyName = findViewById<EditText>(R.id.company_name)
        val jobDescriptionText = findViewById<EditText>(R.id.job_description_text)
        val qualificationText = findViewById<EditText>(R.id.qualification_text)
        val specificationsText = findViewById<EditText>(R.id.specifications_text)
        val skillsText = findViewById<EditText>(R.id.skills_text)
        val responsibilitiesText = findViewById<EditText>(R.id.responsibilities_text)
        val salaryRangeText = findViewById<EditText>(R.id.salary_range_text)
        val benefitsText = findViewById<EditText>(R.id.benefits_text)
        val jobLocationText = findViewById<EditText>(R.id.job_location_text)
        val jobTypeTimingsText = findViewById<EditText>(R.id.job_type_timings_text)
        val contactInformationText = findViewById<EditText>(R.id.contact_information_text)
        val imageButton = findViewById<ImageButton>(R.id.camera)
        val save_button = findViewById<Button>(R.id.add_button)

        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val storageRef =
                    storage.reference.child("jobImages/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}")
                storageRef.putFile(uri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        image_url = it.toString()
                        Glide.with(this).load(image_url).into(imageButton)
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        imageButton.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        save_button.setOnClickListener {
            val jobTitleText = jobTitle.text.toString()
            val companyNameText = companyName.text.toString()
            val jobDescription = jobDescriptionText.text.toString()
            val qualification = qualificationText.text.toString()
            val specifications = specificationsText.text.toString()
            val skills = skillsText.text.toString()
            val responsibilities = responsibilitiesText.text.toString()
            val salaryRange = salaryRangeText.text.toString()
            val benefits = benefitsText.text.toString()
            val jobLocation = jobLocationText.text.toString()
            val jobTypeTimings = jobTypeTimingsText.text.toString()
            val contactInformation = contactInformationText.text.toString()

            if (jobTitleText.isEmpty() || companyNameText.isEmpty() || jobDescription.isEmpty() || qualification.isEmpty() || specifications.isEmpty() || skills.isEmpty() || responsibilities.isEmpty() || salaryRange.isEmpty() || benefits.isEmpty() || jobLocation.isEmpty() || jobTypeTimings.isEmpty() || contactInformation.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                addJobPost(jobTitleText, companyNameText, jobDescription, qualification, specifications, skills, responsibilities, salaryRange, benefits, jobLocation, jobTypeTimings, contactInformation);

            }
        }
    }

    fun addJobPost(jobTitleText: String, companyNameText : String,jobDescription : String,qualification: String, specifications: String, skills: String ,responsibilities: String, salaryRange: String ,benefits: String , jobLocation: String, jobTypeTimings:String, contactInformation : String)
    {
        val url = getString(R.string.IP) + "addJobPost.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("success")) {
                        Toast.makeText(
                            this,
                            "Job Post Added Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add job post", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(
                    this,
                    "Failed to add job post: $error",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["job_title"] = jobTitleText
                params["company_name"] = companyNameText
                params["job_description"] = jobDescription
                params["qualifications"] = qualification
                params["specifications"] = specifications
                params["skills"] = skills
                params["responsibilities"] = responsibilities
                params["salary_range"] = salaryRange
                params["benefits"] = benefits
                params["job_location"] = jobLocation
                params["job_type_timings"] = jobTypeTimings
                params["contact_information"] = contactInformation
                params["image_url"] = image_url
                return params;
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}