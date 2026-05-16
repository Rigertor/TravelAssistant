package ru.rigertor.smarttravelassistant.presentation.trip

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
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

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = sheetState,
        sheetPeekHeight = 420.dp,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetShadowElevation = 0.dp,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        },
        sheetContent = {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        onDaySelected = component::onClickDay
                    )
                }

                item {

                    HotelCard(
                        hotel = state.trip.baseHotel
                    )
                }

                item {

                    Column {

                        Text(
                            text = selectedPlan.theme,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = "${selectedPlan.places.size} places",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${selectedPlan.dailyRout.totalDistance} km",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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

                    PlaceCard(place = place)
                }

                item {

                    InfoCard(
                        title = "Daily Tips",
                        text = selectedPlan.dailyTips
                    )
                }

                item {

                    InfoCard(
                        title = "General Advice",
                        text = state.trip.advice
                    )
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

            TopBar(
                title = state.trip.destination,
                subtitle = state.trip.dates,
                onBackClick = {},
                onSettingsClick = {},
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onSettingsClick) {

            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }

//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .statusBarsPadding()
//            .padding(horizontal = 20.dp, vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        IconButton(
//            onClick = onBackClick
//        ) {
//
//            Icon(
//                imageVector = Icons.AutoMirrored.Default.ArrowBack,
//                contentDescription = null
//            )
//        }
//
//        Column(
//            modifier = Modifier.weight(1f),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Text(
//                text = title,
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold
//            )
//
//            Text(
//                text = subtitle,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//            )
//        }
//    }
}

@Composable
private fun BudgetCard(
    budget: String,
    weather: String
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total_budget_per_person),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Text(
                    text = budget,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = weather,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun DaySelector(
    days: List<DailyPlan>,
    selectedDay: Int,
    onDaySelected: (DailyPlan) -> Unit
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        itemsIndexed(days) { index, day ->

            val selected = index == selectedDay

            Surface(
                onClick = {
                    onDaySelected(day)
                },
                shape = RoundedCornerShape(18.dp),
                color = if (selected)
                    Blue20
                else
                    MaterialTheme.colorScheme.secondary
            ) {

                Text(
                    text = "Day ${index + 1}",
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 14.dp
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
    place: Place
) {

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier.padding(18.dp)
        ) {

            Surface(
                modifier = Modifier.size(52.dp),
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

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = place.time,
                        color = Blue20,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${place.durationHours}h",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = place.nameLocal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = place.description,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = place.costNote,
                    color = Blue20,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun HotelCard(
    hotel: BaseHotel
) {

    Card(
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(
            1.dp,
            Orange20.copy(alpha = 0.4f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBF5)
        )
    ) {

        Row(
            modifier = Modifier.padding(20.dp)
        ) {

            Surface(
                modifier = Modifier.size(54.dp),
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

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = "Recommended Hotel",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = hotel.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = hotel.description,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${hotel.location.lat}, ${hotel.location.lng}",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
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
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun WeatherNoteCard(
    text: String
) {

    Surface(
        shape = RoundedCornerShape(20.dp),
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
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}