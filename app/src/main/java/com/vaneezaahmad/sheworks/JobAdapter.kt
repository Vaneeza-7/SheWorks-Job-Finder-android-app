package com.vaneezaahmad.sheworks

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(private val jobList: List<Job>) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitle: TextView = itemView.findViewById(R.id.job_title)
        val companyName: TextView = itemView.findViewById(R.id.company_name)
        val salaryRange: TextView = itemView.findViewById(R.id.salary_range)
        val jobLocation: TextView = itemView.findViewById(R.id.job_location)
        val jobTypeTimings : TextView = itemView.findViewById(R.id.job_type_timings)
        val companyLogo: ImageView = itemView.findViewById(R.id.company_logo)
        val viewDetais : Button = itemView.findViewById(R.id.view_details_button)
        val apply : Button = itemView.findViewById(R.id.apply_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.jobTitle.text = currentItem.title
        holder.companyName.text = currentItem.company
        holder.salaryRange.text = currentItem.salary
        holder.jobLocation.text = currentItem.location
        holder.jobTypeTimings.text = currentItem.jobType + " | " + currentItem.timings
        holder.companyLogo.setImageResource(currentItem.logo)
        holder.viewDetais.setOnClickListener {

            Intent(holder.viewDetais.context, JobDetailsActivity::class.java).also {
                startActivity(holder.viewDetais.context, it, null)
            }
        }
        holder.apply.setOnClickListener {
            Intent(holder.apply.context, ApplyJobActivity::class.java).also {
                startActivity(holder.apply.context, it, null)
            }
        }
    }

    override fun getItemCount() = jobList.size
}