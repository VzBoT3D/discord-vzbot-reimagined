package org.vzbot.models

import net.dv8tion.jda.api.entities.Member
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.statix.HasOne
import org.statix.Model
import org.statix.ModelIgnore

object SerialTickets : IntIdTable("serial_tickets") {
    val ownerID = long("ticket_owner_id")
    val printer = reference("printer", Printers.id)
    val description = text("description")
    val mediaURL = varchar("media_url", 255)
    val country = enumeration("country", Country::class).nullable()

    val reviewedBy = long("reviewer_id").nullable().default(null)
    val accepted = bool("is_accepted").default(false)
    val discordChannel = long("discord_channel_id")
    val open = bool("is_open").default(true)
}

@Model
class SerialTicket(id: EntityID<Int>): Entity<Int>(id) {
    companion object : EntityClass<Int, SerialTicket>(SerialTickets) {

        fun hasOpenTicket(member: Member): Boolean {
            val memberID = member.idLong

            return transaction { find { (SerialTickets.ownerID eq memberID) and (SerialTickets.open eq true) }.firstOrNull() } != null
        }
    }

    @ModelIgnore
    var ownerID by SerialTickets.ownerID

    var description by SerialTickets.description
    var mediaURL by SerialTickets.mediaURL

    @ModelIgnore
    var reviewedBy by SerialTickets.reviewedBy
    @ModelIgnore
    var discordChannel by SerialTickets.discordChannel

    @HasOne
    var printer by Printer referencedOn SerialTickets.printer

    var accepted by SerialTickets.accepted
    @ModelIgnore
    var open by SerialTickets.open

    @ModelIgnore
    var country by SerialTickets.country
}