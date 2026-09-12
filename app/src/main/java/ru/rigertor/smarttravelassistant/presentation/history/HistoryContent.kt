package ru.rigertor.smarttravelassistant.presentation.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.rigertor.smarttravelassistant.R
import ru.rigertor.smarttravelassistant.domain.entity.Trip
import ru.rigertor.smarttravelassistant.presentation.extensions.formatedFullDate

@Composable
fun HistoryContent(component: HistoryComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                IconButton(onClick = component::onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.trip_history),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )

            if (state.historyTripList.isEmpty()) {

                EmptyHistoryContent()

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(
                        items = state.historyTripList,
                        key = { it.id }
                    ) { trip ->

                        TripHistoryCard(
                            trip = trip,
                            onHistoryItemClick = {
                                component.onHistoryTripClick(trip = trip)
                            },
                            onHistoryItemDeleteClick = {
                                component.onHistoryTripDelete(tripId = trip.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TripHistoryCard(
    trip: Trip,
    onHistoryItemClick: () -> Unit,
    onHistoryItemDeleteClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
        ),
        onClick = onHistoryItemClick
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.Top
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = trip.destination,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            modifier = Modifier.size(22.dp),
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = trip.dates,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                IconButton(
                    onClick = onHistoryItemDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = Color.Red.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Text(
                    text = "${trip.days.size} days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Text(
                    text = "${trip.days.sumOf { it.places.size }} places",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Text(
                    text = "${trip.currencySymbol}${trip.totalBudgetOnPerson} per person",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Saved ${trip.creationDate.formatedFullDate()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
private fun EmptyHistoryContent() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            modifier = Modifier.size(88.dp),
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No trips yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your saved trips will appear here",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}