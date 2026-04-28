package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class DailyRouteDto(
    @SerializedName("total_walking_distance_km") val totalDistance: Float,
    @SerializedName("transport_used") val transport: String,
    @SerializedName("transport_cost_estimate") val transportCost: Int
)
