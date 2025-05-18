package com.example.campusconnect.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnect.R
import com.example.campusconnect.adapter.MentorsAdapter
import com.example.campusconnect.model.Mentor
import com.example.campusconnect.network.RetrofitClient
import kotlinx.coroutines.launch

class MentorsListActivity : AppCompatActivity() {

    private lateinit var mentorsRecyclerView: RecyclerView
    private lateinit var adapter: MentorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentors_list)

        mentorsRecyclerView = findViewById(R.id.mentorsRecyclerView)
        adapter = MentorsAdapter { mentor -> onMentorClick(mentor) }

        mentorsRecyclerView.layoutManager = LinearLayoutManager(this)
        mentorsRecyclerView.adapter = adapter

        loadMentors()
    }

    private fun loadMentors() {
        lifecycleScope.launch {
            try {
                val service = RetrofitClient.getClient(this@MentorsListActivity)
                val response = service.getMentors()
                adapter.submitList(response.mentors)
            } catch (e: Exception) {
                Toast.makeText(this@MentorsListActivity, "Failed to load mentors", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onMentorClick(mentor: Mentor) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("mentor_id", mentor.id)
        intent.putExtra("mentor_name", mentor.name)
        startActivity(intent)
    }
}
