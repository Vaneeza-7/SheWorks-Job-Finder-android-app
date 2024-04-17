package com.vaneezaahmad.sheworks

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile_image: CircleImageView = itemView.findViewById(R.id.image_profile)
        val username: TextView = itemView.findViewById(R.id.text_username)
        val timeAgo: TextView = itemView.findViewById(R.id.text_time)
        val postImage: ImageView = itemView.findViewById(R.id.image_post)
        val postContent: TextView = itemView.findViewById(R.id.text_post_content)
        val textLikes: TextView = itemView.findViewById(R.id.text_likes_count)
        val textComments: TextView = itemView.findViewById(R.id.text_comments_count)
        val commentButton = itemView.findViewById<ImageView>(R.id.button_comment)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val post = posts[position]
        Glide.with(holder.itemView.context).load(post.profileImageResId).into(holder.profile_image)
        Glide.with(holder.itemView.context).load(post.postImageResId).into(holder.postImage)
        holder.username.text = post.username
        holder.timeAgo.text = post.timeAgo
        holder.postContent.text = post.postContent
        holder.textLikes.text = "${post.likesCount} likes"
        holder.textComments.text = "${post.commentsCount} comments"
        holder.commentButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, CommentActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = posts.size
}
