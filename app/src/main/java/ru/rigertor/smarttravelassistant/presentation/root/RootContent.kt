package ru.rigertor.smarttravelassistant.presentation.root

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.rigertor.smarttravelassistant.domain.entity.ThemeMode
import ru.rigertor.smarttravelassistant.presentation.history.HistoryContent
import ru.rigertor.smarttravelassistant.presentation.loading.LoadingContent
import ru.rigertor.smarttravelassistant.presentation.plan.PlanContent
import ru.rigertor.smarttravelassistant.presentation.start.StartContent
import ru.rigertor.smarttravelassistant.presentation.trip.TripContent
import ru.rigertor.smarttravelassistant.presentation.ui.theme.LightBlue90
import ru.rigertor.smarttravelassistant.presentation.ui.theme.SmartTravelAssistantTheme

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {

    val themeMode by component.appTheme.subscribeAsState()
    val systemDark = isSystemInDarkTheme()

    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    SmartTravelAssistantTheme(darkTheme = isDark) {
        Box(modifier = modifier.fillMaxSize()) {
            Children(
                stack = component.stack
            ) {
                when (val instance = it.instance) {
                    is RootComponent.Child.History -> {
                        HistoryContent(component = instance.component)
                    }

                    is RootComponent.Child.Plan -> {
                        PlanContent(
                            component = instance.component
                        )
                    }

                    is RootComponent.Child.Start -> {
                        StartContent(component = instance.component)
                    }

                    is RootComponent.Child.Loading -> {
                        LoadingContent(component = instance.component)
                    }

                    is RootComponent.Child.Trip -> {
                        TripContent(component = instance.component)
                    }
                }
                // Кнопка темы (поверх всех экранов)
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = CircleShape,
                            clip = false
                        )
                        .clip(CircleShape)
                        .size(48.dp)
                        .background(
                            if (it.instance is RootComponent.Child.Start)
                                MaterialTheme.colorScheme.tertiary
                            else
                                MaterialTheme.colorScheme.secondary
                        ),
                    onClick = { component.toggleTheme(systemDark) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DarkMode,
                        contentDescription = null,
                        tint = if (it.instance !is RootComponent.Child.Start)
                            MaterialTheme.colorScheme.onSurface
                        else
                            LightBlue90
                    )
                }
            }
        }
    }
}