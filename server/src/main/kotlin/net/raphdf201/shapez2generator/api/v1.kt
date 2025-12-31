package net.raphdf201.shapez2generator.api

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.raphdf201.shapez2generator.CachedWorkshopItem
import net.raphdf201.shapez2generator.SimpleWorkshopItem
import net.raphdf201.shapez2generator.apikey
import net.raphdf201.shapez2generator.client
import net.raphdf201.shapez2generator.database.db
import net.raphdf201.shapez2generator.getItem
import net.raphdf201.shapez2generator.prettyJson
import net.raphdf201.shapez2generator.steam.IPublishedFileService
import net.raphdf201.shapez2generator.steamItemList

fun Application.v1Routes() {
    routing {
        route("/v1") {
            route("/item") {
                get("/list") {
                    val newList = Json.parseToJsonElement(client.get(IPublishedFileService.url) {
                        url {
                            parameters.append("key", apikey)
                            parameters.append("input_json", Json.encodeToString(IPublishedFileService.query))
                        }
                    }.bodyAsText()).jsonObject["response"]?.jsonObject["publishedfiledetails"]?.jsonArray
                    val simpleList = mutableListOf<SimpleWorkshopItem>()
                    val steamItemListTmp = mutableListOf<CachedWorkshopItem>()
                    newList?.forEach {
                        val id = it.jsonObject["publishedfileid"]?.jsonPrimitive
                        val title = it.jsonObject["title"]?.jsonPrimitive
                        val updateTime = it.jsonObject["time_updated"]?.jsonPrimitive
                        if (title != null && id != null && updateTime != null) {
                            simpleList.add(
                                SimpleWorkshopItem(
                                    id.content.toUInt(),
                                    title.content
                                )
                            )
                            steamItemListTmp.add(CachedWorkshopItem(
                                id.content.toUInt(),
                                title.content,
                                updateTime.content.toLong()
                            ))
                        }
                    }
                    steamItemList = steamItemListTmp
                    call.respond(simpleList.toList())
                }
                get("/{id}") {
                    val id = call.pathParameters["id"]?.toUInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val title = call.queryParameters["name"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                    val item = db.read(id) ?: db.createAndGet(getItem(id, title))
                    call.respondText(prettyJson.encodeToString(item), ContentType.Application.Json)
                }
            }
        }
    }
}
