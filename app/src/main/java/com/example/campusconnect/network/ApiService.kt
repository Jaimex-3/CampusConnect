package com.example.campusconnect.network

import retrofit2.http.*
import com.example.campusconnect.model.*

interface ApiService {

    @POST("register")
    suspend fun register(@Body req: RegisterRequest): SimpleResponse

    @POST("login")
    suspend fun login(@Body req: LoginRequest): LoginResponse

    @GET("events")
    suspend fun getEvents(): EventsResponse

    @GET("mentors")
    suspend fun getMentors(): MentorsResponse

    @GET("messages")
    suspend fun getMessages(@Query("mentor_id") mentorId: Int): MessagesResponse

    @POST("messages")
    suspend fun sendMessage(@Body req: SendMessageRequest): SimpleResponse

    @GET("faqs")
    suspend fun getFaqs(): FaqsResponse

    @GET("resources")
    suspend fun getResources(): ResourcesResponse

    @GET("locations")
    suspend fun getLocations(): LocationsResponse

    @GET("map_paths")
    suspend fun getMapPaths(): MapPathsResponse
}
