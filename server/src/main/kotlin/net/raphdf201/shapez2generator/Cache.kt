package net.raphdf201.shapez2generator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonArray
import net.raphdf201.shapez2generator.database.db
import java.io.File
import kotlin.time.Clock

var steamItemList = listOf<CachedWorkshopItem>()

suspend fun getItem(id: UInt, title: String): DbWorkshopItem {
    val steamItem = steamItemList.singleOrNull { it.id == id }
        ?: throw IllegalArgumentException("Workshop item $id not found")

    val cachedItem = db.read(id)

    // If we have a cached version and Steam hasn't updated since our last check
    if (cachedItem != null && steamItem.updateTime <= cachedItem.lastSteamUpdate) {
        return cachedItem
    }

    downloadItem(id)
    val manifest = File(workshopDownloadPath, id.toString()).resolve("manifest.json").readText()
    val serialized = notStrictJson.decodeFromString<Manifest>(manifest)
    val newItem = DbWorkshopItem(
        id,
        steamItemList.single { it.id == id }.updateTime,
        Clock.System.now().epochSeconds,
        serialized.Title ?: title,
        title,
        serialized.Assemblies,
        serialized.Version
    )
    db.update(newItem)
    return newItem
}

suspend fun downloadItem(id: UInt) {
    val process = withContext(Dispatchers.IO) {
        ProcessBuilder(
            "steamcmd",
            "+login", steamUser, steamPassword,
            "+workshop_download_item", "2162800", id.toString(),
            "+quit"
        ).start()
    }

    withContext(Dispatchers.IO) {
        process.waitFor()
    }

    if (process.exitValue() != 0) {
        throw Exception(process.errorStream.bufferedReader().readText())
    }
}

data class CachedWorkshopItem(
    val id: UInt,
    val title: String,
    val updateTime: Long,
)
