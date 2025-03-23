package main.src.openmeteoweatherapp.ui.util

fun getWindDirection(degrees: Int): Pair<String, Float> {
    val directionString = when {
        degrees >= 338 || degrees < 23 -> "North"
        degrees in 23..67 -> "North East"
        degrees in 68..112 -> "East"
        degrees in 113..157 -> "South East"
        degrees in 158..202 -> "South"
        degrees in 203..247 -> "South West"
        degrees in 248..292 -> "West"
        else -> "North West"
    }
    return Pair(directionString, degrees.toFloat())
}