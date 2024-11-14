package org.vzbot.discord.commands

import com.zellerfeld.zellerbotapi.ZellerBot
import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.DiscordSubCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DCommand
import com.zellerfeld.zellerbotapi.discord.components.commands.annotations.DSubCommand
import org.jetbrains.exposed.sql.transactions.transaction
import org.vzbot.discord.restrictions.AdminRestriction
import org.vzbot.discord.restrictions.TeamMemberRestriction
import org.vzbot.io.buildPrettyEmbed
import org.vzbot.io.infoEmbed
import org.vzbot.io.prettyEmbed
import org.vzbot.io.rawInfoEmbed
import org.vzbot.models.APIToken
import org.vzbot.models.SerialTicket
import org.vzbot.models.SerialTickets
import org.vzbot.models.generated.SerialTicketModelDTO
import org.vzbot.models.generated.toModel
import java.awt.Color
import java.util.*
import java.util.concurrent.TimeUnit

@DCommand("token", "manage api tokens for the rest api")
class TokenCommand: DiscordCommand() {

    @DSubCommand("view all the tokens")
    class View(): DiscordSubCommand() {
        @Restricted(AdminRestriction::class, "mustBeAdmin")
        override fun execute(actionSender: ActionSender) {
            val tokens = transaction { APIToken.all().toList() }

            if (tokens.isEmpty()) {
                actionSender.respondEmbed(infoEmbed("There are no tokens yet!"), true)
                return
            }

            val embed = rawInfoEmbed()
            for ((i, token) in tokens.withIndex()) {
                val member = ZellerBot.getMember(token.initiator)
                embed.addField("Token - $i", "******** - by ${member?.effectiveName ?: "N/A"}", true)
            }

            actionSender.respondEmbed(embed.build(), true)
        }
    }

    @DSubCommand("create a new api token")
    class Create(): DiscordSubCommand() {
        @Restricted(AdminRestriction::class, "mustBeAdmin")
        override fun execute(actionSender: ActionSender) {
            val token = transaction { APIToken.new {
                this.token = UUID.randomUUID().toString().replace("-","").slice(0..10)
                this.initiator = actionSender.member.idLong
            } }.toModel()

            val tokenEmbed = prettyEmbed("Your token", "Your token has been generated, be sure to copy it. This is the last time you'll see it", Color.GREEN)
            tokenEmbed.addField("Token", "||${token.token}||", false)

            actionSender.respondEmbed(tokenEmbed.build(), true)
        }
    }
}

fun ActionSender.fetchSerialTicket(): SerialTicket? {
    val channelID = textChannel.channel.idLong
    return transaction { SerialTicket.find { SerialTickets.discordChannel eq channelID }.firstOrNull() }
}