package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.statix.Model

object Printers: IntIdTable("printers") {
    val name = varchar("printer_name", 255).uniqueIndex()
}

@Model
class Printer(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, Printer>(Printers) {
        fun hasPrinter(name: String): Boolean {
            return transaction { find { Printers.name eq name }.firstOrNull() } != null
        }
    }

    var name by Printers.name
}