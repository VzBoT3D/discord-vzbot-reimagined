package org.vzbot.plugins

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.*
import org.vzbot.models.generated.toModel
import org.vzbot.plugins.routing.blogs
import org.vzbot.plugins.routing.default
import org.vzbot.plugins.routing.printers
import org.vzbot.plugins.routing.serials
import java.nio.file.Files
import java.nio.file.Paths

val geoClient = HttpClient(CIO) {
    expectSuccess = true
    install(HttpTimeout) {
        requestTimeoutMillis = 120_000
    }
    install(HttpCache) {
        val cacheFile = Files.createDirectories(Paths.get("cache")).toFile()
        publicStorage(FileStorage(cacheFile))
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}


fun Application.configureRouting() {
    routing {
        default()
        serials()
        printers()
        blogs()
    }
}
