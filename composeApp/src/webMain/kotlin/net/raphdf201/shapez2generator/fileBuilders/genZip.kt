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
    steamUsername: String,
    affectsSavegames: Boolean,
    disablesAchievements: Boolean,
    useNewSolutionFormat: Boolean,
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
    zip.file("README.md", genReadme(projectTitle))
    zip.file("$projectId.csproj", genCsprojFile(projectId, langVersion, assemblies, modDependencies[0].ModId == "steam:3542611357"))
    zip.file(if (useNewSolutionFormat) "$projectId.slnx" else "$projectId.sln", genSolutionFile(projectId, useNewSolutionFormat))
    zip.file("Steam/base.vdf", genVdfFile(projectTitle, projectDescription))
    zip.file("Steam/SteamPublishLinux.sh", genLinuxSteamScript(steamUsername))
    zip.file("Steam/SteamPublishWindows.sh", genWindowsSteamScript(steamUsername))

    val blob = zip.generate(createZipOptions())

    saveAs(blob, "$projectId.zip")
}
