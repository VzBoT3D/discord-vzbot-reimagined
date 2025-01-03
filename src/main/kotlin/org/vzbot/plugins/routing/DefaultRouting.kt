package org.vzbot.plugins.routing

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.BlogPost
import org.vzbot.models.PrinterProfile

fun Route.default() {
    get("/status") {

        val hasBlog = transaction { BlogPost.count() > 0 }
        val hasPrinterProfiles = transaction { PrinterProfile.count() > 0 }

        if (!(hasBlog && hasPrinterProfiles)) {
            return@get call.respond(HttpStatusCode.ServiceUnavailable)
        }

        call.respondText { "OK" }
    }
}