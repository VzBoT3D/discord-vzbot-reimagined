package org.vzbot.discord.restrictions

import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import org.vzbot.discord.commands.fetchSerialTicket
import org.vzbot.io.prettyEmbed
import java.awt.Color

class TicketRestrictions {

    fun validTicket(member: ActionSender): Boolean {
        val ticket = member.fetchSerialTicket()
        if (ticket == null) {
            member.respondEmbed(prettyEmbed("Error", "There was an error fetching your ticket", Color.RED).build())
            return false
        }
        return true
    }

}