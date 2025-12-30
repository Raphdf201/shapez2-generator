package net.raphdf201.shapez2generator

import net.raphdf201.shapez2generator.fileBuilders.ManifestDependency

fun String.removeWhitespace() = this
    .replace(" ", "")
    .replace("\n", "")
    .replace("$", "")

fun getDefaultDependencies(shifterVersion: String) = listOf(
    ManifestDependency("steam:3542611357", "Shapez Shifter", ">=$shifterVersion")
)
