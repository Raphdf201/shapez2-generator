package net.raphdf201.shapez2generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleWorkshopItem(
    val id: UInt,
    val steamName: String
)

@Serializable
data class SharedWorkshopItem(
    val id: UInt,
    val steamName: String,
    val dlls: List<String>,
    val latestVersion: String,
    )

@Serializable
data class DbWorkshopItem(
    val id: UInt,
    val lastSteamUpdate: Long,
    val lastLocalUpdate: Long,
    val steamName: String,
    val dlls: List<String>,
    val latestVersion: String,
)

@Serializable
data class Manifest(
    @SerialName("Version")
    val version: String,
    @SerialName("Title")
    val title: String? = null,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Author")
    val author: String? = null,
    @SerialName("SavedModVersionCompabilityRangeWithSelf")
    val savedModVersionCompabilityRangeWithSelf: String? = null,
    @SerialName("GameVersionSupportRange")
    val gameVersionSupportRange: String? = null,
    @SerialName("AffectsSaveGames")
    val affectsSaveGames: Boolean? = null,
    @SerialName("DisablesAchievements")
    val disablesAchievements: Boolean? = null,
    @SerialName("Conflicts")
    val conflicts: List<String>? = null,
    @SerialName("IconPath")
    val iconPath: String? = null,
    @SerialName("Assemblies")
    val assemblies: List<String>,
    @SerialName("Dependencies")
    val dependencies: List<ManifestDependency>? = null
)

@Serializable
data class ManifestDependency(
    @SerialName("ModId")
    val modId: String,
    @SerialName("ModTitle")
    val modTitle: String,
    @SerialName("Version")
    val version: String
)
