package main.src.openmeteoweatherapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import main.src.openmeteoweatherapp.model.ForecastViewModel
import main.src.openmeteoweatherapp.model.data.ForecastMetadata
import main.src.openmeteoweatherapp.model.data.ViewDailyForecastItems
import main.src.openmeteoweatherapp.ui.util.LocationSelector
import main.src.openmeteoweatherapp.ui.weatherIcons.AnimatedWeatherIcon
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SevenDayForecastScreen(
    navController: NavHostController,
    viewModel: ForecastViewModel
) {
    val gpsLocation by viewModel.gpsLocation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { viewModel.getCurrentWeather(gpsLocation) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    SevenDayForecast(
                        navController,
                        viewModel
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
                LocationSelector(viewModel)
            }
        }
    }
}

@Composable
private fun SevenDayForecast(
    navController: NavHostController,
    viewModel: ForecastViewModel
) {
    val complete7DayForecast by viewModel.sevenDayForecast.collectAsState()

    complete7DayForecast?.let {
        val days = complete7DayForecast!!.dailyForecasts

        return LazyColumn {
            itemsIndexed(days) { index, day ->
                DailyStatCard(
                    index = index,
                    day = day,
                    navController = navController,
                    metadata = complete7DayForecast!!.metadata
                )
            }
        }
    }
}

@Composable
private fun DailyStatCard(
    index: Int,
    day: ViewDailyForecastItems,
    navController: NavHostController,
    metadata: ForecastMetadata
) {
    val dateTime = LocalDateTime.parse(day.date, DateTimeFormatter.ISO_DATE_TIME)
    val formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEEE  dd.MM.yyyy"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("DetailedDailyForecastScreen/${index}") },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(formattedDate)
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedWeatherIcon(day.dailyForecastItem.weatherCode.toString(), 70)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedWeatherIcon("raindrops", 70)
                Text("${day.dailyForecastItem.precipitationProbabilityMax}${metadata.precipitationProbabilityUnit}")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedWeatherIcon("thermometer_warmer", 70)
                Text("${day.dailyForecastItem.temperatureMax}${metadata.temperature2mUnit}")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedWeatherIcon("thermometer_colder", 70)
                Text("${day.dailyForecastItem.temperatureMin}${metadata.temperature2mUnit}")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val uvIndex = day.dailyForecastItem.uvIndexMax.toInt()
                AnimatedWeatherIcon("uvIndex$uvIndex", 70)
                Text("UV index: $uvIndex")
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 22.dp),
            color = Color.Gray,
            thickness = 2.dp
        )
    }
}
