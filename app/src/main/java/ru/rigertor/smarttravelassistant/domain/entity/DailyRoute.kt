package ru.rigertor.smarttravelassistant.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class DailyRoute(
    val totalDistance: Float,
    val transport: String,
    val transportCost: Int
)
