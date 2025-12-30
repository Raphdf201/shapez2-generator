package net.raphdf201.shapez2generator.api

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.v1Routes() {
    routing {
        route("/v1/") {
            get("/getItems") {

            }
        }
    }
}
