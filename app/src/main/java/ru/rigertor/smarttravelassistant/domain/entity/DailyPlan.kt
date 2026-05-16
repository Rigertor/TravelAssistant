package ru.rigertor.smarttravelassistant.domain.entity

import kotlinx.serialization.Serializable
import ru.rigertor.smarttravelassistant.domain.extensions.CalendarSerializer
import java.util.Calendar

@Serializable
data class DailyPlan(
    val id: String,
    val dayNumber: Int,
    @Serializable(with = CalendarSerializer::class)
    val date: Calendar,
    val theme: String,
    val weather: String,
    val places: List<Place>,
    val dailyRout: DailyRoute,
    val dailyTips: String
)
