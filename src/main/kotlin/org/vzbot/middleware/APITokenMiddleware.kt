package org.vzbot.middleware

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.APIToken
import org.vzbot.models.APITokens
import statix.org.Middleware
import statix.org.MiddlewareData

class APITokenMiddleware: Middleware {

    override suspend fun handleCall(call: ApplicationCall, receives: MiddlewareData?): MiddlewareData {
        val auth = call.request.header("token") ?: run {
            call.respondText("This route requires authorization", status = HttpStatusCode.Forbidden)
            return MiddlewareData.empty()
        }

        val isAPITokenValid = transaction {
            APIToken.find { APITokens.token eq auth }.firstOrNull() != null
        }

        if (!isAPITokenValid) {
            call.respondText("This route requires authorization", status = HttpStatusCode.Forbidden)
            return MiddlewareData.empty()
        }

        return MiddlewareData.empty()
    }
}