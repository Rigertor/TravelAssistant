package ru.rigertor.smarttravelassistant.data.repository

import ru.rigertor.smarttravelassistant.BuildConfig
import ru.rigertor.smarttravelassistant.data.mapper.toRoutePoints
import ru.rigertor.smarttravelassistant.data.mapper.toWalkingRouteRequest
import ru.rigertor.smarttravelassistant.data.network.api.RoutesApiService
import ru.rigertor.smarttravelassistant.domain.entity.Location
import ru.rigertor.smarttravelassistant.domain.repository.RouteRepository
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val apiService: RoutesApiService
) : RouteRepository {
    override suspend fun getWalkingRoute(points: List<Location>): List<List<Location>> {
        if (points.size < 2) return emptyList()
        check(BuildConfig.ORS_API_KEY.isNotBlank()) { "OpenRouteService API key is missing" }
        // OpenRouteService accepts up to 50 waypoints; adjacent sections share an endpoint.
        return points.windowed(size = 50, step = 49, partialWindows = true)
            .filter { it.size > 1 }
            .map { section ->
                apiService.computeRoute(
                    apiKey = BuildConfig.ORS_API_KEY,
                    request = section.toWalkingRouteRequest()
                ).toRoutePoints()
            }
    }
}
