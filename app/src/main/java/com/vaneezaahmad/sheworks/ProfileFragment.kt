package com.vaneezaahmad.sheworks

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class ProfileFragment : Fragment(R.layout.fragment_profile) {
val mAuth = FirebaseAuth.getInstance()
    var url : String = "";
    var storage = FirebaseStorage.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserData();
        val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawerLayout)
        val menu = view.findViewById<ImageButton>(R.id.menu_button)
        menu.setOnClickListener {
            if (drawerLayout.isOpen) {
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


            }
            // Close the navigation drawer when an item is tapped.
            drawerLayout.closeDrawers()
            true
        }

        val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // Set up the ViewPager with the adapter
        viewPager.adapter = ViewPagerAdapter(requireActivity())

        // Connect the TabLayout and the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Details"
                else -> throw IllegalStateException("Invalid position: $position")
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val icon = tab.icon
                if (icon != null) {
                    DrawableCompat.setTint(
                        icon,
                        ContextCompat.getColor(view.context, R.color.iconSelectedColor)
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val icon = tab.icon
                if (icon != null) {
                    DrawableCompat.setTint(
                        icon,
                        ContextCompat.getColor(view.context, R.color.white)
                    )
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do something when tab reselected, if needed
            }
        })
        val professionTextView = view.findViewById<TextView>(R.id.profession)
        professionTextView.setOnClickListener {
            showDialogToEditMessage(professionTextView)
        }

        val pic = view.findViewById<CircleImageView>(R.id.pic)

        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val storageRef =
                    storage.reference.child("dpImages/${mAuth.currentUser?.uid}/${System.currentTimeMillis()}")
                storageRef.putFile(uri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        url = it.toString()
                        Glide.with(this).load(url).into(pic)
                        saveImageToDB(url)
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        val iconButton = view.findViewById<Button>(R.id.iconButton)
        iconButton.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

    }

    private fun showDialogToEditMessage(professionTextView: TextView) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setTitle("Update Information")

        val input = EditText(requireContext())
        input.setText(professionTextView.text)
        builder.setView(input)

        builder.setPositiveButton("Update", null)
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(Color.WHITE)
            positiveButton.setOnClickListener {
                val newMessage = input.text.toString()
                professionTextView.text = newMessage
                dialog.dismiss()
            }

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(Color.WHITE)
            negativeButton.setOnClickListener {
                dialog.cancel()
            }
        }
        dialog.show()
    }

    fun saveImageToDB(imageurl : String)
    {
        val url = requireContext().getString(R.string.IP) + "uploaddp.php"
        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val JsonResponse = JSONObject(response)
                val status = JsonResponse.getInt("status")
                if (status == 1) {
                    Toast.makeText(requireContext(), "Image saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Couldn't save image", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Couldn't save image", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = mAuth.currentUser!!.uid
                params["imageurl"] = imageurl
                return params
            }
        }
        Volley.newRequestQueue(requireContext()).add(request)
    }

    fun getUserData() {
        // Ensure that the fragment is attached and the context is available
        val context = context ?: return
        val currentUser = mAuth.currentUser ?: return

        val url = context.getString(R.string.IP) + "getUser.php"
        val request = object : StringRequest(
            Method.POST, url,
            StringRequest@{ response ->
                // It is safe to require the context here because we check if the fragment is added
                if (!isAdded) return@StringRequest

                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")
                if (status == 1) {
                    val data = jsonResponse.getJSONObject("data")
                    val imageurl = data.getString("profileImage")
                    val username = data.getString("username")

                    val nameTextView = view?.findViewById<TextView>(R.id.name)
                    nameTextView?.text = username

                    val picImageView = view?.findViewById<CircleImageView>(R.id.pic)
                    picImageView?.let {
                        Glide.with(this).load(imageurl).placeholder(R.drawable.woman_profile).into(it)
                    }
                }
            },
            { error ->
                // Use the context safely checked earlier
                Toast.makeText(context, "Couldn't get user data", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = currentUser.uid
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    override fun onResume() {
        super.onResume()
        getUserData()
    }
}