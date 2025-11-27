package net.raphdf201.shapez2generator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.fileBuilders.ManifestDependency

@Composable
fun App() {
    var dark by remember { mutableStateOf(false) }
    var projectId by remember { mutableStateOf("") }
    var projectTitle by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var projectAuthor by remember { mutableStateOf("") }
    var gameVersionSupportRange by remember { mutableStateOf("*") }
    var version by remember { mutableStateOf("0.0.1") }
    var affectsSavegames by remember { mutableStateOf(false) }
    var disablesAchievements by remember { mutableStateOf(false) }
    var modDependencies by remember {
        mutableStateOf(
            listOf(
                ManifestDependency(
                    "steam:3542611357",
                    "Shapez Shifter",
                    ">=0.9.1"
                )
            )
        )
    }
    val textColor = if (dark) Color.White else Color.Black

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = if (dark) grey else Color.White) {
            Column(Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxSize().weight(1f), Arrangement.SpaceEvenly) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Spacer(Modifier.height(10.dp))
                        TextField(projectId, { projectId = it }, label = {
                            Text("Mod Id", Modifier, textColor)
                        }, singleLine = true)
                        Spacer(Modifier.height(10.dp))
                        TextField(projectTitle, {
                            projectTitle = it
                            projectId = it.removeWhitespace()
                        }, label = {
                            Text("Mod name", Modifier, textColor)
                        }, singleLine = true)
                        Spacer(Modifier.height(10.dp))
                        TextField(projectDescription, { projectDescription = it }, label = {
                            Text("Mod description", Modifier, textColor)
                        })
                        Spacer(Modifier.height(10.dp))
                        TextField(projectAuthor, { projectAuthor = it }, label = {
                            Text("Mod author", Modifier, textColor)
                        }, singleLine = true)
                        Spacer(Modifier.height(10.dp))
                        TextField(
                            gameVersionSupportRange,
                            { gameVersionSupportRange = it },
                            label = {
                                Text("Game version support range", Modifier, textColor)
                            },
                            singleLine = true
                        )
                        Spacer(Modifier.height(10.dp))
                        CheckBox(affectsSavegames, { affectsSavegames = it }) {
                            Text("Affects savegames", Modifier, textColor)
                        }
                        CheckBox(disablesAchievements, { disablesAchievements = it }) {
                            Text("Disables achievements", Modifier, textColor)
                        }
                    }
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Text("Dependencies", Modifier, textColor)
                        modDependencies.forEachIndexed { index, dep ->
                            Spacer(Modifier.height(10.dp))
                            OutlinedCard {
                                TextField(dep.ModId, { newValue ->
                                    modDependencies = modDependencies.toMutableList().apply {
                                        this[index] =
                                            ManifestDependency(newValue, dep.ModTitle, dep.Version)
                                    }
                                }, label = {
                                    Text("Mod Id", Modifier, textColor)
                                }, singleLine = true)
                                TextField(dep.ModTitle, { newValue ->
                                    modDependencies = modDependencies.toMutableList().apply {
                                        this[index] =
                                            ManifestDependency(dep.ModId, newValue, dep.Version)
                                    }
                                }, label = {
                                    Text("Mod Title", Modifier, textColor)
                                }, singleLine = true)
                                TextField(dep.Version, { newValue ->
                                    modDependencies = modDependencies.toMutableList().apply {
                                        this[index] =
                                            ManifestDependency(dep.ModId, dep.ModTitle, newValue)
                                    }
                                }, label = {
                                    Text("Version", Modifier, textColor)
                                }, singleLine = true)
                            }
                        }
                        Row(Modifier, Arrangement.SpaceEvenly) {
                            Button({
                                modDependencies = modDependencies + ManifestDependency("", "", "")
                            }) {
                                Text("+", Modifier, textColor)
                            }
                            Spacer(Modifier.width(10.dp))
                            Button({
                                if (modDependencies.isNotEmpty()) {
                                    modDependencies = modDependencies.dropLast(1)
                                }
                            }) {
                                Text("-", Modifier, textColor)
                            }
                        }
                    }
                }
                Button({
                    genAndDownload(
                        projectId = projectId.trim(),
                        projectTitle = projectTitle.trim(),
                        projectDescription = projectDescription.trim(),
                        projectAuthor = projectAuthor.trim(),
                        gameVersionSupportRange = gameVersionSupportRange.trim(),
                        affectsSavegames = affectsSavegames,
                        disablesAchievements = disablesAchievements,
                        version = version.trim(),
                        modDependencies = modDependencies
                    )
                }, Modifier) {
                    Text("Download", Modifier, textColor)
                    Icon(Icons.Default.Download, null)
                }
            }
        }
    }
}
