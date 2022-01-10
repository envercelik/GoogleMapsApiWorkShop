package com.envercelik.googlemapsapiworkshop.domain.repository

import com.envercelik.googlemapsapiworkshop.data.remote.model.DirectionResponse

interface DirectionRepository {
    suspend fun getRoutes(origin: String, destination: String, key: String): DirectionResponse
}