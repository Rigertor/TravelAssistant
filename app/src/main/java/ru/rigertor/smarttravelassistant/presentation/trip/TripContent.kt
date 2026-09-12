package ru.rigertor.smarttravelassistant.presentation.trip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import ru.rigertor.smarttravelassistant.R
import ru.rigertor.smarttravelassistant.domain.entity.BaseHotel
import ru.rigertor.smarttravelassistant.domain.entity.DailyPlan
import ru.rigertor.smarttravelassistant.domain.entity.Place
import ru.rigertor.smarttravelassistant.presentation.ui.theme.Blue20
import ru.rigertor.smarttravelassistant.presentation.ui.theme.Orange20
import ru.rigertor.smarttravelassistant.presentation.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripContent(
    component: TripComponent,
    modifier: Modifier = Modifier
) {

    val state by component.model.collectAsState()

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    val selectedPlan = state.currentDay

    val cameraPositionState = rememberCameraPositionState {
        val location = selectedPlan.places.first().location
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                location.lat,
                location.lng
            ),
            12f
        )
    }

    val routePoints = remember(selectedPlan) {
        selectedPlan.places.map {
            LatLng(it.location.lat, it.location.lng)
        }
    }

    LaunchedEffect(selectedPlan) {

        val location = selectedPlan.places.first().location

        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(location.lat, location.lng),
                12f
            ),
            durationMs = 1000
        )
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val sheetPeekHeight = (maxHeight - 80.dp).coerceAtLeast(0.dp) * 0.5f
        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    title = state.trip.destination,
                    subtitle = state.trip.dates,
                    onBackClick = component::onClickBack
                )
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = sheetPeekHeight,
            sheetShape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            sheetMaxWidth = Dp.Unspecified,
            sheetContainerColor = MaterialTheme.colorScheme.surface,
            sheetShadowElevation = 0.dp,
            sheetDragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(48.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                )
            },
            sheetContent = {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {

                        BudgetCard(
                            budget = "${state.trip.currencySymbol}${state.trip.totalBudgetOnPerson}",
                            weather = state.trip.weatherForecast
                        )
                    }

                    item {

                        DaySelector(
                            days = state.trip.days,
                            selectedDay = state.currentDay.dayNumber,
                            onDaySelect = component::onClickDay
                        )
                    }

                    if (selectedPlan == state.trip.days.first()) {
                        item {
                            HotelCard(
                                hotel = state.trip.baseHotel,
                                currencySymbol = state.trip.currencySymbol
                            )
                        }
                    }

                    item {

                        Column {

                            Text(
                                text = selectedPlan.theme,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    text = stringResource(R.string.places_count, selectedPlan.places.size),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${selectedPlan.dailyRout.totalDistance} km",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {

                        WeatherNoteCard(
                            text = selectedPlan.weather
                        )
                    }

                    items(selectedPlan.places) { place ->

                        PlaceCard(place = place, currencySymbol = state.trip.currencySymbol)
                    }

                    item {

                        InfoCard(
                            title = stringResource(R.string.daily_tips),
                            text = selectedPlan.dailyTips
                        )
                    }

                    if (selectedPlan == state.trip.days.last()) {
                        item {
                            InfoCard(
                                title = stringResource(R.string.general_advice),
                                text = state.trip.advice
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
            }
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = false
                    ),
                    mapColorScheme = if (MaterialTheme.colorScheme.background.luminance() < 0.5f)
                        ComposeMapColorScheme.DARK
                    else
                        ComposeMapColorScheme.LIGHT,
                    contentPadding = PaddingValues(
                        bottom = sheetPeekHeight
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false
                    )
                ) {

                    routePoints.forEachIndexed { index, point ->

                        Marker(
                            state = MarkerState(position = point),
                            title = selectedPlan.places[index].name
                        )
                    }

                    Polyline(
                        points = routePoints,
                        color = Blue20,
                        width = 8f
                    )
                }

            }
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Spacer(modifier = Modifier.width(48.dp))
    }
}

@Composable
private fun BudgetCard(
    budget: String,
    weather: String
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Blue20.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total_budget_per_person),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = budget,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = weather,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DaySelector(
    days: List<DailyPlan>,
    selectedDay: Int,
    onDaySelect: (DailyPlan) -> Unit
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(days) { day ->

            val selected = day.dayNumber == selectedDay

            Surface(
                onClick = {
                    onDaySelect(day)
                },
                shape = CircleShape,
                color = if (selected)
                    Blue20
                else
                    MaterialTheme.colorScheme.secondary
            ) {

                Text(
                    text = stringResource(R.string.day_number, day.dayNumber),
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 8.dp
                    ),
                    color = if (selected)
                        White
                    else
                        MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun PlaceCard(
    place: Place,
    currencySymbol: String
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary
            ) {

                Icon(
                    modifier = Modifier.padding(14.dp),
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Blue20
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = place.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = Blue20,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "${place.durationHours}h",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = place.nameLocal,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = place.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$currencySymbol${place.estimatedCost} · ${place.costNote}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Blue20,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun HotelCard(
    hotel: BaseHotel,
    currencySymbol: String
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            Orange20.copy(alpha = 0.4f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Orange20.copy(alpha = 0.1f)
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Orange20
            ) {

                Icon(
                    modifier = Modifier.padding(14.dp),
                    imageVector = Icons.Default.Apartment,
                    contentDescription = null,
                    tint = White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = stringResource(R.string.recommended_hotel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = hotel.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = hotel.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = hotel.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "%.4f, %.4f".format(hotel.location.lat, hotel.location.lng),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.nightly_price, currencySymbol, hotel.estimatedPricePerNight),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    text: String
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Blue20
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WeatherNoteCard(
    text: String
) {

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = Blue20
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}