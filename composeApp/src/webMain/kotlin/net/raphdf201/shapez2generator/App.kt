package net.raphdf201.shapez2generator

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    var assemblies by remember { mutableStateOf(getDefaultAssemblies()) }

    LaunchedEffect(Unit) {
        version = getShifterVersion() ?: version
    }

    MaterialTheme {
        Surface(Modifier.fillMaxSize().padding(15.dp)) {
            Column(Modifier.fillMaxHeight()) {
                Row(Modifier.fillMaxSize().horizontalScroll(rememberScrollState()).weight(1f)) {
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
                        onProjectAuthorChange = { projectAuthor = it },
                        onPublishingSteamUsernameChange = { publishingSteamUsername = it },
                        onGameVersionChange = { gameVersionSupportRange = it },
                        onAffectsSavegamesChange = { affectsSavegames = it },
                        onDisablesAchievementsChange = { disablesAchievements = it },
                        onUseNewSolutionFormatChange = { useNewSolutionFormat = it },
                    )

                    Spacer(Modifier.width(10.dp))

                    DependenciesSection(
                        dependencies = modDependencies,
                        onDependenciesChange = { modDependencies = it }
                    )

                    Spacer(Modifier.width(10.dp))

                    AssembliesSection(
                        assemblies = assemblies,
                        onAssembliesChange = { assemblies = it }
                    )
                }

                ActionButtons(
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
}
