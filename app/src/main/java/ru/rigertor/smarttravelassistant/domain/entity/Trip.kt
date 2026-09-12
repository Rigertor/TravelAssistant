package ru.rigertor.smarttravelassistant.domain.entity

import kotlinx.serialization.Serializable
import ru.rigertor.smarttravelassistant.domain.extensions.CalendarSerializer
import java.util.Calendar

@Serializable
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
    val advice: String,
    @Serializable(with = CalendarSerializer::class)
    val creationDate: Calendar =
        Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }
)
