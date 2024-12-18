package org.vzbot.discord.components

import com.zellerfeld.zellerbotapi.annotations.DCButton
import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.DiscordButton
import com.zellerfeld.zellerbotapi.discord.components.PermanentDiscordButton
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.custom.ConfirmModal
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.commands.fetchSerialTicket
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.discord.restrictions.TicketRestrictions
import org.vzbot.io.buildPrettyEmbed
import java.awt.Color

@DCButton
class DeclineSerialRequestButton: PermanentDiscordButton("vz_decline_serial", DiscordButton(label = "Decline", buttonStyle = ButtonStyle.DANGER, emoji = Emoji.fromUnicode("U+1F44E"))) {
    @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
    @Restricted(TicketRestrictions::class, "validTicket")
    override fun execute(actionSender: ActionSender, hook: Message) {
        val ticket = actionSender.fetchSerialTicket()!!

        if (transaction { !ticket.open }) {
            actionSender.respondEmbed(buildPrettyEmbed("Error", "This ticket has already been reviewed!", Color.RED), true)
            return
        }

        val confirmModal = ConfirmModal("Confirm decline") { sender, message, values ->
            transaction {
                ticket.open = false
                ticket.accepted = false
                ticket.reviewedBy = sender.member.idLong
            }

            sender.textChannel.sendEmbed(
                buildPrettyEmbed(
                    "Declined",
                    "Sadly your application has been declined by our reviewers. Feel free to contact us about more information",
                    Color.RED
                ), ActionRow.of(DeleteTicketButton())
            )
            sender.respondText("You have declined this application. You have been rewarded +1 VZSocialCredit", true)
        }

        actionSender.respondModal(confirmModal)

    }
}