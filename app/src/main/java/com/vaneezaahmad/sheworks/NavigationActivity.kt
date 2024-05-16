package com.vaneezaahmad.sheworks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigation
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigationItem
import com.tenclouds.fluidbottomnavigation.listener.OnTabSelectedListener

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)


        val fluidBottomNavigation = findViewById<FluidBottomNavigation>(R.id.fluidBottomNavigation)
        fluidBottomNavigation.accentColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fluidBottomNavigation.backColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fluidBottomNavigation.textColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fluidBottomNavigation.iconColor = ContextCompat.getColor(this, R.color.colorPrimary)
        fluidBottomNavigation.iconSelectedColor = ContextCompat.getColor(this, R.color.iconSelectedColor)
        fluidBottomNavigation.items =
            listOf(
                FluidBottomNavigationItem(
                    getString(R.string.home),
                    ContextCompat.getDrawable(this, R.drawable.ic_home)),
                FluidBottomNavigationItem(
                    getString(R.string.dashboard),
                    ContextCompat.getDrawable(this, R.drawable.baseline_dashboard_24)),
                FluidBottomNavigationItem(
                    getString(R.string.calendar),
                    ContextCompat.getDrawable(this, R.drawable.ic_calendar)),
                FluidBottomNavigationItem(
                    getString(R.string.chat),
                    ContextCompat.getDrawable(this, R.drawable.ic_chat)),
                FluidBottomNavigationItem(
                    getString(R.string.profile),
                    ContextCompat.getDrawable(this, R.drawable.ic_profile)))


        if (intent.getBooleanExtra("openProfileFragment", false)) {
            setCurrentFragment(ProfileFragment())
            fluidBottomNavigation.selectTab(4)
        } else {
            setCurrentFragment(HomeFragment())
        }
        fluidBottomNavigation.onTabSelectedListener = object : OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                when (position) {
                    0 -> {
                        setCurrentFragment(HomeFragment())
                    }
                    1 -> {
                        setCurrentFragment(JobBoardFragment())
                    }
                    2 -> {
                        setCurrentFragment(CalendarFragment())
                    }
                    3 -> {
                        setCurrentFragment(ChatFragment())
                    }
                    4 -> {
                        setCurrentFragment(ProfileFragment())
                    }
                }
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment)
            commit()
        }
}