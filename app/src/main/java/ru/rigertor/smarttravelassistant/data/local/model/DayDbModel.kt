package ru.rigertor.smarttravelassistant.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "day",
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
data class DayDbModel(
    @PrimaryKey val id: String,
    val tripId: String,
    val dayNumber: Int,
    val date: Long,
    val theme: String,
    val weather: String,
    val dailyTips: String,
    val dailyRoutTotalDistance: Float,
    val dailyRoutTransport: String,
    val dailyRoutTransportCost: Int
)
