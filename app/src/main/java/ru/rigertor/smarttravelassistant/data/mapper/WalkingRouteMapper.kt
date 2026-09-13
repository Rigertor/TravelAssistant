package ru.rigertor.smarttravelassistant.data.mapper

import ru.rigertor.smarttravelassistant.data.network.dto.WalkingRouteRequestDto
import ru.rigertor.smarttravelassistant.data.network.dto.WalkingRouteResponseDto
import ru.rigertor.smarttravelassistant.domain.entity.Location

fun List<Location>.toWalkingRouteRequest() = WalkingRouteRequestDto(
    coordinates = map { listOf(it.lng, it.lat) }
)

fun WalkingRouteResponseDto.toRoutePoints(): List<Location> {
    val geometry = features?.firstOrNull()?.geometry
    check(geometry?.type == "LineString") { "Walking route is unavailable" }
    val coordinates = checkNotNull(geometry.coordinates)
    check(coordinates.size >= 2) { "Walking route is empty" }
    return coordinates.map { coordinate ->
        check(coordinate.size >= 2) { "Invalid route coordinate" }
        val longitude = coordinate[0]
        val latitude = coordinate[1]
        check(longitude in -180.0..180.0 && latitude in -90.0..90.0) {
            "Invalid route coordinate"
        }
        Location(lat = latitude, lng = longitude)
    }
}
