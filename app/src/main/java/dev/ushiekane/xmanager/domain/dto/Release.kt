package dev.ushiekane.xmanager.domain.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface SpotifyRelease {
    val version: String
    val arch: String
    val downloadUrl: String
}

@Serializable
class Release(
    @SerialName("Regular_Latest") val latest: String,
    @SerialName("Amoled_Latest") val amoledLatest: String,
    @SerialName("RC_Latest") val latestCloned: String,
    @SerialName("ABC_Latest") val amoledLatestCloned: String,
    @SerialName("App_Changelogs") val appChangelogs: String,
    @SerialName("Regular") val releases: List<NormalReleases>,
    @SerialName("Amoled") val amoledReleases: List<AmoledReleases>,
    @SerialName("Regular_Cloned") val clonedReleases: List<NormalClonedReleases>,
    @SerialName("Amoled_Cloned") val amoledClonedReleases: List<AmoledClonedReleases>,
    @SerialName("Mod_Changelogs") val modChangelogs: List<Changelogs>,
) {
    @Serializable
    data class NormalReleases(
        @SerialName("Version") override val version: String,
        @SerialName("Arch") override val arch: String,
        @SerialName("Link") override val downloadUrl: String
    ): SpotifyRelease
    @Serializable
    data class AmoledReleases(
        @SerialName("Version") override val version: String,
        @SerialName("Arch") override val arch: String,
        @SerialName("Link") override val downloadUrl: String
    ): SpotifyRelease
    @Serializable
    data class NormalClonedReleases(
        @SerialName("Version") override val version: String,
        @SerialName("Arch") override val arch: String,
        @SerialName("Link") override val downloadUrl: String
    ): SpotifyRelease
    @Serializable
    data class AmoledClonedReleases(
        @SerialName("Version") override val version: String,
        @SerialName("Arch") override val arch: String,
        @SerialName("Link") override val downloadUrl: String
    ): SpotifyRelease
    @Serializable
    data class Changelogs(
        @SerialName("Mod_Changelogs") val changelog: String,
    )
}