package ru.rigertor.smarttravelassistant.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "place",
    indices = [Index("dayId")],
    foreignKeys = [
        ForeignKey(
            entity = DayDbModel::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaceDbModel(
    @PrimaryKey val id: String,
    val dayId: String,
    val name: String,
    val time: String,
    val nameLocal: String,
    val lat: Double,
    val lng: Double,
    val address: String,
    val description: String,
    val durationHours: Float,
    val estimatedCost: Int,
    val costNote: String,
    val placeType: String,
    val imageQuery: String
)