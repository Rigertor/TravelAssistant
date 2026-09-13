package ru.rigertor.smarttravelassistant.presentation.trip

import kotlinx.coroutines.flow.StateFlow
import ru.rigertor.smarttravelassistant.domain.entity.DailyPlan
import ru.rigertor.smarttravelassistant.domain.entity.Place

interface TripComponent {

    val model: StateFlow<TripStore.State>

    fun onRetryRoute()

    fun onClickBack()

    fun onClickDay(day: DailyPlan)

    fun onClickPlace(place: Place)
}