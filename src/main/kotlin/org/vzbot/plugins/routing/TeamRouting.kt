package org.vzbot.plugins.routing

import com.zellerfeld.zellerbotapi.ZellerBot
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.vzbot.io.EnvVariables
import org.vzbot.io.TeamLoader
import org.vzbot.io.env
import org.vzbot.models.author
import org.vzbot.models.generated.BlogAuthorModelDTO
import org.vzbot.models.generated.toModel

fun Routing.teamRouting() {

    get("/team") {
        if (!ZellerBot.isBotRunning()) {
            call.respond(HttpStatusCode.ServiceUnavailable)
        }

        val members = TeamLoader.getTeamMembers()
        call.respond(members)
    }
}