package com.example.campusconnect.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnect.R
import com.example.campusconnect.model.Event
import com.example.campusconnect.ui.EventDetailsActivity

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private val events = mutableListOf<Event>()

    fun submitList(list: List<Event>) {
        events.clear()
        events.addAll(list)
        notifyDataSetChanged()
    }

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView = view.findViewById(R.id.eventTitle)
        private val dateText: TextView = view.findViewById(R.id.eventDateTime)

        fun bind(event: Event) {
            titleText.text = event.title
            dateText.text = event.date

            android.util.Log.d("EventsAdapter", "Description: ${event.description}")

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EventDetailsActivity::class.java)
                intent.putExtra("event", event)
                context.startActivity(intent)
            }
            Log.d("EventDetails", "DESCRIPTION = ${event.description}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}
