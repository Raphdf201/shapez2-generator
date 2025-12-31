package net.raphdf201.shapez2generator.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.raphdf201.shapez2generator.DbWorkshopItem
import net.raphdf201.shapez2generator.Manifest
import net.raphdf201.shapez2generator.database.db
import net.raphdf201.shapez2generator.notStrictJson
import net.raphdf201.shapez2generator.steamPassword
import net.raphdf201.shapez2generator.steamUser
import net.raphdf201.shapez2generator.workshopDownloadPath
import java.io.File
import kotlin.time.Clock

suspend fun getWorkshopItem(id: UInt, title: String): DbWorkshopItem {
    val steamItem = getSteamItemList().singleOrNull { it.id == id }
        ?: throw IllegalArgumentException("Workshop item $id not found")

    val cachedItem = db.read(id)

    // If we have a cached version and Steam hasn't updated since our last check
    if (cachedItem != null && steamItem.updateTime <= cachedItem.lastSteamUpdate) {
        return cachedItem
    }

    downloadItem(id)

    val manifestFile = File(workshopDownloadPath, id.toString()).resolve("manifest.json")
    if (!manifestFile.exists()) {
        throw IllegalStateException("Manifest file not found for workshop item $id after download")
    }
    val manifest = manifestFile.readText()
    val serialized = notStrictJson.decodeFromString<Manifest>(manifest)
    val newItem = DbWorkshopItem(
        id,
        steamItem.updateTime,
        Clock.System.now().epochSeconds,
        serialized.Title ?: title,
        title,
        serialized.Assemblies,
        serialized.Version
    )
    db.update(newItem)
    return newItem
}

private suspend fun downloadItem(id: UInt) {
    repeat(3) { attempt ->
        try {
            val process = withContext(Dispatchers.IO) {
                ProcessBuilder(
                    "steamcmd",
                    "+login", steamUser, steamPassword,
                    "+workshop_download_item", "2162800", id.toString(),
                    "+quit"
                ).redirectErrorStream(true).start()
            }

            val exitCode = withContext(Dispatchers.IO) {
                process.waitFor()
            }

            if (exitCode != 0) {
                val output = withContext(Dispatchers.IO) {
                    process.inputStream.bufferedReader().readText()
                }
                throw Exception("SteamCMD failed (exit $exitCode): $output")
            }
            return
        } catch (e: Exception) {
            if (attempt == 2) throw e
            delay(1000L * (attempt + 1)) // Exponential backoff
        }
    }
}

data class CachedWorkshopItem(
    val id: UInt,
    val title: String,
    val updateTime: Long,
)
