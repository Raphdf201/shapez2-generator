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
import net.raphdf201.shapez2generator.cache.CachedWorkshopItem
import net.raphdf201.shapez2generator.SimpleWorkshopItem
import net.raphdf201.shapez2generator.apikey
import net.raphdf201.shapez2generator.client
import net.raphdf201.shapez2generator.db
import net.raphdf201.shapez2generator.cache.getWorkshopItem
import net.raphdf201.shapez2generator.cache.shouldUpdateSteamList
import net.raphdf201.shapez2generator.steam.IPublishedFileService
import net.raphdf201.shapez2generator.cache.updateSteamItemList

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
                    if (shouldUpdateSteamList()) updateSteamItemList(steamItemListTmp)
                    call.respond(simpleList)
                }
                get("/{id}") {
                    val id = call.pathParameters["id"]?.toUIntOrNull()
                        ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    val title = call.queryParameters["name"]
                        ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing title")
                    try {
                        val item = db.read(id) ?: db.createAndGet(getWorkshopItem(id, title))
                        call.respond(item)
                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.NotFound, e.message ?: "Item not found")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to fetch : ${e.message}")
                    }
                }
            }
        }
    }
}
