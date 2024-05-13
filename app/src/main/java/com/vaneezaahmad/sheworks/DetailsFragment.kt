package com.vaneezaahmad.sheworks

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailsFragment : Fragment(R.layout.fragment_details) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
}