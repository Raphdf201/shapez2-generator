package net.raphdf201.shapez2generator

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import net.raphdf201.shapez2generator.fileBuilders.Assembly
import net.raphdf201.shapez2generator.fileBuilders.ManifestDependency

fun String.removeWhitespace() = this.replace(" ", "")

fun getDefaultDependencies(shifterVersion: String) = listOf(
    ManifestDependency("steam:3542611357", "Shapez Shifter", ">=$shifterVersion")
)

fun getDefaultAssemblies() = listOf(
    Assembly("Core.dll", true, false),
    Assembly("Core.Localization.dll", true, false),
    Assembly("SPZGameAssembly.dll", true, false),
    Assembly("Game.Core.dll", true, false),
    Assembly("Game.Core.Coordinates.dll", true, false),
    Assembly("Game.Core.Editor.MeshBaker.dll", false, false),
    Assembly("Game.Core.Effects.dll", false, false),
    Assembly("Game.Core.Effects.Editor.MonoBehaviours.dll", false, false),
    Assembly("Game.Core.Map.dll", false, false),
    Assembly("Game.Core.Map.Layout.dll", false, false),
    Assembly("Game.Core.Map.Layout.Model.dll", false, false),
    Assembly("Game.Core.Map.Model.dll", true, false),
    Assembly("Game.Core.Map.Simulation.dll", true, false),
    Assembly("Game.Core.Modding.dll", true, false),
    Assembly("Game.Core.Rendering.dll", true, true),
    Assembly("Game.Core.Rendering.ShaderProfiling.dll", false, false),
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

val prettyJson = Json {
    prettyPrint = true
}

val notStrictJson = Json {
    ignoreUnknownKeys = true
}

val client = HttpClient {
    install(ContentNegotiation) {
        json(notStrictJson)
    }
}
