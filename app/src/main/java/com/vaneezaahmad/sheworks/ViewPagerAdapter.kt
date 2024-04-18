package com.vaneezaahmad.sheworks

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2 // number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment() // Fragment for first tab
            1 -> DetailsFragment() // Fragment for second tab
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}