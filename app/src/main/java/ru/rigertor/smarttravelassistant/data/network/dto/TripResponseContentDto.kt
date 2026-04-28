package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class TripResponseContentDto(
    @SerializedName("id") val id: String,
    @SerializedName("content") val content: List<TripContentTextDto>
)
