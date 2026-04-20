package ru.rigertor.smarttravelassistant.domain.entity

data class BaseHotel(
    val name: String,
    val lat: Double,
    val lng: Double,
    val reason: String,
    val imageUrl: String
)
