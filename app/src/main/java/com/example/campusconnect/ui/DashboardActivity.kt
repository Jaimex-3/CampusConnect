package com.example.campusconnect.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnect.R
import com.example.campusconnect.adapter.EventsAdapter
import com.example.campusconnect.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var scheduleButton: LinearLayout
    private lateinit var mapButton: LinearLayout
    private lateinit var mentorsButton: LinearLayout
    private lateinit var adapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
        scheduleButton = findViewById(R.id.scheduleButton)
        mapButton = findViewById(R.id.mapButton)
        mentorsButton = findViewById(R.id.mentorsButton)

        adapter = EventsAdapter()
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsRecyclerView.adapter = adapter

        fetchUpcomingEvents()

        scheduleButton.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }

        mapButton.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        mentorsButton.setOnClickListener {
            startActivity(Intent(this, MentorsListActivity::class.java))
        }

        val faqButton: ImageButton = findViewById(R.id.faqButton)
        faqButton.setOnClickListener {
            startActivity(Intent(this, ResourcesFaqActivity::class.java))
        }
    }

    private fun fetchUpcomingEvents() {
        lifecycleScope.launch {
            try {
                val service = RetrofitClient.getClient(this@DashboardActivity)
                val response = service.getEvents()

                val now = System.currentTimeMillis()
                val thirtyDaysLater = now + (30L * 24 * 60 * 60 * 1000)
                val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)

                val upcomingEvents = response.events.filter { event ->
                    try {
                        val eventTime = format.parse(event.date)?.time
                        eventTime != null && eventTime in now..thirtyDaysLater
                    } catch (e: Exception) {
                        false
                    }
                }.sortedBy { event ->
                    try {
                        format.parse(event.date)?.time
                    } catch (e: Exception) {
                        Long.MAX_VALUE
                    }
                }

                adapter.submitList(upcomingEvents)

            } catch (e: Exception) {
                Toast.makeText(this@DashboardActivity, "Failed to load events", Toast.LENGTH_SHORT).show()
            }
        }
    }
}