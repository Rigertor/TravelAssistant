package ru.rigertor.smarttravelassistant.data.network.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.rigertor.smarttravelassistant.data.network.dto.TripResponseDto

interface TripApiService {

    @POST("responses")
    suspend fun createTrip(
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: AiRequest
    ): TripResponseDto


}