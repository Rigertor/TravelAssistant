package ru.rigertor.smarttravelassistant.domain.entity

data class DailyRout(
    val totalDistance: Float,
    val transport: String,
    val transportCost: Int
)
