import com.android.build.gradle.tasks.MergeSourceSetFolders
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization").version("1.9.23")
    id("com.google.devtools.ksp").version("1.9.23-1.0.20")
}

android {
    namespace = "main.src.openmeteoweatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "main.src.openmeteoweatherapp"
        minSdk = 34
        targetSdk = 34
        versionCode = 4
        versionName = "0.9.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.material)
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("io.ktor:ktor-client-core:2.3.10")
    implementation("io.ktor:ktor-client-cio:2.3.10")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")
    implementation(libs.androidx.room.runtime)
    implementation(libs.play.services.location)
    implementation(libs.kotlin.coroutines.play)

    // OpenStreetMap for location select
    implementation("org.osmdroid:osmdroid-android:6.1.11")

    // Animated weather icons
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // Swipe down gesture
    implementation("com.google.accompanist:accompanist-swiperefresh:0.20.0")

    // Vico graphs
    implementation("com.patrykandpatrick.vico:compose:2.0.0-alpha.20")
    implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.20")
    implementation("com.patrykandpatrick.vico:core:2.0.0-alpha.20")
    implementation("com.patrykandpatrick.vico:views:2.0.0-alpha.20")

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}