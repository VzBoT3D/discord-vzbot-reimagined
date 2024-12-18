package org.vzbot.discord.components

import com.zellerfeld.zellerbotapi.annotations.DCButton
import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.DiscordButton
import com.zellerfeld.zellerbotapi.discord.components.PermanentDiscordButton
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.custom.ConfirmModal
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.commands.fetchSerialTicket
import org.vzbot.discord.restrictions.TicketRestrictions
import org.vzbot.io.buildPrettyEmbed
import java.awt.Color
import java.util.concurrent.TimeUnit

@DCButton
class DeleteTicketButton: PermanentDiscordButton("vz_serial_delete", DiscordButton(label = "Delete", buttonStyle = ButtonStyle.DANGER, emoji = Emoji.fromUnicode("U+1F5D1"))) {
    @Restricted(TicketRestrictions::class, "validTicket")
    override fun execute(actionSender: ActionSender, hook: Message) {
        val ticket = actionSender.fetchSerialTicket()!!

        val confirmModal = ConfirmModal("Confirm") { sender, _, _ ->
            transaction { ticket.delete() }
            sender.respondText("This ticket has been deleted, and this channel will delete itself in 1 minute.")
            sender.textChannel.channel.delete().queueAfter(1, TimeUnit.MINUTES)
        }

        actionSender.respondModal(confirmModal)
    }
}