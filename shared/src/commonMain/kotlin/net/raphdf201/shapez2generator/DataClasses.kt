package net.raphdf201.shapez2generator

import kotlinx.serialization.Serializable

@Serializable
data class SimpleWorkshopItem(
    val id: UInt,
    val steamName: String
)

@Serializable
data class WorkshopItem(
    val id: UInt,
    val title: String,
    val dllName: String,
    val latestVersion: String,
)
