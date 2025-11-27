package net.raphdf201.shapez2generator

import net.raphdf201.shapez2generator.fileBuilders.Manifest
import net.raphdf201.shapez2generator.fileBuilders.ManifestDependency
import net.raphdf201.shapez2generator.fileBuilders.genCsprojFile
import net.raphdf201.shapez2generator.fileBuilders.genGitignoreFile
import net.raphdf201.shapez2generator.fileBuilders.genMainFile
import net.raphdf201.shapez2generator.fileBuilders.genManifestFile
import net.raphdf201.shapez2generator.fileBuilders.genSolutionFile
import net.raphdf201.shapez2generator.fileBuilders.genVdfFile
import net.raphdf201.shapez2generator.fileBuilders.getTranslationsFile
import net.raphdf201.shapez2generator.fileBuilders.steamScriptLinux
import net.raphdf201.shapez2generator.fileBuilders.steamScriptWindows
import net.raphdf201.shapez2generator.npm.JSZip
import net.raphdf201.shapez2generator.npm.saveAs

fun genAndDownload(
    projectId: String,
    projectTitle: String,
    projectDescription: String,
    projectAuthor: String,
    gameVersionSupportRange: String,
    affectsSavegames: Boolean,
    modDependencies: List<ManifestDependency>
) {
    val zip = JSZip()

    zip.file(".gitignore", genGitignoreFile())
    zip.file("Main.cs", genMainFile(projectId))
    zip.file("manifest.json", genManifestFile(Manifest(
        Version = "0.0.1",
        Title = projectTitle,
        Description = projectDescription,
        Author = projectAuthor,
        SavedModVersionCompabilityRangeWithSelf = "0.*.*",
        GameVersionSupportRange = gameVersionSupportRange,
        AffectsSaveGames = affectsSavegames,
        Conflicts = emptyList(),
        IconPath = "Resources/icon.png",
        Assemblies = listOf("$projectId.dll"),
        Dependencies = modDependencies
    )))
    zip.file("translations.json", getTranslationsFile())
    zip.file("$projectId.csproj", genCsprojFile())
    zip.file("$projectId.sln", genSolutionFile(projectId))
    zip.file("Steam/base.vdf", genVdfFile(projectTitle, projectDescription))
    zip.file("Steam/SteamPublishLinux.sh", steamScriptLinux)
    zip.file("Steam/SteamPublishWindows.sh", steamScriptWindows)

    val blob = zip.generateAsync(
        js("({ type: 'blob', compression: 'DEFLATE' })")
    ).await()

    saveAs(blob, "$projectId.zip")
}
