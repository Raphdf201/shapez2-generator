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
} catch (e: Exception) {
    println(e.message)
    null
}

suspend fun getWorkshopItems(): List<SimpleWorkshopItem>? = try {
    client.get("$backendUrl/v1/item/list").body<List<SimpleWorkshopItem>>()
} catch (e: Exception) {
    println(e.message)
    null
}

suspend fun SimpleWorkshopItem.get(): SharedWorkshopItem? = try {
    val name = this.steamName
    client.get("$backendUrl/v1/item/${this.id}") {
        url {
            parameters.append("name", name)
        }
    }.body<SharedWorkshopItem>()
} catch (e: Exception) {
    println(e.message)
    null
}
