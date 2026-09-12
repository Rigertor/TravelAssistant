package ru.rigertor.smarttravelassistant.presentation.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.rigertor.smarttravelassistant.R
import ru.rigertor.smarttravelassistant.presentation.ui.theme.Blue20
import ru.rigertor.smarttravelassistant.presentation.ui.theme.GrayText
import ru.rigertor.smarttravelassistant.presentation.ui.theme.LightGray60
import ru.rigertor.smarttravelassistant.presentation.ui.theme.White

@Composable
fun PlanContent(
    component: PlanComponent
) {
    val state by component.model.collectAsState()

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) { focusRequester.requestFocus() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Top bar
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = component::onClickBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.describe_your_trip),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Blue20
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.ai_travel_planner),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.tell_me_about_trip),
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input
            OutlinedTextField(
                value = state.tripText,
                onValueChange = component::changeTripText,
                placeholder = {
                    Text(
                        stringResource(R.string.plan_hint),
                        color = GrayText
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue20,
                    unfocusedBorderColor = LightGray60,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.examples),
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                state.examplePromptList.forEach { exampleText ->
                    SuggestionChip(
                        onClick = { component.onClickExamplePrompt(exampleText = exampleText) },
                        label = {
                            Text(text = exampleText)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Button
            Button(
                onClick = { component.onGenerateClick(state.tripText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue20,
                    contentColor = White
                ),
                enabled = state.isGenerateButtonActive
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate Plan")
            }
        }
    }
}