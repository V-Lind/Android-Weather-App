package main.src.openmeteoweatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.content.ContextCompat
import main.src.openmeteoweatherapp.model.ForecastViewModel
import main.src.openmeteoweatherapp.ui.OpenmeteoWeatherApp
import main.src.openmeteoweatherapp.ui.theme.OpenmeteoWeatherAppTheme


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun launchApp() {

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            OpenmeteoWeatherAppTheme {
                OpenmeteoWeatherApp(widthSizeClass)
            }
        }
    }

    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val permissionResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                launchApp()
            } else {
                Toast.makeText(
                    this,
                    "Permissions denied. This App cannot run without them. Try again",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ForecastViewModel(application)
        viewModel.deleteDatabase() //TODO: Remove after testing

        if (!hasPermissions()) {
            permissionResultLauncher.launch(permissions)
        } else {
            launchApp()
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
                ) {
                return false
            }
        }
        return true
    }

}




