package net.raphdf201.shapez2generator.fileBuilders

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

fun genManifestFile(config: Manifest): String {
    return Json.encodeToString(config)
}

@Serializable
data class Manifest(
    val Version: String,
    val Title: String,
    val Description: String,
    val Author: String,
    val SavedModVersionCompabilityRangeWithSelf: String,
    val GameVersionSupportRange: String,
    val AffectsSaveGames: Boolean,
    val Conflicts: List<String>,
    val IconPath: String,
    val Assemblies: List<String>,
    val Dependencies: List<ManifestDependency>
)

@Serializable
data class ManifestDependency(
    val ModId: String,
    val ModTitle: String,
    val Version: String
)
