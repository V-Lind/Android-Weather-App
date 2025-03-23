package main.src.openmeteoweatherapp.ui.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import main.src.openmeteoweatherapp.model.ForecastViewModel
import main.src.openmeteoweatherapp.model.data.CurrentWeather
import main.src.openmeteoweatherapp.model.data.Settings
import main.src.openmeteoweatherapp.ui.util.LocationSelector
import main.src.openmeteoweatherapp.ui.util.getWindDirection
import main.src.openmeteoweatherapp.ui.weatherIcons.AnimatedWeatherIcon
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun CurrentWeatherScreen(viewModel: ForecastViewModel) {
    val currentWeather by viewModel.currentWeather.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val gpsLocation by viewModel.gpsLocation.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { viewModel.getCurrentWeather(gpsLocation) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier.fillMaxSize().weight(1f)
                ) {
                    CurrentWeatherCard(currentWeather, settings)
                }
                LocationSelector(viewModel)
            }
        }
    }
}

@Composable
private fun CurrentWeatherCard(
    currentWeather: CurrentWeather?,
    settings: Settings?
) {
    val temperatureUnit = if (settings?.temperatureUnit == "fahrenheit")
        "thermometerFahrenheit" else "thermometerCelsius"

    currentWeather?.let {
        val windDirection = getWindDirection(it.windDirection10m)

        LazyColumn {
            item {
                val dateTime = LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME)
                val formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEEE dd.MM.yyyy"))
                val formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = CenterHorizontally
                ) {
                    AnimatedWeatherIcon(it.weatherCode.toString(), 80)
                    Text(formattedDate)
                    Text(formattedTime)
                }
                Spacer(Modifier.padding(12.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                    ) {

                        Text("Temperature: ${it.temperature2m}")
                        AnimatedWeatherIcon(temperatureUnit, 80)
                        Text("Precipitation: ${it.precipitation}")
                        AnimatedWeatherIcon("raindrop", 80)
                        Text("Snowfall: ${it.snowfall}")
                        AnimatedWeatherIcon("snowflake", 80)
                        Text("Wind Direction:\n${windDirection.first}")
                        AnimatedWeatherIcon(
                            "pressure_low",
                            80,
                            // Icon points wrong way by default; rotate 180 degrees to correct
                            windDirection.second + 180f
                        )
                    }
                    Column(
                        horizontalAlignment = CenterHorizontally,
                    ) {
                        Text("Humidity: ${it.relativeHumidity2m}%")
                        AnimatedWeatherIcon("humidity", 80)
                        Text("Showers: ${it.showers}")
                        AnimatedWeatherIcon("rain", 80)
                        Text("Wind Speed: ${it.windSpeed10m}")
                        AnimatedWeatherIcon("windsock", 80)
                        Text("Wind Gusts: ${it.windGusts10m}")
                        AnimatedWeatherIcon("wind", 80)
                    }
                }
            }
        }
    }
}