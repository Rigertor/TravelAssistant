package ru.rigertor.smarttravelassistant.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hotel",
    foreignKeys = [
        ForeignKey(
            entity = TripDbModel::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tripId")]
)
data class HotelDbModel(
    @PrimaryKey val id: String,
    val tripId: String,
    val name: String,
    val nameLocal: String,
    val lat: Double,
    val lng: Double,
    val address: String,
    val description: String,
    val estimatedPricePerNight: Int,
    val priceNote: String,
    val imageQuery: String
)
