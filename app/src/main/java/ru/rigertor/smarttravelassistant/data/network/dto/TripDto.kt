package ru.rigertor.smarttravelassistant.data.network.dto

import com.google.gson.annotations.SerializedName

data class TripDto(
    val id: String,
    @SerializedName("destination") val destination: String,
    @SerializedName("dates") val dates: String,
    @SerializedName("date_start_timestamp") val startTimestamp: Long,
    @SerializedName("date_end_timestamp") val endTimestamp: Long,
    @SerializedName("weather_forecast") val weatherForecast: String,
    @SerializedName("total_estimated_budget_per_person") val totalBudgetOnPerson: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("currency_symbol") val currencySymbol: String,
    @SerializedName("base_hotel") val baseHotel: BaseHotelDto,
    @SerializedName("days") val days: List<DailyPlanDto>,
    @SerializedName("general_advice") val advice: String
)
