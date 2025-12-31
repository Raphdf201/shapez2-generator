package net.raphdf201.shapez2generator

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import net.raphdf201.shapez2generator.steam.IPublishedFileService

fun Application.routing() {
    routing {
        get("/items") {
            val resp = client.get(IPublishedFileService.url) {
                url {
                    parameters.append("key", apikey)
                    parameters.append("input_json", Json.encodeToString(IPublishedFileService.query))
                }
            }.bodyAsText()
            call.respondText(prettifyJson(resp), ContentType.Application.Json)
        }
    }
}
