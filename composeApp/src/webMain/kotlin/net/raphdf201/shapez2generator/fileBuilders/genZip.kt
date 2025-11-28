package net.raphdf201.shapez2generator.fileBuilders

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
    affectsSavegames: Boolean,
    disablesAchievements: Boolean,
    langVersion: Int,
    modDependencies: List<ManifestDependency>,
    assemblies: List<Assembly>
) {

    val zip = createZip()
    zip.file(".gitignore", genGitignoreFile())
    zip.file("Main.cs", genMainFile(projectId))
    zip.file(
        "manifest.json", genManifestFile(
            Manifest(
                Version = version,
                Title = projectTitle,
                Description = projectDescription,
                Author = projectAuthor,
                SavedModVersionCompabilityRangeWithSelf = "0.*.*",
                GameVersionSupportRange = gameVersionSupportRange,
                AffectsSaveGames = affectsSavegames,
                DisablesAchievements = disablesAchievements,
                Conflicts = emptyList(),
                IconPath = "",
                Assemblies = listOf("$projectId.dll"),
                Dependencies = modDependencies
            )
        )
    )
    zip.file("translations.json", getTranslationsFile())
    zip.file("$projectId.csproj", genCsprojFile(projectId, langVersion, assemblies, modDependencies[0].ModTitle == "ShapezShifter"))
    zip.file("$projectId.sln", genSolutionFile(projectId))
    zip.file("Steam/base.vdf", genVdfFile(projectTitle, projectDescription))
    zip.file("Steam/SteamPublishLinux.sh", steamScriptLinux)
    zip.file("Steam/SteamPublishWindows.sh", steamScriptWindows)

    val blob = zip.generate(createZipOptions())

    saveAs(blob, "$projectId.zip")
}
