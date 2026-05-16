package ru.rigertor.smarttravelassistant.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.rigertor.smarttravelassistant.domain.entity.ThemeMode
import ru.rigertor.smarttravelassistant.domain.usecase.ChangeThemeUseCase
import ru.rigertor.smarttravelassistant.domain.usecase.GetAppThemeUseCase
import ru.rigertor.smarttravelassistant.presentation.extensions.componentScope
import ru.rigertor.smarttravelassistant.presentation.history.DefaultHistoryComponent
import ru.rigertor.smarttravelassistant.presentation.loading.DefaultLoadingComponent
import ru.rigertor.smarttravelassistant.presentation.plan.DefaultPlanComponent
import ru.rigertor.smarttravelassistant.presentation.root.RootComponent.Child.History
import ru.rigertor.smarttravelassistant.presentation.root.RootComponent.Child.Loading
import ru.rigertor.smarttravelassistant.presentation.root.RootComponent.Child.Plan
import ru.rigertor.smarttravelassistant.presentation.root.RootComponent.Child.Start
import ru.rigertor.smarttravelassistant.presentation.root.RootComponent.Child.Trip
import ru.rigertor.smarttravelassistant.presentation.start.DefaultStartComponent
import ru.rigertor.smarttravelassistant.presentation.trip.DefaultTripComponent

class DefaultRootComponent @AssistedInject constructor(
    private val startComponentFactory: DefaultStartComponent.Factory,
    private val planComponentFactory: DefaultPlanComponent.Factory,
    private val loadingComponentFactory: DefaultLoadingComponent.Factory,
    private val historyComponentFactory: DefaultHistoryComponent.Factory,
    private val tripComponentFactory: DefaultTripComponent.Factory,
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Start,
        handleBackButton = true,
        childFactory = ::child,
        serializer = Config.serializer()
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            Config.History -> {
                val component = historyComponentFactory.create(
                    componentContext = componentContext,
                    onBackClicked = navigation::pop,
                    onHistoryItemClicked = {
                        navigation.push(Config.Trip(trip = it))
                    }
                )
                History(component = component)
            }

            Config.Plan -> {
                val component = planComponentFactory.create(
                    componentContext = componentContext,
                    onBackClicked = navigation::pop,
                    onGenerateClicked = {
                        navigation.push(Config.Loading(userPrompt = it))
                    }
                )
                Plan(component = component)
            }

            Config.Start -> {
                val component = startComponentFactory.create(
                    componentContext = componentContext,
                    onStartClicked = {
                        navigation.push(Config.Plan)
                    },
                    onHistoryClicked = {
                        navigation.push(Config.History)
                    })
                Start(component = component)
            }

            is Config.Loading -> {
                val component = loadingComponentFactory.create(
                    userPrompt = config.userPrompt,
                    onTripLoaded = {
                        navigation.push(Config.Trip(trip = it))
                    },
                    componentContext = componentContext
                )
                Loading(component = component)
            }

            is Config.Trip -> {
                val component = tripComponentFactory.create(
                    trip = config.trip,
                    onBackClicked = navigation::pop,
                    onPlaceClicked = {
                        TODO()
                    },
                    componentContext = componentContext,
                )
                Trip(component = component)
            }
        }
    }

    private val _appTheme = MutableValue(ThemeMode.SYSTEM)
    override val appTheme: Value<ThemeMode>
        get() = _appTheme

    init {
        componentScope().launch {
            getAppThemeUseCase().collect {
                _appTheme.value = it
            }
        }
    }

    override fun toggleTheme(isSystemDark: Boolean) {
        val current = _appTheme.value

        val newMode = when (current) {
            ThemeMode.SYSTEM ->
                if (isSystemDark) ThemeMode.LIGHT else ThemeMode.DARK

            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
        }

        _appTheme.value = newMode

        componentScope().launch {
            changeThemeUseCase(themeMode = newMode)
        }
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Start : Config

        @Serializable
        data object Plan : Config

        @Serializable
        data object History : Config

        @Serializable
        data class Loading(val userPrompt: String) : Config

        @Serializable
        data class Trip(val trip: ru.rigertor.smarttravelassistant.domain.entity.Trip) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}