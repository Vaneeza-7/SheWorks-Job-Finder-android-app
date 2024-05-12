package com.vaneezaahmad.sheworks

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usernameField = findViewById<EditText>(R.id.username)
        val userEmailField = findViewById<EditText>(R.id.useremail)
        val userContactField = findViewById<EditText>(R.id.usercontact)
        val userCountryField = findViewById<EditText>(R.id.usercountry)
        val userCityField = findViewById<EditText>(R.id.usercity)
        val userUseCaseField = findViewById<EditText>(R.id.userusecase)
        val userPasswordField = findViewById<EditText>(R.id.userpassword)



        val signUp = findViewById<Button>(R.id.signUp)
        signUp.setOnClickListener {
            val username = usernameField.text.toString()
            val userEmail = userEmailField.text.toString()
            val userContact = userContactField.text.toString()
            val userCountry = userCountryField.text.toString()
            val userCity = userCityField.text.toString()
            val userUseCase = userUseCaseField.text.toString()
            val userPassword = userPasswordField.text.toString()
            if (username.isEmpty() || userEmail.isEmpty() || userContact.isEmpty() || userCountry.isEmpty() || userCity.isEmpty() || userUseCase.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else {
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                            val user = mAuth.currentUser
                            saveUsertoDB(user!!.uid, username, userEmail, userContact, userCountry, userCity, userUseCase, userPassword)
                        } else {
                            Toast.makeText(this, "Couldn't register user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }

    fun saveUsertoDB(uid : String, username: String, email: String, contactNumber: String, country: String, city: String, use_case: String, password: String){
//        val user = User(uid, username, email, contactNumber, country, city, use_case, password, profileImage)
        val url =  getString(R.string.IP) + "insert.php"
        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val JsonResponse = JSONObject(response)
                val status = JsonResponse.getInt("status")
                if (status == 1) {
                    startActivity(
                        Intent(this, NavigationActivity::class.java)
                    );
                    Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Couldn't register user", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["uid"] = uid
                map["username"] = username
                map["email"] = email
                map["contactNumber"] = contactNumber
                map["country"] = country
                map["city"] = city
                map["use_case"] = use_case
                map["password"] = password
                return map
            }
        }
        Volley.newRequestQueue(this).add(request)

    }
}