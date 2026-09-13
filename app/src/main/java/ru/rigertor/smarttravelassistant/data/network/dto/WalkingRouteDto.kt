package ru.rigertor.smarttravelassistant.data.network.dto

data class WalkingRouteRequestDto(
    val coordinates: List<List<Double>>,
    val instructions: Boolean = false
)

data class WalkingRouteResponseDto(val features: List<WalkingRouteFeatureDto>?)
data class WalkingRouteFeatureDto(val geometry: WalkingRouteGeometryDto?)
data class WalkingRouteGeometryDto(val type: String?, val coordinates: List<List<Double>>?)
