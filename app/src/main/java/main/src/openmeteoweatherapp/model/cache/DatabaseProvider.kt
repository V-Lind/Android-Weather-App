package main.src.openmeteoweatherapp.model.cache

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import main.src.openmeteoweatherapp.model.data.Settings

// Provides database with prepopulated settings
object DatabaseProvider {
    fun getDatabase(context: Context): ForecastAppDatabase {
        // Make an instance of RoomDatabase.Callback
        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("DatabaseProvider", "Creating database")
                    getDatabase(context).forecastAppDao().saveSettings(
                        Settings(
                            temperatureUnit = "celsius",
                            windSpeedUnit = "m/s",
                            precipitationUnit = "mm",
                            lightMode = false
                        )
                    )
                }
            }
        }

        return Room.databaseBuilder(
            context = context,
            klass = ForecastAppDatabase::class.java,
            name = "weatherapp-db"
        ).addCallback(callback)
            .fallbackToDestructiveMigration()
            .build()
    }
}

