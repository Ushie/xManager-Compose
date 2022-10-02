buildscript {
    extra.apply {
        set("compose_version", "1.3.0-rc02")
    }
    repositories {
        google()
    }
}

plugins {
    id("com.android.application") version "7.4.0-alpha10" apply false
    id("com.android.library") version "7.4.0-alpha10" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
}
repositories {
    google()
}