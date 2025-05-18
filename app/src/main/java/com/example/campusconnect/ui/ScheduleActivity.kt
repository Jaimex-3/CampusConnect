package com.example.campusconnect.ui

import android.os.Bundle
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

class ScheduleActivity : AppCompatActivity() {

    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var adapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView)
        adapter = EventsAdapter()

        scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        scheduleRecyclerView.adapter = adapter

        loadSchedule()
    }

    private fun loadSchedule() {
        lifecycleScope.launch {
            try {
                val service = RetrofitClient.getClient(this@ScheduleActivity)
                val response = service.getEvents()

                val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)

                val sortedEvents = response.events.sortedBy { event ->
                    try {
                        format.parse(event.date)?.time
                    } catch (e: Exception) {
                        Long.MAX_VALUE
                    }
                }

                adapter.submitList(sortedEvents)

            } catch (e: Exception) {
                Toast.makeText(this@ScheduleActivity, "Failed to load schedule", Toast.LENGTH_SHORT).show()
            }
        }
    }
}