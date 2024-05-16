package com.vaneezaahmad.sheworks
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter (var chats: List<User> ) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.username)
        val profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        val relativeLayout = view.findViewById<View>(R.id.RelativeLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.name.text = chat.username
        Glide.with(holder.profileImage.context).load(chat.profileImage).centerCrop().into(holder.profileImage)

        holder.relativeLayout.setOnClickListener {

            val intent = Intent(holder.relativeLayout.context, MessageActivity::class.java)
            intent.putExtra("receiveruid", chat.uid)
            intent.putExtra("receivername", chat.username)
            intent.putExtra("receiverimage", chat.profileImage)
            holder.relativeLayout.context.startActivity(intent)
        }
    }

    override fun getItemCount() = chats.size

    fun filterList(filteredList: List<User>) {
        chats = filteredList
        notifyDataSetChanged()
    }
}