# OpenMeteoWeatherApp

OpenMeteoWeatherApp is an android compose weather application.
It provides current weather information and a seven-day forecast.
The application also includes settings for customization.

## Libraries and Frameworks Used

- **Kotlin**: The primary language used for development.
- **Gradle**: Used for dependency management and build automation.
- **Android Jetpack Compose**: Primary UI toolkit used to build the interfaces for the application.
- **AndroidX Core**: Provides core functionality like context compatibility checks.
- **AndroidX Activity**: Used for managing the lifecycle of the application's activities.
- Includes libraries like ActivityResultContracts for handling permissions requests.
- **Ktor**: Used for making network requests and handling responses.
- **Kotlinx Serialization**: Used for serializing and deserializing JSON data.
- **Room**: Used for data persistence and to provide offline support. SQLite based database.
- **Google Play Services Location**: Used for accessing the device's location.
- **Kotlin Coroutines**: Used for managing background threads.
- **Osmdroid**: Used for displaying and interacting with a map.
- **Lottie**: Animating Lottie files in the application.
- **Accompanist Swipe Refresh**: Provides a SwipeRefresh layout for Jetpack Compose.
- **Vico**: Used for creating graphs in Jetpack Compose.


## Features

- **Current Weather**: Displays the current weather information of given location.
- **Seven Day Forecast**: Provides a seven-day weather forecast for given location.
- **Favourites**: Not implemented.
- **Settings**: Allows users to customize the application according to their preferences.
- **Location Selection**: Users can select a location on a map to get weather information for that location.
- This is achieved using the Osmdroid library.
- **Animated Weather Icons**: The application uses Lottie to display animated weather icons.
- **Swipe to Refresh**: Users can update the weather information by using a swipe to refresh gesture.

Please note that this application requires certain permissions to function correctly, including internet access and location access.

## Screencasts
[youtube link here](https://youtu.be/G9VvKJKOLEk)
