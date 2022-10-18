buildscript {
    extra.apply {
        set("compose_version", "1.3.0-rc02")
    }
    repositories {
        google()
    }
}

plugins {
    id("com.android.application") version "7.4.0-beta02" apply false
    id("com.android.library") version "7.4.0-beta02" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
}
repositories {
    google()
}