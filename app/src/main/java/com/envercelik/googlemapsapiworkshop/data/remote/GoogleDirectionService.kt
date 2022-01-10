package com.envercelik.googlemapsapiworkshop.data.remote

import com.envercelik.googlemapsapiworkshop.data.remote.model.DirectionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionService {
    @GET("api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): DirectionResponse
}