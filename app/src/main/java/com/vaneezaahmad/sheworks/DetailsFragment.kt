package com.vaneezaahmad.sheworks

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
class DetailsFragment : Fragment(R.layout.fragment_details) {
    val mAuth = FirebaseAuth.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCareerInfo();
        val projectTextView = view.findViewById<TextView>(R.id.projectsText)
        val projectImageView = view.findViewById<ImageButton>(R.id.editProjects)
        projectImageView.setOnClickListener {
            showDialogToEditMessage(projectImageView, projectTextView)
        }
        val interestsTextView = view.findViewById<TextView>(R.id.interestsText)
        val interestsImageView = view.findViewById<ImageButton>(R.id.editInterests)
        interestsImageView.setOnClickListener {
            showDialogToEditMessage(interestsImageView, interestsTextView)
        }
        val experienceTextView = view.findViewById<TextView>(R.id.experienceText)
        val experienceImageView = view.findViewById<ImageButton>(R.id.editExperience)
        experienceImageView.setOnClickListener {
            showDialogToEditMessage(experienceImageView, experienceTextView)
        }
        val aboutMeTextView = view.findViewById<TextView>(R.id.educationText)
        val aboutMeImageView = view.findViewById<ImageButton>(R.id.editEducation)
        aboutMeImageView.setOnClickListener {
            showDialogToEditMessage(aboutMeImageView, aboutMeTextView)
        }

    }

    private fun showDialogToEditMessage(imageView: ImageButton, textView: TextView) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setTitle("Update Information")

        val input = EditText(requireContext())
        input.setText(textView.text)
        builder.setView(input)

        builder.setPositiveButton("Update", null)
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(Color.WHITE)
            positiveButton.setOnClickListener {
                val newMessage = input.text.toString()
                textView.text = newMessage
                updateCareerInfo()
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

    fun getCareerInfo()
    {
        val url = requireContext().getString(R.string.IP) + "getCareerInfo.php"
        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val JsonResponse = JSONObject(response)
                val status = JsonResponse.getInt("status")
                if (status == 1) {
                    val data = JsonResponse.getJSONObject("data")
                    val experience = data.optString("experience", "Add your experience here")
                    val education = data.optString("education", "Add your education here")
                    val interests = data.optString("interests", "Add your interests here")
                    val projects = data.optString("projects", "Add your projects here")
                    val experienceTextView = view?.findViewById<TextView>(R.id.experienceText)
                    val educationTextView = view?.findViewById<TextView>(R.id.educationText)
                    val interestsTextView = view?.findViewById<TextView>(R.id.interestsText)
                    val projectsTextView = view?.findViewById<TextView>(R.id.projectsText)
                    experienceTextView?.text = experience
                    educationTextView?.text = education
                    interestsTextView?.text = interests
                    projectsTextView?.text = projects

                } else {
                    Toast.makeText(requireContext(), "Couldn't get career info", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Couldn't get career info", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = mAuth.currentUser!!.uid
                return params
            }
        }
        Volley.newRequestQueue(requireContext()).add(request)
    }

    fun updateCareerInfo()
    {
        val url = requireContext().getString(R.string.IP) + "updateCareerDetails.php"
        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val JsonResponse = JSONObject(response)
                val status = JsonResponse.getInt("status")
                if (status == 1) {
                    Toast.makeText(requireContext(), "Career info updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Couldn't update career info", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Couldn't update career info", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = mAuth.currentUser!!.uid
                val experience = view?.findViewById<TextView>(R.id.experienceText)?.text.toString()
                val education = view?.findViewById<TextView>(R.id.educationText)?.text.toString()
                val interests = view?.findViewById<TextView>(R.id.interestsText)?.text.toString()
                val projects = view?.findViewById<TextView>(R.id.projectsText)?.text.toString()
                params["experience"] = experience
                params["education"] = education
                params["interests"] = interests
                params["projects"] = projects
                return params
            }
        }
        Volley.newRequestQueue(requireContext()).add(request)
    }
}