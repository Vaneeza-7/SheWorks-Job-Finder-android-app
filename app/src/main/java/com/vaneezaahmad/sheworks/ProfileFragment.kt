package com.vaneezaahmad.sheworks

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton

class ProfileFragment : Fragment(R.layout.fragment_profile)  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
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
        val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // Set up the ViewPager with the adapter
        viewPager.adapter = ViewPagerAdapter(requireActivity())

        // Connect the TabLayout and the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Details"
                else -> throw IllegalStateException("Invalid position: $position")
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val icon = tab.icon
                if (icon != null) {
                    DrawableCompat.setTint(icon, ContextCompat.getColor(view.context, R.color.iconSelectedColor))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val icon = tab.icon
                if (icon != null) {
                    DrawableCompat.setTint(icon, ContextCompat.getColor(view.context, R.color.white))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do something when tab reselected, if needed
            }
        })



    }

}