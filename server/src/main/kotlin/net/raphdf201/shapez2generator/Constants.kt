package net.raphdf201.shapez2generator

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File

const val SERVER_PORT = 7000

val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

private val config = File("config").readLines()
val apikey = config[0]
val dbUrl = config[1]
val dbUser = config[2]
val dbPassword = config[3]
val workshopDownloadPath = config[4]
val steamUser = config[5]
val steamCmdPath = try {
    val it = config[6]
    if (it.isBlank() || it.isEmpty()) throw Exception("no steamcmd path")
    else it
} catch (e: Exception) {
    println("Error : ${e.message}")
    println("using default \"steamcmd\" path")
    "steamcmd"
}

val prettyJson = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
}

val notStrictJson = Json {
    ignoreUnknownKeys = true
}
