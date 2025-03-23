package main.src.openmeteoweatherapp.model


import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import main.src.openmeteoweatherapp.model.cache.DatabaseProvider
import main.src.openmeteoweatherapp.model.cache.ForecastAppDatabase
import main.src.openmeteoweatherapp.model.data.CurrentWeather
import main.src.openmeteoweatherapp.model.data.CurrentWeatherResponse
import main.src.openmeteoweatherapp.model.data.DailyForecastItem
import main.src.openmeteoweatherapp.model.data.Forecast7DayResponse
import main.src.openmeteoweatherapp.model.data.ForecastMetadata
import main.src.openmeteoweatherapp.model.data.GeocodingData
import main.src.openmeteoweatherapp.model.data.GeocodingResponse
import main.src.openmeteoweatherapp.model.data.Settings
import main.src.openmeteoweatherapp.model.data.ViewCompleteSevenDayForecast
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class ForecastRepository(context: Context) {
    private val application = context.applicationContext as Application
    private val db: ForecastAppDatabase by lazy {
        DatabaseProvider.getDatabase(application)
    }

    private val client: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }

    suspend fun getGpsLocation(): GeoPoint? {
        if (
            ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            ) { return null }

        val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()

        return GeoPoint(location.latitude, location.longitude)
    }

    fun deleteDatabase(context: Context) {
        Log.d("ForecastRepository", "Deleting database")
        context.getDatabasePath("weatherapp-db").delete()
        Log.d("ForecastRepository", "deleted: ${context.deleteDatabase("weatherapp-db")}")
    }

    suspend fun fetchCurrentWeather(url: String) {
        val response: CurrentWeatherResponse = client.get(url).body()
        Log.d("ForecastRepository", "fetchCurrentWeather: $url")
        db.forecastAppDao().saveCurrentWeather(response.toCurrentWeather())
    }

    suspend fun loadCurrentWeather(): CurrentWeather {
        return db.forecastAppDao().getCurrentWeather()
    }

    suspend fun fetch7DayForecast(
        url: String,
        locationName: String
    ) {
        val forecastResponse: Forecast7DayResponse = client.get(url).body()

        // Map data to Room compatible types
        val metadata: ForecastMetadata = forecastResponse.toForecastMetadata()
        val hourlyForecasts = forecastResponse.toHourlyForecasts()

        metadata.locationName = locationName
        metadata.timeStored = LocalDateTime.now(ZoneOffset.UTC).toString()

        // Insert metadata and grab its id
        val metadataId = db.forecastAppDao().saveForecastMetadata(metadata).toInt()

        // Create Daily objects and link them to metadata
        for(day in 0 until 7) {
            val dailyForecastItem = DailyForecastItem(
                metadataId = metadataId,
                date = forecastResponse.daily.date[day],
                weatherCode = forecastResponse.daily.weatherCode[day],
                temperatureMax = forecastResponse.daily.temperatureMax[day],
                temperatureMin = forecastResponse.daily.temperatureMin[day],
                uvIndexMax = forecastResponse.daily.uvIndexMax[day],
                precipitationSum = forecastResponse.daily.precipitationSum[day],
                rainSum = forecastResponse.daily.rainSum[day],
                precipitationProbabilityMax = forecastResponse.daily.precipitationProbabilityMax[day],
                windDirectionDominant = forecastResponse.daily.windDirectionDominant[day]
            )

            // Save DailyForecastItem and get its id
            val dailyForecastItemId = db.forecastAppDao()
                .saveDailyForecastItem(dailyForecastItem)
                .toInt()

            // Take 24 hourly items from the list
            val hourlyForecastItems = hourlyForecasts.subList(day * 24, (day + 1) * 24)

            // Link each hour to current day by injecting foreign key dayId
            hourlyForecastItems.forEach { it.dayId = dailyForecastItemId }
        }
        // Save linked hourly items
        db.forecastAppDao().saveHourlyForecastItems(hourlyForecasts)
    }

    suspend fun load7DayForecast(locationName: String = "DEFAULT"): ViewCompleteSevenDayForecast? {
        return db.forecastAppDao().getCompleteForecast(locationName)
    }

    suspend fun storeSettings(settings: Settings) {
        db.forecastAppDao().saveSettings(settings)
    }

    suspend fun loadSettings(): Settings {
        return db.forecastAppDao().getSettings()
    }

    suspend fun fetchGeocodingLocation(url: String) {
        val response: GeocodingResponse = client.get(url).body()
        response.results.forEach {
            db.forecastAppDao().saveGeocodingLocation(it.toGeocodingData())
        }
    }

    suspend fun loadGeocodingLocations(locationName: String): List<GeocodingData> {
        return db.forecastAppDao().getGeocodingLocations(locationName)
    }
}

class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val forecastRepository = ForecastRepository(application)
    private lateinit var lastUsedSettings: Settings

    private val _currentWeather = MutableStateFlow<CurrentWeather?>(null)
    val currentWeather: StateFlow<CurrentWeather?> = _currentWeather.asStateFlow()

    private val _sevenDayForecast = MutableStateFlow<ViewCompleteSevenDayForecast?>(null)
    val sevenDayForecast: StateFlow<ViewCompleteSevenDayForecast?> = _sevenDayForecast.asStateFlow()

    private val _settings = MutableStateFlow<Settings?>(null)
    val settings: StateFlow<Settings?> = _settings.asStateFlow()

    private val _gpsLocation = MutableStateFlow(GeoPoint(61.5, 23.73))
    val gpsLocation: StateFlow<GeoPoint> = _gpsLocation.asStateFlow()
    private var previousGpsLocation = _gpsLocation.value

    private val _geocodingLocations = MutableStateFlow<List<GeocodingData>>(emptyList())
    val geocodingLocations: StateFlow<List<GeocodingData>> = _geocodingLocations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            lastUsedSettings = forecastRepository.loadSettings()
            // Init with current GPS location
        }
        getGpsLocation()
    }

    private fun settingsHaveChanged(currentSettings: Settings): Boolean {
        return lastUsedSettings != currentSettings
    }

    private fun locationHasChanged(currentLocation: GeoPoint): Boolean {
        return previousGpsLocation != currentLocation
    }

    // Emits current location based on GPS readings
    fun getGpsLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val location = forecastRepository.getGpsLocation()
            Log.d("LocationSelector", "$location")
            location?.let {
                _gpsLocation.emit(location)
                previousGpsLocation = location
            }
        }
    }

    fun getGeocodingLocation(locationName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var geocodingLocations = forecastRepository.loadGeocodingLocations(locationName)
            if (geocodingLocations.isEmpty()) {
                val url = """
                    |https://geocoding-api.open-meteo.com/v1/search
                    |?name=
                        |$locationName
                    |&count=
                        |10
                    |&language=
                        |en
                    |&format=
                        |json
                |""".trimMargin().replace("\n", "")

                try {
                    // Fetch and store locations from API
                    forecastRepository.fetchGeocodingLocation(url)
                } catch (e: Exception) {
                    Log.e("ForecastViewModel", "getGeocodingLocation: $e")
                }

                try {
                    // Load the result from the database
                    geocodingLocations = forecastRepository.loadGeocodingLocations(locationName)
                } catch (e: Exception) {
                    Log.e("ForecastViewModel", "getGeocodingLocation: $e")
                }
            }
            _geocodingLocations.emit(geocodingLocations)
        }
    }

    fun getCurrentWeather(location: GeoPoint) {
        Log.d("ForecastViewModel", "getCurrentWeather: $location")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            val settings = forecastRepository.loadSettings()

            val url = buildCurrentWeatherUrl(location, settings)
            var currentWeather: CurrentWeather? = forecastRepository.loadCurrentWeather()

            // Fetch only if conditions have changed
            val shouldFetchNewWeather = currentWeather == null
                    || isDataOutdated(currentWeather.timeStored, 5)
                    || locationHasChanged(location)
                    || settingsHaveChanged(settings)

            if (shouldFetchNewWeather) {
                forecastRepository.fetchCurrentWeather(url)
                currentWeather = forecastRepository.loadCurrentWeather()
                // Store settings for future comparison
                lastUsedSettings = settings.copy()
            }
            Log.d("ForecastViewModel", "Emitting: $currentWeather")
            _currentWeather.emit(currentWeather)
            _isLoading.emit(false)
        }
    }

    // Returns true if data was stored more than durationMinutes minutes ago
    private fun isDataOutdated(
        timeStored: String,
        durationMinutes: Long
    ): Boolean {
        val timeStoredLocalDateTime = LocalDateTime.parse(timeStored, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val currentTime = LocalDateTime.now(ZoneOffset.UTC)
        return timeStoredLocalDateTime.plusMinutes(durationMinutes).isBefore(currentTime)
    }

    private fun buildCurrentWeatherUrl(
        location: GeoPoint,
        settings: Settings
    ): String {
        val windSpeedUnit = if (settings.windSpeedUnit == "m/s")
            "&wind_speed_unit=ms" else "&wind_speed_unit=mph"
        val precipitationUnit = if (settings.precipitationUnit == "mm")
            "" else "&precipitation_unit=inch"
        val temperatureUnit = if (settings.temperatureUnit == "celsius")
            "" else "&temperature_unit=fahrenheit"

        val url = """
            |https://api.open-meteo.com/v1/forecast
            |?latitude=
                |${location.latitude}
            |&longitude=
                |${location.longitude}
            |&current=
                |temperature_2m,
                |relative_humidity_2m,
                |apparent_temperature,
                |precipitation,
                |rain,
                |showers,
                |snowfall,
                |weather_code,
                |wind_speed_10m,
                |wind_direction_10m,
                |wind_gusts_10m
            |&timezone=
                |auto
            |$windSpeedUnit
            |$precipitationUnit
            |$temperatureUnit
        |""".trimMargin().replace("\n", "")

        Log.d("BuildCurrentWeatherUrl", "url: $url")
        return url
    }


    fun get7DayForecast(
        location: GeoPoint,
        locationName: String = "DEFAULT"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            val settings = forecastRepository.loadSettings()
            val url = buildForecastUrl(location.latitude, location.longitude, settings)

            var completeForecast = forecastRepository.load7DayForecast(locationName)

            // Check for conditions that require fetching a new forecast
            val shouldFetchNewForecast = completeForecast == null
                    || isDataOutdated(completeForecast.metadata.timeStored, 15)
                    || locationHasChanged(location)
                    || settingsHaveChanged(settings)




            if (shouldFetchNewForecast) {
                // Fetch and store new forecast
                forecastRepository.fetch7DayForecast(url, locationName)
                // Load stored forecast
                completeForecast = forecastRepository.load7DayForecast(locationName)
                // Store settings for future comparison
                lastUsedSettings = settings.copy()
            }

            _sevenDayForecast.emit(completeForecast)
            _isLoading.emit(false)
        }
    }

    private fun buildForecastUrl(
        latitude: Double,
        longitude: Double,
        settings: Settings
    ): String {
        val windSpeedUnit = if (settings.windSpeedUnit == "m/s")
            "&wind_speed_unit=ms" else "&wind_speed_unit=mph"
        val precipitationUnit = if (settings.precipitationUnit == "mm")
            "" else "&precipitation_unit=inch"
        val temperatureUnit = if (settings.temperatureUnit == "celsius")
            "" else "&temperature_unit=fahrenheit"

        val url = """
            |https://api.open-meteo.com/v1/forecast
            |?latitude=
                |$latitude
            |&longitude=
                |$longitude
            |&hourly=
                |temperature_2m,
                |relative_humidity_2m,
                |apparent_temperature,
                |precipitation_probability,
                |precipitation,
                |rain,
                |showers,
                |snowfall,
                |weather_code,
                |wind_speed_10m,
                |wind_direction_10m,
                |uv_index
            |&daily=
                |weather_code,
                |temperature_2m_max,
                |temperature_2m_min,
                |uv_index_max,
                |precipitation_sum,
                |rain_sum,
                |precipitation_probability_max,
                |wind_direction_10m_dominant
            |$windSpeedUnit
            |$precipitationUnit
            |$temperatureUnit
            |&timezone=
            |auto
            
        |""".trimMargin().replace("\n", "")

        Log.d("BuildForecastUrl", "url: $url")
        return url
    }

    fun deleteDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            forecastRepository.deleteDatabase(getApplication())
        }
    }

    fun getSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = forecastRepository.loadSettings()
            _settings.emit(settings)
        }
    }

    fun storeSettings(newSettings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            forecastRepository.storeSettings(newSettings)
        }
    }
}
