package net.raphdf201.shapez2generator

fun getDefaultDependencies(shifterVersion: String) = listOf(
    ManifestDependency("steam:${ModIds.ShapezShifter}", "Shapez Shifter", ">=$shifterVersion")
)
