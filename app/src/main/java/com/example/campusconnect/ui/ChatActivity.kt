package com.example.campusconnect.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnect.R
import com.example.campusconnect.adapter.MessagesAdapter
import com.example.campusconnect.model.SendMessageRequest
import com.example.campusconnect.network.RetrofitClient
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: MessagesAdapter

    private var mentorId: Int = -1
    private var mentorName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mentorId = intent.getIntExtra("mentor_id", -1)
        mentorName = intent.getStringExtra("mentor_name") ?: ""

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)

        adapter = MessagesAdapter()
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter

        sendButton.setOnClickListener { sendMessage() }

        loadMessages()
    }

    private fun loadMessages() {
        lifecycleScope.launch {
            try {
                val service = RetrofitClient.getClient(this@ChatActivity)
                val response = service.getMessages(mentorId)
                adapter.submitList(response.messages)
                chatRecyclerView.scrollToPosition(response.messages.size - 1)
            } catch (e: Exception) {
                Toast.makeText(this@ChatActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessage() {
        val messageText = messageEditText.text.toString().trim()
        if (messageText.isEmpty()) return

        lifecycleScope.launch {
            try {
                val service = RetrofitClient.getClient(this@ChatActivity)
                service.sendMessage(SendMessageRequest(mentorId, messageText))
                messageEditText.text.clear()
                loadMessages()
            } catch (e: Exception) {
                Toast.makeText(this@ChatActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
