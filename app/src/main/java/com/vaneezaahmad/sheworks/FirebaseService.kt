
package com.vaneezaahmad.sheworks

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.remoteMessage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.UUID

class FirebaseService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val newToken = task.result
            Log.d("My token:", newToken)

            // Log and toast
            //val msg = token.toString()
            //val tokenRef = Firebase.database.getReference("token")
            //tokenRef.setValue(newToken)
            //Log.d(TAG, msg)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        //val tokenRef = Firebase.database.getReference("token")
        //tokenRef.setValue(token)


    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("Message_notification", message.notification?.title.toString())

        val intent = Intent(this, MessageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        createNotificationChannel()
        var builder = NotificationCompat.Builder(this, "channel1")
            .setSmallIcon(R.drawable.she_logo_foreground)
            .setContentTitle(message.notification?.title.toString())
            .setContentText(message.notification?.body.toString())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        builder.build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = UUID.randomUUID().hashCode()
        notificationManager.notify(notificationId, builder.build()) // Use a unique ID for notificationId

        //sendPushNotification("f0M9NLYAQKmKZ33MyAs3Q1:APA91bFJaX9gy6ejbl1T-Bxc1-vOjtyrf-rYnbvtjVP8br5Va65ZVQTSR3kS-lEJ3lR-fWaF53pm5bSM-AzcFK0qfzdKHeHM-hy0FKnzcgq283BOvpI0SVorrUruoAFCqT5vOJHnv5Tp", "Hello", "World", "This is a test notification", mapOf("key" to "value"))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "myNotification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel1", name, importance).apply {
                description = "msg Notifications"
            }
            channel.lightColor = Color.CYAN
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendPushNotification(token: String, title: String, subtitle: String, body: String, data: Map<String, String> = emptyMap()) {
        val url = "https://fcm.googleapis.com/fcm/send"
        val bodyJson = JSONObject()
        bodyJson.put("to", token)
        bodyJson.put("notification",
            JSONObject().also {
                it.put("title", title)
                it.put("subtitle", subtitle)
                it.put("body", body)
                it.put("sound", "social_notification_sound.wav")
            }
        )
        if (data.isNotEmpty()) {
            bodyJson.put("data", JSONObject(data))
        }
        var key="AAAAfl0BoEE:APA91bGdVGsXtlDx9v9j096VP6qToZJvcdsBoGYHcaiMsAlj9v_z90H_Fwc-PyowebXCSQYmMcN1JqlmXS7fffu44Z-gh4Ww5YBrnAA2yj3JuA1jmqOt0DmaoBP6vpDjf1Fjz2k49jX4"
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=$key")
            .post(
                bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            )
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.d("TAG", "onResponse: ${response} ")
                }
                override fun onFailure(call: Call, e: IOException) {Log.d("TAG", "onFailure: ${e.message.toString()}")
                }
            }
        )
    }


}
