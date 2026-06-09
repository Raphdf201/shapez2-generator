package net.raphdf201.shapez2generator

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.raphdf201.shapez2generator.api.v1Routes
import java.util.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"))

    install(ContentNegotiation) {
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
