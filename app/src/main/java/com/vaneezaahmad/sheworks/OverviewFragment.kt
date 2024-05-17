package com.vaneezaahmad.sheworks

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class OverviewFragment : Fragment(R.layout.fragment_overview) {
 val mAuth = FirebaseAuth.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCareerInfo();
        val aboutMeTextView = view.findViewById<TextView>(R.id.aboutmeText)
        val aboutMeImageView = view.findViewById<ImageButton>(R.id.editAboutMe)
        aboutMeImageView.setOnClickListener {
            showDialogToEditMessage(aboutMeImageView, aboutMeTextView)
        }
        val skillsChipGroup = view.findViewById<ChipGroup>(R.id.skillsText)

        val skillsImageView = view.findViewById<ImageButton>(R.id.editSkills)
       skillsImageView.setOnClickListener {
            showDialogToEditSkills(skillsChipGroup)
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
                updateCareerInfo();
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

    fun showDialogToEditSkills(chipGroup: ChipGroup) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setTitle("Update Skills")
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        chipGroup.children.forEach { view ->
            val chip = view as Chip
            val input = EditText(requireContext())
            input.setText(chip.text)
            layout.addView(input)
        }
        // Add an extra EditText for adding new skills
        val newSkillInput = EditText(requireContext())
        newSkillInput.hint = "Add new skill here..."
        layout.addView(newSkillInput)
        builder.setView(layout)

        builder.setPositiveButton("Update", null)
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(Color.WHITE)
            positiveButton.setOnClickListener {
                chipGroup.removeAllViews()
                for (i in 0 until layout.childCount) {
                    val input = layout.getChildAt(i) as EditText
                    val skill = input.text.toString()
                    // If the EditText is not empty, create a new Chip for it
                    if (skill.isNotBlank()) {
                        val themedContext = ContextThemeWrapper(requireContext(), R.style.Chip_Custom)
                        val chip = Chip(themedContext, null, 0)
                        chip.text = skill
                        chip.isCheckable = true
                        chipGroup.addView(chip)
                    }
                }
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
                    val aboutme = data.optString("about_me", "Add a brief description about yourself...")
                    val skills = data.optString("skills", "Add your skills here...")
                    val aboutmeTextView = view?.findViewById<TextView>(R.id.aboutmeText)
                    val skillsChipGroup = view?.findViewById<ChipGroup>(R.id.skillsText)
                    aboutmeTextView?.text = aboutme
                    skillsChipGroup?.removeAllViews()
                    skills.split(",").forEach { skill ->
                        if (skill.isNotBlank()) {
                            val themedContext = ContextThemeWrapper(requireContext(), R.style.Chip_Custom)
                            val chip = Chip(themedContext, null, 0)
                            chip.text = skill
                            chip.isCheckable = true
                            skillsChipGroup?.addView(chip)
                        }
                    }

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
        val url = requireContext().getString(R.string.IP) + "updateCareerOverview.php"
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
                val aboutme = view?.findViewById<TextView>(R.id.aboutmeText)?.text.toString()
                val skills = view?.findViewById<ChipGroup>(R.id.skillsText)?.children?.joinToString(",") { (it as Chip).text }
                params["about_me"] = aboutme
                params["skills"] = skills.toString()
                return params
            }
        }
        Volley.newRequestQueue(requireContext()).add(request)
    }
}