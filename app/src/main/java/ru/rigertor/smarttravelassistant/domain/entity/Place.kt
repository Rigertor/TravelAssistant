package ru.rigertor.smarttravelassistant.domain.entity

data class Place(
    val id: String,
    val time: String,
    val name: String,
    val nameLocal: String,
    val location: Location,
    val address: String,
    val description: String,
    val durationHours: Float,
    val estimatedCost: Int,
    val costNote: String,
    val placeType: String,
    val imageQuery: String
)