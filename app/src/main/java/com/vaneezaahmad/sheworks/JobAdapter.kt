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
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

class JobAdapter(private var jobList: List<Job>) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitle: TextView = itemView.findViewById(R.id.job_title)
        val companyName: TextView = itemView.findViewById(R.id.company_name)
        val salaryRange: TextView = itemView.findViewById(R.id.salary_range)
        val jobLocation: TextView = itemView.findViewById(R.id.job_location)
        val jobTypeTimings : TextView = itemView.findViewById(R.id.job_type_timings)
        val companyLogo: ImageView = itemView.findViewById(R.id.company_logo)
        val viewDetails : Button = itemView.findViewById(R.id.view_details_button)
        val apply : Button = itemView.findViewById(R.id.apply_button)
        val posted_time : TextView = itemView.findViewById(R.id.posted_time)
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
        holder.jobTypeTimings.text = currentItem.jobType
        holder.posted_time.text = "Posted on " + currentItem.createdAt
        Glide.with(holder.companyLogo.context).load(currentItem.logo).into(holder.companyLogo)
        holder.viewDetails.setOnClickListener {
            //put id extra
            val intent = Intent(holder.viewDetails.context, JobDetailsActivity::class.java);
            intent.putExtra("jobId", currentItem.id);
            startActivity(holder.viewDetails.context, intent, null)

            }

        holder.apply.setOnClickListener {
            //put id extra
            val intent = Intent(holder.apply.context, ApplyJobActivity::class.java);
            intent.putExtra("jobId", currentItem.id);
            startActivity(holder.apply.context, intent, null)
            }
        }

    override fun getItemCount() = jobList.size

    fun filterList(filteredList: List<Job>) {
        jobList = filteredList
        notifyDataSetChanged()
    }

}