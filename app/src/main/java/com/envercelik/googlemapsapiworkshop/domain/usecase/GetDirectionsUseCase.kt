package com.envercelik.googlemapsapiworkshop.domain.usecase

import com.envercelik.googlemapsapiworkshop.common.Resource
import com.envercelik.googlemapsapiworkshop.data.remote.model.DirectionResponse
import com.envercelik.googlemapsapiworkshop.domain.repository.DirectionRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDirectionsUseCase @Inject constructor(
    private val repository: DirectionRepository
) {
    operator fun invoke(origin: String, destination: String, key: String) = flow {
        try {
            val routes = repository.getRoutes(origin, destination, key)
            emit(Resource.Success<DirectionResponse>(routes))
        } catch (e: HttpException) {
            emit(
                Resource.Error<DirectionResponse>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error<DirectionResponse>(
                    "Couldn't reach server. Check your internet connection."
                )
            )
        }
    }
}