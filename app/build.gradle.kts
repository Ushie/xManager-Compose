
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "dev.ushiekane.xmanager"
    compileSdk = 33

    defaultConfig {
        applicationId = "dev.ushiekane.xmanager"
        minSdk = 24
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
    composeOptions.kotlinCompilerExtensionVersion = "1.3.0-rc02"

}

dependencies {

    // AndroidX core
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.core:core-splashscreen:1.0.0")

    implementation("androidx.activity:activity-compose:1.6.0-rc02")

    val composeVersion = "1.3.0-alpha03"
    implementation("androidx.compose.ui:ui:${composeVersion}")
    debugImplementation("androidx.compose.ui:ui-tooling:${composeVersion}")
    implementation("androidx.compose.material3:material3:1.0.0-beta02")
    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")
}