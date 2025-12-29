package net.raphdf201.shapez2generator

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.fileBuilders.Assembly
import net.raphdf201.shapez2generator.fileBuilders.ManifestDependency
import net.raphdf201.shapez2generator.fileBuilders.genAndDownloadCsproj
import net.raphdf201.shapez2generator.fileBuilders.genAndDownloadZip

@Composable
fun CheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked, onCheckedChange, modifier, enabled, colors, interactionSource)
        content()
    }
}

@Composable
fun ProjectInfoSection(
    projectId: String,
    projectTitle: String,
    projectDescription: String,
    projectAuthor: String,
    publishingSteamUsername: String,
    gameVersionSupportRange: String,
    affectsSavegames: Boolean,
    disablesAchievements: Boolean,
    useNewSolutionFormat: Boolean,
    onProjectIdChange: (String) -> Unit,
    onProjectTitleChange: (String) -> Unit,
    onProjectDescriptionChange: (String) -> Unit,
    onProjectAuthorChange: (String) -> Unit,
    onPublishingSteamUsernameChange: (String) -> Unit,
    onGameVersionChange: (String) -> Unit,
    onAffectsSavegamesChange: (Boolean) -> Unit,
    onDisablesAchievementsChange: (Boolean) -> Unit,
    onUseNewSolutionFormatChange: (Boolean) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        TextField(
            value = projectId,
            onValueChange = onProjectIdChange,
            label = { Text("Mod Id") },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))

        TextField(
            value = projectTitle,
            onValueChange = onProjectTitleChange,
            label = { Text("Mod name") },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))

        TextField(
            value = projectDescription,
            onValueChange = onProjectDescriptionChange,
            label = { Text("Mod description") }
        )
        Spacer(Modifier.height(10.dp))

        TextField(
            value = projectAuthor,
            onValueChange = onProjectAuthorChange,
            label = { Text("Mod author") },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))

        TextField(
            value = publishingSteamUsername,
            onValueChange = onPublishingSteamUsernameChange,
            label = { Text("Publisher steam username") },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))

        TextField(
            value = gameVersionSupportRange,
            onValueChange = onGameVersionChange,
            label = { Text("Game version support range") },
            singleLine = true
        )
        Spacer(Modifier.height(10.dp))

        CheckBox(
            checked = affectsSavegames,
            onCheckedChange = onAffectsSavegamesChange
        ) {
            Text("Affects savegames")
        }

        CheckBox(
            checked = disablesAchievements,
            onCheckedChange = onDisablesAchievementsChange
        ) {
            Text("Disables achievements")
        }

        CheckBox(
            checked = useNewSolutionFormat,
            onCheckedChange = onUseNewSolutionFormatChange
        ) {
            Text("Use new solution format (slnx)")
        }
    }
}

@Composable
fun DependenciesSection(
    dependencies: List<ManifestDependency>,
    onDependenciesChange: (List<ManifestDependency>) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Text("Dependencies")

        dependencies.forEachIndexed { i, dep ->
            DependencyCard(
                dependency = dep,
                onDependencyChange = { newDep ->
                    onDependenciesChange(
                        dependencies.toMutableList().apply { this[i] = newDep }
                    )
                }
            )
            Spacer(Modifier.height(10.dp))
        }

        DependencyButtons(
            onAdd = { onDependenciesChange(dependencies + ManifestDependency("", "", "")) },
            onRemove = {
                if (dependencies.isNotEmpty()) {
                    onDependenciesChange(dependencies.dropLast(1))
                }
            }
        )
    }
}

@Composable
fun DependencyCard(
    dependency: ManifestDependency,
    onDependencyChange: (ManifestDependency) -> Unit
) {
    OutlinedCard {
        TextField(
            value = dependency.ModId,
            onValueChange = { onDependencyChange(ManifestDependency(it, dependency.ModTitle, dependency.Version)) },
            label = { Text("Mod Id") },
            singleLine = true
        )

        TextField(
            value = dependency.ModTitle,
            onValueChange = { onDependencyChange(ManifestDependency(dependency.ModId, it, dependency.Version)) },
            label = { Text("Mod Title") },
            singleLine = true
        )

        TextField(
            value = dependency.Version,
            onValueChange = { onDependencyChange(ManifestDependency(dependency.ModId, dependency.ModTitle, it)) },
            label = { Text("Version") },
            singleLine = true
        )
    }
}

@Composable
fun DependencyButtons(
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(Modifier, Arrangement.SpaceEvenly) {
        Button(onClick = onAdd) {
            Text("+")
        }
        Spacer(Modifier.width(10.dp))
        Button(onClick = onRemove) {
            Text("-")
        }
    }
}

@Composable
fun AssembliesSection(
    assemblies: List<Assembly>,
    onAssembliesChange: (List<Assembly>) -> Unit
) {
    LazyColumn {
        itemsIndexed(assemblies) { i, asm ->
            AssemblyCard(
                assembly = asm,
                onAssemblyChange = { newAsm ->
                    onAssembliesChange(
                        assemblies.toMutableList().apply { this[i] = newAsm }
                    )
                }
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun AssemblyCard(
    assembly: Assembly,
    onAssemblyChange: (Assembly) -> Unit
) {
    OutlinedCard(Modifier.width(550.dp)) {
        Column(Modifier.padding(10.dp)) {
            Text(assembly.name)

            CheckBox(
                checked = assembly.included,
                onCheckedChange = { onAssemblyChange(Assembly(assembly.name, it, assembly.publicized)) }
            ) {
                Text("Enable")
            }

            CheckBox(
                checked = assembly.publicized,
                onCheckedChange = { onAssemblyChange(Assembly(assembly.name, assembly.included, it)) }
            ) {
                Text("Publicized")
            }
        }
    }
}

@Composable
fun ActionButtons(
    projectId: String,
    projectTitle: String,
    projectAuthor: String,
    projectDescription: String,
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
    Row {
        Button(
            onClick = {
                genAndDownloadZip(
                    projectId.trim(),
                    projectTitle.trim(),
                    projectDescription.trim(),
                    projectAuthor.trim(),
                    gameVersionSupportRange.trim(),
                    version.trim(),
                    steamUsername,
                    affectsSavegames,
                    disablesAchievements,
                    useNewSolutionFormat,
                    langVersion,
                    modDependencies,
                    assemblies
                )
            },
            enabled = projectId.isNotBlank() && projectTitle.isNotBlank() && projectAuthor.isNotBlank()
        ) {
            Text("Download project")
            Icon(Icons.Default.Download, null)
        }

        Spacer(Modifier.width(10.dp))

        Button(
            onClick = {
                genAndDownloadCsproj(
                    projectId.trim(),
                    langVersion,
                    modDependencies,
                    assemblies
                )
            }
        ) {
            Text("Download .csproj only")
            Icon(Icons.Default.Download, null)
        }
    }
}
