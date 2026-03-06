package org.vzbot.discord.restrictions

import com.ktbot.api.KtBot
import com.ktbot.api.discord.components.commands.actionsenders.ActionSender
import net.dv8tion.jda.api.entities.Member
import org.vzbot.io.EnvVariables
import org.vzbot.io.buildPrettyEmbed
import org.vzbot.io.env
import java.awt.Color

class AdminRestriction {
    fun mustBeAdmin(actionSender: ActionSender): Boolean {
        if (!actionSender.member.isAdmin()) {
            actionSender.respondUnauthorized()
            return false
        }
        return true
    }
}

fun Member.isAdmin(): Boolean {
    val adminRole = KtBot.getRole(env[EnvVariables.VZ_ADMIN_ROLE])
    return adminRole in roles
}

fun ActionSender.respondUnauthorized() {
    respondEmbed(buildPrettyEmbed("Unauthorized", "You are not permitted to execute this action!", Color.RED), true)
}