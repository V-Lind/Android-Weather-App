package main.src.openmeteoweatherapp.ui

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import main.src.openmeteoweatherapp.model.ForecastViewModel
import main.src.openmeteoweatherapp.ui.screens.CurrentWeatherScreen
import main.src.openmeteoweatherapp.ui.screens.DetailedDailyForecastScreen
import main.src.openmeteoweatherapp.ui.screens.FavouritesScreen
import main.src.openmeteoweatherapp.ui.screens.SettingsScreen
import main.src.openmeteoweatherapp.ui.screens.SevenDayForecastScreen

// Navigation setup, routes + button icons
private sealed class Screen(
    val route: String,
    val name: String,
    val icon: ImageVector
) {
    data object CurrentWeather : Screen(
        "currentWeather",
        "Current weather",
        Icons.Default.Home
    )
    data object SevenDayForecast : Screen(
        "sevenDayForecast",
        "Seven day forecast",
        Icons.Default.Place //TODO: Get better icon
    )
    data object Favourites : Screen(
        "favourites",
        "Favourites",
        Icons.Default.Favorite
    )
    data object Settings : Screen(
        "settings",
        "Settings",
        Icons.Default.Settings
    )
    // Add mapper for route to name
    companion object {
        val routesToNames: Map<String, String> by lazy {
            mapOf(
                CurrentWeather.route to CurrentWeather.name,
                SevenDayForecast.route to SevenDayForecast.name,
                Favourites.route to Favourites.name,
                Settings.route to Settings.name
            )
        }
    }
}

@Composable
fun OpenmeteoWeatherApp(widthSizeClass: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Select layout based on screen size
    val isCompactScreen = widthSizeClass == WindowWidthSizeClass.Compact

    return if (isCompactScreen) {
        Scaffold (
            topBar = { CreateTopBar(navController) },
            bottomBar = { CreateBottomBar(navController) },
            content = { paddingValues -> CreateContent(navController, paddingValues) },
        )
    } else {
        //TODO: Add tablet layout
        ModalNavigationDrawer(
            drawerContent = {},
            drawerState = drawerState,
            content = {
                Row {
                //TODO: Add tablet content
                }
            }
        )
    }
}

@Composable
private fun CreateContent(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel = remember { ForecastViewModel(application) }

    NavHost(
        navController = navController,
        startDestination = Screen.CurrentWeather.route,
        modifier = Modifier.padding(innerPadding),
        builder = {
            composable(Screen.CurrentWeather.route) { CurrentWeatherScreen(viewModel) }
            composable(Screen.SevenDayForecast.route) { SevenDayForecastScreen(navController, viewModel) }
            composable(Screen.Favourites.route) { FavouritesScreen() }
            composable(Screen.Settings.route) { SettingsScreen(viewModel) }

            // Pass id as placeholder solution until database has a view for getting a specific day
            composable("DetailedDailyForecastScreen/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")!!
                DetailedDailyForecastScreen(id, viewModel)
            }
        }
    )
}

@Composable
private fun CreateBottomBar(navController: NavController) {
    BottomNavigation(
        modifier = Modifier.height(80.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val bottomNavDestinations = listOf(
            Screen.CurrentWeather,
            Screen.SevenDayForecast,
            Screen.Favourites
        )

        bottomNavDestinations.forEach { screen ->
            val isSelected = currentRoute == screen.route
            Log.d("bottomnav", "Current route: $currentRoute, screen route: ${screen.route}")
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = {
                    Text(
                        text = screen.name,
                        textAlign = TextAlign.Center
                    )
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTopBar(navController: NavController) {
    var currentLocation by remember { mutableStateOf("") }

    // Update topbar title when destination changes
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentLocation = Screen.routesToNames[destination.route] ?: ""
        }
    }

    TopAppBar(
        title = { Text(currentLocation) },
        actions = {
            IconButton(
                onClick = { navController.navigate(Screen.Settings.route) },
                content = { Icon(Icons.Filled.Settings, "Settings button") }
            )
        }
    )
}