package main.src.openmeteoweatherapp.model.data

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
data class Settings(
    @PrimaryKey
    val id: Int = 0,
    val temperatureUnit: String,
    val windSpeedUnit: String,
    val precipitationUnit: String,
    val lightMode: Boolean
)

@Entity
data class CurrentWeather(
    @PrimaryKey
    val id: Int = 1, // Only allows one stored CurrentWeather

    // Capture time object was stored in db
    val timeStored: String = LocalDateTime.now(ZoneOffset.UTC).toString(),

    val time: String,
    val interval: Int,
    val temperature2m: Double,
    val relativeHumidity2m: Int,
    val apparentTemperature: Double,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    val weatherCode: Int,
    val windSpeed10m: Double,
    val windDirection10m: Int,
    val windGusts10m: Double,
    val timezone: String,
    val timezoneAbbreviation: String,
    val elevation: Double,
)

@Entity(
    indices = [Index(
        value = ["locationName"],
        unique = true
    )]
)
data class ForecastMetadata(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Capture time object was stored in db
    var timeStored: String,
    var locationName: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezoneAbbreviation: String,

    // Added measurement types from HourlyUnits
    val temperature2mUnit: String,
    val relativeHumidity2mUnit: String,
    val apparentTemperatureUnit: String,
    val precipitationProbabilityUnit: String,
    val precipitationUnit: String,
    val rainUnit: String,
    val showersUnit: String,
    val snowfallUnit: String,
    val weatherCodeUnit: String,
    val windSpeed10mUnit: String,
    val windDirection10mUnit: String,
    val uvIndexUnit: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = ForecastMetadata::class,
        parentColumns = ["id"],
        childColumns = ["metadataId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("metadataId")]
)
data class DailyForecastItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var metadataId: Int,
    val date: String,
    val weatherCode: Int,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val uvIndexMax: Double,
    val precipitationSum: Double,
    val rainSum: Double,
    val precipitationProbabilityMax: Int,
    val windDirectionDominant: Int
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = DailyForecastItem::class,
        parentColumns = ["id"],
        childColumns = ["dayId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("dayId")]
)
data class HourlyForecastItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var dayId: Int,
    val time: String,
    val temperature2m: Double,
    val relativeHumidity2m: Int,
    val apparentTemperature: Double,
    val precipitationProbability: Int,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    val weatherCode: Int,
    val windSpeed10m: Double,
    val windDirection10m: Int,
    val uvIndex: Double
)

// Will hold all fetched unique names in database to avoid unnecessary API calls
@Entity(
    indices = [Index(
        value = ["name"],
        unique = true
    )]
)
data class GeocodingData(
    @PrimaryKey()
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)

data class ViewDailyForecastItems(
    @Embedded val dailyForecastItem: DailyForecastItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    )
    val hourlyForecasts: List<HourlyForecastItem>
) {
    // Get date from first hourly forecast
    val date: String by lazy { hourlyForecasts.first().time }
}


data class ViewCompleteSevenDayForecast(
    @Embedded val metadata: ForecastMetadata,
    @Relation(
        parentColumn = "id",
        entityColumn = "metadataId",
        entity = DailyForecastItem::class
    )
    val dailyForecasts: List<ViewDailyForecastItems>
)









