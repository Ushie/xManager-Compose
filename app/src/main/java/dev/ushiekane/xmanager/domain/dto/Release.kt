package dev.ushiekane.xmanager.domain.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Release {
    val version: String
    val downloadUrl: String
    val downloadUrl2: String
    val downloadUrl3: String
}

interface AmoledRelease: Release

interface StockRelease: Release

interface ClonedRelease: Release

interface ExperimentalRelease: Release

@Serializable
class Response(
    @SerialName("App_Changelogs") val appChangelogs: String,
    @SerialName("Stock_Patched") val stockReleases: List<Stock>,
    @SerialName("Amoled_Patched") val amoledReleases: List<Amoled>,
    @SerialName("Stock_Cloned_Patched") val clonedReleases: List<StockCloned>,
    @SerialName("Amoled_Cloned_Patched") val amoledClonedReleases: List<AmoledCloned>,
    @SerialName("Stock_Experimental_Patched") val experimentalReleases: List<StockExperimental>,
    @SerialName("Amoled_Experimental_Patched") val amoledExperimentalReleases: List<AmoledExperimental>,
    @SerialName("Stock_Experimental_Cloned_Patched") val clonedExperimentalReleases: List<StockClonedExperimental>,
    @SerialName("Amoled_Experimental_Cloned_Patched") val amoledClonedExperimentalReleases: List<AmoledClonedExperimental>,
    @SerialName("Lite_Patched") val liteReleases: List<Lite>,
    @SerialName("Patched_Changelogs") val changelogs: List<Changelogs>,
)

@Serializable
data class Stock(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): StockRelease
@Serializable
data class Amoled(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): AmoledRelease
@Serializable
data class StockCloned(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): StockRelease, ClonedRelease
@Serializable
data class AmoledCloned(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): AmoledRelease, ClonedRelease
@Serializable
data class StockExperimental(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): StockRelease, ExperimentalRelease
@Serializable
data class AmoledExperimental(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): AmoledRelease, ExperimentalRelease

@Serializable
data class StockClonedExperimental(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): StockRelease, ClonedRelease, ExperimentalRelease
@Serializable
data class AmoledClonedExperimental(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): AmoledRelease, ClonedRelease, ExperimentalRelease
@Serializable
data class Lite(
    @SerialName("Title") override val version: String,
    @SerialName("Link_1") override val downloadUrl: String,
    @SerialName("Link_2") override val downloadUrl2: String,
    @SerialName("Link_3") override val downloadUrl3: String
): Release
@Serializable
data class Changelogs(
    @SerialName("Patched_Changelogs") val changelog: String,
)