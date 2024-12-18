package org.vzbot.discord.components

import com.zellerfeld.zellerbotapi.ZellerBot
import com.zellerfeld.zellerbotapi.annotations.DCButton
import com.zellerfeld.zellerbotapi.discord.components.DiscordButton
import com.zellerfeld.zellerbotapi.discord.components.DiscordModal
import com.zellerfeld.zellerbotapi.discord.components.PermanentDiscordButton
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.util.userPrinterSelections
import org.vzbot.io.EnvVariables
import org.vzbot.io.buildPrettyEmbed
import org.vzbot.io.env
import org.vzbot.io.prettyEmbed
import org.vzbot.models.Country
import org.vzbot.models.SerialTicket
import java.awt.Color

@DCButton
class ApplyForSerialButton: PermanentDiscordButton("vz_apply_serial", DiscordButton(label = "Continue", emoji = Emoji.fromUnicode("U+27A1"), buttonStyle = ButtonStyle.PRIMARY)) {
    override fun execute(actionSender: ActionSender, hook: Message) {
        val printer = userPrinterSelections[actionSender.member]
        if (printer == null) {
            actionSender.respondEmbed(buildPrettyEmbed("Error", "Your entered printer is not valid for applications currently", Color.RED), true)
            return
        }

        val descriptionInput = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH).setPlaceholder("Please describe your machine").setMinLength(100).build()
        val countryInput = TextInput.create("country", "Country", TextInputStyle.SHORT).setRequired(false).setPlaceholder("Please enter a valid country code, or leave it empty").build()
        val videoInput = TextInput.create("media", "Media", TextInputStyle.PARAGRAPH).setRequired(true).setPlaceholder("Please enter a video linkt of your printer printing").build()

        if (SerialTicket.hasOpenTicket(actionSender.member)) {
            actionSender.respondEmbed(buildPrettyEmbed("Warning","You already have an open ticket. Please wait for a team member to close it.", Color.ORANGE),true)
            return
        }

        val serialCategory = ZellerBot.bot?.getCategoryById(env[EnvVariables.VZ_SERIAL_CATEGORY]) ?: run {
            actionSender.respondText("There was an error creating your ticket. Please inform the team", true)
            return
        }

        if (serialCategory.channels.size > 45) {
            actionSender.respondEmbed(buildPrettyEmbed("Error", "There are currently too many open tickets. Feel free to nag the VzBoT Team to do their job :).", Color.RED), true)
            return
        }

        val modal = DiscordModal("Apply for serial", mutableListOf(
            ActionRow.of(descriptionInput),
            ActionRow.of(countryInput),
            ActionRow.of(videoInput))) {
          modalSender, _, values ->
            val publicRole = ZellerBot.mainGuild?.publicRole ?: run {
                actionSender.respondText("There was an error creating your ticket. Please inform the team", true)
                return@DiscordModal
            }

            val vzTeamRole = ZellerBot.getRole(env[EnvVariables.VZ_TEAM_ROLE]) ?: run {
                actionSender.respondText("There was an error creating your ticket. Please inform the team", true)
                return@DiscordModal
            }

            val serialChannel = serialCategory.createTextChannel("OPEN-serial-${modalSender.member.effectiveName}")
                .addPermissionOverride(publicRole, listOf(), listOf(Permission.VIEW_CHANNEL))
                .addPermissionOverride(actionSender.member, listOf(Permission.VIEW_CHANNEL), listOf())
                .addPermissionOverride(vzTeamRole, listOf(Permission.VIEW_CHANNEL), listOf())
                .complete()

            val description = values["description"]!!.asString
            val country = values["country"]!!.asString
            val video = values["media"]!!.asString

            val resolvedCountry = if (country.isNotEmpty()) Country.getCountry(country) else null

            if (resolvedCountry == Country.UNKNOWN) {
                modalSender.respondText("The country you entered is not known by our list. Please use a valid country from this list: https://github.com/VzBoT3D/Discord-VzBoT/blob/main/countries.txt. Or leave it empty.", true)
                return@DiscordModal
            }

            val ticket = transaction {
                SerialTicket.new {
                    this.description = description
                    this.printer = printer
                    this.ownerID = modalSender.member.idLong
                    this.mediaURL = video
                    this.country  = resolvedCountry
                    this.discordChannel = serialChannel.idLong
                }
            }

            val embed = prettyEmbed("Serial application from ${modalSender.member.effectiveName}", "", Color.ORANGE)
            embed.addField("Status", "Pending", false)
            embed.addField("Printer", transaction { printer.name }, false)
            embed.addField("Description", description, false)
            embed.addField("Country", country.ifEmpty { "Not provided" }, false)

            serialChannel.sendMessageEmbeds(embed.build()).addActionRow(AcceptSerialRequestButton(), DeclineSerialRequestButton()).queue {
                serialChannel.sendMessage(video).queue()
            }

            modalSender.respondEmbed(buildPrettyEmbed("Application created", "We have created an application channel for you over here ${serialChannel.asMention}. As soon as we have looked into it, you will be notified!", Color.GREEN), true)
        }
        actionSender.respondModal(modal)
    }
}