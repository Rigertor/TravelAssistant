package ru.rigertor.smarttravelassistant.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import ru.rigertor.smarttravelassistant.presentation.ui.theme.SmartTravelAssistantTheme

class MainActivity : ComponentActivity() {

//    private val apiFactory = TripApiFactory
//    private val apiService: TripApiService = apiFactory.apiService
//    private val repositoryImpl = TravelRepositoryImpl(apiService)
//
//    private val buildTripUseCase = BuildTripUseCase(repository = repositoryImpl)
//
//    val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)


    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        coroutineScope.launch {
//            val trip = buildTripUseCase(
//                promptTemplate = getString(R.string.prompt),
//                userRequest = "Я хочу съездить в Нефтекамск на 4 дня"
//            )
//            Log.d("MainActivity", "Result: $trip")
//        }
        enableEdgeToEdge()
        setContent {
            SmartTravelAssistantTheme {
                val neftekamsk = LatLng(56.0833, 54.2833)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(neftekamsk, 100f)
                }
                val markerState = rememberUpdatedMarkerState(
                    position = neftekamsk
                )
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = markerState,
                        title = "Neftekamsk",
                        snippet = null
                    )
                }
            }
        }
    }
}