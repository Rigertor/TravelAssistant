package ru.rigertor.smarttravelassistant.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_history")
data class TripDbModel(
    @PrimaryKey val id: String,
    val destination: String,
    val dates: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val weatherForecast: String,
    val totalBudgetOnPerson: Int,
    val currency: String,
    val currencySymbol: String,
    val advice: String,
    val createdAt: Long = System.currentTimeMillis()
)