package net.raphdf201.shapez2generator

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
    val tag_name: String
)

suspend fun getShifterVersion(): String? = try {
    client.get(ghReleasesApiUrl) {
        header("User-Agent", "raphdf201/shapez2-generator")
    }.body<GithubRelease>().tag_name.removePrefix("v")
} catch (_: Exception) {
    null
}

suspend fun getWorkshopItems(): List<WorkshopItem>? = try {
    client.get("$backendUrl/v1/getItems").body<List<WorkshopItem>>()
} catch (_: Exception) {
    null
}
