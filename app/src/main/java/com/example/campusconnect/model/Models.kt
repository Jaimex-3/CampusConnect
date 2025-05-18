package com.example.campusconnect.model

import java.io.Serializable

// Auth
data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val access_token: String)
data class SimpleResponse(val message: String)

// Events
data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val location: String,
    val price: Double,
    val category: String? = null,
    val image_res: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val description: String? = null
) : Serializable

data class EventsResponse(val events: List<Event>)

// Mentors
data class Mentor(val id: Int, val name: String, val email: String, val department: String, val profile: String)
data class MentorsResponse(val mentors: List<Mentor>)

// Messages
data class Message(val id: Int, val mentor_id: Int, val user_id: Int, val message: String, val sent_at: String)
data class MessagesResponse(val messages: List<Message>)
data class SendMessageRequest(val mentor_id: Int, val message: String)

// FAQ / Resources
data class Faq(val id: Int, val question: String, val answer: String)
data class FaqsResponse(val faqs: List<Faq>)

data class ResourceItem(val id: Int, val title: String, val url: String, val description: String)
data class ResourcesResponse(val resources: List<ResourceItem>)

// Map
data class LocationItem(val id: Int, val name: String, val building: String, val floor: String,
                        val room: String, val latitude: Double, val longitude: Double)
data class LocationsResponse(val locations: List<LocationItem>)

data class MapPath(val id: Int, val from_location: Int, val to_location: Int, val path_data: List<Map<String, Double>>)
data class MapPathsResponse(val map_paths: List<MapPath>)