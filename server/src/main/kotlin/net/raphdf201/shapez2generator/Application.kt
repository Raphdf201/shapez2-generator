package net.raphdf201.shapez2generator

import io.ktor.client.HttpClient
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.ExperimentalSerializationApi
import net.raphdf201.shapez2generator.api.v1Routes
import net.raphdf201.shapez2generator.database.database
import java.io.File

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    this.plugins()
    database()
    this.routing()
    this.v1Routes()
}

val client = HttpClient {
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
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
val steamPassword = config[6]
