package org.vzbot.plugins.routing

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.BlogAuthor
import org.vzbot.models.BlogPost
import org.vzbot.models.BlogPosts
import org.vzbot.models.generated.toModel

fun Route.blogs() {
    get("/blogs") {
        val blogs = transaction { BlogPost.all().orderBy(BlogPosts.createdAt to SortOrder.DESC).map { it.toModel() } }
        runBlocking { blogs.forEach { it.with { author() } } }

        call.respond(blogs)
    }

    get("/blogs/latest") {
        val latestBlog = transaction { BlogPost.find { BlogPosts.public eq true }.orderBy(BlogPosts.createdAt to SortOrder.DESC).first().toModel() }
        runBlocking { latestBlog.with { author() } }
        call.respond(latestBlog)
    }

    get("/blogs/{id}") {
        val blogID = call.request.pathVariables["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val blog = transaction { runBlocking {  BlogPost.findById(blogID)?.toModel()?.with { author() } } } ?: return@get call.respond(HttpStatusCode.NotFound)

        call.respond(blog)
    }

    get("/authors") {
        val authors = transaction { BlogAuthor.all().map { it.toModel() } }
        call.respond(authors)
    }

    get("/authors/{id}") {
        val authorID = call.request.pathVariables["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val author = transaction { runBlocking {  BlogAuthor.findById(authorID)?.toModel()?.with { blogs() } } } ?: return@get call.respond(HttpStatusCode.NotFound)

        call.respond(author)
    }
}