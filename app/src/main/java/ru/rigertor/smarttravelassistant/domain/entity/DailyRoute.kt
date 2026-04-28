package ru.rigertor.smarttravelassistant.domain.entity

data class DailyRoute(
    val totalDistance: Float,
    val transport: String,
    val transportCost: Int
)
