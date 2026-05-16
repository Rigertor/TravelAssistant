package ru.rigertor.smarttravelassistant.presentation.plan

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

class DefaultPlanComponent @AssistedInject constructor(
    private val planStoreFactory: PlanStoreFactory,
    @Assisted("onBackClicked") onBackClicked: () -> Unit,
    @Assisted("onGenerateClicked") onGenerateClicked: (String) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : PlanComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { planStoreFactory.create() }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    PlanStore.Label.ClickBack -> {
                        onBackClicked()
                    }

                    is PlanStore.Label.ClickGenerate -> {
                        onGenerateClicked(it.userPrompt)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<PlanStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(PlanStore.Intent.ClickBack)
    }

    override fun onGenerateClick(userPrompt: String) {
        store.accept(PlanStore.Intent.ClickGenerate(userPrompt = userPrompt))
    }

    override fun onClickExamplePrompt(exampleText: String) {
        store.accept(PlanStore.Intent.ClickExamplePrompt(text = exampleText))
    }

    override fun changeTripText(planText: String) {
        store.accept(PlanStore.Intent.ChangePlanText(newPlanText = planText))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onGenerateClicked") onGenerateClicked: (String) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPlanComponent
    }
}