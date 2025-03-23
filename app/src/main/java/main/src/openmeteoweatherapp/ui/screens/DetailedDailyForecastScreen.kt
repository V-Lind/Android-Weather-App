package main.src.openmeteoweatherapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import main.src.openmeteoweatherapp.model.ForecastViewModel
import main.src.openmeteoweatherapp.model.data.ForecastMetadata
import main.src.openmeteoweatherapp.model.data.HourlyForecastItem
import main.src.openmeteoweatherapp.model.data.ViewDailyForecastItems
import main.src.openmeteoweatherapp.ui.util.VicoLineGraph
import main.src.openmeteoweatherapp.ui.util.getWindDirection
import main.src.openmeteoweatherapp.ui.weatherIcons.AnimatedWeatherIcon
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailedDailyForecastScreen(
    id: String,
    viewModel: ForecastViewModel
) {
    val complete7DayForecast by viewModel.sevenDayForecast.collectAsState()
    val dailyForecastData: ViewDailyForecastItems? = complete7DayForecast?.dailyForecasts?.get(id.toInt())
    val temperatures = dailyForecastData?.hourlyForecasts?.map { it.temperature2m }

    LaunchedEffect(Unit) {
        viewModel.getSettings()
    }
    dailyForecastData?.let {
        Column {
            Text(
                text = "Hourly temperature",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            VicoLineGraph(temperatures)
            Text(
                text = "Hours",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(8.dp))

            complete7DayForecast?.let {
                val pagerState = rememberPagerState(
                    pageCount = { dailyForecastData.hourlyForecasts.size }
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        DetailedDailyCard(
                            hourlyForecast = dailyForecastData.hourlyForecasts[page],
                            metadata = complete7DayForecast!!.metadata,
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun DetailedDailyCard(
    hourlyForecast: HourlyForecastItem,
    metadata: ForecastMetadata
) {
    hourlyForecast?.let {
        LazyColumn {
            item {
                val dateTime = LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME)
                val formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedWeatherIcon(it.weatherCode.toString(), 80)
                    Text(formattedTime)
                }
                Spacer(Modifier.padding(8.dp))
            }
            item {
                val windDirection = getWindDirection(it.windDirection10m)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Feels like: ${it.apparentTemperature} ${metadata.apparentTemperatureUnit}")
                        AnimatedWeatherIcon("thermometerCelsius", 80)
                        Text("Precipitation: ${it.precipitation} ${metadata.precipitationUnit}")
                        AnimatedWeatherIcon("raindrop", 80)
                        Text("Snowfall: ${it.snowfall} ${metadata.snowfallUnit}")
                        AnimatedWeatherIcon("snowflake", 80)
                        Text("Wind :${windDirection.first}")
                        AnimatedWeatherIcon(
                            "pressure_low",
                            80,
                            // Icon used points wrong way by default; rotate 180 degrees to correct
                            windDirection.second + 180f
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Humidity: ${it.relativeHumidity2m} ${metadata.relativeHumidity2mUnit}")
                        AnimatedWeatherIcon("humidity", 80)
                        Text("Showers: ${it.showers} ${metadata.showersUnit}")
                        AnimatedWeatherIcon("rain", 80)
                        Text("Wind Speed: ${it.windSpeed10m} ${metadata.windSpeed10mUnit}")
                        AnimatedWeatherIcon("windsock", 80)
                        Text("UV ind: ${it.uvIndex} ${metadata.uvIndexUnit}")
                        AnimatedWeatherIcon("uvIndex${it.uvIndex.toInt()}", 70)
                    }
                }
            }
        }
    }
}