package ru.rigertor.smarttravelassistant.presentation.plan

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.rigertor.smarttravelassistant.presentation.plan.PlanStore.Intent
import ru.rigertor.smarttravelassistant.presentation.plan.PlanStore.Label
import ru.rigertor.smarttravelassistant.presentation.plan.PlanStore.State
import ru.rigertor.smarttravelassistant.presentation.plan.PlanStoreFactory.Msg.ChangePlanText
import javax.inject.Inject

interface PlanStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangePlanText(val newPlanText: String) : Intent

        data class ClickExamplePrompt(val text: String) : Intent

        data object ClickBack : Intent

        data class ClickGenerate(val userPrompt: String) : Intent
    }

    data class State(
        val tripText: String,
        val isGenerateButtonActive: Boolean = false,
        val examplePromptList: List<String> = listOf(
            "2 days in Istanbul",
            "Weekend in Paris",
            "2 days in Berlin, budget-friendly",
            "Cheap trip to Rome",
            "5 days in Tokyo, food focused",
            "New York City on a budget"
        )
    )

    sealed interface Label {


        data object ClickBack : Label

        data class ClickGenerate(val userPrompt: String) : Label
    }
}

class PlanStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): PlanStore =
        object : PlanStore, Store<Intent, State, Label> by storeFactory.create(
            name = "PlanStore",
            initialState = State(
                tripText = ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangePlanText(val newPlanText: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {}
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {

            when (intent) {
                is Intent.ChangePlanText -> {
                    dispatch(ChangePlanText(newPlanText = intent.newPlanText))
                }

                is Intent.ClickExamplePrompt -> {
                    dispatch(ChangePlanText(newPlanText = intent.text))
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickGenerate -> {
                    publish(Label.ClickGenerate(userPrompt = intent.userPrompt))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is ChangePlanText -> {
                val promptText = msg.newPlanText
                if (promptText.isEmpty())
                    copy(tripText = promptText, isGenerateButtonActive = false)
                else
                    copy(tripText = promptText, isGenerateButtonActive = true)
            }
        }
    }
}
