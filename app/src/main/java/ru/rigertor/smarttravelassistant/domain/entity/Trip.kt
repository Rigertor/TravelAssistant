package ru.rigertor.smarttravelassistant.domain.entity

data class Trip(
    val id: String,
    val destination: String,
    val dates: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val weatherForecast: String,
    val totalBudgetOnPerson: Int,
    val currency: String,
    val currencySymbol: String,
    val baseHotel: BaseHotel,
    val days: List<DailyPlan>,
    val advice: String
)
