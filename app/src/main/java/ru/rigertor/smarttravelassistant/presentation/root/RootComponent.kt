package ru.rigertor.smarttravelassistant.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.rigertor.smarttravelassistant.domain.entity.ThemeMode
import ru.rigertor.smarttravelassistant.presentation.history.HistoryComponent
import ru.rigertor.smarttravelassistant.presentation.loading.LoadingComponent
import ru.rigertor.smarttravelassistant.presentation.plan.PlanComponent
import ru.rigertor.smarttravelassistant.presentation.start.StartComponent
import ru.rigertor.smarttravelassistant.presentation.trip.TripComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    val appTheme: Value<ThemeMode>

    fun toggleTheme(isSystemDark: Boolean)

    sealed interface Child {

        data class Start(val component: StartComponent) : Child

        data class Plan(val component: PlanComponent) : Child

        data class History(val component: HistoryComponent) : Child

        data class Loading(val component: LoadingComponent) : Child

        data class Trip(val component: TripComponent): Child
    }
}