package com.vaneezaahmad.sheworks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter (val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: CircleImageView = itemView.findViewById(R.id.image_profile)
        val username: TextView = itemView.findViewById(R.id.text_username)
        val commentText: TextView = itemView.findViewById(R.id.text_content)
        val timestamp: TextView = itemView.findViewById(R.id.text_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.profileImage.setImageResource(comment.profileImageResId)
        holder.username.text = comment.username
        holder.commentText.text = comment.commentText
        holder.timestamp.text = comment.timestamp
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}