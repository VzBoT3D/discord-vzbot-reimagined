package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.statix.Model

object PrinterMedias: IntIdTable("printer_medias") {

    val location = text("file_location")
    val printerProfile = reference("printer_profile_id", PrinterProfiles.id)
    
}

@Model
class PrinterMedia(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, PrinterMedia>(PrinterMedias)

    var location by PrinterMedias.location
    var printerProfile by PrinterProfile referencedOn PrinterMedias.printerProfile
}