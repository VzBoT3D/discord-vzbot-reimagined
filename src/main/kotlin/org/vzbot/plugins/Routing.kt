package org.vzbot.plugins

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.SerialNumber
import org.vzbot.models.generated.toModel
import java.nio.file.Files
import java.nio.file.Paths

fun Application.configureRouting() {

    val client = HttpClient(CIO) {
        expectSuccess = true
        install(DefaultRequest) {
            url("https://restcountries.com/v3.1/")
        }
        install(HttpCache) {
            val cacheFile = Files.createDirectories(Paths.get("build/cache")).toFile()
            publicStorage(FileStorage(cacheFile))
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    routing {
        get("/") {
            call.respondText { "OK" }
        }

        get("/serials") {
            val serials = transaction { SerialNumber.all().map { it.toModel() } }
            serials.forEach {
                it.with { printer() }
                it.attributes["location"] = Json.encodeToJsonElement(it.country?.getLocation(client))
            }

            call.respond(serials)
        }
    }
}
