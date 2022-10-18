package app.revanced.manager.dto.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        @SerialName("Title") val name: String,
        @SerialName("Link") val downloadUrl: String
    )
    @Serializable
    data class AmoledReleases(
        @SerialName("Title") val name: String,
        @SerialName("Link") val downloadUrl: String
    )
    @Serializable
    data class NormalClonedReleases(
        @SerialName("Title") val name: String,
        @SerialName("Link") val downloadUrl: String
    )
    @Serializable
    data class AmoledClonedReleases(
        @SerialName("Title") val name: String,
        @SerialName("Link") val downloadUrl: String
    )
    @Serializable
    data class Changelogs(
        @SerialName("Mod_Changelogs") val changelog: String,
    )
}