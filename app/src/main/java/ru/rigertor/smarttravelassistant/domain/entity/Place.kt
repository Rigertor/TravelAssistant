package ru.rigertor.smarttravelassistant.domain.entity

data class Place(
    val time: String,
    val name: String,
    val nameLocal: String,
    val lat: Double,
    val lon: Double,
    val address: String,
    val description: String,
    val durationHours: Float,
    val estimatedCost: Int,
    val costNote: String,
    val placeType: String,
    val imageUrl: String
)