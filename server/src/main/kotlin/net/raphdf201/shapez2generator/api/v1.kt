package net.raphdf201.shapez2generator.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.raphdf201.shapez2generator.SimpleWorkshopItem
import net.raphdf201.shapez2generator.cache.CachedWorkshopItem
import net.raphdf201.shapez2generator.cache.getWorkshopItem
import net.raphdf201.shapez2generator.cache.shouldUpdateSteamList
import net.raphdf201.shapez2generator.cache.updateSteamItemList
import net.raphdf201.shapez2generator.db
import net.raphdf201.shapez2generator.steam.IPublishedFileService

fun Application.v1Routes() {
    routing {
        route("/v1") {
            swaggerUI("openapi", "openapi/v1.yaml")
            route("/item") {
                get("/list") {
                    val newList = IPublishedFileService.getCache()
                    val simpleList = mutableListOf<SimpleWorkshopItem>()
                    val steamItemListTmp = mutableListOf<CachedWorkshopItem>()
                    newList.forEach {
                        simpleList.add(
                            SimpleWorkshopItem(
                                it.publishedFileId.toUInt(),
                                it.title,
                                it.tags.orEmpty()
                            )
                        )
                        steamItemListTmp.add(
                            CachedWorkshopItem(
                                it.publishedFileId.toUInt(),
                                it.title,
                                it.timeUpdated,
                                it.tags.orEmpty()
                            )
                        )
                    }
                    if (shouldUpdateSteamList()) updateSteamItemList(steamItemListTmp)
                    call.respond(simpleList)
                }
                get("/{id}") {
                    val id = call.pathParameters["id"]?.toUIntOrNull()
                        ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    try {
                        val item = db.read(id) ?: db.createAndGet(getWorkshopItem(id))
                        call.respond(item)
                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.NotFound, e.message ?: "Item not found")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, "Error : ${e.message}")
                    }
                }
            }
        }
    }
}
