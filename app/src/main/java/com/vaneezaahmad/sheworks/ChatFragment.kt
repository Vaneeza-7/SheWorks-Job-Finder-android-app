package com.vaneezaahmad.sheworks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton

class ChatFragment : Fragment(R.layout.fragment_chat) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawerLayout)
        val menu = view.findViewById<ImageButton>(R.id.menu_button)
        menu.setOnClickListener {
            if (drawerLayout.isOpen()) {
                drawerLayout.close()
            } else {
                drawerLayout.open()
            }
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.newChat)
        fab.setOnClickListener {
            // Open new chat activity
            val intent = Intent(context, NewChatActivity::class.java)
            startActivity(intent);
        }

val chatList = listOf(
            User(
                "Vaneeza Ahmad", R.drawable.baseline_person_24),
            User(
                "Sara Ali", R.drawable.woman_profile),
            User(
                "Ayesha Khan", R.drawable.facebook),
            User(
                "Ali Ahmed", R.drawable.baseline_person_24),
            User(
                "Zainab Ali", R.drawable.company_default_logo),
            User(
                "Ahmed Khan", R.drawable.microsoft_logo));

        val adapter = ChatAdapter(chatList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.chatsRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

    }

}