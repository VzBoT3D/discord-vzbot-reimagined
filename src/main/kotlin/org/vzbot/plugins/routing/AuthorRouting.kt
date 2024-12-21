package org.vzbot.plugins.routing

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.BlogAuthor
import org.vzbot.models.generated.toModel

fun Route.authors() {
    get("/author/{id}") {
        val authorID = call.request.pathVariables["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val author = transaction { runBlocking { BlogAuthor.findById(authorID)?.toModel()?.with { posts() } } } ?: return@get call.respond(HttpStatusCode.NotFound)

        call.respond(author)
    }

    get("/authors") {
        val authors = transaction { BlogAuthor.all().map { it.toModel() } }
        call.respond(authors)
    }
}