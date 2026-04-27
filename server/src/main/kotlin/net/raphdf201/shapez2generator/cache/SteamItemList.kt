package net.raphdf201.shapez2generator.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.raphdf201.shapez2generator.steam.IPublishedFileService
import kotlin.time.Clock

private val steamItemListMutex = Mutex()
private var steamItemList = listOf<CachedWorkshopItem>()

private var lastUpdate = 0L

suspend fun shouldUpdateSteamList(): Boolean {
    if (getSteamItemList().isEmpty()) return true
    if (Clock.System.now().epochSeconds - lastUpdate > 300) return true
    return false
}

suspend fun getSteamItemList(): List<CachedWorkshopItem> =
    steamItemListMutex.withLock { steamItemList }

suspend fun updateSteamItemList(newList: List<CachedWorkshopItem>) =
    steamItemListMutex.withLock { steamItemList = newList }

suspend fun updateSteamItemList() {
    val cache = IPublishedFileService.getCache()
    val newList = mutableListOf<CachedWorkshopItem>()
    cache?.forEach {
        val id = it.jsonObject["publishedfileid"]?.jsonPrimitive
        val title = it.jsonObject["title"]?.jsonPrimitive
        val updateTime = it.jsonObject["time_updated"]?.jsonPrimitive
        if (title != null && id != null && updateTime != null)
            newList.add(CachedWorkshopItem(
                id.content.toUInt(),
                title.content,
                updateTime.content.toLong()
            ))
    }
    updateSteamItemList(newList)
}
