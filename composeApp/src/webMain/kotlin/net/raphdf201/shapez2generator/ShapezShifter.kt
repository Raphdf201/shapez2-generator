package net.raphdf201.shapez2generator

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.serialization.Serializable

const val ghReleasesApiLink = "https://api.github.com/repos/tobspr-games/shapez2-shifter/releases/latest"

@Serializable
data class GithubRelease(
    val tag_name: String
)

suspend fun getShifterVersion(): String? = try {
    client.get(ghReleasesApiLink) {
        header("User-Agent", "raphdf201/shapez2-generator")
    }.body<GithubRelease>().tag_name.removePrefix("v")
} catch (_: Exception) {
    null
}
