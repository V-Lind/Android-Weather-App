package main.src.openmeteoweatherapp.model.cache

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import main.src.openmeteoweatherapp.model.data.ViewCompleteSevenDayForecast
import main.src.openmeteoweatherapp.model.data.CurrentWeather
import main.src.openmeteoweatherapp.model.data.DailyForecastItem
import main.src.openmeteoweatherapp.model.data.ForecastMetadata
import main.src.openmeteoweatherapp.model.data.GeocodingData
import main.src.openmeteoweatherapp.model.data.HourlyForecastItem
import main.src.openmeteoweatherapp.model.data.Settings


@Dao
interface ForecastAppDao {

    // region Settings
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: Settings)

    @Query("SELECT * FROM Settings")
    suspend fun getSettings(): Settings
    // endregion Settings


    // region Current weather
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentWeather(currentWeather: CurrentWeather)

    @Query("SELECT * FROM CurrentWeather")
    suspend fun getCurrentWeather(): CurrentWeather
    // endregion Current weather


    // region 7 Day forecast
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForecastMetadata(metadata: ForecastMetadata): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyForecastItem(dailyForecastItem: DailyForecastItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHourlyForecastItems(hourlyForecastItems: List<HourlyForecastItem>)

    @Transaction
    @Query("SELECT * FROM ForecastMetadata WHERE locationName = :locationName")
    suspend fun getCompleteForecast(locationName: String): ViewCompleteSevenDayForecast?

    @Query("""
    SELECT hourly.time 
    FROM hourlyforecastitem hourly
    INNER JOIN dailyforecastitem daily ON hourly.dayId = daily.id
    INNER JOIN forecastmetadata meta ON daily.metadataId = meta.id
    WHERE meta.id = :metadataId
    LIMIT 1
""")
    suspend fun getFirstDayTime(metadataId: Int): String

    @Query("SELECT id FROM ForecastMetadata LIMIT 1")
    suspend fun getFirstMetadataId(): Int
    // endregion 7 Day forecast

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGeocodingLocation(geoCodingData: GeocodingData)

    // Fetch locations that best match the given name
    @Query("SELECT * FROM GeocodingData WHERE name LIKE :locationName || '%' ORDER BY name LIMIT 10")
    suspend fun getGeocodingLocations(locationName: String): List<GeocodingData>

}

@Database(
    entities = [
        ForecastMetadata::class,
        HourlyForecastItem::class,
        DailyForecastItem::class,
        Settings::class,
        CurrentWeather::class,
        GeocodingData::class,
               ],
    version = 1,
    exportSchema = false
)
abstract class ForecastAppDatabase : RoomDatabase() {
    abstract fun forecastAppDao(): ForecastAppDao // TODO: Make separate DAOs
}
