package net.raphdf201.shapez2generator

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import net.raphdf201.shapez2generator.fileBuilders.Assembly
import net.raphdf201.shapez2generator.fileBuilders.ManifestDependency

@Composable
fun App() {
    var projectId by remember { mutableStateOf("") }
    var projectTitle by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var projectAuthor by remember { mutableStateOf("") }
    var gameVersionSupportRange by remember { mutableStateOf("*") }
    var version by remember { mutableStateOf("0.0.1") }
    var affectsSavegames by remember { mutableStateOf(false) }
    var disablesAchievements by remember { mutableStateOf(false) }
    var langVersion by remember { mutableStateOf(12) }
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
    var assemblies by remember {
        mutableStateOf(
            listOf(
                Assembly("Core.dll", true, false),
                Assembly("Core.Localization.dll", true, false),
                Assembly("SPZGameAssembly.dll", true, false),
                Assembly("Game.Core.dll", true, false),
                Assembly("Game.Core.Coordinates.dll", true, false),
                Assembly("Game.Core.Editor.MeshBaker.dll", false, false),
                Assembly("Game.Core.Effects.dll", false, false),
                Assembly("Game.Core.Effects.Editor.MonoBehaviours.dll", false, false),
                Assembly("Game.Core.Map.dll", true, false),
                Assembly("Game.Core.Map.Layout.dll", false, false),
                Assembly("Game.Core.Map.Layout.Model.dll", false, false),
                Assembly("Game.Core.Map.Model.dll", true, false),
                Assembly("Game.Core.Map.Simulation.dll", true, false),
                Assembly("Game.Core.Modding.dll", true, false),
                Assembly("Game.Core.Rendering.dll", true, true),
                Assembly("Game.Core.Rendering.ShaderProfiling.dll", false, true),
                Assembly("Game.Core.Simulation.dll", true, false),
                Assembly("Game.Achievements.dll", false, false),
                Assembly("Game.Achievements.Platform.dll", false, false),
                Assembly("Game.Achievements.Representation.dll", false, false),
                Assembly("Game.Content.dll", true, false),
                Assembly("Game.Content.Achievements.dll", false, false),
                Assembly("Game.Content.Editor.ArtWorkflow.GameObjectRepresentation.dll", false, false),
                Assembly("Game.Content.Editor.MeshBaker.dll", false, false),
                Assembly("Game.Content.Features.dll", true, false),
                Assembly("Game.Hud.View.dll", false, false),
                Assembly("Game.Hud.View.Model.dll", false, false),
                Assembly("Game.Interaction.dll", false, false),
                Assembly("Game.Modding.dll", false, false),
                Assembly("Game.Observation.dll", false, false),
                Assembly("Game.Orchestration.dll", true, false),
                Assembly("Game.Orchestration.Achievements.dll", false, false),
                Assembly("UnityEngine.CoreModule.dll", true, false),
                )
        )
    }

    MaterialTheme {
        Surface(Modifier.fillMaxSize().padding(15.dp)) {
            Column(Modifier.fillMaxHeight()) {
                Row(Modifier.fillMaxSize().horizontalScroll(rememberScrollState()).weight(1f)) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        TextField(projectId, { projectId = it }, label = {
                            Text("Mod Id")
                        }, singleLine = true)
                        Spacer(Modifier.height(10.dp))
                        TextField(projectTitle, {
                            projectTitle = it
                            projectId = it.removeWhitespace()
                        }, label = {
                            Text("Mod name")
                        }, singleLine = true)
                        Spacer(Modifier.height(10.dp))
                        TextField(projectDescription, { projectDescription = it }, label = {
                            Text("Mod description")
                        })
                        Spacer(Modifier.height(10.dp))
                        TextField(projectAuthor, { projectAuthor = it }, label = {
                            Text("Mod author")
                        }, singleLine = true)
                        Spacer(Modifier.height(10.dp))
                        TextField(
                            gameVersionSupportRange,
                            { gameVersionSupportRange = it },
                            label = {
                                Text("Game version support range")
                            },
                            singleLine = true
                        )
                        Spacer(Modifier.height(10.dp))
                        CheckBox(affectsSavegames, { affectsSavegames = it }) {
                            Text("Affects savegames")
                        }
                        CheckBox(disablesAchievements, { disablesAchievements = it }) {
                            Text("Disables achievements")
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Text("Dependencies")
                        modDependencies.forEachIndexed { i, dep ->
                            OutlinedCard {
                                TextField(dep.ModId, { newValue ->
                                    modDependencies = modDependencies.toMutableList().apply {
                                        this[i] =
                                            ManifestDependency(newValue, dep.ModTitle, dep.Version)
                                    }
                                }, label = {
                                    Text("Mod Id")
                                }, singleLine = true)
                                TextField(dep.ModTitle, { newValue ->
                                    modDependencies = modDependencies.toMutableList().apply {
                                        this[i] =
                                            ManifestDependency(dep.ModId, newValue, dep.Version)
                                    }
                                }, label = {
                                    Text("Mod Title")
                                }, singleLine = true)
                                TextField(dep.Version, { newValue ->
                                    modDependencies = modDependencies.toMutableList().apply {
                                        this[i] =
                                            ManifestDependency(dep.ModId, dep.ModTitle, newValue)
                                    }
                                }, label = {
                                    Text("Version")
                                }, singleLine = true)
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                        Row(Modifier, Arrangement.SpaceEvenly) {
                            Button({
                                modDependencies = modDependencies + ManifestDependency("", "", "")
                            }) {
                                Text("+")
                            }
                            Spacer(Modifier.width(10.dp))
                            Button({
                                if (modDependencies.isNotEmpty()) {
                                    modDependencies = modDependencies.dropLast(1)
                                }
                            }) {
                                Text("-")
                            }
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    LazyColumn {
                        itemsIndexed(assemblies) { i, asm ->
                            OutlinedCard(Modifier.width(550.dp)) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(asm.name)
                                    CheckBox(asm.included, {
                                        assemblies.toMutableList().apply {
                                            this[i] = Assembly(asm.name, it, asm.publicized)
                                        }
                                    }) {
                                        Text("Enable")
                                    }
                                    CheckBox(asm.publicized, {
                                        assemblies.toMutableList().apply {
                                            this[i] = Assembly(asm.name, asm.included, it)
                                        }
                                    }) {
                                        Text("Publicized")
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
                Button({
                    genAndDownload(projectId.trim(), projectTitle.trim(), projectDescription.trim(),
                        projectAuthor.trim(), gameVersionSupportRange.trim(), version.trim(),
                        affectsSavegames, disablesAchievements, langVersion, modDependencies, assemblies)
                }) {
                    Text("Download")
                    Icon(Icons.Default.Download, null)
                }
            }
        }
    }
}
