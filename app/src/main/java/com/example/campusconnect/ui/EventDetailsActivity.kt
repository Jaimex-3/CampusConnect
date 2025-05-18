package com.example.campusconnect.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnect.R
import com.example.campusconnect.model.Event
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var dateTimeTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var addToCalendarButton: Button
    private lateinit var imageView: ImageView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        titleTextView = findViewById(R.id.eventTitleTextView)
        dateTimeTextView = findViewById(R.id.eventDateTimeTextView)
        locationTextView = findViewById(R.id.eventLocationTextView)
        descriptionTextView = findViewById(R.id.eventDescriptionTextView)
        addToCalendarButton = findViewById(R.id.addToCalendarButton)
        imageView = findViewById(R.id.eventImageView)

        val event = intent.getSerializableExtra("event", Event::class.java)
        if (event != null) {
            bindEvent(event)
            addToCalendarButton.setOnClickListener { addToCalendar(event) }
        }
    }

    private fun bindEvent(event: Event) {
        titleTextView.text = event.title
        dateTimeTextView.text = event.date
        locationTextView.text = event.location
        descriptionTextView.text = event.description ?: "⚠ No description provided."

        val resId = when (event.image_res) {
            "cinema_event" -> R.drawable.cinema_event
            "sports_event" -> R.drawable.sports_event
            "art_event" -> R.drawable.art_event
            "theater_event" -> R.drawable.theater_event
            else -> R.drawable.art_event
        }
        imageView.setImageResource(resId)
    }

    private fun addToCalendar(event: Event) {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
        val date = try {
            formatter.parse(event.date)
        } catch (e: Exception) {
            null
        }

        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, event.title)
            putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
            putExtra(CalendarContract.Events.DESCRIPTION, event.description)
            date?.let {
                val startMillis = it.time
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + (60 * 60 * 1000))
            }
        }

        if (calendarIntent.resolveActivity(packageManager) != null) {
            startActivity(calendarIntent)
        } else {
            Toast.makeText(this, "No calendar app available on device", Toast.LENGTH_SHORT).show()
        }
    }
}