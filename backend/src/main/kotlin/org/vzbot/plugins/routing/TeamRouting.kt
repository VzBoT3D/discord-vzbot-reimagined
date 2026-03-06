package org.vzbot.plugins.routing

import com.ktbot.api.KtBot
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.vzbot.io.TeamLoader

fun Routing.teamRouting() {

    get("/team") {
        if (!KtBot.isBotRunning()) {
            call.respond(HttpStatusCode.ServiceUnavailable)
        }

        val members = TeamLoader.getTeamMembers()
        call.respond(members)
    }
}