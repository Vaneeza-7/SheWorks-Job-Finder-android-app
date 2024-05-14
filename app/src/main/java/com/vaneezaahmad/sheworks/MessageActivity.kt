package com.vaneezaahmad.sheworks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MessageActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    private var recorder: MediaRecorder? = null
    private var fileName: String? = null
    private var photoUri: Uri? = null
    private var currentPhotoPath: String? = null
    var senderImage = ""

    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
        private const val REQUEST_CODE_CAMERA = 101
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)


        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val call = findViewById<ImageButton>(R.id.call)
        call.setOnClickListener {
            val intent = Intent(this, AudioCallActivity::class.java)
            startActivity(intent)
        }

        val videoCall = findViewById<ImageButton>(R.id.video)
        videoCall.setOnClickListener {
            val intent = Intent(this, VideoCallActivity::class.java)
            startActivity(intent)
        }

        var audioUrl = ""
        var mediaUrl = ""
        val mic = findViewById<ImageButton>(R.id.mic)

        var heading = findViewById<TextView>(R.id.heading)
        var receivername = intent.getStringExtra("receivername")
        heading.text = receivername

        var receiverimage = intent.getStringExtra("receiverimage")
        var profileImage = findViewById<CircleImageView>(R.id.profile_image)
        Glide.with(this).load(receiverimage).centerCrop().into(profileImage)

        val receiverId = intent.getStringExtra("receiveruid")
        getSenderImage();


        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val uri = data?.data
                    val mimeType = contentResolver.getType(uri!!) // Get the MIME type
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    // Save the uri to firebase storage
                    val storageRef = FirebaseStorage.getInstance();
                    val st =
                        storageRef.reference.child("chatMedia/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}.$extension")
                    val uploadTask = st.putFile(uri!!)
                    uploadTask.addOnSuccessListener {
                        Toast.makeText(this, "Media uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                        st.downloadUrl.addOnSuccessListener {
                            mediaUrl = it.toString()
                            Toast.makeText(this, "Press Send", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to upload media", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        val gallery = findViewById<ImageButton>(R.id.gallery)
        gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            val mimeTypes = arrayOf("image/*", "video/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            resultLauncher.launch(intent)

        }

        val recyclerView = findViewById<RecyclerView>(R.id.chat_recycler_view)
        val messages = mutableListOf<Message>()
        val messagesRef = database.getReference("messages")
        messagesRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (message in snapshot.children) {
                    val messageObj = message.getValue(Message::class.java)
                    if (messageObj != null) {
                        if (messageObj.sender == mAuth.currentUser?.uid && messageObj.receiver == receiverId) {
                            messages.add(messageObj)
                        }
                        if (messageObj.sender == receiverId && messageObj.receiver == mAuth.currentUser?.uid) {
                            messages.add(messageObj)
                        }
                    }
                }
                recyclerView.adapter = MessageAdapter(this@MessageActivity, messages)
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.scrollToPosition(messages.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        var isRecording = false

        mic.setOnClickListener {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_RECORD_AUDIO_PERMISSION
            )
            fileName = "${getExternalFilesDir(null)?.absolutePath}/audiorecordtest.3gp"
            if (isRecording) {
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
                stopRecording()
                val file = File(fileName!!)
                if (file.exists()) {
                    //playRecordedAudio(fileName!!)
                    Log.d("Audio", "Audio file saved at $fileName")
                    val storageRef = FirebaseStorage.getInstance();
                    val st =
                        storageRef.reference.child("voiceMessages/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}.3gp")
                    val uploadTask = st.putFile(Uri.fromFile(file))
                    uploadTask.addOnSuccessListener {
                        Toast.makeText(this, "Press Send", Toast.LENGTH_SHORT).show()
                        st.downloadUrl.addOnSuccessListener {
                            audioUrl = it.toString()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to upload audio file", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Audio file not found", Toast.LENGTH_SHORT).show()
                }

                // Optionally, upload the audio file to your server or Firebase Storage here
            } else {
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
                startRecording(this@MessageActivity)
            }
            isRecording = !isRecording

        }

        val resultLauncherCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    //val uri = data?.data
                    val mimeType = contentResolver.getType(photoUri!!) // Get the MIME type
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    // Save the uri to firebase storage
                    val storageRef = FirebaseStorage.getInstance();
                    val st =
                        storageRef.reference.child("chatMedia/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}.$extension")
                    val uploadTask = st.putFile(photoUri!!)
                    uploadTask.addOnSuccessListener {
                        Toast.makeText(this, "Media uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                        st.downloadUrl.addOnSuccessListener {
                            mediaUrl = it.toString()
                            Toast.makeText(this, "Press Send", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to upload media", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        findViewById<ImageButton>(R.id.camera).setOnClickListener {
            /*  val intent = Intent(this, Activity12::class.java)
              if(mentorName != null) {
                  intent.putExtra("mentorName", mentorName)
                  intent.putExtra("mentorImage", mentorImage)
              }
              else {
                  intent.putExtra("userName", userName)
                  intent.putExtra("userImage", userImage)
              }
              //startActivity(intent);
              if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                  ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                  ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_CAMERA)
              } else {*/

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_CODE_CAMERA
            )
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.vaneezaahmad.sheworks.fileprovider",
                        it
                    )
                    photoUri = FileProvider.getUriForFile(
                        this,
                        "com.vaneezaahmad.sheworks.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncherCamera.launch(takePictureIntent)
                }
            }

            //}
        }


        val sendButton = findViewById<ImageButton>(R.id.send)
        val messageBox = findViewById<TextInputEditText>(R.id.message_box)
        sendButton.setOnClickListener {
            val messageText = messageBox.text.toString()
            //if (messageText.isNotEmpty()) {

            var message: Message;
            val sender = mAuth.currentUser?.uid.toString();
            val receiver = receiverId.toString();
            val timestamp = System.currentTimeMillis()
            val read = false


            val messageRef = database.getReference("messages").push()
            val messageKey = messageRef.key

            if (messageText.isNotEmpty() && audioUrl.isEmpty()) {
                val type = "text"
                message = Message(
                    messageText,
                    sender,
                    receiver,
                    timestamp,
                    read,
                    receiverimage ?: "",
                    senderImage ?: "",
                    key = messageKey,
                    audioUrl = "",
                    mediaUrl = "",
                    type = type
                )
            } else if (mediaUrl.isNotEmpty()) {
                val type = "media"
                message = Message(
                    "Media Message",
                    sender,
                    receiver,
                    timestamp,
                    read,
                    receiverimage ?: "",
                    senderImage ?: "",
                    key = messageKey,
                    audioUrl = "",
                    mediaUrl = mediaUrl,
                    type = type
                )
            } else {
                val type = "audio"
                message = Message(
                    "Voice Message",
                    sender,
                    receiver,
                    timestamp,
                    read,
                    receiverimage ?: "",
                    senderImage ?: "",
                    key = messageKey,
                    audioUrl = audioUrl,
                    mediaUrl = "",
                    type = type
                )
            }
            messageRef.setValue(message).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    messageBox.text?.clear()
                } else {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }
            //}
        }
    }
    fun getSenderImage()
    {
        val url = getString(R.string.IP) + "getUserdp.php"
        //use volley to make post request
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.POST, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("status") == 1) {
                        senderImage = obj.getString("profileImage")
                    } else {
                        Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = mAuth.currentUser?.uid.toString()
                return params
            }
        }

        queue.add(stringRequest)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("RestrictedApi")
    private fun startRecording(context : Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // If the RECORD_AUDIO permission has not been granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        } else {
            // If the RECORD_AUDIO permission has been granted, initialize the MediaRecorder and start recording
            recorder = MediaRecorder(context).apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setOutputFile(fileName)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    prepare()
                    start()
                } catch (e: Exception) {
                    // Handle the case where another app is using the microphone
                    Toast.makeText(context, "Microphone is currently in use by another app.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}