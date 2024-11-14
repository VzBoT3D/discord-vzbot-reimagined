package org.vzbot.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.statix.Model

object SerialNumbers : IntIdTable() {
    val memberID = long("discord_member_id")
    val description = text("description")
    val mediaURL = varchar("media_url", 255)
    val country = varchar("country", 200)
    val date = datetime("date").defaultExpression(CurrentDateTime)
}

@Model
class SerialNumber(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, SerialNumber>(SerialNumbers)

    var memberID by SerialNumbers.memberID
    var description by SerialNumbers.description
    var mediaURL by SerialNumbers.mediaURL
    var country by SerialNumbers.country
}