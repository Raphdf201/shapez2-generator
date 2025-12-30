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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.views.ActionButtons
import net.raphdf201.shapez2generator.views.AssembliesSection
import net.raphdf201.shapez2generator.views.DependenciesSection
import net.raphdf201.shapez2generator.views.ProjectInfoSection

@Composable
fun App() {
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

    LaunchedEffect(Unit) {
        version = getShifterVersion() ?: version
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                        onProjectIdChange = { projectId = it.removeWhitespace() },
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
                        onDependenciesChange = { modDependencies = it }
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
                assemblies
            )
        }
    }
}
