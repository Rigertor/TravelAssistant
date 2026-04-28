package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class TripResponseDto(
    @SerializedName("output") val outputContent: List<TripResponseContentDto>
)