pluginManagement {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        maven("https://jitpack.io")
        mavenCentral()
    }
}
rootProject.name = "XManager"
include(":app")
