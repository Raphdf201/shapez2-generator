package net.raphdf201.shapez2generator

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import net.raphdf201.shapez2generator.steam.IPublishedFileService
import java.io.File

fun Application.routing() {
    routing {
        get("/items") {
            val resp = client.get(IPublishedFileService.url) {
                url {
                    parameters.append("key", config[0])
                    parameters.append("input_json", Json.encodeToString(IPublishedFileService.query))
                }
            }.bodyAsText()
            call.respondText(prettifyJson(resp), ContentType.Application.Json)
        }
        get("/dl/{file}") {
            val publishedFileId = call.pathParameters["file"]

            val process = withContext(Dispatchers.IO) {
                ProcessBuilder(
                    "steamcmd",
                    "+login", "anonymous",
                    "+workshop_download_item", "2162800", publishedFileId,
                    "+quit"
                ).start()
            }

            withContext(Dispatchers.IO) {
                process.waitFor()
            }

            val manifest = File(System.getProperty("user.home"),
                ".steam/steam/steamapps/workshop/content/2162800/$publishedFileId/manifest.json")

            call.respondFile(manifest)
        }
    }
}
