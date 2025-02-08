package org.vzbot.discord.components

import com.zellerfeld.zellerbotapi.ZellerBot
import com.zellerfeld.zellerbotapi.annotations.DCButton
import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.DiscordButton
import com.zellerfeld.zellerbotapi.discord.components.PermanentDiscordButton
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.custom.ConfirmModal
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.utils.FileUpload
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.commands.fetchSerialTicket
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.discord.restrictions.TicketRestrictions
import org.vzbot.discord.util.fetchFilesForSerial
import org.vzbot.io.*
import org.vzbot.models.SerialNumber
import java.awt.Color
import java.io.File

@DCButton
class AcceptSerialRequestButton: PermanentDiscordButton("vz_accept_serial", DiscordButton(label = "Accept", buttonStyle = ButtonStyle.SUCCESS, emoji = Emoji.fromUnicode("U+1F44D"))) {
    @OptIn(DelicateCoroutinesApi::class)
    @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
    @Restricted(TicketRestrictions::class, "validTicket")
    override fun execute(actionSender: ActionSender, hook: Message) {
        val ticket = actionSender.fetchSerialTicket()!!

        if (transaction { !ticket.open }) {
            actionSender.respondEmbed(buildPrettyEmbed("Error", "This ticket has already been reviewed!", Color.RED), true)
            return
        }

        val announcementChannel = ZellerBot.getTextChannel(env[EnvVariables.VZ_SERIAL_ANNOUNCEMENT_CHANNEL].toLong())

        if (announcementChannel == null) {
            actionSender.respondEmbed(buildPrettyEmbed("Error", "There was an error fetching the announcement channel. Please report this to devin.", Color.RED), true)
            return
        }

        val ticketOwner = ZellerBot.getMember(ticket.ownerID)

        if (ticketOwner == null) {
            actionSender.respondEmbed(buildPrettyEmbed("Error", "It seems like the creator of this ticket has left the server. Feel free to delete it using /serial ticket delete", Color.RED), true)
            return
        }

        val confirmModal = ConfirmModal("Accept this application?") { sender, _, _ ->

            val serialCount = transaction { SerialNumber.count() }

            val serialNumber = transaction {
                SerialNumber.new {
                    this.serialID = serialCount + 1
                    this.description = ticket.description
                    this.country = ticket.country
                    this.printer = ticket.printer
                    this.mediaURL = ticket.mediaURL
                    this.memberID = ticket.ownerID
                }
            }

            GlobalScope.async {
                val coordinates = ticket.country?.randomCoordinates() ?: return@async

                transaction {
                    serialNumber.latitude = coordinates.first
                    serialNumber.longitude = coordinates.second
                }
            }

            transaction {
                ticket.open = false
                ticket.accepted = true
                ticket.reviewedBy = sender.member.idLong
            }

            val printer = transaction { ticket.printer.name }

            val channel = actionSender.textChannel

            val serialID = transaction { serialNumber.serialID }

            val embed = prettyEmbed("Application Accepted", "**Congratulations**. Your application has been accepted and you have been granted your new serial id. In the following messages, we will send you the files to print your serial badge. Welcome to the **VZParty!** Feel free to delete your ticket, when you have grabbed your filed.", Color.GREEN)
            embed.addField("Serial ID", serialID.toString(), false)

            channel.sendEmbedWithText(ticketOwner.asMention, embed.build(), ActionRow.of(DeleteTicketButton()))
            channel.channel.manager.setName("closed-serial-${ticketOwner.effectiveName}").queue()

            val ownersRole = ZellerBot.getRole(env[EnvVariables.VZ_OWNERS_ROLE])

            if (ownersRole != null) {
                ZellerBot.mainGuild!!.addRoleToMember(ticketOwner, ownersRole).queue()
            }

            ZellerBot.mainGuild!!.modifyNickname(ticketOwner, "${ticketOwner.effectiveName} VZ.${serialID}").queue()

            val announcementEmbed = prettyEmbed("New Serial! #$serialID", "The user ${ticketOwner.effectiveName} has just finished their $printer. Spread some VZLove!", Color.GREEN)
            announcementChannel.sendMessageEmbeds(announcementEmbed.build()).queue {
                announcementChannel.sendMessage(transaction { ticket.mediaURL }).queue()
            }

            val serialFiles = fetchFilesForSerial(serialNumber.serialID)
            channel.channel.sendFiles(serialFiles.map { FileUpload.fromData(it) }).queue()

            sender.respondText("You have accepted this application. You have been rewarded +1 VZSocialCredit", true)
        }

        actionSender.respondModal(confirmModal)
    }
}