package org.vzbot.discord.commands

import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordSubCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommandOption
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DSubCommand
import net.dv8tion.jda.api.interactions.components.ActionRow
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.components.ApplyForSerialButton
import org.vzbot.discord.restrictions.AdminRestriction
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.discord.restrictions.respondUnauthorized
import org.vzbot.io.buildPrettyEmbed
import org.vzbot.models.Printer
import org.vzbot.models.Printers
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

    @DSubCommand("manage official vzbot printers")
    class Printer(): DiscordSubCommand() {
        @DSubCommand("create a new official vzbot printer, to allow them for vzbot serial program.")
        class Create(): DiscordSubCommand() {
            @DCommandOption("the printer to create")
            lateinit var printer: String

            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {

                if (org.vzbot.models.Printer.hasPrinter(printer)) {
                    actionSender.respondText("A printer with this name already exists", true)
                    return
                }

                transaction {
                    org.vzbot.models.Printer.new {
                        this.name = printer
                    }
                }

                actionSender.respondText("The printer $printer has been created, and can now be used in serial applications.", true)
            }
        }

        @DSubCommand("delete an existing vzbot printer")
        class Delete(): DiscordSubCommand() {
            @DCommandOption("the printer to delete")
            lateinit var printer: String

            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {

                if (!org.vzbot.models.Printer.hasPrinter(printer)) {
                    actionSender.respondText("A printer with this name does not exist", true)
                    return
                }

                transaction {
                    org.vzbot.models.Printer.find {
                        Printers.name eq printer
                    }.firstOrNull()?.delete()
                }

                actionSender.respondText("The printer $printer has been deleted, and can't be used in serial applications.", true)
            }
        }
    }
}