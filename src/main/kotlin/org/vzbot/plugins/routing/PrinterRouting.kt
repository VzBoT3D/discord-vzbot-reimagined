package org.vzbot.plugins.routing

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.models.*
import org.vzbot.models.generated.toModel

fun Route.printers() {
    get("/printers/stats") {

        val printers = transaction { Printer.all().map { it.toModel() } }
        val printerMap = mutableMapOf<String, Long>()


        for (printer in printers) {
            val dbPrinter = transaction { Printer.find { Printers.name eq printer.name }.first() }
            printerMap[printer.name] = transaction { SerialNumber.count(SerialNumbers.printer eq dbPrinter.id) }
        }

        call.respond(printerMap)
    }

    get("/printer/{name}") {
        val printerName = call.request.pathVariables["name"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val printer = transaction { Printer.all().firstOrNull { it.name.uppercase() == printerName.uppercase() }?.toModel() } ?: return@get call.respond(
            HttpStatusCode.NotFound)

        runBlocking {
            printer.with {
                profile {
                    medias()
                }
            }
        }

        call.respond(printer)
    }

    get("/printers/profiles") {
        val profiles = transaction { PrinterProfile.all().map { it.toModel() } }
        runBlocking {
            profiles.forEach {
                it.with {
                    printer()
                    medias()
                }
            }
        }
        call.respond(profiles)
    }
}