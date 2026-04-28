package ru.rigertor.smarttravelassistant.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import ru.rigertor.smarttravelassistant.data.local.model.DayDbModel
import ru.rigertor.smarttravelassistant.data.local.model.PlaceDbModel

data class DayWithPlaces(
    @Embedded val day: DayDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    )
    val places: List<PlaceDbModel>
)
