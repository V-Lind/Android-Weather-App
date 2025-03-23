package main.src.openmeteoweatherapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main.src.openmeteoweatherapp.model.ForecastViewModel
import main.src.openmeteoweatherapp.model.data.Settings

@Composable
fun SettingsScreen(viewModel: ForecastViewModel) {
    val settings by viewModel.settings.collectAsState()
    var newSettings by remember { mutableStateOf<Settings?>(null) }

    LaunchedEffect(Unit) { viewModel.getSettings() }
    LaunchedEffect(settings) { newSettings = settings }

    Column(
        modifier = Modifier.padding(16.dp),
        content = {

            Text("Temperature Unit: ${newSettings?.temperatureUnit}")
            Switch(
                checked = newSettings?.temperatureUnit == "celsius",
                onCheckedChange = { isChecked ->
                    newSettings = newSettings?.copy(
                        temperatureUnit = if (isChecked) "celsius" else "fahrenheit"
                    )
                    newSettings?.let { viewModel.storeSettings(it) }
                }
            )

            Text("Wind Speed Unit: ${newSettings?.windSpeedUnit}")
            Switch(
                checked = newSettings?.windSpeedUnit == "m/s",
                onCheckedChange = { isChecked ->
                    newSettings = newSettings?.copy(
                        windSpeedUnit = if (isChecked) "m/s" else "mph"
                    )
                    newSettings?.let { viewModel.storeSettings(it) }
                }
            )

            Text("Precipitation Unit: ${newSettings?.precipitationUnit}")
            Switch(
                checked = newSettings?.precipitationUnit == "mm",
                onCheckedChange = { isChecked ->
                    newSettings = newSettings?.copy(
                        precipitationUnit = if (isChecked) "mm" else "in"
                    )
                    newSettings?.let { viewModel.storeSettings(it) }
                }
            )

            Text(if (newSettings?.lightMode == true) "Light Mode" else "Dark Mode")
            Switch(
                checked = newSettings?.lightMode ?: false,
                onCheckedChange = { isChecked ->
                    newSettings = newSettings?.copy(
                        lightMode = isChecked
                    )
                    newSettings?.let { viewModel.storeSettings(it) }
                }
            )
        }
    )
}

