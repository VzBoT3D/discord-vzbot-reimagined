package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.statix.BelongsTo
import org.statix.HasMany
import org.statix.Model

object PrinterProfiles: IntIdTable("printer_profiles") {

    val description = text("printer_description")
    val kitURL = text("kit_url").nullable()
    val learnMoreURL = text("learn_more_url")
    val printer = reference("printer_id", Printers)

}

@Model
class PrinterProfile(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, PrinterProfile>(PrinterProfiles)

    var description by PrinterProfiles.description
    var kitURL by PrinterProfiles.kitURL
    var learnMoreURL by PrinterProfiles.learnMoreURL

    @BelongsTo
    var printer by Printer referencedOn PrinterProfiles.printer

    @HasMany
    val medias by PrinterMedia referrersOn PrinterMedias.printerProfile
}