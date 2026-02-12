package net.raphdf201.shapez2generator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import net.raphdf201.shapez2generator.fileBuilders.Assembly
import net.raphdf201.shapez2generator.views.ActionButtons
import net.raphdf201.shapez2generator.views.AssembliesSection
import net.raphdf201.shapez2generator.views.DependenciesSection
import net.raphdf201.shapez2generator.views.ProjectInfoSection

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
fun App() {
    val scope = rememberCoroutineScope()
    var projectId by remember { mutableStateOf("") }
    var projectTitle by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var projectAuthor by remember { mutableStateOf("") }
    var publishingSteamUsername by remember { mutableStateOf("") }
    var gameVersionSupportRange by remember { mutableStateOf("*") }
    var version by remember { mutableStateOf("0.0.1") }
    var shifterVersion by remember { mutableStateOf("0.10.0") }
    var affectsSavegames by remember { mutableStateOf(false) }
    var disablesAchievements by remember { mutableStateOf(false) }
    var useNewSolutionFormat by remember { mutableStateOf(false) }
    var langVersion by remember { mutableStateOf(12) }
    var modDependencies by remember { mutableStateOf(getDefaultDependencies(shifterVersion)) }
    var assemblies by remember { mutableStateOf(defaultAssemblies) }
    var modAssemblies by remember { mutableStateOf(listOf<Assembly>()) }
    var steamSimpleWorkshopItems by remember { mutableStateOf(listOf<SimpleWorkshopItem>()) }

    LaunchedEffect(Unit) {
        withTimeout(5000) {
            version = getShifterVersion() ?: version
        }
        launch {
            try {
                withTimeout(5000) { // 5 second timeout
                    steamSimpleWorkshopItems = getWorkshopItems() ?: steamSimpleWorkshopItems
                }
            } catch (_: TimeoutCancellationException) {
                println("Workshop items request timed out")
                steamSimpleWorkshopItems = emptyList()
            } catch (e: Exception) {
                println("Failed to load workshop items: ${e.message}")
                steamSimpleWorkshopItems = emptyList()
            }
        }
    }

    MaterialTheme {
        Box(Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 60.dp)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    ProjectInfoSection(
                        projectId = projectId,
                        projectTitle = projectTitle,
                        projectDescription = projectDescription,
                        projectAuthor = projectAuthor,
                        publishingSteamUsername = publishingSteamUsername,
                        gameVersionSupportRange = gameVersionSupportRange,
                        affectsSavegames = affectsSavegames,
                        disablesAchievements = disablesAchievements,
                        useNewSolutionFormat = useNewSolutionFormat,
                        onProjectIdChange = { projectId = it.removeWhitespace().removeNonAlpha() },
                        onProjectTitleChange = { projectTitle = it },
                        onProjectDescriptionChange = { projectDescription = it },
                        onProjectAuthorChange = {
                            if (projectAuthor == publishingSteamUsername) publishingSteamUsername = it
                            projectAuthor = it
                                                },
                        onPublishingSteamUsernameChange = { publishingSteamUsername = it },
                        onGameVersionChange = { gameVersionSupportRange = it },
                        onAffectsSavegamesChange = { affectsSavegames = it },
                        onDisablesAchievementsChange = { disablesAchievements = it },
                        onUseNewSolutionFormatChange = { useNewSolutionFormat = it },
                    )

                    DependenciesSection(
                        dependencies = modDependencies,
                        onDependenciesChange = { modDependencies = it },
                        steamDependencies = steamSimpleWorkshopItems,
                        onSteamDependencySelect = {
                            scope.launch {
                                val dep = (steamSimpleWorkshopItems[it].get() ?: return@launch).processExceptions()
                                val tmpAssemblies = mutableListOf<Assembly>()
                                dep.dlls.forEach { asmName ->
                                    tmpAssemblies.add(Assembly(asmName, true, false))
                                }
                                modAssemblies += tmpAssemblies
                                modDependencies += ManifestDependency(
                                    "steam:${dep.id}",
                                    dep.manifestName,
                                    "^${dep.latestVersion}"
                                )
                            }
                        }
                    )

                    AssembliesSection(
                        assemblies = assemblies,
                        onAssembliesChange = { assemblies = it }
                    )
                }
            }

            ActionButtons(
                Modifier.align(Alignment.BottomStart).padding(bottom = 10.dp),
                projectId,
                projectTitle,
                projectAuthor,
                projectDescription,
                gameVersionSupportRange,
                version,
                publishingSteamUsername,
                affectsSavegames,
                disablesAchievements,
                useNewSolutionFormat,
                langVersion,
                modDependencies,
                assemblies,
                modAssemblies
            )
        }
    }
}
