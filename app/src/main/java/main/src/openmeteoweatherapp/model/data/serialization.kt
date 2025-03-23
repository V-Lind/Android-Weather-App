package main.src.openmeteoweatherapp.model.data

import com.google.android.gms.location.LocationResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CurrentWeatherValues(
    val time: String,
    val interval: Int,
    @SerialName("temperature_2m")
    val temperature2m: Double,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: Int,
    @SerialName("apparent_temperature")
    val apparentTemperature: Double,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    @SerialName("weather_code")
    val weatherCode: Int,
    @SerialName("wind_speed_10m")
    val windSpeed10m: Double,
    @SerialName("wind_direction_10m")
    val windDirection10m: Int,
    @SerialName("wind_gusts_10m")
    val windGusts10m: Double
)


//{"latitude":23.75,"longitude":61.5,"generationtime_ms":0.09202957153320312,"utc_offset_seconds":0,"timezone":"GMT","timezone_abbreviation":"GMT","elevation":0.0,"current_units":{"time":"iso8601","interval":"seconds","temperature_2m":"°C","relative_humidity_2m":"%","apparent_temperature":"°C","precipitation":"mm","rain":"mm","showers":"mm","snowfall":"cm","weather_code":"wmo code","wind_speed_10m":"km/h","wind_direction_10m":"°","wind_gusts_10m":"km/h"},"current":{"time":"2024-05-21T00:30","interval":900,"temperature_2m":30.1,"relative_humidity_2m":83,"apparent_temperature":34.7,"precipitation":0.00,"rain":0.00,"showers":0.00,"snowfall":0.00,"weather_code":1,"wind_speed_10m":23.4,"wind_direction_10m":225,"wind_gusts_10m":36.7}}

@Serializable
data class CurrentWeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val elevation: Double,
    val current: CurrentWeatherValues
) {
    fun toCurrentWeather(): CurrentWeather {
        return CurrentWeather(
            time = this.current.time,
            interval = this.current.interval,
            temperature2m = this.current.temperature2m,
            relativeHumidity2m = this.current.relativeHumidity2m,
            apparentTemperature = this.current.apparentTemperature,
            precipitation = this.current.precipitation,
            rain = this.current.rain,
            showers = this.current.showers,
            snowfall = this.current.snowfall,
            weatherCode = this.current.weatherCode,
            windSpeed10m = this.current.windSpeed10m,
            windDirection10m = this.current.windDirection10m,
            windGusts10m = this.current.windGusts10m,
            timezone = this.timezone,
            timezoneAbbreviation = this.timezoneAbbreviation,
            elevation = this.elevation,
        )
    }
}

@Serializable
data class Forecast7DayResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val daily: Daily,
    @SerialName("hourly_units")
    val hourlyUnits: HourlyUnits,
    val hourly: Hourly
) {
    fun toForecastMetadata(): ForecastMetadata {
        return ForecastMetadata(
            timeStored = "",
            locationName = "DEFAULT",
            latitude = this.latitude,
            longitude = this.longitude,
            timezone = this.timezone,
            timezoneAbbreviation = this.timezoneAbbreviation,
            temperature2mUnit = this.hourlyUnits.temperature2m,
            relativeHumidity2mUnit = this.hourlyUnits.relativeHumidity2m,
            apparentTemperatureUnit = this.hourlyUnits.apparentTemperature,
            precipitationProbabilityUnit = this.hourlyUnits.precipitationProbability,
            precipitationUnit = this.hourlyUnits.precipitation,
            rainUnit = this.hourlyUnits.rain,
            showersUnit = this.hourlyUnits.showers,
            snowfallUnit = this.hourlyUnits.snowfall,
            weatherCodeUnit = this.hourlyUnits.weatherCode,
            windSpeed10mUnit = this.hourlyUnits.windSpeed10m,
            windDirection10mUnit = this.hourlyUnits.windDirection10m,
            uvIndexUnit = this.hourlyUnits.uvIndex
        )
    }

    fun toHourlyForecasts(): List<HourlyForecastItem> {
        return this.hourly.time.indices.map { index ->
            HourlyForecastItem(
                dayId = 0, // Actual value injected in repository
                time = this.hourly.time[index],
                temperature2m = this.hourly.temperature2m[index],
                relativeHumidity2m = this.hourly.relativeHumidity2m[index],
                apparentTemperature = this.hourly.apparentTemperature[index],
                precipitationProbability = this.hourly.precipitationProbability[index],
                precipitation = this.hourly.precipitation[index],
                rain = this.hourly.rain[index],
                showers = this.hourly.showers[index],
                snowfall = this.hourly.snowfall[index],
                weatherCode = this.hourly.weatherCode[index],
                windSpeed10m = this.hourly.windSpeed10m[index],
                windDirection10m = this.hourly.windDirection10m[index],
                uvIndex = this.hourly.uvIndex[index]
            )
        }
    }
}

@Serializable
data class Daily(
    @SerialName("time")
    val date: List<String>,
    @SerialName("weather_code")
    val weatherCode: List<Int>,
    @SerialName("temperature_2m_max")
    val temperatureMax: List<Double>,
    @SerialName("temperature_2m_min")
    val temperatureMin: List<Double>,
    @SerialName("uv_index_max")
    val uvIndexMax: List<Double>,
    @SerialName("precipitation_sum")
    val precipitationSum: List<Double>,
    @SerialName("rain_sum")
    val rainSum: List<Double>,
    @SerialName("precipitation_probability_max")
    val precipitationProbabilityMax: List<Int>,
    @SerialName("wind_direction_10m_dominant")
    val windDirectionDominant: List<Int>
)

@Serializable
data class HourlyUnits(
    val time: String,
    @SerialName("temperature_2m")
    val temperature2m: String,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: String,
    @SerialName("apparent_temperature")
    val apparentTemperature: String,
    @SerialName("precipitation_probability")
    val precipitationProbability: String,
    val precipitation: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
    @SerialName("weather_code")
    val weatherCode: String,
    @SerialName("wind_speed_10m")
    val windSpeed10m: String,
    @SerialName("wind_direction_10m")
    val windDirection10m: String,
    @SerialName("uv_index")
    val uvIndex: String
)

@Serializable
data class Hourly(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temperature2m: List<Double>,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: List<Int>,
    @SerialName("apparent_temperature")
    val apparentTemperature: List<Double>,
    @SerialName("precipitation_probability")
    val precipitationProbability: List<Int>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    @SerialName("weather_code")
    val weatherCode: List<Int>,
    @SerialName("wind_speed_10m")
    val windSpeed10m: List<Double>,
    @SerialName("wind_direction_10m")
    val windDirection10m: List<Int>,
    @SerialName("uv_index")
    val uvIndex: List<Double>
)

@Serializable
data class GeocodingResponse(
    val results: List<LocationResponse>
)

@Serializable
data class LocationResponse(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
) {
    fun toGeocodingData(): GeocodingData {
        return GeocodingData(
            name = this.name,
            latitude = this.latitude,
            longitude = this.longitude,
            timezone = this.timezone
        )
    }
}