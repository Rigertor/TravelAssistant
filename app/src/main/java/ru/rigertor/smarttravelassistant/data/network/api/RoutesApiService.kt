package ru.rigertor.smarttravelassistant.data.network.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.rigertor.smarttravelassistant.data.network.dto.WalkingRouteRequestDto
import ru.rigertor.smarttravelassistant.data.network.dto.WalkingRouteResponseDto

interface RoutesApiService {
    @POST("v2/directions/foot-walking/geojson")
    suspend fun computeRoute(
        @Header("Authorization") apiKey: String,
        @Body request: WalkingRouteRequestDto
    ): WalkingRouteResponseDto
}
