# OpenMeteoWeatherApp

This is a pretty basic weather app done as a course end project. It was too easy and i wasn't interested in the set goals so i made up some of my own such as going for MVVM structure, using a properly normalized database(Room/sqlite), map feature and animated icons.
Due to priorities i ended up not having time to finish some features like favourites page or dark/light mode. The UI is pretty bad at places but this was never my focus.

The app uses:
- OpenStreetMap: https://www.openstreetmap.org/
- OpenMeteo weather API: https://open-meteo.com/
- OpenMeteo Geocoding API: https://open-meteo.com/en/docs/geocoding-api

## Requirements
To run this app, install the .apk file included in the releases section on an Android SDK34+ device ( Android 14+ ) or run the project through Android studio using an emulator.
The app requires access to internet and gps for the weather and gps data.

## Libraries and Frameworks Used

- **Kotlin**: The primary language used for development.
- **Gradle**: Used for dependency management and build automation.
- **Android Jetpack Compose**: Primary UI toolkit used to build the interfaces for the application.
- **Ktor**: Used for making network requests and handling responses.
- **Kotlinx Serialization**: Used for serializing and deserializing JSON data.
- **Room**: Used for data persistence. SQLite based database.
- **Kotlin Coroutines**: Used for managing background threads.
- **Osmdroid**: OpenStreetMap - Used for displaying and interacting with a map.
- **Lottie**: Animating Lottie files in the application.
- **Animated Weather Icons**: Lottie icons by basmilius (https://basmilius.github.io/weather-icons/)
- **Accompanist Swipe Refresh**: Provides a SwipeRefresh layout for Jetpack Compose.
- **Vico**: Used for creating graphs in Jetpack Compose.

## Images
![image](https://github.com/user-attachments/assets/bbba24fd-53bf-4c25-a5ac-6be692781679)
![image](https://github.com/user-attachments/assets/bd4b165f-5319-4952-96a9-b8f276e0f3d4)
![image](https://github.com/user-attachments/assets/20931d94-535f-4edf-81d0-477c3720fbcf)
![image](https://github.com/user-attachments/assets/df2f8219-e1a6-49bc-af77-3a80395135a2)



