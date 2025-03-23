package main.src.openmeteoweatherapp.ui.util

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import main.src.openmeteoweatherapp.model.ForecastViewModel
import org.osmdroid.library.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay


@Composable
fun LocationSelector(
    viewModel: ForecastViewModel,
    modifier: Modifier = Modifier
) {
    var showMap by remember { mutableStateOf(false) }
    var locationQuery by remember { mutableStateOf("") }
    var changeLocationTriggeredByClick by remember { mutableStateOf(false) }
    val mapLocation = remember { mutableStateOf(GeoPoint(61.5, 23.73)) }
    val geocodingLocations by viewModel.geocodingLocations.collectAsState()
    val gpsLocation by viewModel.gpsLocation.collectAsState()
    var showCityList by remember { mutableStateOf(true) }

    LaunchedEffect(locationQuery) {
        if (!changeLocationTriggeredByClick) {
            // Buffer to prevent excess API calls
            if (locationQuery.length > 2) {
                delay(300)
                Log.d("LocationSelector", "Search query: $locationQuery")
                viewModel.getGeocodingLocation(locationQuery)
                showCityList = true
            }
        } else { showCityList = false }
    }

    // Update weather data when chosen location changes
    LaunchedEffect(mapLocation.value.latitude, mapLocation.value.longitude) {
        viewModel.getCurrentWeather(mapLocation.value)
        viewModel.get7DayForecast(mapLocation.value)
    }

    // Move map marker to gps location when it changes
    LaunchedEffect(gpsLocation) {
        mapLocation.value = gpsLocation as GeoPoint
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = locationQuery,
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp,
        )
        Row {
            Button({ showMap = true }) { Text("Select location") }
            Spacer(Modifier.size(16.dp))
            Column {
                Text("Longitude: ${mapLocation.value.longitude}")
                Text("Latitude: ${mapLocation.value.latitude}")
            }
        }
    }
    if (showMap) {
        Dialog(
            onDismissRequest = { showMap = false }
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = locationQuery,
                        onValueChange = {
                            locationQuery = it
                            // Writing resets click check to allow fetch
                            changeLocationTriggeredByClick = false
                                        },
                        label = { Text("Location name") }
                    )
                    Box {
                        if (showCityList) {
                            LazyColumn(
                                modifier = Modifier
                                    .background(Color.DarkGray)
                                    .fillMaxWidth()
                                    .zIndex(1f)
                            ) {
                                items(geocodingLocations) { location ->
                                    Column {
                                        Text(
                                            text = location.name,
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable {
                                                    // Skip fetching when clicking
                                                    changeLocationTriggeredByClick = true
                                                    locationQuery = location.name
                                                    mapLocation.value = GeoPoint(
                                                        location.latitude,
                                                        location.longitude
                                                    )
                                                    showCityList = false
                                                }
                                        )
                                        HorizontalDivider(
                                            modifier = Modifier.padding(8.dp),
                                            thickness = Dp.Hairline,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                        Column {
                            Spacer(Modifier.size(80.dp))
                            MapContainer(mapLocation, locationQuery)
                            Button(
                                onClick = {
                                    viewModel.getGpsLocation()
                                    mapLocation.value = gpsLocation
                                }
                            ) { Text("Current location") }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun MapContainer(
    mapLocation: MutableState<GeoPoint>,
    locationName: String
) {
    val mapView = remember { mutableStateOf<MapView?>(null) }
    val marker = remember { mutableStateOf<Marker?>(null) }

    Box(
        modifier = Modifier.size(400.dp)
    ) {
        Text(
            text = locationName,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val map = createMapView(context, mapLocation, marker)
                mapView.value = map
                map
            }
        )
    }


    // Listen for changes in mapLocation
    LaunchedEffect(mapLocation.value) {
        // Code to execute when mapLocation changes
        mapView.value?.controller?.animateTo(mapLocation.value)
        marker.value?.position = mapLocation.value
    }
}

private fun createMapView(
    context: Context,
    mapLocation: MutableState<GeoPoint>,
    marker: MutableState<Marker?>
): MapView {
    // Initialize map view
    val mapView = MapView(context).apply {
        setTileSource(TileSourceFactory.USGS_TOPO)
        controller.setCenter(mapLocation.value)
        controller.setZoom(5.0)
        isVerticalMapRepetitionEnabled = false
        isHorizontalMapRepetitionEnabled = false
        minZoomLevel = 4.0
    }

    // Create map marker on current position
    val locationMarker = Marker(mapView).apply {
        position = mapLocation.value
        icon = context.getDrawable(R.drawable.marker_default)
    }
    marker.value = locationMarker

    // Add overlay
    mapView.overlayManager.apply {
        add(locationMarker)
        add(createOverlay(mapLocation, locationMarker))
    }

    return mapView
}

private fun createOverlay(
    mapLocation: MutableState<GeoPoint>,
    locationMarker: Marker
): Overlay {

    return object : Overlay() {

        // Single tap to select location and center on it
        override fun onSingleTapConfirmed(
            e: MotionEvent?,
            mapView: MapView?
        ): Boolean {
            // Osmdroid: calculate GeoPoint from projection
            val projection = mapView?.projection
            val eventGeoPoint = projection?.fromPixels(
                e?.x?.toInt() ?: 0,
                e?.y?.toInt() ?: 0
            )
            mapLocation.value = eventGeoPoint as GeoPoint
            // Center map on selected location
            mapView.controller?.animateTo(mapLocation.value)
            // Move marker to selected location
            locationMarker.position = mapLocation.value

            return true
        }

        // Animated zoom to double tap location
        override fun onDoubleTap(
            e: MotionEvent?,
            mapView: MapView?
        ): Boolean {
            val projection = mapView?.projection
            val eventGeoPoint = projection?.fromPixels(
                e?.x?.toInt() ?: 0,
                e?.y?.toInt() ?: 0
            )
            mapLocation.value = eventGeoPoint as GeoPoint
            mapView.controller?.animateTo(
                mapLocation.value,
                mapView.zoomLevelDouble + 1.0,
                500L)
            return true
        }
    }
}