package com.vaneezaahmad.sheworks

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddEventActivity : AppCompatActivity() {
    private lateinit var pickDate: EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        pickDate = findViewById<EditText>(R.id.datePicker)
        pickDate.setOnClickListener {

            showDatePickerDialog()
        }

        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // save event
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val date = pickDate.text.toString()
            // save to database
            saveEvent(title, description, date)


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

    fun saveEvent(title: String, description: String, date: String)
    {
        val url = getString(R.string.IP) + "addEvent.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getInt("status") == 1) {
                        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, "Failed to add event: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["title"] = title
                params["description"] = description
                params["date"] = date
                return params
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)

    }

}