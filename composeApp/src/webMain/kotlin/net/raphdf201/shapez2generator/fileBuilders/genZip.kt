package net.raphdf201.shapez2generator.fileBuilders

import net.raphdf201.shapez2generator.Manifest
import net.raphdf201.shapez2generator.ManifestDependency
import net.raphdf201.shapez2generator.ModIds
import net.raphdf201.shapez2generator.npm.createZip
import net.raphdf201.shapez2generator.npm.createZipOptions
import net.raphdf201.shapez2generator.npm.saveAs

fun genAndDownloadZip(
    projectId: String,
    projectTitle: String,
    projectDescription: String,
    projectAuthor: String,
    gameVersionSupportRange: String,
    version: String,
    steamUsername: String,
    affectsSavegames: Boolean,
    disablesAchievements: Boolean,
    useNewSolutionFormat: Boolean,
    langVersion: Int,
    modDependencies: List<ManifestDependency>,
    assemblies: List<Assembly>,
    modAssemblies: List<Assembly>
) {
    val shapezShifterIncluded = modDependencies.any { it.modId.endsWith(ModIds.ShapezShifter) }
    val zip = createZip()
    zip.file(".gitignore", genGitignoreFile())
    zip.file("Main.cs", genMainFile(projectId,
        assemblies.any { it.name == "Core.dll" }, shapezShifterIncluded))
    zip.file(
        "manifest.json", genManifestFile(
            Manifest(
                version = version,
                title = projectTitle,
                description = projectDescription,
                author = projectAuthor,
                savedModVersionCompabilityRangeWithSelf = "0.*.*",
                gameVersionSupportRange = gameVersionSupportRange,
                affectsSaveGames = affectsSavegames,
                disablesAchievements = disablesAchievements,
                conflicts = emptyList(),
                iconPath = "",
                assemblies = listOf("$projectId.dll"),
                dependencies = modDependencies
            )
        )
    )
    zip.file("README.md", genReadme(projectTitle))
    zip.file("$projectId.csproj", genCsprojFile(projectId, langVersion, assemblies,
        modAssemblies, shapezShifterIncluded))
    zip.file(if (useNewSolutionFormat) "$projectId.slnx" else "$projectId.sln", genSolutionFile(projectId, useNewSolutionFormat))
    zip.file("Steam/base.vdf", genVdfFile(projectTitle, projectDescription))
    zip.file("Steam/SteamPublishLinux.sh", genLinuxSteamScript(steamUsername))
    zip.file("Steam/SteamPublishWindows.sh", genWindowsSteamScript(steamUsername))

    val blob = zip.generate(createZipOptions())

    saveAs(blob, "$projectId.zip")
}
