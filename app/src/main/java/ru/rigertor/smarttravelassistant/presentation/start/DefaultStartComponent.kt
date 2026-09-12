package ru.rigertor.smarttravelassistant.presentation.start

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.rigertor.smarttravelassistant.presentation.extensions.componentScope

class DefaultStartComponent @AssistedInject constructor(
    private val startStoreFactory: StartStoreFactory,
    @Assisted("onHistoryClick") private val onHistoryClicked: () -> Unit,
    @Assisted("onStartClick") private val onStartClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : StartComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { startStoreFactory.create() }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    StartStore.Label.ClickHistory -> {
                        onHistoryClicked()
                    }

                    StartStore.Label.ClickStart -> {
                        onStartClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<StartStore.State>
        get() = store.stateFlow

    override fun onHistoryClick() {
        store.accept(intent = StartStore.Intent.ClickHistory)
    }

    override fun onStartClick() {
        store.accept(intent = StartStore.Intent.ClickStart)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onHistoryClick") onHistoryClicked: () -> Unit,
            @Assisted("onStartClick") onStartClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultStartComponent
    }

}