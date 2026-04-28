package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class PlaceDto(
    @SerializedName("time") val time: String,
    @SerializedName("name") val name: String,
    @SerializedName("name_local") val nameLocal: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("address") val address: String,
    @SerializedName("description") val description: String,
    @SerializedName("duration_hours") val durationHours: Float,
    @SerializedName("estimated_cost") val estimatedCost: Int,
    @SerializedName("cost_note") val costNote: String,
    @SerializedName("place_type") val placeType: String,
    @SerializedName("image_query") val imageQuery: String
)
