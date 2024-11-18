package org.vzbot.discord.commands

import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordSubCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommandOption
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DSubCommand
import net.dv8tion.jda.api.entities.Message.Attachment
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.utils.FileUpload
import org.jetbrains.exposed.sql.transactions.transaction
import org.simpleyaml.configuration.file.YamlConfiguration
import org.vzbot.discord.components.PrinterSelection
import org.vzbot.discord.restrictions.AdminRestriction
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.io.buildPrettyEmbed
import org.vzbot.models.*
import org.vzbot.models.generated.toModel
import java.awt.Color
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.io.path.writeText

@DCommand("serial", "manage the vzbot serial system")
class SerialCommand: DiscordCommand() {

    @DSubCommand("create a new 'apply for serial' panel in the current channel")
    class Panel(): DiscordSubCommand() {
        @Restricted(AdminRestriction::class, "mustBeAdmin")
        override fun execute(actionSender: ActionSender) {
            actionSender.textChannel.sendEmbed(buildPrettyEmbed("VzBoT Serial Program", "Click the button below to apply for a serial id!", Color.RED), ActionRow.of(PrinterSelection()))
            actionSender.respondText("The panel has been created", true)
        }
    }

    @DSubCommand("Imports serial data from a provided .yml")
    class Import(): DiscordSubCommand() {

        @DCommandOption("input .yml file")
        lateinit var input: Attachment

        @Restricted(AdminRestriction::class, "mustBeAdmin")
        override fun execute(actionSender: ActionSender) {
            val hook = actionSender.respondLater(false)
            val file = input.proxy.downloadToFile(Files.createTempFile("input", ".yml").toFile()).get()
            val yaml = YamlConfiguration.loadConfiguration(file)

            val log = StringBuilder()
            log.appendLine("Found ${yaml.getKeys(false)} serials. Starting to import...")
            var created = 0

            for (key in yaml.getKeys(false)) {
                val section = yaml.getConfigurationSection(key)
                val description = section.getString("description")
                val id = section.getInt("id").toLong()

                if (transaction { SerialNumber.find { SerialNumbers.serialID eq id }.firstOrNull() != null }) continue

                val mediaURL = section.getString("mediaURL")
                val memberID = section.getLong("memberID")
                val country = Country.getCountry(section.getString("country"))

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                val date = LocalDateTime.parse(section.getString("date").substring(0, 19), formatter)

                val printer = transaction { org.vzbot.models.Printer.find { Printers.name eq section.getString("printer") }.firstOrNull() }

                transaction {
                    SerialNumber.new {
                        this.description = description
                        this.serialID = id
                        this.memberID = memberID
                        this.mediaURL = mediaURL
                        this.country = country
                        this.createdAt = date
                        this.printer = printer
                    }
                }
                created++
                log.appendLine("Created serial $id in database.")
            }

            log.appendLine("Finished importing $created serials!")

            val logFile = Files.createTempFile("log", "txt")
            logFile.writeText(log.toString())

            hook.editOriginal("The import has finished").setFiles(FileUpload.fromData(logFile)).queue()
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

        @DSubCommand("list all vzbot printers")
        class List(): DiscordSubCommand() {
            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {
                val printers = transaction { org.vzbot.models.Printer.all().map { it.toModel() } }
                actionSender.respondText("The following printers are registered currently: ${printers.joinToString { it.name }.ifEmpty { "No printers are registered yet" }}")
            }
        }
    }
}