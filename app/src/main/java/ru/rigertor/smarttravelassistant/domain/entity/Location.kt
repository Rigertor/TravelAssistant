package ru.rigertor.smarttravelassistant.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)
