package com.vaneezaahmad.sheworks

import android.R.layout
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.bumptech.glide.Glide
import com.applandeo.materialcalendarview.EventDay
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private var events: MutableList<Event> = mutableListOf()
    var adapter = EventAdapter(events)
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

        //getEvents();
        val recyclerView = view.findViewById<RecyclerView>(R.id.eventsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
//        val eventDays = events.map { event ->
//            val calendar = event.date
//            val eventDay = CalendarDay(calendar)
//            eventDay.imageResource = R.drawable.event_icon
//            eventDay
//        }
//        calendarView.setCalendarDays(eventDays)

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

    fun getEvents(){
        events.clear();
        val url = requireContext().getString(R.string.IP) + "getEvents.php"
        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getInt("status")
                    if (status == 1) {
                        val eventsJson = jsonResponse.getJSONArray("events")
                        for (i in 0 until eventsJson.length()) {
                            val eventJson = eventsJson.getJSONObject(i)
                            val title = eventJson.getString("title")
                            val description = eventJson.getString("description")
                            val date = eventJson.getString("date")
                            val calendar = Calendar.getInstance()
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
                            calendar.time = sdf.parse(date)!!
                            events.add(Event(title, description, calendar))
                        }
                        adapter.updateEvents(events)
                        adapter.notifyDataSetChanged()

                        // Set the icons after the events have been retrieved
                        val calendarView = view?.findViewById<CalendarView>(R.id.calendarView)
                        val eventDays = events.map { event ->
                            val calendar = event.date
                            val eventDay = CalendarDay(calendar)
                            eventDay.imageResource = R.drawable.event_icon
                            eventDay
                        }
                        calendarView?.setCalendarDays(eventDays)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            {
                Toast.makeText(requireContext(), "Couldn't get events", Toast.LENGTH_SHORT).show()
            })
        {}
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }

    override fun onResume() {
        super.onResume()
        events.clear();
        getEvents()
    }
}