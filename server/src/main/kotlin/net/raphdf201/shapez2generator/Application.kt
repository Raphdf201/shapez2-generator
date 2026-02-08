package net.raphdf201.shapez2generator

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import net.raphdf201.shapez2generator.api.v1Routes
import java.io.File

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ServerContentNegotiation) {
        json()
    }
    install(XForwardedHeaders)
    database()
    this.v1Routes()
    routing {
        get("/") {
            call.respondRedirect("https://shapez2.raphdf201.net", true)
        }
    }
}

val client = HttpClient {
    install(ClientContentNegotiation) {
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
