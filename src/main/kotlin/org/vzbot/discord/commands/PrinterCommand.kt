package org.vzbot.discord.commands

import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.DiscordModal
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordSubCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommandOption
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DSubCommand
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.models.Printer
import org.vzbot.models.PrinterProfile
import org.vzbot.models.PrinterProfiles
import org.vzbot.models.Printers
import org.vzbot.models.generated.toModel

@DCommand("printer", "manage the vz printers")
class PrinterCommand: DiscordCommand() {

    @DSubCommand("manage the profiles of a printer")
    class Profile: DiscordSubCommand() {
        @DSubCommand("create a new profile")
        class Create: DiscordSubCommand() {

            @DCommandOption("printer", name = "printer", priority = 5)
            lateinit var printerInput: String

            @DCommandOption("description", "description", priority = 4)
            lateinit var descriptionInput: String

            @DCommandOption("link to the docs for example", "learn_more_url", priority = 3)
            lateinit var learnMoreURLInput: String

            @DCommandOption("URL for the kit. Optional", name = "kit_url", optional = true, priority = 2)
            var kitURLInput: String? = null

            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {
                if (transaction { PrinterProfile.all().any { it.printer.name == printerInput } }) {
                    actionSender.respondText("There already exists a profile for this printer", true)
                    return
                }

                val printer = transaction { Printer.find { Printers.name eq printerInput }.firstOrNull() }
                if (printer == null) {
                    actionSender.respondText("The given printer was not found", true)
                    return
                }

                transaction {
                    PrinterProfile.new {
                        this.printer = printer
                        this.description = descriptionInput
                        this.kitURL = kitURLInput
                        this.learnMoreURL = learnMoreURLInput
                    }
                }

                actionSender.respondText("A new profile has been created. You can now view it on the web page", true)
            }
        }

        @DSubCommand("delete an existing profile for a printer")
        class Delete: DiscordSubCommand() {
            @DCommandOption("printer", name = "printer")
            lateinit var printerInput: String

            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {
                val profile = transaction { PrinterProfile.all().firstOrNull { it.printer.name == printerInput } }

                if (profile == null) {
                    actionSender.respondText("This printer does not have a profile yet!", true)
                    return
                }

                transaction { profile.delete() }

                actionSender.respondText("The given profile has been deleted", true)
            }
        }

        @DSubCommand("edit an existing profile")
        class Edit: DiscordSubCommand() {
            @DCommandOption("printer", name = "printer")
            lateinit var printerInput: String

            @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
            override fun execute(actionSender: ActionSender) {
                val profile = transaction { PrinterProfile.all().firstOrNull { it.printer.name == printerInput } }

                if (profile == null) {
                    actionSender.respondText("This printer does not have a profile yet!", true)
                    return
                }

                val descriptionInput = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH).setPlaceholder("Leave empty for no change").setRequired(false).build()
                val kitURLInput = TextInput.create("kit_url", "Kit URL", TextInputStyle.SHORT).setPlaceholder("Leave empty for no change").setRequired(false).build()
                val learnMoreURLInput = TextInput.create("learn_more", "Learn more URL", TextInputStyle.SHORT).setPlaceholder("Leave empty for no change").setRequired(false).build()

                val editModal = DiscordModal("Edit ${profile.printer.name}", components = mutableListOf(ActionRow.of(descriptionInput)))
                { modalSender, _, values ->
                    val description = values["description"]!!.asString
                    val kitURL = values["kit_url"]!!.asString
                    val learnMoreURL = values["learn_more"]!!.asString

                    transaction {
                        profile.kitURL = kitURL
                        profile.learnMoreURL = learnMoreURL
                        profile.description = description
                    }

                    modalSender.respondText("The given profile has been edited.", true)
                }

                actionSender.respondModal(editModal)

            }
        }
    }

    @DSubCommand("create a new official vzbot printer, to allow them for vzbot serial program.")
    class Create: DiscordSubCommand() {
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
    class Delete: DiscordSubCommand() {
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
    class List: DiscordSubCommand() {
        @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
        override fun execute(actionSender: ActionSender) {
            val printers = transaction { org.vzbot.models.Printer.all().map { it.toModel() } }
            actionSender.respondText("The following printers are registered currently: ${printers.joinToString { it.name }.ifEmpty { "No printers are registered yet" }}")
        }
    }

}