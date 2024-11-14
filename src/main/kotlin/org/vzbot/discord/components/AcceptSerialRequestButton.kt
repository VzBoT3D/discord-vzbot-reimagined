package org.vzbot.discord.components

import com.zellerfeld.zellerbotapi.annotations.DCButton
import com.zellerfeld.zellerbotapi.annotations.Restricted
import com.zellerfeld.zellerbotapi.discord.components.DiscordButton
import com.zellerfeld.zellerbotapi.discord.components.PermanentDiscordButton
import com.zellerfeld.zellerbotapi.discord.components.commands.actionsenders.ActionSender
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import org.vzbot.discord.restrictions.TeamMemberRestriction

@DCButton
class AcceptSerialRequestButton: PermanentDiscordButton("vz_accept_serial", DiscordButton(label = "Accept", buttonStyle = ButtonStyle.PRIMARY, emoji = Emoji.fromUnicode("U+1F44D"))) {
    @Restricted(TeamMemberRestriction::class, "mustBeInTeam")
    override fun execute(actionSender: ActionSender, hook: Message) {
        actionSender.respondText("Not implemented", true)
    }
}