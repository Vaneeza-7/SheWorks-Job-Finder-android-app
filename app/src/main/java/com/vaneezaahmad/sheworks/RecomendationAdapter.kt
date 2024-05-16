package com.vaneezaahmad.sheworks

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class RecomendationAdapter (val recomendations: List<Reccomendation>): RecyclerView.Adapter<RecomendationAdapter.RecomendationViewHolder>() {
    class RecomendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: CircleImageView = itemView.findViewById(R.id.profile_image)
        val username: TextView = itemView.findViewById(R.id.profile_name)
        val designation: TextView = itemView.findViewById(R.id.profile_designation)
        val connectButton : Button = itemView.findViewById(R.id.profile_connect_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reccomend_item, parent, false)
        return RecomendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecomendationViewHolder, position: Int) {
        val recomendation = recomendations[position]
        Glide.with(holder.profileImage.context).load(recomendation.profileImage).centerCrop().into(holder.profileImage)
        holder.username.text = recomendation.username
        holder.designation.text = recomendation.profession
        holder.connectButton.setOnClickListener {
            // Connect with the user
            val intent = Intent(holder.connectButton.context, MessageActivity::class.java)
            intent.putExtra("receiveruid", recomendation.userID)
            intent.putExtra("receivername", recomendation.username)
            intent.putExtra("receiverimage", recomendation.profileImage)
            holder.connectButton.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recomendations.size
    }
}