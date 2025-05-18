package com.example.campusconnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnect.R
import com.example.campusconnect.model.Mentor

class MentorsAdapter(
    private val onClick: (Mentor) -> Unit
) : RecyclerView.Adapter<MentorsAdapter.MentorViewHolder>() {

    private val mentors = mutableListOf<Mentor>()

    fun submitList(list: List<Mentor>) {
        mentors.clear()
        mentors.addAll(list)
        notifyDataSetChanged()
    }

    inner class MentorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.mentorNameTextView)
        private val department: TextView = view.findViewById(R.id.mentorDepartmentTextView)

        fun bind(mentor: Mentor) {
            name.text = mentor.name
            department.text = mentor.department
            itemView.setOnClickListener { onClick(mentor) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mentor, parent, false)
        return MentorViewHolder(view)
    }

    override fun getItemCount(): Int = mentors.size

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        holder.bind(mentors[position])
    }
}
