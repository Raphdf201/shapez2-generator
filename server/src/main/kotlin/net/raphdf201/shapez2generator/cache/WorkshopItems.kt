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
import kotlin.time.Duration.Companion.milliseconds

suspend fun getWorkshopItem(id: UInt): DbWorkshopItem {
    var steamItem = getSteamItemList().singleOrNull { it.id == id }
    if (steamItem == null) {
        updateSteamItemList()
        steamItem = getSteamItemList().singleOrNull { it.id == id }
    }
    if (steamItem == null) throw IllegalArgumentException("Workshop item $id not found")

    val cachedItem = db.read(id)

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
        steamItem.title,
        serialized.Assemblies,
        serialized.Version
    )
    db.update(newItem)
    return newItem
}

private suspend fun downloadItem(id: UInt) {
    println("Running \"steamcmd +login $steamUser +workshop_download_item 2162800 $id +quit\"")
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
            println(outputText)
            return
        } catch (e: Exception) {
            if (attempt == 2) throw e
            delay((1000L * (attempt + 1)).milliseconds) // Exponential backoff
        }
    }
}

data class CachedWorkshopItem(
    val id: UInt,
    val title: String,
    val updateTime: Long,
)
