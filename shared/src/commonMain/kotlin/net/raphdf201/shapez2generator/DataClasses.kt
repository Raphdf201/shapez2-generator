package net.raphdf201.shapez2generator

import kotlinx.serialization.Serializable

@Serializable
data class SimpleWorkshopItem(
    val id: UInt,
    val steamName: String
)

@Serializable
data class SharedWorkshopItem(
    val id: UInt,
    val manifestName: String,
    val steamName: String,
    val dlls: List<String>,
    val latestVersion: String,
    )

@Serializable
data class DbWorkshopItem(
    val id: UInt,
    val lastSteamUpdate: Long,
    val lastLocalUpdate: Long,
    val manifestName: String,
    val steamName: String,
    val dlls: List<String>,
    val latestVersion: String,
)

@Serializable
data class Manifest(
    val Version: String,
    val Title: String? = null,
    val Description: String? = null,
    val Author: String? = null,
    val SavedModVersionCompabilityRangeWithSelf: String? = null,
    val GameVersionSupportRange: String? = null,
    val AffectsSaveGames: Boolean? = null,
    val DisablesAchievements: Boolean? = null,
    val Conflicts: List<String>? = null,
    val IconPath: String? = null,
    val Assemblies: List<String>,
    val Dependencies: List<ManifestDependency>? = null
)

@Serializable
data class ManifestDependency(
    val ModId: String,
    val ModTitle: String,
    val Version: String
)
