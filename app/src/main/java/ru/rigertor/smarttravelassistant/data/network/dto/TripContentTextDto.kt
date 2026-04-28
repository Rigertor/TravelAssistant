package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class TripContentTextDto(
    @SerializedName("text") val contentText: String
)
