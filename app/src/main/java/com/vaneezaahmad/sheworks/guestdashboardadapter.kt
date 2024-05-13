package com.vaneezaahmad.sheworks

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class guestdashboardadapter(private val jobList: List<Job>) : RecyclerView.Adapter<guestdashboardadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = jobList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.jobTitle.text = currentItem.title
        holder.companyName.text = currentItem.company
        holder.salaryRange.text = currentItem.salary
        holder.jobLocation.text = currentItem.location
        holder.jobTypeTimings.text = "${currentItem.jobType} | ${currentItem.timings}"
        holder.companyLogo.setImageResource(currentItem.logo)
        holder.viewDetails.setOnClickListener {
            Toast.makeText(
                holder.apply.context,
                "To See Detail Please login first",
                Toast.LENGTH_SHORT
            ).show()
            Intent(holder.apply.context, LoginActivity::class.java).also {
                startActivity(holder.apply.context, it, null)
            }
        }
        holder.apply.setOnClickListener {
            Toast.makeText(
                holder.viewDetails.context,
                "To Apply Please login first",
                Toast.LENGTH_SHORT
            ).show()
            Intent(holder.viewDetails.context, LoginActivity::class.java).also {
                startActivity(holder.viewDetails.context, it, null)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitle: TextView = itemView.findViewById(R.id.job_title)
        val companyName: TextView = itemView.findViewById(R.id.company_name)
        val salaryRange: TextView = itemView.findViewById(R.id.salary_range)
        val jobLocation: TextView = itemView.findViewById(R.id.job_location)
        val jobTypeTimings: TextView = itemView.findViewById(R.id.job_type_timings)
        val companyLogo: ImageView = itemView.findViewById(R.id.company_logo)
        val viewDetails: Button = itemView.findViewById(R.id.view_details_button)
        val apply: Button = itemView.findViewById(R.id.apply_button)
    }
}
