package ru.rigertor.smarttravelassistant.domain.entity

import java.util.Calendar

data class DailyPlan(
    val id: String,
    val dayNumber: Int,
    val date: Calendar,
    val theme: String,
    val weather: String,
    val places: List<Place>,
    val dailyRout: DailyRoute,
    val dailyTips: String
)
