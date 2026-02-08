package net.raphdf201.shapez2generator.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.raphdf201.shapez2generator.DbWorkshopItem
import net.raphdf201.shapez2generator.Manifest
import net.raphdf201.shapez2generator.db
import net.raphdf201.shapez2generator.notStrictJson
import net.raphdf201.shapez2generator.removeWhitespace
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
        serialized.Title ?: title.removeWhitespace(),
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
                    "+login", steamUser,// steamPassword,
                    "+workshop_download_item", "2162800", id.toString(),
                    "+quit"
                ).redirectErrorStream(true).start()
            }

            val output = withContext(Dispatchers.IO) {
                val exitCode = process.waitFor()
                val result = process.inputStream.bufferedReader().use { it.readText() }

                // Ensure all streams are closed
                process.inputStream.close()
                process.outputStream.close()
                process.errorStream.close()

                exitCode to result
            }

            val (exitCode, outputText) = output

            if (exitCode != 0) {
                throw Exception("SteamCMD failed (exit $exitCode): $outputText")
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
