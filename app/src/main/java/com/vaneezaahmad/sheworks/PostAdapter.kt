package com.firstclass.project



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class PostAdapter(private val list: ArrayList<Model>, private val context: Context) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater
            .from(context)
            .inflate(R.layout.activity_show_post, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]
        holder.name.text = currentItem.name
        // For now, let's assume you are using image and video URIs directly in your Model
        // You can load images and videos from URIs using appropriate libraries
        // holder.image.setImageURI(currentItem.imageUri)
        if (currentItem.imageUri != null) {
            holder.image.setVisibility(View.VISIBLE); // Set visibility to visible if videoUri is not null
            holder.image.setImageURI(currentItem.imageUri);
        } else {
            holder.image.setVisibility(View.GONE); // Set visibility to gone if videoUri is null
        }
         //holder.video.setVideoURI(currentItem.videoUri)
        if (currentItem.videoUri != null) {
            holder.video.setVisibility(View.VISIBLE); // Set visibility to visible if videoUri is not null
            holder.video.setVideoURI(currentItem.videoUri);
        } else {
            holder.video.setVisibility(View.GONE); // Set visibility to gone if videoUri is null
        }
        holder.caption.text = currentItem.caption
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        // Example:
        val image: ImageView = itemView.findViewById(R.id.image)
        val video: VideoView = itemView.findViewById(R.id.video)
        val caption: TextView = itemView.findViewById(R.id.caption)
    }
}

