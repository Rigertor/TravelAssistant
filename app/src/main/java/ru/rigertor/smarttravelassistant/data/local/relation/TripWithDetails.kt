package ru.rigertor.smarttravelassistant.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import ru.rigertor.smarttravelassistant.data.local.model.DayDbModel
import ru.rigertor.smarttravelassistant.data.local.model.HotelDbModel
import ru.rigertor.smarttravelassistant.data.local.model.TripDbModel

data class TripWithDetails(
    @Embedded val trip: TripDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "tripId"
    )
    val hotel: HotelDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "tripId",
        entity = DayDbModel::class
    )
    val days: List<DayWithPlaces>
)
