package org.vzbot.plugins.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*
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

    get("/printers/profiles") {
        val profiles = transaction { PrinterProfile.all().map { it.toModel() } }
        call.respond(profiles)
    }
}