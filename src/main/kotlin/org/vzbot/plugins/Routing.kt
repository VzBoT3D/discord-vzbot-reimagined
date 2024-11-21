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
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.Country
import org.vzbot.models.SerialNumber
import org.vzbot.models.SerialNumbers
import org.vzbot.models.generated.toModel
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
        get("/") {
            call.respondText { "OK" }
        }

        get("/serials") {
            val serials = transaction { SerialNumber.all().map { it.toModel() } }
            serials.forEach {
                it.with { printer() }
                it.attributes["latitude"] = JsonPrimitive(it.latitude)
                it.attributes["longitude"] = JsonPrimitive(it.longitude)
            }

            call.respond(serials)
        }

        get("/serial/{country}") {
            val country = call.request.pathVariables["country"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val resolvedCountry = Country.getCountry(country)
            val serial = transaction { SerialNumber.find { SerialNumbers.country eq resolvedCountry }.firstOrNull() }

            if (serial == null) return@get call.respondText("No serail found", status = HttpStatusCode.NotFound)
            val location = serial.country!!.getLocation(geoClient) ?: return@get call.respondText("Country not found", status = HttpStatusCode.NotFound)
            val latLng = serial.country!!.randomPointInPolygon(location.random())

            println(latLng)

            call.respond(Json.encodeToJsonElement(latLng))
        }
    }
}
