package com.vaneezaahmad.sheworks

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aboutMeTextView = view.findViewById<TextView>(R.id.aboutmeText)
        val aboutMeImageView = view.findViewById<ImageButton>(R.id.editAboutMe)
        aboutMeImageView.setOnClickListener {
            showDialogToEditMessage(aboutMeImageView, aboutMeTextView)
        }
        //val skillsChipGroup = view.findViewById<ChipGroup>(R.id.skillsText)
        //val skillsTextView = view.findViewById<TextView>(R.id.skillsText)
        val skillsImageView = view.findViewById<ImageButton>(R.id.editSkills)
//        skillsImageView.setOnClickListener {
//            showDialogToEditMessage(skillsImageView, skillsTextView)
//        }

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

    private fun showDialogToEditSkills(imageView: ImageButton, chipGroup: ChipGroup) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setTitle("Update Skills")

        val newChipGroup = ChipGroup(requireContext())
        newChipGroup.isSingleSelection = false // change this to true if you want single selection

        // Add chips to the newChipGroup
        val skills = arrayOf("Java", "Kotlin", "Python", "C++", "HTML", "CSS", "JavaScript")
        for (skill in skills) {
            val chip = Chip(requireContext())
            chip.id = View.generateViewId() // Generate a unique ID for the chip
            chip.text = skill
            chip.isCheckable = true
            newChipGroup.addView(chip)


            val originalChip = chipGroup.children.find { it is Chip && it.text == skill } as? Chip
            chip.isChecked = originalChip?.isChecked ?: false


            chip.setOnCloseIconClickListener {
                newChipGroup.removeView(chip)
            }
        }


        val newChipInput = EditText(requireContext())
        val addNewChipButton = Button(requireContext())
        addNewChipButton.text = "Add"
        addNewChipButton.setOnClickListener {
            val newChipText = newChipInput.text.toString()
            if (newChipText.isNotEmpty()) {
                val newChip = Chip(requireContext())
                newChip.text = newChipText
                newChip.isCheckable = true
                newChip.isCloseIconVisible = true
                newChip.setOnCloseIconClickListener {
                    newChipGroup.removeView(newChip)
                }
                newChipGroup.addView(newChip)
            }
        }


        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(newChipGroup)
        layout.addView(newChipInput)
        layout.addView(addNewChipButton)

        builder.setView(layout)

        builder.setPositiveButton("Update", null)
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(Color.WHITE)
            positiveButton.setOnClickListener {
                for (i in 0 until newChipGroup.childCount) {
                    val chip = newChipGroup.getChildAt(i) as Chip
                    val originalChip = chipGroup.children.find { it is Chip && it.text == chip.text } as? Chip
                    if (originalChip != null) {
                        originalChip.isChecked = chip.isChecked
                    } else {

                        val newOriginalChip = Chip(requireContext())
                        newOriginalChip.text = chip.text
                        newOriginalChip.isCheckable = true
                        newOriginalChip.isChecked = chip.isChecked
                        chipGroup.addView(newOriginalChip)
                    }
                }
                val newChipTexts = newChipGroup.children.map { (it as Chip).text }.toList()
                chipGroup.children.filter { it is Chip && it.text !in newChipTexts }.forEach {
                    chipGroup.removeView(it)
                }
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