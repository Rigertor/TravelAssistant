package ru.rigertor.smarttravelassistant.domain.entity

data class Trip(
    val destination: String,
    val dates: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val weatherForecast: String,
    val totalBudgetOnPerson: Int,
    val currency: String,
    val currencySymbol: String,
    val baseHotel: BaseHotel,
    val days: DailyPlan,
    val advice: String
)
