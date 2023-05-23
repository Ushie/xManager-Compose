plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.8.21"
    id("kotlin-parcelize")
}

android {
    namespace = "dev.ushiekane.xmanager"
    compileSdk = 33

    defaultConfig {
        applicationId = "dev.ushiekane.xmanager"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1"

        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.4.7"

}

dependencies {
    // AndroidX core
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.activity:activity-compose:1.5.1")

    val composeVersion = "1.4.0-alpha03"
    implementation("androidx.compose.ui:ui:${composeVersion}")
    implementation("androidx.compose.material3:material3:1.2.0-alpha01")
    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")

    // Accompanist
    val accompanistVersion = "0.28.0"
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

    // Navigation
    implementation("dev.olshevski.navigation:reimagined:1.3.1")

    // Koin
    val koinVersion = "3.3.2"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:3.4.1")

    // Ktor
    val ktorVersion = "2.2.2"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")
    implementation("androidx.core:core-splashscreen:1.0.1")
}