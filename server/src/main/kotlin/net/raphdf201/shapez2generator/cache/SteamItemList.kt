package net.raphdf201.shapez2generator.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

suspend fun updateSteamItemList() =
    updateSteamItemList(IPublishedFileService.getCache().map {
        CachedWorkshopItem(
            it.publishedFileId.toUInt(),
            it.title,
            it.timeUpdated
        )
    })
