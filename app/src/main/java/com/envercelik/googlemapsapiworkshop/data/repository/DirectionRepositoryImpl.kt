package com.envercelik.googlemapsapiworkshop.data.repository

import com.envercelik.googlemapsapiworkshop.data.remote.GoogleDirectionService
import com.envercelik.googlemapsapiworkshop.data.remote.model.DirectionResponse
import com.envercelik.googlemapsapiworkshop.domain.repository.DirectionRepository
import javax.inject.Inject

class DirectionRepositoryImpl @Inject constructor(
    private val service: GoogleDirectionService
) : DirectionRepository {

    override suspend fun getRoutes(origin: String, destination: String, key: String):
            DirectionResponse {
        return service.getDirections(origin, destination, key)
    }
}