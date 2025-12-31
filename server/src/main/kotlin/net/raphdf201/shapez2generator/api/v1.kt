package net.raphdf201.shapez2generator.api

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.raphdf201.shapez2generator.SimpleWorkshopItem
import net.raphdf201.shapez2generator.WorkshopItem
import net.raphdf201.shapez2generator.client
import net.raphdf201.shapez2generator.config
import net.raphdf201.shapez2generator.database.db
import net.raphdf201.shapez2generator.getItem
import net.raphdf201.shapez2generator.steam.IPublishedFileService

fun Application.v1Routes() {
    routing {
        route("/v1") {
            route("/item") {
                get("/list") {
                    val resp = Json.parseToJsonElement(client.get(IPublishedFileService.url) {
                        url {
                            parameters.append("key", config[0])
                            parameters.append("input_json", Json.encodeToString(IPublishedFileService.query))
                        }
                    }.bodyAsText()).jsonObject["response"]?.jsonObject["publishedfiledetails"]?.jsonArray
                    val list = mutableListOf<SimpleWorkshopItem>()
                    resp?.forEach {
                        val id = it.jsonObject["publishedfileid"]?.jsonPrimitive
                        val title = it.jsonObject["title"]?.jsonPrimitive
                        if (title != null && id != null) list.add(
                            SimpleWorkshopItem(
                                id.content.toUInt(),
                                title.content
                            )
                        )
                    }
                    call.respond(list.toList())
                }
                get("/{id}") {
                    val id = call.pathParameters["id"]?.toUInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val item = db.read(id) ?: db.createAndGet(getItem(id))
                    call.respond(item)
                }
            }
        }
    }
}
