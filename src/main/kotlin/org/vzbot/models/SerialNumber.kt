package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.statix.HasOne
import org.statix.Model

object SerialNumbers : IntIdTable("serial_numbers") {
    val memberID = long("discord_member_id")
    val description = text("description")
    val mediaURL = text("media_url")
    val country = enumeration("country", Country::class).nullable()
    val printer = reference("printer", Printers).nullable()
    val serialID = long("serial_id").uniqueIndex()
    val longitude = double("loc_longitude").nullable()
    val latitude = double("loc_latitude").nullable()
    
    val createdAt = datetime("date").defaultExpression(CurrentDateTime)
}

@Model
class SerialNumber(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, SerialNumber>(SerialNumbers)

    var memberID by SerialNumbers.memberID
    var description by SerialNumbers.description
    var mediaURL by SerialNumbers.mediaURL
    var country by SerialNumbers.country
    var serialID by SerialNumbers.serialID

    var longitude by SerialNumbers.longitude
    var latitude by SerialNumbers.latitude

    @HasOne
    var printer by Printer optionalReferencedOn SerialNumbers.printer

    var createdAt by SerialNumbers.createdAt
}