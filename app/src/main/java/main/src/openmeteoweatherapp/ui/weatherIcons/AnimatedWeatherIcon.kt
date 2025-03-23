package main.src.openmeteoweatherapp.ui.weatherIcons
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import main.src.openmeteoweatherapp.R

@Composable
fun AnimatedWeatherIcon(
    iconName: String,
    size: Int = 100,
    degrees: Float = 0f
) {
    val icon = getLottieFile(iconName)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(icon))

    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .size(size.dp)
            .rotate(degrees)
        ,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )
}

// TODO: Add more icons
fun getLottieFile(
    iconName: String,
    isNight: Boolean = false
): Int {

    /*
0	Clear sky
1, 2, 3	Mainly clear, partly cloudy, and overcast
45, 48	Fog and depositing rime fog
51, 53, 55	Drizzle: Light, moderate, and dense intensity
56, 57	Freezing Drizzle: Light and dense intensity
61, 63, 65	Rain: Slight, moderate and heavy intensity
66, 67	Freezing Rain: Light and heavy intensity
71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
77	Snow grains
80, 81, 82	Rain showers: Slight, moderate, and violent
85, 86	Snow showers slight and heavy
95 *	Thunderstorm: Slight or moderate
96, 99 *	Thunderstorm with slight and heavy hail
*/
    return when (iconName) {
        "placeholder" -> R.raw.flag_gale_warning
        "placeholder" -> R.raw.flag_hurricane_warning
        "placeholder" -> R.raw.flag_small_craft_advisory
        "placeholder" -> R.raw.flag_storm_warning
        "45" -> R.raw.fog
        "48" -> R.raw.fog
        "placeholder" -> R.raw.fog_day
        "placeholder" -> R.raw.fog_night
        "placeholder" -> R.raw.glove
        "placeholder" -> R.raw.hail
        "placeholder" -> R.raw.haze
        "placeholder" -> R.raw.haze_day
        "placeholder" -> R.raw.haze_night
        "placeholder" -> R.raw.horizon
        "humidity" -> R.raw.humidity
        "placeholder" -> R.raw.hurricane
        "placeholder" -> R.raw.lightning_bolt
        "placeholder" -> R.raw.mist
        "placeholder" -> R.raw.moonrise
        "placeholder" -> R.raw.moonset
        "placeholder" -> R.raw.moon_first_quarter
        "placeholder" -> R.raw.moon_full
        "placeholder" -> R.raw.moon_last_quarter
        "placeholder" -> R.raw.moon_new
        "placeholder" -> R.raw.moon_waning_crescent
        "placeholder" -> R.raw.moon_waning_gibbous
        "placeholder" -> R.raw.moon_waxing_crescent
        "placeholder" -> R.raw.moon_waxing_gibbous
        "3" -> R.raw.overcast
        "placeholder" -> R.raw.overcast_day
        "placeholder" -> R.raw.overcast_day_drizzle
        "placeholder" -> R.raw.overcast_day_fog
        "placeholder" -> R.raw.overcast_day_hail
        "placeholder" -> R.raw.overcast_day_haze
        "placeholder" -> R.raw.overcast_day_rain
        "placeholder" -> R.raw.overcast_day_sleet
        "placeholder" -> R.raw.overcast_day_smoke
        "placeholder" -> R.raw.overcast_day_snow
        "51" -> R.raw.overcast_drizzle
        "53" -> R.raw.overcast_drizzle
        "55" -> R.raw.overcast_drizzle
        "56" -> R.raw.overcast_drizzle
        "57" -> R.raw.overcast_drizzle
        "placeholder" -> R.raw.overcast_fog
        "placeholder" -> R.raw.overcast_hail
        "placeholder" -> R.raw.overcast_haze
        "placeholder" -> R.raw.overcast_night
        "placeholder" -> R.raw.overcast_night_drizzle
        "placeholder" -> R.raw.overcast_night_fog
        "placeholder" -> R.raw.overcast_night_hail
        "placeholder" -> R.raw.overcast_night_haze
        "placeholder" -> R.raw.overcast_night_rain
        "placeholder" -> R.raw.overcast_night_sleet
        "placeholder" -> R.raw.overcast_night_smoke
        "placeholder" -> R.raw.overcast_night_snow
        "placeholder" -> R.raw.overcast_rain
        "placeholder" -> R.raw.overcast_sleet
        "placeholder" -> R.raw.overcast_smoke
        "placeholder" -> R.raw.overcast_snow
        "2" -> R.raw.partly_cloudy_day
        "placeholder" -> R.raw.partly_cloudy_day_drizzle
        "placeholder" -> R.raw.partly_cloudy_day_fog
        "placeholder" -> R.raw.partly_cloudy_day_hail
        "placeholder" -> R.raw.partly_cloudy_day_haze
        "placeholder" -> R.raw.partly_cloudy_day_rain
        "placeholder" -> R.raw.partly_cloudy_day_sleet
        "placeholder" -> R.raw.partly_cloudy_day_smoke
        "placeholder" -> R.raw.partly_cloudy_day_snow
        "2n" -> R.raw.partly_cloudy_night
        "placeholder" -> R.raw.partly_cloudy_night_drizzle
        "placeholder" -> R.raw.partly_cloudy_night_fog
        "placeholder" -> R.raw.partly_cloudy_night_hail
        "placeholder" -> R.raw.partly_cloudy_night_haze
        "placeholder" -> R.raw.partly_cloudy_night_rain
        "placeholder" -> R.raw.partly_cloudy_night_sleet
        "placeholder" -> R.raw.partly_cloudy_night_smoke
        "placeholder" -> R.raw.partly_cloudy_night_snow
        "placeholder" -> R.raw.pollen
        "placeholder" -> R.raw.pollen_flower
        "placeholder" -> R.raw.pollen_grass
        "placeholder" -> R.raw.pollen_tree
        "placeholder" -> R.raw.pressure_high
        "placeholder" -> R.raw.pressure_high_alt
        "pressure_low" -> R.raw.pressure_low
        "placeholder" -> R.raw.pressure_low_alt
        "61" -> R.raw.rain
        "63" -> R.raw.rain
        "65" -> R.raw.rain
        "80" -> R.raw.rain
        "81" -> R.raw.rain
        "82" -> R.raw.rain
        "rain" -> R.raw.rain
        "placeholder" -> R.raw.rainbow
        "placeholder" -> R.raw.rainbow_clear
        "raindrop" -> R.raw.raindrop
        "raindrops" -> R.raw.raindrops
        "placeholder" -> R.raw.raindrop_measure
        "placeholder" -> R.raw.sleet
        "placeholder" -> R.raw.smoke
        "placeholder" -> R.raw.smoke_particles
        "snowflake" -> R.raw.snowflake
        "placeholder" -> R.raw.snowman
        "placeholder" -> R.raw.solar_eclipse
        "placeholder" -> R.raw.star
        "placeholder" -> R.raw.starry_night
        "placeholder" -> R.raw.sunrise
        "placeholder" -> R.raw.sunset
        "0" -> R.raw.sun_hot
        "1" -> R.raw.sun_hot
        "placeholder" -> R.raw.thermometer
        "thermometerCelsius" -> R.raw.thermometer_celsius
        "thermometer_colder" -> R.raw.thermometer_colder
        "thermometerFahrenheit" -> R.raw.thermometer_fahrenheit
        "placeholder" -> R.raw.thermometer_glass
        "placeholder" -> R.raw.thermometer_glass_celsius
        "placeholder" -> R.raw.thermometer_glass_fahrenheit
        "placeholder" -> R.raw.thermometer_mercury
        "placeholder" -> R.raw.thermometer_mercury_cold
        "placeholder" -> R.raw.thermometer_moon
        "placeholder" -> R.raw.thermometer_raindrop
        "placeholder" -> R.raw.thermometer_snow
        "placeholder" -> R.raw.thermometer_sun
        "thermometer_warmer" -> R.raw.thermometer_warmer
        "placeholder" -> R.raw.thermometer_water
        "95" -> R.raw.thunderstorms
        "placeholder" -> R.raw.thunderstorms_day
        "placeholder" -> R.raw.thunderstorms_day_extreme
        "placeholder" -> R.raw.thunderstorms_day_extreme_rain
        "placeholder" -> R.raw.thunderstorms_day_extreme_snow
        "placeholder" -> R.raw.thunderstorms_day_overcast
        "placeholder" -> R.raw.thunderstorms_day_overcast_rain
        "placeholder" -> R.raw.thunderstorms_day_overcast_snow
        "placeholder" -> R.raw.thunderstorms_day_rain
        "placeholder" -> R.raw.thunderstorms_day_snow
        "99" -> R.raw.thunderstorms_extreme
        "placeholder" -> R.raw.thunderstorms_extreme_rain
        "placeholder" -> R.raw.thunderstorms_extreme_snow
        "placeholder" -> R.raw.thunderstorms_night
        "placeholder" -> R.raw.thunderstorms_night_extreme
        "placeholder" -> R.raw.thunderstorms_night_extreme_rain
        "placeholder" -> R.raw.thunderstorms_night_extreme_snow
        "placeholder" -> R.raw.thunderstorms_night_overcast
        "placeholder" -> R.raw.thunderstorms_night_overcast_rain
        "placeholder" -> R.raw.thunderstorms_night_overcast_snow
        "placeholder" -> R.raw.thunderstorms_night_rain
        "placeholder" -> R.raw.thunderstorms_night_snow
        "placeholder" -> R.raw.thunderstorms_overcast
        "placeholder" -> R.raw.thunderstorms_overcast_rain
        "placeholder" -> R.raw.thunderstorms_overcast_snow
        "96" -> R.raw.thunderstorms_rain
        "placeholder" -> R.raw.thunderstorms_snow
        "placeholder" -> R.raw.tide_high
        "placeholder" -> R.raw.tide_low
        "placeholder" -> R.raw.time_afternoon
        "placeholder" -> R.raw.time_evening
        "placeholder" -> R.raw.time_late_afternoon
        "placeholder" -> R.raw.time_late_evening
        "placeholder" -> R.raw.time_late_morning
        "placeholder" -> R.raw.time_late_night
        "placeholder" -> R.raw.time_morning
        "placeholder" -> R.raw.time_night
        "placeholder" -> R.raw.tornado
        "placeholder" -> R.raw.umbrella
        "placeholder" -> R.raw.umbrella_wind
        "placeholder" -> R.raw.umbrella_wind_alt
        "uvIndex0" -> R.raw.uv_index
        "uvIndex1" -> R.raw.uv_index_1
        "uvIndex2" -> R.raw.uv_index_2
        "uvIndex3" -> R.raw.uv_index_3
        "uvIndex4" -> R.raw.uv_index_4
        "uvIndex5" -> R.raw.uv_index_5
        "uvIndex6" -> R.raw.uv_index_6
        "uvIndex7" -> R.raw.uv_index_7
        "uvIndex8" -> R.raw.uv_index_8
        "uvIndex9" -> R.raw.uv_index_9
        "uvIndex10" -> R.raw.uv_index_10
        "uvIndex11" -> R.raw.uv_index_11
        "wind" -> R.raw.wind
        "windsock" -> R.raw.windsock
        "placeholder" -> R.raw.windsock_weak
        "placeholder" -> R.raw.wind_alert
        "placeholder" -> R.raw.wind_beaufort_0
        "placeholder" -> R.raw.wind_beaufort_1
        "placeholder" -> R.raw.wind_beaufort_10
        "placeholder" -> R.raw.wind_beaufort_11
        "placeholder" -> R.raw.wind_beaufort_12
        "placeholder" -> R.raw.wind_beaufort_2
        "placeholder" -> R.raw.wind_beaufort_3
        "placeholder" -> R.raw.wind_beaufort_4
        "placeholder" -> R.raw.wind_beaufort_5
        "placeholder" -> R.raw.wind_beaufort_6
        "placeholder" -> R.raw.wind_beaufort_7
        "placeholder" -> R.raw.wind_beaufort_8
        "placeholder" -> R.raw.wind_beaufort_9
        "placeholder" -> R.raw.wind_offshore
        "placeholder" -> R.raw.wind_onshore
        "placeholder" -> R.raw.wind_snow
        else -> R.raw.not_available
    }
}