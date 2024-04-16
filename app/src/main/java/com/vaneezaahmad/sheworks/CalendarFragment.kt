package com.vaneezaahmad.sheworks

import android.R.layout
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.bumptech.glide.Glide
import com.applandeo.materialcalendarview.EventDay
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import java.util.Calendar


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private var events: List<Event> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
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

        val fab = view.findViewById<FloatingActionButton>(R.id.addEvent)
        fab.setOnClickListener {
            // Navigate to the AddEventActivity
            val intent = Intent(requireContext(), AddEventActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.eventsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = EventAdapter(events)

        val calendar1 = Calendar.getInstance()
        calendar1[Calendar.YEAR] = 2024
        calendar1[Calendar.MONTH] = Calendar.APRIL // Note: Calendar month is zero-based.
        calendar1[Calendar.DAY_OF_MONTH] = 20

        val calendar2 = Calendar.getInstance()
        calendar2[Calendar.YEAR] = 2024
        calendar2[Calendar.MONTH] = Calendar.APRIL // Note: Calendar month is zero-based.
        calendar2[Calendar.DAY_OF_MONTH] = 21

        val calendar3 = Calendar.getInstance()
        calendar3[Calendar.YEAR] = 2024
        calendar3[Calendar.MONTH] = Calendar.APRIL // Note: Calendar month is zero-based.
        calendar3[Calendar.DAY_OF_MONTH] = 22

        events = listOf(
            Event("Meeting", "Discuss project details", calendar1),
            Event("Webinar", "Big Data Webinar link: http://bigdata.org",  calendar2),
            Event("Presentation", "Present project to the client", calendar3)
        )

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val eventDays = events.map { event ->
            val calendar = event.date
            val eventDay = CalendarDay(calendar)
            eventDay.imageResource = R.drawable.event_icon
            eventDay
        }
        calendarView.setCalendarDays(eventDays)

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                var selectedEvents = events.filter { event ->
                    val eventCalendar = event.date
                    val eventDay = CalendarDay(eventCalendar)
                    eventDay == calendarDay
                }

                if (selectedEvents.isEmpty()) {
                    selectedEvents = listOf(Event("No events", "", calendarDay.calendar))
                }

                // Update the events in the adapter and notify the adapter that the data set has changed
                (recyclerView.adapter as EventAdapter).apply {
                    updateEvents(selectedEvents)
                    notifyDataSetChanged()
                }
            }
        })



/*        //adding event on calendar
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = 2024
        calendar[Calendar.MONTH] = Calendar.APRIL // Note: Calendar month is zero-based.
        calendar[Calendar.DAY_OF_MONTH] = 20

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.company_default_logo)
        val eventDay = CalendarDay(calendar)
        eventDay.imageResource = R.drawable.baseline_date_range_24

        val calendarView =  view.findViewById<CalendarView>(R.id.calendarView)
        calendarView.setCalendarDays(listOf(eventDay))*/
    }
}