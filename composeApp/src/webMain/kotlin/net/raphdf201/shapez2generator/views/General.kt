package net.raphdf201.shapez2generator.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.ManifestDependency
import net.raphdf201.shapez2generator.fileBuilders.Assembly
import net.raphdf201.shapez2generator.fileBuilders.genAndDownloadCsproj
import net.raphdf201.shapez2generator.fileBuilders.genAndDownloadZip
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

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
    Column {
        Text("Info")
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
fun ActionButtons(
    modifier: Modifier,
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
    Row(modifier) {
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

@Composable
fun IconToggle(
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    icons: List<DrawableResource>,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    iconTint: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(unselectedColor),
    ) {
        icons.forEachIndexed { i, icon ->
            Box(
                Modifier
                    .weight(1f)
                    .background(
                        if (selectedIndex == i) selectedColor else Color.Transparent
                    )
                    .clickable { onSelectionChanged(i) }
                    .padding(1.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Option ${i + 1}",
                    tint = if (selectedIndex == i) {
                        Color.White
                    } else {
                        iconTint.copy(alpha = 0.6f)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
