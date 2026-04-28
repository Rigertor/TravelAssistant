package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class BaseHotelDto(
    @SerializedName("name") val name: String,
    @SerializedName("name_local") val nameLocal: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("address") val address: String,
    @SerializedName("description") val description: String,
    @SerializedName("estimated_price_per_night") val estimatedPricePerNight: Int,
    @SerializedName("price_note") val priceNote: String,
    @SerializedName("image_query") val imageQuery: String
)