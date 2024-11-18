package org.vzbot.discord.components

import com.zellerfeld.zellerbotapi.discord.components.PermanentSimpleSelectionMenu
import com.zellerfeld.zellerbotapi.discord.components.SimpleSelectionMenu
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.util.userPrinterSelections
import org.vzbot.io.buildPrettyEmbed
import org.vzbot.models.Printer
import org.vzbot.models.Printers
import java.awt.Color

class PrinterSelection: PermanentSimpleSelectionMenu("vz_selection_printers", SimpleSelectionMenu("Please select your printer", options = transaction { Printer.all().map { SelectOption.of(it.name, it.name) }.toMutableList() }, maxOptions = 1, minOptions = 1 )) {

    override fun execute(selections: List<String>, sender: ActionSender, hook: Message) {
        val selectedPrinter = selections.first()
        val fetchedPrinter = transaction { Printer.find { Printers.name eq selectedPrinter }.firstOrNull() }

        if (fetchedPrinter == null) {
            sender.respondText("There was an error fetching your printer. Please report this to our team", true)
            return
        }

        userPrinterSelections[sender.member] = fetchedPrinter
        sender.respondEmbed(buildPrettyEmbed("Info", "You have selected the $selectedPrinter as your machine. If this is correct please click the continue button. If not you can cancel this process", Color.ORANGE), true, ActionRow.of(ApplyForSerialButton()))
    }
}