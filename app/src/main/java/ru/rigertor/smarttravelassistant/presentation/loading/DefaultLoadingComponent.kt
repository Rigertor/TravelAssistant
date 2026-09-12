package ru.rigertor.smarttravelassistant.presentation.loading

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
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.presentation.extensions.componentScope

class DefaultLoadingComponent @AssistedInject constructor(
    private val storeFactory: LoadingStoreFactory,
    @Assisted("userPrompt") private val userPrompt: String,
    @Assisted("onTripLoaded") private val onTripLoaded: (Trip) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : LoadingComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(prompt = userPrompt) }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is LoadingStore.Label.Loaded -> {
                        onTripLoaded(it.trip)
                    }
                    else -> {}
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<LoadingStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("userPrompt") userPrompt: String,
            @Assisted("onTripLoaded") onTripLoaded: (Trip) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultLoadingComponent
    }
}