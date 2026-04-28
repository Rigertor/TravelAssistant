package ru.rigertor.smarttravelassistant.domain.entity

data class BaseHotel(
    val id: String,
    val name: String,
    val nameLocal: String,
    val location: Location,
    val address: String,
    val description: String,
    val estimatedPricePerNight: Int,
    val priceNote: String,
    val imageQuery: String
)
