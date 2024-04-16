package com.vaneezaahmad.sheworks

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddEventActivity : AppCompatActivity() {
    private lateinit var pickDate: EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        pickDate = findViewById<EditText>(R.id.datePicker)
        pickDate.setOnClickListener {

            showDatePickerDialog()
        }

        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }
    }
    private fun showDatePickerDialog() {
        // Define the listener to handle the date selection
        val listener = object : OnSelectDateListener {

            override fun onSelect(calendar: List<Calendar>) {
                val selectedDate = calendar[0]
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                pickDate.setText(sdf.format(selectedDate.time))
            }
        }

        // Initialize DatePickerBuilder
        val builder = DatePickerBuilder(this, listener)
            .pickerType(CalendarView.MANY_DAYS_PICKER)
            .headerColor(R.color.iconSelectedColor) // Color of the dialog header
            .selectionColor(R.color.colorPrimary) // Color of the selection circle
            .selectionLabelColor(R.color.white) // Color of the label in the selection circle
            .daysLabelsColor(R.color.colorAccent) // Color of days numbers
            .todayColor(R.color.black) // Color of the present day background
            .todayLabelColor(R.color.black) // Color of the today number
            .abbreviationsBarColor(R.color.colorAccent) // Color of bar with day symbols
            .abbreviationsLabelsColor(R.color.white)
            .pagesColor(R.color.white) // Color of the calendar background
            .firstDayOfWeek(CalendarWeekDay.MONDAY) // Set first day of the week

        // Build and show the DatePicker
        val datePicker = builder.build()
        datePicker.show()
    }

}