package ru.rigertor.smarttravelassistant.presentation.plan

import kotlinx.coroutines.flow.StateFlow

interface PlanComponent {

    val model: StateFlow<PlanStore.State>

    fun onClickBack()

    fun onGenerateClick(userPrompt: String)

    fun onClickExamplePrompt(exampleText: String)

    fun changeTripText(planText: String)
}