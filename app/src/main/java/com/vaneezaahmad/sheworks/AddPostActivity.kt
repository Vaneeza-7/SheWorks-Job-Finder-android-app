package com.vaneezaahmad.sheworks

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AddPostActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }

        val cameraButton = findViewById<ImageButton>(R.id.camera)
        val addImage = findViewById<TextView>(R.id.addImage)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        var url: String = ""


        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val storageRef =
                    storage.reference.child("postImages/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}")
                storageRef.putFile(uri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        url = it.toString()
                        Glide.with(this).load(url).into(cameraButton)
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        cameraButton.setOnClickListener {
            // pick image
            selectImageLauncher.launch("image/*")
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            if (title.isEmpty() || url.isEmpty()) {
                Toast.makeText(this, "Please select image and add comment", Toast.LENGTH_SHORT).show()
            } else {
                // save post to database
                val uid = mAuth.currentUser?.uid
                savePostToDB(title, url, uid!!)
            }
        }
    }

    fun savePostToDB(title: String, imageurl: String, uid: String) {
        val url = getString(R.string.IP) + "insertPost.php"
        val request = object: StringRequest(Method.POST,url,
            {response->
                try {
                    val JsonResponse = JSONObject(response)
                    val status = JsonResponse.getInt("status")
                    if (status == 1) {
                        Toast.makeText(this, "Post saved successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Couldn't save post", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            {
                Toast.makeText(this, "Couldn't save post", Toast.LENGTH_SHORT).show()

            })
        {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = uid
                params["timeAgo"] = System.currentTimeMillis().toString()
                params["postImage"] = imageurl
                params["postContent"] = title
                return params
            }

        }
        Volley.newRequestQueue(this).add(request)
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
}