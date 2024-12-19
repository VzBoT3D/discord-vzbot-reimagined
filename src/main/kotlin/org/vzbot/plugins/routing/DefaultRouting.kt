package org.vzbot.plugins.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.default() {
    get("/") {
        call.respondText { "OK" }
    }
}