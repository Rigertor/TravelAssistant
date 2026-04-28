package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class DailyPlanDto(
    @SerializedName("day") val dayNumber: Int,
    @SerializedName("date_timestamp") val date: Long,
    @SerializedName("theme") val theme: String,
    @SerializedName("weather_note") val weather: String,
    @SerializedName("places") val places: List<PlaceDto>,
    @SerializedName("daily_route_summary") val dailyRout: DailyRouteDto,
    @SerializedName("daily_tips") val dailyTips: String
)
