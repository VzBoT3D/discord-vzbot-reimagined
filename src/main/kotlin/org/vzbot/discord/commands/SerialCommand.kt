package org.vzbot.discord.commands

import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordSubCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DSubCommand
import net.dv8tion.jda.api.interactions.components.ActionRow
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.components.ApplyForSerialButton
import org.vzbot.discord.restrictions.AdminRestriction
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.io.buildPrettyEmbed
import java.awt.Color
import java.util.concurrent.TimeUnit

@DCommand("serial", "manage the vzbot serial system")
class SerialCommand: DiscordCommand() {

    @DSubCommand("create a new 'apply for serial' panel in the current channel")
    class Panel(): DiscordSubCommand() {
        @Restricted(AdminRestriction::class, "mustBeAdmin")
        override fun execute(actionSender: ActionSender) {
            actionSender.textChannel.sendEmbed(buildPrettyEmbed("VzBoT Serial Program", "Click the button below to apply for a serial id!", Color.RED), ActionRow.of(ApplyForSerialButton()))
            actionSender.respondText("The panel has been created", true)
        }
    }

    @DSubCommand("manage serial tickets")
    class Ticket(): DiscordSubCommand() {
        @DSubCommand("deletes the current ticket")
        class Delete(): DiscordSubCommand() {
            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {
                val ticket = actionSender.fetchSerialTicket()

                if (ticket == null) {
                    actionSender.respondEmbed(buildPrettyEmbed("Error", "There was an error fetching the ticket for this channel", Color.RED), true)
                    return
                }

                transaction { ticket.delete() }
                actionSender.respondEmbed(buildPrettyEmbed("Success", "This channel will delete itself in 10 seconds!", Color.GREEN), true)
                actionSender.textChannel.channel.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }
    }

}